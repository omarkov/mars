package server.domain;

import java.util.*;
import java.lang.*;



/**
 * <p></p>
 */
public class ComponentAttribute {

	
    public static final String FLAG_SELECTABLE = "s";
    public static final String FLAG_READONLY = "r";
    public static final String FLAG_WRITEONLY = "w";
    public static final String FLAG_READWRITE = "rw";

    private Long id;
    private String name;
    private String flag;
    
    public AttributeType attributeType;
    
    public SmartRoomComponent smartRoomComponent;
    
    /**
	 * 
	 */
	public ComponentAttribute() {
		this.name = "";
		this.flag = "";		
	}

	public Long getId(){
    	return this.id;
    }
    
    public void setId(Long id){
    	this.id = id;
    }
    
    public String getName(){
    	return this.name;
    }
    
    public void setName(String name){
    	if (name == null) {
    		this.name = "";
    	} else {
    		this.name = name;
    	}
    }

    public boolean isSelectable(){
	return flag.matches(".*s.*");
    }
    
    public boolean isEditable(){
    	return flag.matches(".*w.*");
    }

    public void setEditable(boolean v){}

    public boolean isReadOnly(){
    	return ((! flag.matches(".*w.*")) && (flag.matches(".*r.*")));
    }

    public void setReadOnly(boolean v){}
       
    public AttributeType getAttributeType(){
    	return this.attributeType;
    }
    
    public void setAttributeType(AttributeType attributeType){
    	this.attributeType = attributeType;
    }
    
    public SmartRoomComponent getSmartRoomComponent(){
    	return this.smartRoomComponent;
    }
    
    public void setSmartRoomComponent(SmartRoomComponent smartRoomComponent){
    	this.smartRoomComponent = smartRoomComponent;
    	if (smartRoomComponent != null && ! smartRoomComponent.getComponentAttributes().contains(this)) {
        	smartRoomComponent.getComponentAttributes().add(this);
    	}
    }
    
    public ComponentAttributeValue getDefaultAttributeValue() {
    	ComponentAttributeValue cv = this.getAttributeType().getDefaultAttributeValue();
    	cv.setComponentAttribute(this);
    	return cv;
    }

    public void setDefaultValue(Object def) {
    	this.getAttributeType().setDefaultValue(def);
    }

    public Object getDefaultValue() {
    	return this.getAttributeType().getDefaultValue();
    }

	/**
	 * @return Returns the flag.
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag The flag to set.
	 */
	public void setFlag(String flag) {
		if (flag == null) {
			this.flag = "";
		} else {
			this.flag = flag;
		}
	}
    
    
    
    
    
 }
