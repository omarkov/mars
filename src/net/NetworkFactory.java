/*
 * Created on 13.06.2005
 */
package net;

import net.xstream.*;
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
import org.hibernate.Session;
import server.DatabaseAccess;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class NetworkFactory extends Thread {
	private static NetworkFactory theFactory = null;
	private ModuleDescription description;
	private Vector modules;
	public final String VERSION = "1.0"; 
	private boolean bRunning;
	private boolean bStillRunning;
	private boolean bIsController;
	private ServerSocket serverSocket;
	private Controller receiver;
	private Controller transmitter;
	private ModuleListener moduleListener = null;

	private boolean bRegLock;

	private Hashtable cTransmitters;
	private Hashtable cReceivers;

    private int port;
    private String server;
    private int checkTime = 10000;

    private Thread connectionChecker;

	private NetworkFactory()
	{
		modules = new Vector();
		bRegLock = false;
		cTransmitters = new Hashtable();
		cReceivers = new Hashtable();
	}

	protected void removeModule(String id)
	{
		System.out.println("Try to remove module " + id);
		if(id != null)
		{
			ModuleDescription d = findModule(id);
			if(d != null)
			{
				removeModuleDescription(d);
			}
		}
	}

	protected void removeTransmitter(String id)
   	{	
	    System.out.println("Try to remove transmitter " + id);
        if(id == null)
        {
            if(transmitter != null)
            {
                Controller c = transmitter;
                transmitter = null;
                c.stopTransmitter();
            }
            return;
        }

        Controller c = getTransmitter(id);
        if(c != null)
            	c.stopTransmitter();

		lock();
		cTransmitters.remove(id);
		unlock();
	}

	protected void removeReceiver(String id)
	{
		System.out.println("Try to remove receiver " + id);
       if(id == null)
       {
            if(receiver != null)
                receiver.stopReceiver();

            receiver = null;
            return;
       }

       Controller c = getReceiver(id);
        if(c != null)
            c.stopReceiver();

		lock();
		cReceivers.remove(id);
		unlock();
	}

    protected void stopAllTransmitters()
    {
        System.out.println("Stopping all Transmitters ...");
        lock();
        Enumeration e = cTransmitters.elements();
        while(e.hasMoreElements())
        {
            Controller c = (Controller)e.nextElement();
            c.stopTransmitter();
        }
        unlock();
    }

	private Controller getTransmitter(String id)
	{
		lock();
		Object obj = cTransmitters.get(id);
		unlock();

		if(obj != null && obj instanceof Controller)
			return (Controller)obj;
		else
			return null;	
	}

	private Controller getReceiver(String id)
	{
		lock();
		Object obj = cReceivers.get(id);
		unlock();

		if(obj != null && obj instanceof Controller)
			return (Controller)obj;
		else
			return null;	
	}

	private void setReceiver(String id, Controller c)
	{
		lock();
		cReceivers.put(id, c);
		unlock();
	}

	private void setTransmitter(String id, Controller c)
	{
		lock();
		cTransmitters.put(id, c);
		unlock();
	}

    public boolean isController()
    {
        return bIsController;
    }

	public static synchronized NetworkFactory getInstance()
	{
        /*if(theFactory != null && !theFactory.isController() && !theFactory.isConnected())
        {
            theFactory.stopModule();
            theFactory = null;
        }*/
		if(theFactory == null)
			theFactory = new NetworkFactory();
        
        return theFactory;
	}

	private synchronized void lock(){
		while (bRegLock == true) {
			try {
				wait();
			} catch (InterruptedException e) { }
		}
	    bRegLock = true;
	}

	private synchronized void unlock()
	{
		bRegLock = false;
		notify();
	}
	
	public int getWebModulesCount()
	{
		return getModulesCount(true);
	}

	public int getModulesCount()
	{
		return getModulesCount(false);
	}

	public void addModuleListener(ModuleListener listener)
	{
		moduleListener = listener;
	}

	public int getModulesCount(boolean bWeb)
	{
		lock();
		int count = 0;
		if(!bWeb)
		{
			count = modules.size();
		}else
		{
			for(int i=0; i < modules.size(); i++)
			{
				if(((ModuleDescription)modules.get(i)).isWebModule())
					count++;
			}
		}
		unlock();
		return count;
	}
	
	public ModuleDescription getWebModuleDescription(int i)
	{
		return getModuleDescription(i, true);
	}

	public ModuleDescription getModuleDescription()
	{
		return description;
	}

	public ModuleDescription getModuleDescription(int i)
	{
		return getModuleDescription(i, false);
	}

	protected void removeModuleDescription(ModuleDescription d)
	{
		lock();
		modules.remove(d);
		unlock();
	}
	
	protected void addModuleDescription(ModuleDescription d)
	{
		lock();
		modules.add(d);
		unlock();
		registerModule(d);
	}

	private void registerModule(ModuleDescription d)
	{
		if(moduleListener != null)
		{
            if(bIsController)
                DatabaseAccess.startSession();
                
			moduleListener.registerModule(d);

            if(bIsController)
                DatabaseAccess.commitSession();
		}
	}

	public ModuleDescription getModuleDescription(int i, boolean bWeb)
	{
		lock();
		ModuleDescription desc = null;
		if(!bWeb)
		{
			desc = (ModuleDescription)modules.get(i);
		}else
		{
			int idx = 0;
			for(int j=0; j < modules.size(); j++)
			{
				if(((ModuleDescription)modules.get(j)).isWebModule())
				{
					if(idx == i)
					{
						unlock();
						return (ModuleDescription)modules.get(j);
					}
					
					idx++;
				}
			}
		}
		unlock();
		return desc;
	}
	
	public void setModuleDescription(ModuleDescription description)
	{
		this.description = description;
		lock();
		modules.add(description);	
		unlock();
	}
	
	public Object localSet(String module, String name, String value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		ModuleDescription d = getModuleDescription();

		if(module == null || d.getName().equalsIgnoreCase(module))
		{
			NetworkInterface ni = d.getInterface(name);
			if(ni != null && ni instanceof Parameter)
			{
				String first = "";
				String rest  = "";
				if(name.length() > 0)
				{
					first = name.substring(0, 1);
					first = first.toUpperCase();
				}
				if(name.length() > 1)
				{
					rest = name.substring(1, name.length());
					// rest = rest.toLowerCase();
				}
				name = first + rest;

				Parameter p = (Parameter)ni;
				
				Method method = ni.getListener().getClass().getMethod("set" + name, new Class[]{p.getValueClass()});					

				System.out.println("Invoking Method: set" + name);
				
				return method.invoke(
					ni.getListener(), 
					new Object[]{Controller.deserializeObject(p, value)});
			}
		}else
		{
			Controller t = getTransmitter(module);
			if(t != null)
			{
				return new Boolean(set(t, module, name, value));
			}
		}
		return Boolean.FALSE;
	}
	
	public synchronized boolean set(String module, String name, Object value)
    throws NetworkException
	{
        if(!checkConnection()) throw new NetworkException("Not connected");
        if(bIsController)
        {
            try
            {
           		Parameter p = findParameter(module, name);
                if(p == null) return false;

			    String param = p.serialize(value);

                Object o = localSet(module, name, param);
                return ((Boolean)o).booleanValue();
            }catch(Exception e)
            {
                throw new NetworkException(e.getMessage());
            }
        }else
        {
		    return set(transmitter, module, name, value);
        }
	}

	protected boolean set(Controller controller, String module, String name, Object value)
	{
		String ret    = Boolean.FALSE.toString();
		try
		{
       		Parameter p = findParameter(module, name);
            if(p == null) return false;
            
			String param = p.serialize(value);

    	    ret = controller.sendCommand(Controller.SET + Controller.CMDSEP
			    + module + Controller.CMDSEP
		  	    + name 	+ Controller.SEP + param);
		}catch(IOException e)
		{
		}
		return Boolean.getBoolean(ret);
	}
	
	public synchronized boolean set(Parameter p)
    throws NetworkException
	{
		if(transmitter == null)
			return false;
		
		String name   = p.getName();
		String module = p.getModuleDescription().getName();

		return set(module, name, p.getValue());
	}

	protected Object[] localGet(String module, String name) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NetworkException
	{
		ModuleDescription d = getModuleDescription();

		if(module == null || d.getName().equalsIgnoreCase(module))
		{
			NetworkInterface ni = d.getInterface(name);
			if(ni != null && ni instanceof Parameter)
			{
				String first = "";
				String rest  = "";
				if(name.length() > 0)
				{
					first = name.substring(0, 1);
					first = first.toUpperCase();
				}
				if(name.length() > 1)
				{
					rest = name.substring(1, name.length());
					// rest = rest.toLowerCase();
				}
				name = first + rest;
			
				System.out.println("Invoking Method: get" + name);
				Method method = ni.getListener().getClass().getMethod("get" + name, new Class[]{});
				return new Object[]{(Parameter)ni, method.invoke(ni.getListener(), new Object[]{})};
			}
		}else
		{
			Controller t = getTransmitter(module);
			if(t != null)
			{
				return new Object[]{findParameter(module, name), get(t, module, name)};
			}
		}
		return null;
	}
	
	public synchronized Object get(String module, String name)
    throws NetworkException
	{
        if(!checkConnection()) throw new NetworkException("Not connected");
        if(bIsController)
        {
            try
            {
                Object[] o = localGet(module, name);
                return o[1];
            }catch(Exception e)
            {
                throw new NetworkException(e.getMessage());
            }
        }else
        {
		    return get(transmitter, module, name);
        }
	}
	
	public ModuleDescription findModule(String module)
	{
		ModuleDescription myMod = getModuleDescription();
		
		if(module == null || myMod.getName().equalsIgnoreCase(module))
		{
			return myMod;
		}
	
        ModuleDescription d = findForeignModule(module);
    
        if(d == null && transmitter != null && ! bIsController)
        {
	        fetchDescription(transmitter); 
            return findForeignModule(module);
        }
    
		return d;
	}

    private ModuleDescription findForeignModule(String module)
    {
		for(int i=0; i < getModulesCount(); i++)
		{
			ModuleDescription d = getModuleDescription(i);
			
			if(d.getName().equalsIgnoreCase(module))
			{
				return d;
			}
		}
        return null;
    }

	private NetworkInterface findInterface(String module, String name)
	{
		ModuleDescription d = findModule(module);
		
		if(d != null)
		{
			NetworkInterface ni = d.getInterface(name);

			if(ni != null)
			{
				return ni;
			}
		}
		return null;
	}		
	
	private Parameter findParameter(String module, String name)
	{
		NetworkInterface ni = findInterface(module, name);
		if(ni instanceof Parameter)
		{
			return (Parameter)ni;
		}
		return null;
	}

	private Command findCommand(String module, String name)
	{
		NetworkInterface ni = findInterface(module, name);
		if(ni instanceof Command)
		{
			return (Command)ni;
		}
		return null;
	}
	
	protected Object get(Controller controller, String module, String name)
    throws NetworkException
	{
		String ret = null;
		try
		{
			ret = controller.sendCommand(Controller.GET + Controller.CMDSEP
			+ module 	+ Controller.CMDSEP
			+ name);
	
	
			Parameter p = findParameter(module, name);
            if(p == null) return null;

			if(ret != null)
			{
				return Controller.deserializeObject(p, ret);		
			}
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	public synchronized Object get(Parameter p)
    throws NetworkException
	{
		if(transmitter == null)
			return null;

		String name   = p.getName();
		String module = p.getModuleDescription().getName();
	
		return get(module, name);	
	}
	
	protected Object call(Controller controller, String module, String name, String params) throws NetworkException
	{
		String ret = null;
		try
		{
			ret = controller.sendCommand(Controller.CALL + Controller.CMDSEP
			+ module + Controller.CMDSEP
			+ name 	+ Controller.PSEP
			+ params);

            if(ret.startsWith(Controller.UNKNOWN))
            {
                ret = ret.replaceAll(Controller.UNKNOWN, "");
                throw new NetworkException("Unknown Command " + ret + " : " + module + " " + name); 
            }else if(ret.startsWith(Controller.ERROR))
            {
                ret = ret.replaceAll(Controller.ERROR, "");
                throw new NetworkException("Remote Exception: " + ret);
            }

			Command c = findCommand(module, name);
			if(c != null && ret != null)
			{
				return Controller.deserializeObject(c.getReturnParameter(), ret);		
			}
		}catch(IOException e)
		{
		}
		return ret;
	}
	
	protected Object call(String module, String name, String params) throws NetworkException
	{
		return call(transmitter, module, name, params);
	}
	
	public synchronized Object call(String module, String name, Object[] params) throws NetworkException
	{
        if(!checkConnection()) throw new NetworkException("Not connected");
		String stringParams = "";
		if(params != null)
		{
            Command c = findCommand(module, name);
            if(c == null)
            {
                return null;
            }
    		for(int i=0; i < c.getParameterCount(); i++)
			{
    			stringParams += c.get(i).serialize(params[i]);
                if(i < c.getParameterCount() - 1)
					stringParams += Controller.PSEP;
    		}
		}
		System.out.println("Invocing " + module + " " + name);
        if(bIsController)
        {
            try
            {
                Object[] o = localCall(module, name, stringParams);
                return o[1];
            }catch(Exception e)
            {
                throw new NetworkException(e.getMessage());
            }
        }else
        {
		    return call(module, name, stringParams);
        }
	}
	
	public synchronized Object call(Command c) throws NetworkException
	{
		String name   = c.getName();
		String module = c.getModuleDescription().getName();
		Object[] params = new Object[c.getParameterCount()];

		for(int i=0; i < c.getParameterCount(); i++)
		{
			params[i] = c.get(i).getValue();			
		}
		return call(module, name, params);
	}

	protected Object[] localCall(String module, String name, String params) throws Exception
	{

		ModuleDescription d = getModuleDescription();

		if(module == null || d.getName().equalsIgnoreCase(module))
		{
			NetworkInterface ni = d.getInterface(name);
			if(ni != null && ni instanceof Command)
			{
				// name = name.toLowerCase();
				
				Command c = (Command)ni;
					
				String[] stringParams = params.split(Controller.PSEP);
				Object[] objParams = new Object[c.getParameterCount()];
				Class[] classes = new Class[c.getParameterCount()];
					
       			        // System.out.println("Params ("+c.getParameterCount()+"):");
				for(int j=0; j < c.getParameterCount(); j++)
				{
					String value = stringParams[j];
                        // System.out.println(value);
					objParams[j] = Controller.deserializeObject(c.get(j), value);
                        // System.out.println(objParams[j]);
					classes[j] = c.get(j).getValueClass();
				}

				Method method = ni.getListener().getClass().getMethod(name, classes);

				// System.out.println("Invocing " + method.getName());

                if(bIsController)
                    DatabaseAccess.startSession();

                Object[] ret = null;

                try
                {
                    ret = new Object[]{c.getReturnParameter(), method.invoke(ni.getListener(), objParams)};
                }catch(Exception e)
                {
                    if(bIsController)
                        DatabaseAccess.rollback();
                    throw e;
                } 

                if(bIsController)
                    DatabaseAccess.commitSession();

                return ret;
			}
		}else
		{
			Controller t = getTransmitter(module);
			if(t != null)
			{
				return new Object[]{findCommand(module,name).getReturnParameter(),call(t, module, name, params)};
			}
		}
		return null;
	}

	private void fetchDescription(Controller controller)
	{
		try
		{
            if(!isController())
            {
			    String list = controller.sendCommand(Controller.LIST);
		    	String[] modules = list.split(Controller.SEP);
		    	lock();
			    ModuleDescription myDesc = getModuleDescription();
			    this.modules.clear();
			    this.modules.add(myDesc);
			    for(int i=0; i<modules.length; i++)
			    {
				    if(!modules[i].equalsIgnoreCase(myDesc.getName()))
				    {
					    String desc = controller.sendCommand(Controller.DESCRIBE + Controller.CMDSEP + modules[i]);
					    ModuleDescription d = new ModuleDescription(desc);
					    this.modules.add(d);
					    registerModule(d);
				    }
			    }
			    unlock();
            }else
            {
			    String desc = controller.sendCommand(Controller.DESCRIBE);
			    ModuleDescription d = new ModuleDescription(desc);
                ModuleDescription oldDesc = findForeignModule(d.getName());
                lock();
                this.modules.remove(oldDesc);
			    this.modules.add(d);
			    registerModule(d);
                unlock();
            }
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void initModule(String server, int port)
	{
		if(transmitter == null)
		{
			connectToController(server, port, true); // transmitter
		}
		if(receiver == null)
		{
			connectToController(server, port, false); // receiver
		}
	}

	private void connectToController(String host, int port, boolean isTransmitter)
	{
		try
		{
			Socket clientSocket = new Socket(host, port);
			ModuleDescription d = this.getModuleDescription();
			Controller c = new Controller(clientSocket, isTransmitter, VERSION, d.getName(), d.getSerial());
			if(isTransmitter)
			{
				c.startTransmitter();
				System.out.println("Receiver connected");
				transmitter = c;
				fetchDescription(transmitter);
            }else
            {
				c.startReceiver();
				System.out.println("Transmitter connected");
				receiver = c;
			}
		}catch(UnknownHostException e)
		{
			System.out.println("Unknown Host: " + e.getMessage());
		}catch(IOException e)
		{
			System.out.println("Initialisation of the module failed: " + e.getMessage());
		}
	}	

	private void executeModule()
	{
	}

	private void initController(int port)
	{
		try
		{
			serverSocket = new ServerSocket(port);
		}catch(IOException e)
		{
			System.out.println("Could not bind to local port: " + e.getMessage());
		}
	}

	private void executeController()
	{
		if(serverSocket != null)
		{
			try
			{
				ModuleDescription d = this.getModuleDescription();
				//Wait For Clients
				Controller c = new Controller(serverSocket.accept(), VERSION, d.getName(), d.getSerial());
				String id = c.getIdentifier();

				if(id == null)
				{
					System.out.println("Handshake failed");
					return;
				}

                if(c.isTransmitter())
                {
					c.startTransmitter();
					setTransmitter(id, c);
					fetchDescription(c);
					System.out.println("Module " + c.getIdentifier() + ": Transmitter started");
				}else
				{
					c.startReceiver();
					setReceiver(id, c);
					System.out.println("Module " + c.getIdentifier() + ": Receiver started");
				}
				
			}catch(IOException e)
			{
				System.out.println("Initialisation of the controller failed: " + e.getMessage()); 
			}
		}
	}
	
	public void run()
	{
        bStillRunning = true;
		while(bRunning)
		{
			if(bIsController)
			{
				executeController();
			}else
			{
				executeModule();
			}
		}
        bStillRunning = false;
	}
    
    public boolean checkConnection()
    {
        if(!isController() && !isConnected())
        {
           startModule(server, port);
           return isConnected();
        }
        return true;
    }

    public boolean isConnected()
    {
        if(receiver != null && receiver.isConnected() && transmitter != null && transmitter.isConnected())
            return true;
        else
            return false;
    }
	
	public synchronized void startModule(String server, int port) 
	{
        if(bRunning) stopModule();

        this.server = server;
        this.port = port;
		bRunning = true;
		bIsController = false;
		initModule(server, port);

        // WEB FIX
        if(connectionChecker == null && !getModuleDescription().getName().equalsIgnoreCase("WEB"))
        {
            connectionChecker = new Thread()
            {
                public void run()
                {
                    while(true)
                    {
                        checkConnection();
                        try
                        {
                            sleep(checkTime);
                        }catch(InterruptedException e)
                        {
                        }
                    }
                }
            };
            connectionChecker.start();
        }
        
		// this.start();
	}

	public void startController(int port) 
	{
		bRunning = true;
		bIsController = true;
		initController(port);
		this.start();
	}
	
	public void stopModule()
	{
		bRunning = false;
        if(receiver != null)
            receiver.stopReceiver();
        if(transmitter != null)
        	transmitter.stopTransmitter();
	}
	
	public void stopController()
	{
		bRunning = false;
        stopAllTransmitters();
	}
}

