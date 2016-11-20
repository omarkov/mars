package server.domain;

public class NumericValue extends ComponentAttributeValue
{
    private int val;
  
    public NumericValue() {
    	super();
    }
    public NumericValue(int aValue) {
    	val = aValue;
    }
    
    public NumericValue(Object aValue) {
    	setValue(aValue);
    }

    public void setValue(Integer value){
    	setValue((Object)value);
    }

    public void setValue(Object value){
    	val = ((Integer)value).intValue();
    }

    public void setValue(int value) {
    	val = value;
    }
    
    public Integer getValue() {
		return new Integer(val);
	}

	public Class getType() {
		return NumericType.class;
	}
}
