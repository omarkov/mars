/*
 * Created on 13.06.2005
 */
package net;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public abstract class Parameter implements NetworkInterface  {
	private Object defValue;
	private Object value;
	private String flags;
	private String name;
	private String type;
	private ModuleDescription module;
	private NetworkEventListener listener;
	
	public Parameter(String type)
	{
		setListener(null);
		setType(type);
	}
	
	public Parameter(String type, String name, NetworkEventListener listener)
	{
		setListener(listener);
		setType(type);
		setName(name);
	}

    public String serialize(Object obj)
    {
        return serializeParameter(obj);
    }

    public static String serializeParameter(Object obj)
    {
        String param = "";
        if(obj != null)
        {
            String val = (String)obj.toString();
            param = val.replaceAll("[\\s]", Controller.SPACE);
            param = param.replaceAll("[\\n\\r]", Controller.RETURN);
        }
        if(param.length() == 0)
        {
            param = Controller.EMPTY;
        }
        return param;
    }
	
	public Object clone() throws CloneNotSupportedException
	{
		Parameter c = (Parameter)super.clone();
		c.defValue = defValue;
		c.value = value;
		c.flags = flags;
		c.name = name;
		c.type = type;
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
	
	public abstract Class getValueClass();
	public abstract Object getValueObject(String value);
	
	protected Class getCls()
	{
		return getValueClass();
	}
	
	public void setValue(Object value)
	{
		this.value = value;
	}
	
	public Object getValue()
	{
		return value;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getType()
	{
		return type;
	}

	public Object getDefault()
	{
		return defValue;
	}
	public void setDefault(Object def)
	{
	    setValue(def);
		this.defValue = def;
	}
	
	public String getFlags()
	{
		return flags;
	}
	
	public void setFlags(String flags)
	{
		this.flags = flags;
	}

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#unmarshall(java.lang.String)
	 */
	public void unmarshall(String mstring) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#marshall()
	 */
	public String marshall() {
		return getType() + Controller.CMDSEP + getName();
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
