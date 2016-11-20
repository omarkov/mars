package server.domain;

public class StringValue extends ComponentAttributeValue
{
    private String val;
  
    public StringValue() {
    	super();
    }

    public StringValue(String aValue) {
    	val = aValue;
    }

    public String getValue(){
    	return val;
    }
    
    public void setValue(String value){
	    setValue((Object)value);
    }

    public void setValue(Object value){
    	val = (String)value;
    }

	public Class getType() {
		return StringType.class;
	}
}
