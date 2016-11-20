package web.java;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;


import server.domain.BooleanType;
import server.domain.BooleanValue;
import server.domain.ComponentAttribute;

import server.domain.ComponentSetting;
import server.domain.StringType;
import server.domain.StringValue;
import server.domain.ListValue;
import server.domain.ListType;

import server.domain.NumericType;
import server.domain.NumericValue;
import server.domain.SmartRoomComponent;

import server.domain.ComponentAttributeValue;
import java.util.*;


public class ProfileTest {
	
	private Set componentSettings = new HashSet();
	private Integer rotwert = new Integer(3);
	private List playlist = new ArrayList();

	private Logger logger = Logger.getLogger(ProfileTest.class);

	public ProfileTest()
	{

		
		//LightComponent
		SmartRoomComponent lightComponent = new SmartRoomComponent(); 
        lightComponent.setName("Light adjustment"); 
		ComponentSetting lightComponentSetting = new ComponentSetting(lightComponent);
		
		//First Parameter of the lightComponentSetting
		ComponentAttribute componentAttribute1 = new ComponentAttribute();
		componentAttribute1.setName("redlightintensity");
		NumericType type1 = new NumericType();
		componentAttribute1.setAttributeType(type1);
		NumericValue value1 = new NumericValue();
		value1.setValue(10);
		value1.setComponentAttribute(componentAttribute1);

							
		//Second Parameter of the lightComponentSetting
		ComponentAttribute componentAttribute2 = new ComponentAttribute();
		componentAttribute2.setName("greenlightintensity");
		NumericType type2 = new NumericType();
		componentAttribute2.setAttributeType(type2);
		NumericValue value2 = new NumericValue();
		value2.setValue(10);
		value2.setComponentAttribute(componentAttribute2);

		
		//Second Parameter of the lightComponentSetting
		ComponentAttribute componentAttribute3 = new ComponentAttribute();
		componentAttribute3.setName("lightShow");
		BooleanType type3 = new BooleanType();
		componentAttribute3.setAttributeType(type3);
		BooleanValue value3 = new BooleanValue();
		value3.setValue(true);
		value3.setComponentAttribute(componentAttribute3);

		
		Set lightComponentProperties = new HashSet();
		lightComponentProperties.add(value1);
		lightComponentProperties.add(value2);	
		lightComponentProperties.add(value3);
		
		lightComponentSetting.setComponentAttributeValues(lightComponentProperties);

		
/*       NumericValue valuex = new NumericValue();
       valuex.setValue(10);
       componentProperty1.setComponentAttribute(componentAttribute1);
       componentProperty1.setComponentAttributeValue(value1);
*/	    

		
		//Aircondition
		ComponentSetting airComponentSetting = new ComponentSetting();
		SmartRoomComponent airComponent = new SmartRoomComponent();
		airComponent.setName("Airconditioning");
		airComponentSetting.setSmartRoomComponent(airComponent);

		ComponentAttribute componentAttribute4 = new ComponentAttribute();
		componentAttribute4.setName("temperature");
		NumericType type4 = new NumericType();
		componentAttribute4.setAttributeType(type4);
		NumericValue value4 = new NumericValue();
		value4.setValue(30);
		value4.setComponentAttribute(componentAttribute4);
		
		Set airComponentProperties = new HashSet();
		airComponentProperties.add(value4);

		airComponentSetting.setComponentAttributeValues(airComponentProperties);
		
		//Playlists		
		SmartRoomComponent playlistComponent = new SmartRoomComponent();
		playlistComponent.setName("PLAYLISTS");
		ComponentSetting playlistComponentSetting = new ComponentSetting(playlistComponent);
		playlistComponentSetting.setSmartRoomComponent(playlistComponent);
		
		// AUDIOLIST
		ComponentAttribute componentAttribute5 = new ComponentAttribute();
		componentAttribute5.setName("Audiolist");
		// Type of the component  
		ListType listtype = new ListType();		
		listtype.setContentType(ListType.AUDIO_LIST);
		componentAttribute5.setAttributeType(listtype);
		
		//value of the component
		List playlist1 = new ArrayList();
		playlist1.add("song1.mp3");
		/*playlist1.add("song2.wav");
		playlist1.add("song3.wav");
		playlist1.add("ghkghkglkhlhjlhjlhjlhjlhlhlhljhljhlhjlhlfjfdirj.mp3");
		playlist1.add("lied.mp3");
		playlist1.add("lied.mp3");
		playlist1.add("fdjdjf.mp3");*/
		playlist1.add("look_here.mp3");
		playlist1.add("dh.mp3");	
		
		ListValue  listvalue = new ListValue();
		listvalue.setValue(playlist1);
		
		listvalue.setComponentAttribute(componentAttribute5);
		
		// VideoLIST
		ComponentAttribute componentAttribute6 = new ComponentAttribute();
		componentAttribute5.setName("VIDEOlist");
		// Type of the component  
		ListType listtype2 = new ListType();
		listtype2.setContentType(ListType.VIDEO_LIST);
		componentAttribute6.setAttributeType(listtype2);
		
		//value of the component
		List playlist2 = new ArrayList();
		playlist2.add("vid01.mpg");
		/*playlist2.add("vid02.avi");
		playlist2.add("vid03.mpg");
		playlist2.add("vid04.avi");
		playlist2.add("vid05.mpg");
		playlist2.add("shsdf.avi");
		playlist2.add("vid01.mpg");
		playlist2.add("vid02.avi");
		playlist2.add("vid01.mpg");
		playlist2.add("vid02.avi");*/
		playlist2.add("vid01.mpg");
		playlist2.add("vid02avi");	
		
		ListValue  listvalue2 = new ListValue();
		listvalue2.setValue(playlist2);
		listvalue2.setComponentAttribute(componentAttribute6);
		
		 // the set of properties  to the to component Setting
		 Set playlistProperties = new HashSet();
		 playlistProperties.add(listvalue);
		 playlistProperties.add(listvalue2);
		 
		 playlistComponentSetting.setComponentAttributeValues(playlistProperties);		 
		 
		//add to component Setting
		componentSettings.add(lightComponentSetting);
		componentSettings.add(airComponentSetting);
		componentSettings.add(playlistComponentSetting);
/*			} catch (IllegalProfileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/

		
	}


	public Set getComponentSettings() {
		return componentSettings;
	}


	public void setComponentSettings(Set componentSettings) {
		this.componentSettings = componentSettings;
	}





}
