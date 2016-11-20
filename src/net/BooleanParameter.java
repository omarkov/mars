/*
 * Created on 13.06.2005
 */
package net;

import java.util.StringTokenizer;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */ 
public class BooleanParameter extends Parameter {
	public static final String TYPE = "BOOL";
	
	protected BooleanParameter(String mstring)
	{
		super(TYPE);
		this.unmarshall(mstring);
	}

	public BooleanParameter()
	{
		super(TYPE, "RETURN", null);
		setDefault(null);
		setFlags(null);
	}
		
	public BooleanParameter(String name, Boolean def)
	{
		super(TYPE, name, null);
		setDefault(def);
		setFlags(null);
	}

	public BooleanParameter(String name, Boolean def, String flags, NetworkEventListener listener)
	{
		super(TYPE, name, listener);
		setDefault(def);
		setFlags(flags);
	}
	
	public Class getValueClass()
	{
		return Boolean.class;
	}
	
	public Object getValueObject(String value)
	{
		return _getValueObject(value);
	}	
	
	public static Object _getValueObject(String value)
	{
		value = value.trim();
		return Boolean.valueOf(value);
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
			+ ((Boolean)getDefault()).toString();
		}
		if(getFlags() != null)
		{
			mstring += Controller.CMDSEP
			+ this.getFlags();
		}
		return mstring;
	}
}
