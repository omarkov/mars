/*
 * Created on 13.06.2005
 */
package net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class Controller extends Thread {
	private Socket socket;
	private BufferedReader in;
	private DataOutputStream out;
	private boolean bRunning;
	private boolean bIsTransmitter;
	private boolean bIsServer;

	private String version;
	private String id;
	private String serial;
	
	public static final String OK = "OK";
	public static final String ERROR = "ERROR";
	public static final String UNKNOWN = "UNKNOWN";
	public static final String NL = "\n";
	public static final String END = "<<END>>";
	public static final String EMPTY = "<<EMPTY>>";
	public static final String RETURN = "<<RETURN>>";
	public static final String SPACE = "<<SPACE>>";
	public static final String SEP0 = NL + "<<SEP";
	public static final String SEP1 = ">>" + NL;
	public static final String SEP = NL;
	public static final String PSEP = "<<PARAMSEP>>";
	public static final String CMDSEP = " ";
	
	public static final String DESCRIBE = "DESCRIBE";
	public static final String _DESCRIBE = "D";
	public static final String LIST = "LIST";
	public static final String _LIST = "L";
	public static final String MODULELIST = "MODULELIST";
	public static final String _MODULELIST = "M";
	public static final String HELO = "HELO";
	public static final String _HELO = "H";
	public static final String GET = "GET";
	public static final String _GET = "G";
	public static final String SET = "SET";
	public static final String _SET = "S";
	public static final String CALL = "CALL";
	public static final String _CALL = "C";
	public static final String QUIT = "QUIT";
	public static final String _QUIT = "Q";

	public String getVersion()
	{
		return version;
	}

	public String getIdentifier()
	{
		return id;
	}

	public String getSerial()
	{
		return serial;
	}

	public Controller(Socket s, String version, String id, String serial) throws IOException
	{
		this.socket = s;
		this.bIsServer = true;
    
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new DataOutputStream(socket.getOutputStream());

		parseInput();
    	sendCommand(Controller.HELO + Controller.CMDSEP 
					+ version + Controller.CMDSEP 
					+ id 	  + Controller.CMDSEP + serial);

	}

	public Controller(Socket s, boolean isTransmitter, String version, String id, String serial) throws IOException
	{
		this.socket = s;
		this.bIsServer = false;

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new DataOutputStream(socket.getOutputStream());

        this.bIsTransmitter = isTransmitter;

		sendCommand(Controller.HELO + Controller.CMDSEP 
				    + version 	+ Controller.CMDSEP 
				    + id 	+ Controller.CMDSEP 
				    + serial + Controller.CMDSEP
                    + Boolean.toString(bIsTransmitter));

		parseInput();
	}

    public boolean isConnected()
    {
        return socket.isConnected();
    }
	
	protected static Object deserializeObject(Parameter p, String serial)
	{
		serial = serial.trim();
		serial = serial.replaceAll(SPACE, " ");
		serial = serial.replaceAll(RETURN, "\r\n");
		serial = serial.replaceAll(EMPTY, "");
		if(p == null)
			return null;
		else
			return p.getValueObject(serial);
	}


	private boolean parseGet(String line) throws IOException
	{
		StringTokenizer t = new StringTokenizer(line, CMDSEP);

		String module = null;

		int count = t.countTokens();
		
		if(count < 2 || count > 3)
		{
			sendBack(Controller.UNKNOWN);
			return false;
		}
		
		t.nextToken();
		if(count == 3)
		{
			module = t.nextToken();
		}	
		String name = t.nextToken();
		
		Object obj[];
		try
		{
			obj = NetworkFactory.getInstance().localGet(module, name);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			sendBack(Controller.ERROR + e.getMessage());
			return false;
		}

		if(obj != null)
		{
      		Parameter p = (Parameter)obj[0];
            if(p == null) return false;
			String serial = p.serialize(obj[1]).trim();
			if(serial.length() == 0)
			{
				serial = EMPTY;
			}
			sendBack(serial);
			return true;
		}else
		{
			sendBack(Controller.UNKNOWN);
			return false;
		}
	}
	
	private boolean parseSet(String line) throws IOException
	{
		String cmdline[] = line.split(Controller.SEP);

		String set = cmdline[0];
		String value = "";
		for(int i=1; i < cmdline.length; i++)
		{
			value += cmdline[i];
		}

		StringTokenizer t = new StringTokenizer(set, CMDSEP);

		String module = null;

		int count = t.countTokens();
		
		if(count < 2 || count > 3)
		{
			sendBack(Controller.UNKNOWN);
			return false;
		}
		
		t.nextToken();
		if(count == 3)
		{
			module = t.nextToken();
		}	
		String name  = t.nextToken();
		
		try
		{
			Object ret = NetworkFactory.getInstance().localSet(module, name, value);
			sendBack(ret.toString().trim());
		}catch(Exception e)
		{
			e.printStackTrace();
			
			sendBack(Controller.ERROR);
			return false;
		}
		return true;
	}

	private boolean parseCall(String line) throws IOException
	{
		String cmdline[] = line.split(PSEP);
		String cmd = cmdline[0];
		String params = "";
		String ret = "";

		for(int i=1; i < cmdline.length; i++)
		{
			params += cmdline[i];
			
			if(i < cmdline.length - 1)
				params += PSEP;
		}

		StringTokenizer t = new StringTokenizer(cmd, CMDSEP);

		String module = null;

		int count = t.countTokens();
		
		if(count < 2)
			return false;
		
		t.nextToken();
		if(count >= 3)
		{
			module = t.nextToken();
		}	
		String name = t.nextToken();

		//System.out.println("::" + module + " / " + name + "::");

		Object obj[];
		try
		{
			obj = NetworkFactory.getInstance().localCall(module, name, params);
			if(obj != null)
			{
           		Parameter p = (Parameter)obj[0];
				String serial = "";
				if(p != null)
				{
					serial = p.serialize(obj[1]).trim();
				}
				if(serial.length() == 0)
				{
					serial = EMPTY;
				}
				sendBack(serial);
				return true;
			}else
			{
				sendBack(EMPTY);
				return false;
			}
		}
		catch (Exception e)
		{
            		if(e instanceof InvocationTargetException)
            		{
			    sendBack(Controller.ERROR + Controller.CMDSEP + ((InvocationTargetException)e).getTargetException().getMessage());
                ((InvocationTargetException)e).getTargetException().printStackTrace();
            		}else
            		{
			    sendBack(Controller.ERROR + Controller.CMDSEP + e.getClass()); 
                	e.printStackTrace();
            		}
		}
		return false;
	}
	
	private boolean parseModuleList(String line) throws IOException
	{
		String ret = "";
		for(int i=0; i < NetworkFactory.getInstance().getModulesCount(); i++)
		{
			ModuleDescription d = NetworkFactory.getInstance().getModuleDescription(i);
			ret += d.getName() + Controller.SEP;
		}
		sendBack(ret);
		return true;
	}

	private boolean parseDescribe(String line) throws IOException
	{
		StringTokenizer t = new StringTokenizer(line, CMDSEP);
		
		int count = t.countTokens();

		t.nextToken();
		if(count == 2)
		{
			String module = t.nextToken();
			if(module != null)
			{
				if(NetworkFactory.getInstance().getModuleDescription().getName().equalsIgnoreCase(module))
				{
					sendBack(NetworkFactory.getInstance().getModuleDescription().marshall());
					return true;
				}else
				{
				}
				for(int i=0; i < NetworkFactory.getInstance().getModulesCount(); i++)
				{
					ModuleDescription d = NetworkFactory.getInstance().getModuleDescription(i);
					if(d.getName().equalsIgnoreCase(module))
					{
						sendBack(d.marshall()); 
						return true;
					}
				}
				sendBack(Controller.ERROR); 
				return false;
			}
		}
		sendBack(NetworkFactory.getInstance().getModuleDescription().marshall());

		return true;
	}
		
	private boolean parseHelo(String line) throws IOException
	{
		if(line == null)
		{
			return false;
		}
		StringTokenizer t = new StringTokenizer(line, CMDSEP);

		this.version = null;
		this.id      = null;
		this.serial  = null;

		try
		{
			t.nextToken();
			this.version = t.nextToken();
			this.id      = t.nextToken();
			this.serial  = t.nextToken();
            if(t.hasMoreTokens())
            {
                bIsTransmitter = !Boolean.parseBoolean((String)t.nextToken());
            }
			sendBack(Controller.OK);
			return true;
		}catch(Exception e)
		{
			sendBack(Controller.ERROR);
			return false;
		}
	}
	
	private boolean parseBuffer(String line) throws IOException
	{
		if(line == null)
		{
			return false;
		}
		String cmd = line.toUpperCase();
		if(cmd.startsWith(Controller._HELO))
		{
			parseHelo(line);
		}else if(cmd.startsWith(Controller._DESCRIBE))
		{
			parseDescribe(line);
		}else if(cmd.startsWith(Controller._MODULELIST) || cmd.startsWith(Controller._LIST))
		{
			parseModuleList(line);
		}else if(cmd.startsWith(Controller._GET))
		{
			parseGet(line);
		}else if(cmd.startsWith(Controller._SET))
		{
			parseSet(line);
		}else if(cmd.startsWith(Controller._CALL))
		{
			parseCall(line);
		}else if(cmd.startsWith(Controller._QUIT))
		{
			stopController();	
		}
		else if(cmd.length() > 0)
		{
			sendBack(Controller.UNKNOWN + "(" + cmd + ")");
		}
        	return true;
	}

	private void sendBack(String txt) throws IOException
	{
	        // System.out.println("RETURN (" + id + ") >" + txt + "<");
		send(txt);
	}

	private void send(String txt) throws IOException
	{
		out.writeBytes(txt + Controller.NL + Controller.END + Controller.NL);
	}

	private void parseInput() throws IOException
	{
		String buffer = "";
		String line;
		while((line = in.readLine()) != null && !line.equals(Controller.END))
		{
			if(line.length() > 0)
			{
				buffer += line + Controller.SEP;
			}
		}
        	if(line == null)
        	{
            		throw new IOException("Broken Pipe");
        	}
		if(buffer.length() > 1)
		{
			buffer = buffer.substring(0, buffer.length()-1);
		}
		// System.out.println("PARSE (" + id + ") >" + buffer + "<");
		parseBuffer(buffer);
	}

	private void executeReceiver() throws IOException
	{
		parseInput();
	}

	private void executeTransmitter() throws IOException
	{
	}

	public String sendCommand(String txt) throws IOException
	{
	    // System.out.println("SEND (" + id + ") >" + txt + "<");
		out.writeBytes(txt + Controller.NL + Controller.END + Controller.NL);
		String ret = "";

		String line;

		while((line = in.readLine()) != null && !line.equals(Controller.END))
		{
			if(line.length() > 0)
			{
				ret += line + Controller.SEP;
			}
		}	
        
        if(line == null)
        {
        	throw new IOException("Broken Pipe");
        }
	
		ret = ret.replaceAll(EMPTY, "");
		if(ret.length() > 1)
		{
			ret = ret.substring(0, ret.length()-1);
		}
		// System.out.println("RECV (" + id + ") >" + ret + "<");
		
		return ret;
	}

    public boolean isTransmitter()
    {
        return bIsTransmitter;
    }

	public void startTransmitter() throws IOException
	{
//        socket.setSoTimeout(5000);
		bIsTransmitter = true;

/*		String desc = sendCommand(Controller.DESCRIBE)
		ModuleDescription d = new ModuleDescription(desc);
		
		NetworkFactory.getInstance().addModuleDescription(d);*/
		// startController();
	}
	
	public void stopTransmitter()
	{
		// stopController();
        try
        {
  	        sendCommand(Controller.QUIT);
            socket.close();
        }catch(IOException e)
        {
        }
	}

	public void startReceiver()
	{
		bIsTransmitter = false;
		startController();
	}
	
	public void stopReceiver()
	{
		stopController();
	}

	private void startController()
	{
		bRunning = true;
		this.start();
	}

	private void stopController()
	{
		bRunning = false;
	}

	public void run(){
		while(bRunning)
		{
			try
			{
				if(bIsTransmitter)
				{
					executeTransmitter();
				}else
				{
					executeReceiver();
				}
			}catch(IOException e)
			{
				System.out.println("IO-Error: " + e.getMessage());
				bRunning = false;
			}
		}	
        try
        {
            if(bIsServer)
            {
                NetworkFactory.getInstance().removeReceiver(id);
                NetworkFactory.getInstance().removeTransmitter(id);
                NetworkFactory.getInstance().removeModule(id);
            }else
            {
                NetworkFactory.getInstance().removeReceiver(null);
                NetworkFactory.getInstance().removeTransmitter(null);
            }
            socket.close();

        }catch(IOException e)
        {
        }
	}
}

