/*
 * Created on 13.06.2005
 */
package net;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class ModuleDescription implements NetworkInterface, Cloneable {
	private String name;
	private Vector interfaces;
	private Hashtable interfaceHash;
	private ModuleDescription module;
	private String serial;
	private NetworkEventListener listener;
	private boolean webModule = false;
	
	public void setListener(NetworkEventListener listener)
	{
		this.listener = listener;
	}
	
	public NetworkEventListener getListener()
	{
		return listener;
	}
	
	protected ModuleDescription(String mstring)
	{
		interfaces = new Vector();
		interfaceHash = new Hashtable();
		this.unmarshall(mstring);
	}
	
	public ModuleDescription(String name, String serial)
	{
		setName(name);
		setSerial(serial);
		setModuleDescription(this);
		interfaces = new Vector();
		interfaceHash = new Hashtable();
	}

	public Object clone() throws CloneNotSupportedException
	{
		ModuleDescription c = (ModuleDescription)super.clone();
		c.name = name;
		c.serial = serial;
		c.interfaces = new Vector();
		c.interfaceHash = new Hashtable();
		for(int i=0; i < getInterfaceCount(); i++)
		{
			NetworkInterface ni = getInterface(i);
			if(ni instanceof Parameter)
			{
				Parameter p = (Parameter)((Parameter)ni).clone();
				p.setModuleDescription(c);
				c.interfaces.add(p);
				c.interfaceHash.put(p.getName(), p);
			}else if(ni instanceof Command)
			{
				Command cmd = (Command)((Command)ni).clone();
				cmd.setModuleDescription(c);
				c.interfaces.add(cmd);
				c.interfaceHash.put(cmd.getName(), cmd);
			}
		}
		return c;
	}
	
	public ModuleDescription getModuleDescription()
	{
		return module;
	}
	
	public void setModuleDescription(ModuleDescription module)
	{
		this.module = module;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public boolean addInterface(NetworkInterface p) {
		p.setModuleDescription(this);
		interfaceHash.put(p.getName(), p);
		return interfaces.add(p);
	}
	public NetworkInterface getInterface(String name)
	{
		return (NetworkInterface)interfaceHash.get(name);
	}
	public NetworkInterface getInterface(int n) {
		return (NetworkInterface)interfaces.get(n);
	}
	public int getInterfaceCount() {
		return interfaces.size();
	}

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#unmarshall(java.lang.String)
	 */
	public void unmarshall(String mstring) {
		// mstring = mstring.toUpperCase();
		String[] lines = mstring.split("\n");
		for(int i=0; i < lines.length; i++)
		{
			String cmd = lines[i].toUpperCase();
			if(cmd.startsWith("BEGIN DESCRIPTION"))
			{
				StringTokenizer t = new StringTokenizer(lines[i], Controller.CMDSEP);
				t.nextToken();
				t.nextToken();
				this.setName(t.nextToken());
				this.setSerial(t.nextToken());
				this.setWebModule(Boolean.valueOf(t.nextToken().trim()).booleanValue());			}else if(cmd.startsWith("BOOL"))
			{
				this.addInterface(new BooleanParameter(lines[i]));
			}else if(cmd.startsWith("NUMERIC"))
			{
				this.addInterface(new NumericParameter(lines[i]));
			}else if(cmd.startsWith("STRING"))
			{
				this.addInterface(new StringParameter(lines[i]));
			}else if(cmd.startsWith("LIST"))
			{
				this.addInterface(new ListParameter(lines[i]));
			}else if(cmd.startsWith("OBJ"))
			{
				this.addInterface(new ObjectParameter(lines[i]));
			}else if(cmd.startsWith("BEGIN COMMAND"))
			{
				String desc = lines[i];
				for(i=i+1;!cmd.startsWith("END COMMAND") && i<lines.length; i++)
				{
					desc += "\n" + lines[i];
					cmd = lines[i].toUpperCase();
				}
				i=i-1;
				this.addInterface(new Command(desc));
			}else if(cmd.equalsIgnoreCase("END DESCRIPTION"))
			{
				return;
			}
		}
	}

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#marshall()
	 */
	public String marshall() {
		String mstring;
		
		mstring = "BEGIN DESCRIPTION " + getName() + Controller.CMDSEP + getSerial() + Controller.CMDSEP + isWebModule() + Controller.NL; 
	
	    for(int i=0; i < this.getInterfaceCount(); i++)
	    {
	    	NetworkInterface ni = this.getInterface(i);
	    	mstring += ni.marshall() + Controller.NL;
	    }
		
		mstring += "END DESCRIPTION";
		return mstring;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}

	public boolean isWebModule() {
		return webModule;
	}

	public void setWebModule(boolean webModule) {
		this.webModule = webModule;
	}
}
