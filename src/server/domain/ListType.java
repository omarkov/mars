package server.domain;

import java.util.*;

/**
 * <p></p>
 */
public class ListType extends AttributeType
{
	public static final String AUDIO_LIST = "audio";
	public static final String VIDEO_LIST = "video";
	public static final String IMAGE_LIST = "image";

	public static final String GENERIC_LIST = "generic";
	
	private Integer defaultVal;
	private String contentType;

	public ListType() {
		contentType = GENERIC_LIST;
		defaultVal = new Integer(0);
	}

	/**
	 * @param defaultValue The defaultValue to set.
	 */
	public void setDefaultValue(Object defaultValue) {
		defaultVal = (Integer)defaultValue;
	}

	public Class getBasicType() {
		return List.class;
	}

	/**
	 * @return Returns the contentType.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType The contentType to set.
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Integer getDefaultValue() {
		return this.defaultVal;
	}

	public ComponentAttributeValue getDefaultAttributeValue() {
		return new ListValue();
	}
	
	public Boolean isAudioList() {
		return this.contentType.equals(AUDIO_LIST);
	}

	public Boolean isVideoList() {
		return this.contentType.equals(VIDEO_LIST);
	}

	public Boolean isImageList() {
		return this.contentType.equals(IMAGE_LIST);
	}
	
	public Boolean isGenericList() {
		return this.contentType.equals(GENERIC_LIST);
	}

}
