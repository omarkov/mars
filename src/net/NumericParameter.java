/*
 * Created on 13.06.2005
 */
package net;

import java.util.StringTokenizer;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class NumericParameter extends Parameter {
	private int min;
	private int max;
	private int step;
	
	public static final String TYPE = "NUMERIC"; 
	
	protected NumericParameter(String mstring)
	{
		super(TYPE);
		this.unmarshall(mstring);
	}
	
	public NumericParameter()
	{
		super(TYPE, "RETURN", null);
		setDefault(null);
		setFlags(null);
		setMin(0);
		setMax(0);
		setStep(0);
	}
	
	public NumericParameter(String name, Integer def)
	{
		super(TYPE, name, null);
		setDefault(def);
		setFlags(null);
		setMin(0);
		setMax(0);
		setStep(0);
	}

	public NumericParameter(String name, Integer def, String flags, int min, int max, int step, NetworkEventListener listener)
	{
		super(TYPE, name, listener);
		setDefault(def);
		setFlags(flags);
		setMin(min);
		setMax(max);
		setStep(step);
	}
	
	public Class getValueClass()
	{
		return Integer.class;
	}

	public Object getValueObject(String value)
	{
		return NumericParameter._getValueObject(value);
	}
	
	public static Object _getValueObject(String value)
	{
		value = value.trim();
		return new Integer(value);
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
		this.setMin(Integer.parseInt(t.nextToken()));
		this.setMax(Integer.parseInt(t.nextToken()));
		this.setStep(Integer.parseInt(t.nextToken()));
	}

	/* (non-Javadoc)
	 * @see mars.net.NetworkInterface#marshall()
	 */
	public String marshall() {
		String mstring = super.marshall();
		if(getDefault() != null)
		{
			mstring += Controller.CMDSEP 
			+ ((Integer)this.getDefault()).toString();
		}
		if(getFlags() != null)
		{ 
			mstring += Controller.CMDSEP + this.getFlags() + Controller.CMDSEP
			+ Integer.toString(this.getMin()) + Controller.CMDSEP
			+ Integer.toString(this.getMax()) + Controller.CMDSEP
			+ Integer.toString(this.getStep()); 
		}
		return mstring;
	}

	/**
	 * @return Returns the max.
	 */
	public int getMax() {
		return max;
	}
	/**
	 * @param max The max to set.
	 */
	public void setMax(int max) {
		this.max = max;
	}
	/**
	 * @return Returns the min.
	 */
	public int getMin() {
		return min;
	}
	/**
	 * @param min The min to set.
	 */
	public void setMin(int min) {
		this.min = min;
	}
	/**
	 * @return Returns the step.
	 */
	public int getStep() {
		return step;
	}
	/**
	 * @param step The step to set.
	 */
	public void setStep(int step) {
		this.step = step;
	}
}
