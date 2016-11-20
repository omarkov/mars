package server.domain;

public class NumericType extends AttributeType
{	
	private Integer min;
	private Integer max;
	private Integer step;
	
	private Integer defaultVal;

	public NumericType() {
		defaultVal = new Integer(0);
		min = new Integer(0);
		max = new Integer(999);
		step = new Integer(1);
	}

	/**
	 * @return Returns the max.
	 */
	public Integer getMax() {
		return max;
	}

	/**
	 * @param max The max to set.
	 */
	public void setMax(Integer max) {
		this.max = max;
	}

	/**
	 * @return Returns the min.
	 */
	public Integer getMin() {
		return min;
	}

	/**
	 * @param min The min to set.
	 */
	public void setMin(Integer min) {
		this.min = min;
	}

	/**
	 * @return Returns the step.
	 */
	public Integer getStep() {
		return step;
	}

	/**
	 * @param step The step to set.
	 */
	public void setStep(Integer step) {
		this.step = step;
	}

	public void setDefaultValue(Object def)
	{
		defaultVal = (Integer)def;
	}

	public Integer getDefaultValue() {
		return defaultVal;
	}

	public Class getBasicType() {
		return Integer.class;
	}

	public NumericValue getDefaultAttributeValue() {
		return new NumericValue(defaultVal);
	}
}
