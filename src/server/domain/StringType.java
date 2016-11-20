package server.domain;

public class StringType extends AttributeType
{	
	public String defaultVal;

	public StringType() {
		defaultVal = "";
	}

	public void setDefaultValue(Object defaultValue) {
		defaultVal = (String)defaultValue;
	}

	public Object getDefaultValue() {
		return defaultVal;
	}

	public Class getBasicType() {
		return String.class;
	}

	public StringValue getDefaultAttributeValue() {
		return new StringValue(defaultVal);
	}
}
