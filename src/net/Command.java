/*
 * Created on 13.06.2005
 */
package net;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class Command implements NetworkInterface {
	private String name;
	private Vector parameters;
	private Parameter returnParameter;
	private ModuleDescription module;
	private NetworkEventListener listener;

	protected Command(String mstring)
	{
		setListener(null);
		parameters = new Vector();
		this.unmarshall(mstring);
	}
	
	public Command(String name, Parameter returnParameter, NetworkEventListener listener)
	{
		setListener(listener);
		setName(name);
		setReturnParameter(returnParameter);
		parameters = new Vector();
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		Command c = (Command)super.clone();
		c.returnParameter = returnParameter;
		c.name = name;
		c.parameters = new Vector();
		for(int i=0; i<getParameterCount(); i++)
		{
			c.parameters.add(get(i).clone());
		}
		c.module = null;
		return c;
	}	
	
	public void setModuleDescription(ModuleDescription module)
	{
		this.module = module;
	}
	
	public ModuleDescription getModuleDescription()
	{
		return module;
	}
	
	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	public boolean addParameter(Parameter p) {
		return parameters.add(p);
	}
	
	public Parameter get(int n) {
		return (Parameter)parameters.get(n);
	}
	
	public int getParameterCount() {
		return parameters.size();
	}
	
	/**
	 * @return Returns the returnType.
	 */
	public Parameter getReturnParameter() {
		return returnParameter;
	}
	/**
	 * @param returnType The returnType to set.
	 */
	public void setReturnParameter(Parameter returnParameter) {
		this.returnParameter = returnParameter;
	}

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#unmarshall(java.lang.String)
	 */
	public void unmarshall(String mstring) {
		String lines[] = mstring.split("\n");
		for(int i=0; i < lines.length; i++)
		{
			if(lines[i].startsWith("BEGIN COMMAND"))
			{
				StringTokenizer t = new StringTokenizer(lines[i], Controller.CMDSEP);
				t.nextToken();
				t.nextToken();
				this.setName(t.nextToken());
				String type = t.nextToken();
				Class cls = null;
				if(t.hasMoreElements())
				{
					try {
						String name = (String)t.nextToken();
						cls = PrimitiveLoader.loader.loadClass(name);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				if(type.startsWith("BOOL"))
				{
					this.setReturnParameter(new BooleanParameter());
				}else if(type.startsWith("NUMERIC"))
				{
					this.setReturnParameter(new NumericParameter());
				}else if(type.startsWith("STRING"))
				{
					this.setReturnParameter(new StringParameter());
				}else if(type.startsWith("LIST"))
				{
					this.setReturnParameter(new ListParameter(cls));
				}else if(type.startsWith("OBJ"))
				{
					this.setReturnParameter(new ObjectParameter(cls));
				}else
					this.setReturnParameter(null);
				
			}else if(lines[i].startsWith("END COMMAND"))
			{
				return;
			}else
			{
				StringTokenizer t = new StringTokenizer(lines[i], Controller.CMDSEP);
				String type = t.nextToken();
				String name = t.nextToken();
				
				if(type.equalsIgnoreCase(BooleanParameter.TYPE))
				{
					this.addParameter(new BooleanParameter(name, null));
				}else if(type.equalsIgnoreCase(ListParameter.TYPE))
				{
					this.addParameter(new ListParameter(name, null, null));
				}else if(type.equalsIgnoreCase(NumericParameter.TYPE)) 
				{
					this.addParameter(new NumericParameter(name, null));
				}else if(type.equalsIgnoreCase(StringParameter.TYPE))
				{
					this.addParameter(new StringParameter(name, null));
				}else if(type.equalsIgnoreCase(ObjectParameter.TYPE))
				{
					this.addParameter(new ObjectParameter(name, null));
				}
			}
		}
	}	

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#marshall()
	 */
	public String marshall() {
		String mstring;
		
		if(getReturnParameter() != null)
			mstring = "BEGIN COMMAND" + Controller.CMDSEP
			+ getName() + Controller.CMDSEP 
			+ getReturnParameter().getType() + Controller.CMDSEP + getReturnParameter().getCls().getName() + Controller.NL;
		else
			mstring = "BEGIN COMMAND" + Controller.CMDSEP
			+ getName() + Controller.CMDSEP
			+ "void" + Controller.CMDSEP + Controller.NL;
		
		
	    for(int i=0; i < this.getParameterCount(); i++)
	    {
	    	Parameter p = this.get(i);
	    	mstring += p.marshall() + "\n";
	    }
		
		mstring += "END COMMAND";
		return mstring;
	}
	/**
	 * @return
	 */
	public NetworkEventListener getListener()
	{
		return listener;
	}

	/**
	 * @param listener
	 */
	public void setListener(NetworkEventListener listener)
	{
		this.listener = listener;
	}

}
