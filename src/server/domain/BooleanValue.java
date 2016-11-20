package server.domain;

public class BooleanValue extends ComponentAttributeValue
{
    private Boolean val;
    
    public BooleanValue() {
    	super();
    }

    public BooleanValue(boolean aValue) {
    	val = aValue;
    }
    
    public BooleanValue(Object aValue) {
    	setValue(aValue);
    }
    
	public void setValue(boolean value){
    	val = Boolean.valueOf(value);
    }

    public void setValue(Boolean value){
	    val = value;
    }

    public void setValue(Object value){
    	setValue((Boolean)value);
    }
    
	public Boolean getValue() {
		return val;
	}

	public Class getType() {
		return BooleanType.class;
	} 
}
