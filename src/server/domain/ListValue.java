package server.domain;

import java.util.*;

/**
 * <p></p>
 */
public class ListValue extends ComponentAttributeValue
{
	public List val = new ArrayList();
   
	public ListValue() {
		super();
	}

/*    	public ListValue(List aValue) {
    		def = aValue;
    	}	*/
	
    	public List getValue(){
    		return val;
    	}
    
    	public void setValue(List value){
		setValue((Object)value);
	}

    	public void setValue(Object value){
    		val =  (List)value;
    	}

	public Class getType() {
		return ListType.class;
	}
}
