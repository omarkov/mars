package server.domain;

/**
 * <p></p>
 */
public abstract class ComponentAttributeValue
{
    private Long id;
    private ComponentAttribute componentAttribute;
    private ComponentSetting setting;


    public ComponentAttributeValue() {

    }
 
    public Long getId(){
    	return this.id;
    }
    
    public void setId(Long id){
    	this.id = id;
    }
   
    public abstract Class getType();
    public abstract Object getValue();
    public abstract void setValue(Object o);

	/**
	 * @return Returns the componentAttribute.
	 */
	public ComponentSetting getSetting() {
		return setting;
	}

	/**
	 * @param componentAttribute The componentAttribute to set.
	 */
	public void setSetting(ComponentSetting setting) {
		this.setting = setting;
	}


	/**
	 * @return Returns the componentAttribute.
	 */
	public ComponentAttribute getComponentAttribute() {
		return componentAttribute;
	}

	/**
	 * @param componentAttribute The componentAttribute to set.
	 */
	public void setComponentAttribute(ComponentAttribute componentAttribute) {
		this.componentAttribute = componentAttribute;
	}
 }
