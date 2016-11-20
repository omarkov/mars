package ir;

import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.InterruptedException;

import net.*;

interface InputListener
{
    public void performInput();
}

class InputThread extends Thread
{
    private long startTime = 0;
    private InputListener listener;
    private String inputString;
    private boolean doInput;

    public void append(String str)
    {
        inputString += str.trim();
    }

    public InputThread(InputListener listener)
    {
        this.listener = listener;
        doInput = false;
    }

    public String getInputString()
    {
        return inputString;
    }

    public void abort()
    {
        doInput = false;
    }

    public synchronized void reset()
    {
        inputString = "";
        doInput = true;
        startTime = System.currentTimeMillis();
        notify();
        System.out.println("RESET");
    }

    public void stopInputMode()
    {
        doInput = false;
    }

    public boolean isInputMode()
    {
        return doInput;
    }

    private synchronized void waitFor()
    {
        try
        {
            wait();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        while(true)
        {
            while(isInputMode() && System.currentTimeMillis() - startTime < 1000)
            {
                try
                {
                    Thread.sleep(500);
                }catch(Exception e)
                {
                }
            }
            if(isInputMode())
            {
                listener.performInput();
                stopInputMode(); 
            }
            waitFor();
        }
    }
}

public class IRController implements NetworkEventListener, InputListener {
    private NetworkFactory net;
    Properties config = new Properties();
    Hashtable<String,String> commands = new Hashtable<String,String>();
    InputStream inputStream;
    BufferedReader in;
    String active = null;
    String lastCmd = null;
    String osd = null;
    int repeatCount = 1;
    InputThread inputThread;

    public void daemonize() {
	String pidfile_path = System.getProperty("daemon.pidfile");

	// if there is no pidfile, we don't want to daemonize
	if (pidfile_path == null)
	    return;

	// close the pid file on exit
	File pidfile = new File(pidfile_path);
	pidfile.deleteOnExit();

	// close stdout & stderr
	System.out.close();
	System.err.close();
    }
    
    public static void main(String[] args) {
	if (args.length == 0) {
	    System.err.println("no configuration file specified");
	    return;
	}

	try {
	    IRController controller = new IRController(args);
	    controller.daemonize();
	    controller.run();
	} catch (Exception ex) {
	    System.err.println("*** Error: " + ex.getMessage());
	}
    }

    private static void loadResource(String resources, Properties props) throws Exception {
        InputStream in = new FileInputStream(resources);

        if (in == null) {
            throw new Exception("Unable to locate resources: " + resources);
        } else {
            try {
                props.load(in);
            } catch (java.io.IOException e) {
                System.err.println("Caught IOException loading resources: "
                                   + resources);
                System.err.println("Using default resources.");
            }
        }
    }


    public void initNetwork() {
        System.out.print("Starting network...");
        
        net = NetworkFactory.getInstance();
        
        ModuleDescription module = new ModuleDescription("IR", "V1.0");
        net.setModuleDescription(module);
        net.startModule("localhost", 1234);

        /*
        try {
            net.call("system", "createLogInSystem", new Object[]{"IR"});
        } catch (NetworkException ex) {
	        ex.printStackTrace();
	    }
        */
        System.out.println("done");
    }

