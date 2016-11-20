package server.domain;

/**
 * <p></p>
 */
public abstract class AttributeType
{	
    	private Long id;

	public abstract void setDefaultValue(Object val);	
	public abstract Object getDefaultValue();	
	public abstract ComponentAttributeValue getDefaultAttributeValue();
 	public abstract Class getBasicType();

	public AttributeType() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
 }
