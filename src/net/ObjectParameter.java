package net;

import net.xstream.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class ObjectParameter extends Parameter {
	public static final String TYPE = "OBJ";
	private Class cls;
	private static ObjectStream stream = new ObjectStream();

	protected ObjectParameter(String mstring)
	{ 
		super(TYPE);
		this.unmarshall(mstring);
	}
	
	public ObjectParameter()
	{
		super(TYPE, "RETURN", null);
		setDefault(null);
		setFlags(null);
	}

	public ObjectParameter(Class cls)
	{
		super(TYPE, "RETURN", null);
		this.cls = cls;
		setDefault(null);
		setFlags(null);
	}

	
	public ObjectParameter(String name, Class cls)
	{
		super(TYPE, name, null);
		setDefault(null);
		setFlags(null);
		this.cls = cls;
	}

	public ObjectParameter(String name, String flags, NetworkEventListener listener, Class cls)
	{
		super(TYPE, name, listener);
		this.cls = cls;
		setDefault(null);
		setFlags(flags);
	}

    public String serialize(Object obj)
    {
		return serializeObject(obj);
    }

    public static String serializeObject(Object obj)
    {
		return stream.toXML(obj);
    }
	
	public Class getValueClass()
	{
		return cls;
	}
	
	protected Class getCls()
	{
		return cls;
	}
	
	public Object getValueObject(String value)
	{
		return getValueObject(value, cls, 0);
	}
	
	private Object getValueObject(String value, Class cls, int depth)
	{
		return _getValueObject(value, cls, depth);
	}
	
	private static boolean supportedType(Class cls)
	{
		return cls == Integer.class || cls == String.class || cls == Boolean.class;
	}

	protected static Object _getValueObject(String value, Class cls, int depth)
	{
		return stream.fromXML(value);
	}

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#unmarshall(java.lang.String)
	 */
	public void unmarshall(String mstring) {
		StringTokenizer t = new StringTokenizer(mstring, Controller.CMDSEP);
		t.nextToken();
		this.setName(t.nextToken());
		this.setFlags(t.nextToken());
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
		
		if(getFlags() != null)
		{	
		 	mstring += Controller.CMDSEP 
			+ this.getFlags();
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
