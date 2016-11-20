package server.domain;

public class BooleanType extends AttributeType
{
	private boolean defaultVal;

	public BooleanType() {
		defaultVal = false;
	}

	public Object getDefaultValue() {
		return new Boolean(defaultVal);
	}

	public BooleanValue getDefaultAttributeValue() {
		return new BooleanValue(defaultVal);
	}

	public Class getBasicType() {
		return Boolean.class;
	}

	/**
	 * @param defaultValue The defaultValue to set.
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultVal = (Boolean)defaultValue;
	}
}