    public IRController(String args[]) throws Exception {
        inputThread = new InputThread(this);
        inputThread.start();
    	initNetwork();
        loadResource(args[0], config);

        osd = config.getProperty("osd");

        Enumeration keys = config.propertyNames();
        while(keys.hasMoreElements())
        {
            String key = (String)keys.nextElement();
            if(key.startsWith("cmd."))
            {
                String cmd[] = key.split("\\.");
                if(cmd.length == 3)
                {
                    String name   = cmd[1];
                    String action = cmd[2];

                    this.commands.put(name + "." + action, config.getProperty(key));
                }
            }
        }

        try
        {
            Process p = Runtime.getRuntime().exec(config.getProperty("irw"));
            inputStream = p.getInputStream();
            in = new BufferedReader(new InputStreamReader(inputStream));
        }catch(IOException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void run()
    {
        try
        {
            String line = null;
            while(inputStream.available() == 0 || (line = in.readLine()) != null)
            {
                if(line == null)
                {
                    parse(lastCmd);
                    repeatCount = 1;
                    lastCmd = null;
                }else
                {
                    String tokens[] = line.split(" ");
                    if(tokens.length > 3)
                    {
                        String action = tokens[1];
                        String cmd    = tokens[2];
                        String module;
    
                        cmd = commands.get(cmd + "." + action);
    
                        if(cmd != null)
                        {
                            if(cmd.equalsIgnoreCase(lastCmd))
                            {
                                repeatCount ++;
                            }else if(lastCmd != null)
                            {
                                parse(lastCmd);
                                repeatCount = 1;
                                parse(cmd);
    
                                lastCmd = null;
                            }else
                            {
                                lastCmd = cmd;
                            }
                        } 
                    }
                }
                line = null;
            }
        }catch(IOException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean isActivated()
    {
        return  active != null && active.length() > 0;
    }

    private String parse(String cmd)
    {
        if(cmd == null) return null;
   
        String cmds[] = cmd.split("\\|");
        String call[] = cmds[0].split(" ");
        String activeCall[] = null;

        System.out.println(cmds[0]);

        if(cmds.length > 1)
        {
            System.out.println(cmds[1].trim());
            activeCall = cmds[1].trim().split(" ");
        }

        if(isActivated() && activeCall != null)
        {
            call = activeCall;
            cmd = call[0];
            if(cmd.equalsIgnoreCase("input") && call.length == 2)
            {
                input(call);
            }
        }else if(call.length > 0)
        {
            cmd = call[0];
            if(cmd.equalsIgnoreCase("call") && call.length >= 3)
            {
                call(call);
            }else if(cmd.equalsIgnoreCase("set") && call.length == 4)
            {
                set(call);
            }else if(cmd.equalsIgnoreCase("reset") && call.length == 1 && isActivated())
            {
                osd(active + " off");
                inputThread.abort();
                active = null;
            }else if(cmd.equalsIgnoreCase("set") && call.length == 2 && isActivated())
            {
                setActive(call);
            }else if(cmd.equalsIgnoreCase("get") && call.length == 3)
            {
                return get(call);
            }else if(cmd.equalsIgnoreCase("activate") && call.length > 2)
            {
                activate(call);
            }else if(cmd.equalsIgnoreCase("increment") && isActivated())
            {
                incrementActive(call);
            }
        }
        return null;
    }

    public void performInput()
    {
        String[] actlist = active.split("&");

        int val = Integer.parseInt(inputThread.getInputString()); 

        for(int i=0; i < actlist.length; i++)
        {
            String command = actlist[i].trim();
            if(command.startsWith("call"))
            {
                System.out.println(command + " " + Integer.toString(val).trim());
                parse(command + " " + Integer.toString(val).trim());
            }
            else
            {
                System.out.println(command + " " + Integer.toString(val).trim());
                parse("set " + command + " " + Integer.toString(val).trim());
            }
        }
    }

    private void input(String[] call)
    {
        if(!inputThread.isInputMode())
        {
            inputThread.reset();
        }
        inputThread.append(call[1]);
    }

    private void setActive(String[] call)
    {
        String[] actlist = active.split("&");

        int val = Integer.parseInt(call[1]); 

        for(int i=0; i < actlist.length; i++)
        {
            String command = actlist[i].trim();
            System.out.println("set " + command + " " + Integer.toString(val).trim());
            parse("set " + command + " " + Integer.toString(val).trim());
        }
    } 

    private void incrementActive(String[] call)
    {
        String[] actlist = active.split("&");

        String val = parse("get " + actlist[0]);
        int num = Integer.parseInt(val);
        int inc = Integer.parseInt(call[1]) * repeatCount;

        for(int i=0; i < actlist.length; i++)
        {
            String command = actlist[i].trim();
            System.out.println("set " + command + " " + Integer.toString(num + inc).trim());
            parse("set " + command + " " + Integer.toString(num + inc).trim());
        }
    } 

    private void activate(String[] call)
    {
        String new_active = "";
        for(int i=1; i < call.length; i++)
        {
            new_active += call[i] + " ";
        }
        if(new_active.equalsIgnoreCase(active))
        {
            osd(active + " off");
            inputThread.abort();
            active = null;
        }else
        {
            active = new_active;
            osd(active + " on");
        }
    } 

    private String get(String[] call)
    {
        try
        {
            String module = call[1];
            String cmd    = call[2];
            Integer val = (Integer)net.get(module, cmd);
            if(val != null)
                return val.toString();
            else
                return "0";
        }catch(NetworkException e)
        {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private void set(String[] call)
    {
        try
        {
            String module = call[1];
            String cmd    = call[2];
            net.set(module, cmd, call[3]);
        }catch(NetworkException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void osd(String msg)
    {
        String[] call = (osd + " " + msg).split(" ");
        try
        {
            String module = call[1];
            String cmd    = call[2];
            net.call(module, cmd, new Object[]{getParam(call)});
        }catch(NetworkException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void call(String[] call)
    {
        try
        {
            String module = call[1];
            String cmd    = call[2];
            net.call(module, cmd, getParams(call));
        }catch(NetworkException e)
        {
            System.out.println(e.getMessage());
        }
    }

    private String getParam(String[] call)
    {
        String param = "";
        for(int i=3; i < call.length; i++)
        {
            param += call[i] + " ";
        }
        return param;
    } 

    private String[] getParams(String[] call)
    {
        String params[] = new String[call.length - 3];
        for(int i=3; i < call.length; i++)
        {
            params[i-3] = call[i];
        }
        return params;
    } 
}
