/*
 * Created on 13.06.2005
 */
package net;

import java.util.StringTokenizer;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class StringParameter extends Parameter {
	public static final String TYPE = "STRING";
	
	protected StringParameter(String mstring)
	{
		super(TYPE);
		this.unmarshall(mstring);
	}
	
	public StringParameter(String name, String def)
	{
		super(TYPE, name, null);
		setDefault(def);
		setFlags(null);
	}
	
	public StringParameter()
	{
		super(TYPE, "RETURN", null);
		setDefault(null);
		setFlags(null);
	}
	
	public StringParameter(String name, String def, String flags, NetworkEventListener listener)
	{
		super(TYPE, name, listener);
		setDefault(def);
		setFlags(flags);
	}
	
	public Class getValueClass()
	{
		return String.class;
	}
	
	public Object getValueObject(String value)
	{
		return _getValueObject(value);
	}

	public static Object _getValueObject(String value)
	{
		return value;
	}
	
	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#unmarshall(java.lang.String)
	 */
	public void unmarshall(String mstring) {
		StringTokenizer t = new StringTokenizer(mstring, Controller.CMDSEP);
		t.nextToken();
		this.setName(t.nextToken());
		this.setDefault(getValueObject(t.nextToken()));
		this.setFlags(t.nextToken());
	}

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#marshall()
	 */
	public String marshall() {
		String mstring = super.marshall();
		
		if(getDefault() != null)
		{
			mstring += Controller.CMDSEP 
			+ ((String)getDefault()).toString();
		}
		if(getFlags() != null)
		{ 
			mstring += Controller.CMDSEP + this.getFlags();
		}
		
		return mstring;
	}

}
