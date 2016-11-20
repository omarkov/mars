/*
 * Created on 13.06.2005
 */
package net;

import java.util.*;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class ListParameter extends Parameter {
	public static final String TYPE = "LIST";
	private Class cls;
    private String info = null;
	
	protected ListParameter(String mstring)
	{ 
		super(TYPE);
		this.unmarshall(mstring);
	}
	
	public ListParameter()
	{
		super(TYPE, "RETURN", null);
		setDefault(null);
		setFlags(null);
	}

	public ListParameter(Class cls)
	{
		super(TYPE, "RETURN", null);
		this.cls = cls;
		setDefault(null);
		setFlags(null);
	}

	public ListParameter(String name, Class cls)
	{
		super(TYPE, name, null);
		setDefault(null);
		setFlags(null);
		this.cls = cls;
	}

	public ListParameter(String name, Integer def, Class cls)
	{
		super(TYPE, name, null);
		setDefault(def);
		setFlags(null);
		this.cls = cls;
	}

	public ListParameter(String name, int def, String flags, String info, NetworkEventListener listener, Class cls)
	{
		super(TYPE, name, listener);
		this.cls = cls;
        this.info = info;
		setDefault(new Integer(def));
		setFlags(flags);
	}
	
	public Class getValueClass()
	{
		return List.class;
	}
	
	protected Class getCls()
	{
		return cls;
	}

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }
	
	public Object getValueObject(String value)
	{
		return getValueObject(value, cls, 0);
	}

    public String serialize(Object obj)
    {
        return serializeList(obj, 0);
    }

    public static String serializeList(Object obj, int depth)
    {
        String param = "";
        if(obj instanceof Integer || obj instanceof String || obj instanceof Boolean)
        {
            param = Parameter.serializeParameter(obj);
        }
        else if(obj instanceof List)
        {
		    List l = (List)obj;
		    int size = l.size();
		    if(l.size() == 0)
		    {
		        param += Controller.EMPTY;
		    }else
		    {
		        for(int j=0; j < size; j++)
		        {
                    param += serializeList(l.get(j), depth + 1);
			        if(j < size -1)
			        {
		  	            if(depth > 0)
			            {
				            param += Controller.SEP0 + depth + Controller.SEP1;
			            }else
			            {
			                param += Controller.SEP0 + Controller.SEP1;
			            }
                    }
			    }
		    }		
		}else
        {
            param = ObjectParameter.serializeObject(obj);
        }
        return param;
    }

	private Object getValueObject(String value, Class cls, int depth)
	{
		String values[];

		if(depth > 0)
			values = value.split(Controller.SEP0 + depth + Controller.SEP1);
		else
			values = value.split(Controller.SEP0 + Controller.SEP1);
			
		ArrayList l = new ArrayList();
		for(int i=0; i < values.length; i++)
		{
			if(values[i].length() > 0)
			{
				if(cls == Integer.class)
					l.add(NumericParameter._getValueObject(values[i]));
				else if(cls == String.class) 
					l.add(StringParameter._getValueObject(values[i]));
				else if(cls == Boolean.class)
					l.add(BooleanParameter._getValueObject(values[i]));
				else if(cls == List.class)
					l.add(getValueObject(values[i], String.class, depth + 1));
				else
					l.add(ObjectParameter._getValueObject(values[i], cls, depth + 1));
			}
		}
		return l;
	}
	
	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#unmarshall(java.lang.String)
	 */
	public void unmarshall(String mstring) {
		StringTokenizer t = new StringTokenizer(mstring, Controller.CMDSEP);
		t.nextToken();
		this.setName(t.nextToken());
		this.setDefault(new Integer(t.nextToken()));
		this.setFlags(t.nextToken());
        this.setInfo(t.nextToken());
		try {
			cls = PrimitiveLoader.loader.loadClass(t.nextToken());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#marshall()
	 */
	public String marshall() {
		String mstring = super.marshall();
		
		if(getDefault() != null)
		{
			mstring += Controller.CMDSEP
			+ ((Integer)getDefault()).toString();
		}
		if(getFlags() != null)
		{	
		 	mstring += Controller.CMDSEP
			+ this.getFlags();
		}
        if(getInfo() != null)
        {
            mstring += Controller.CMDSEP
            + this.getInfo();
        }
		if(cls != null)
		{
			mstring += Controller.CMDSEP
			+ cls.getName();
//			+ cls.getCanonicalName();
		}
		return mstring;
	}
}
