
package server.controller;

import java.text.DateFormat;
import java.util.*;
import org.apache.log4j.Logger;

import net.*;

import server.controller.exceptions.*;
import server.domain.*;
import server.DatabaseAccess;
import server.*;

/**
 * @author Rajkumar
 *
 * Created on 27.07.2005
 *
 * Date 		Author 		Change
 * 09/08/05		RDE			created
 * 10/08/05		RDE			add/remove-methods
 */
public class SmartRoomProfileController  implements NetworkEventListener {

	private static SmartRoomProfileController _instance;
	private Logger logger = Logger.getLogger(SmartRoomProfileController.class);

	private SmartRoomProfileController() {
	}

	/**
	 * Creates a Profile with name and comment for the user with given userID
	 * 
	 * @param name
	 * @param comment
	 * @param userID
	 * @throws ElementCreationException
	 */
	public SmartRoomProfile createProfile(SmartRoomProfile profile, Long userID) throws ControllerException {
		User user = UserController.getInstance().getUser(userID);
		return createProfile(profile,user);
	}

	/**
	 * Creates a SmartRoomProfil and stores in DB.
	 * Note that a Profile is associated with a User. 
	 * 
	 * @param name
	 * @param comment
	 * @param user
	 * @throws ElementCreationException
	 */
	protected SmartRoomProfile createProfile(SmartRoomProfile profile, User user) throws ElementCreationException {
		try {
			profile.setLastChange(new Date());

            mergeProfile(profile, user);
			validateProfile(profile);

			return (SmartRoomProfile)DBAccessController.save(profile);
			
		} catch (MarsException e) {
			throw new ElementCreationException(e.getMessage());
		}
	}

    protected void removeIds(SmartRoomProfile profile)
    {
        profile.setId(null);
        for(ComponentSetting setting: (Set<ComponentSetting>)profile.getComponentSettings())
        {
            setting.setId(null);
            for(ComponentAttributeValue value: (Set<ComponentAttributeValue>)setting.getComponentAttributeValues())
            {
                value.setId(null);
            }
        }
    }

    protected void mergeProfile(SmartRoomProfile profile, User user) throws ControllerException
    {
        profile.setUser(user); 

        for(ComponentSetting s: (Set<ComponentSetting>)profile.getComponentSettings())
        {
            s.setSmartRoomComponent(SmartRoomComponentController.getInstance().getComponent(s.getSmartRoomComponent().getId()));
            for(ComponentAttributeValue v: (Set<ComponentAttributeValue>)s.getComponentAttributeValues())
            {
                v.setComponentAttribute((ComponentAttribute)DBAccessController.getObjectByID(ComponentAttribute.class, v.getComponentAttribute().getId()));
            }
        }
    }
	
	/**
	 * Updates a SmartRoomProfile.
	 * 
	 * @param id
	 * @param name
	 * @param comment
	 * @param group
	 * @param user
	 * @throws ElementUpdateException
	 */
	public Boolean updateProfile(SmartRoomProfile profile, Long userID) throws ElementUpdateException{
		
		try {
			validateProfile(profile);

			User user = UserController.getInstance().getUser(userID);
            mergeProfile(profile, user);
			DBAccessController.save(profile);
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		} 
        return Boolean.TRUE;
	}
	
	/**
	 * Validates a Profile.
	 * 
	 * @Param profile
	 * @throws ElementValidationException
	 */
	public static void validateProfile(SmartRoomProfile profile)
			throws ElementValidationException {

		if( profile.getName() == null || profile.getName().length() == 0) {
			throw new ElementValidationException("Der NAme eines Profils darf nicht leer sein");
		}
	}
	
	/**
	 * Creates thze defaultSetting for the given component
	 * 
	 * @param c
	 * @return
	 * @throws MarsException
	 */
	private ComponentSetting makeSetting(SmartRoomComponent c) throws MarsException {
		logger.warn("Make Setting for Component" + c.getName());
		ComponentSetting setting = new ComponentSetting();
		setting.setSmartRoomComponent(c);
		for(Iterator i = c.getComponentAttributes().iterator(); i.hasNext();) {
			ComponentAttribute ca = (ComponentAttribute)i.next();
			ComponentAttributeValue cav = ca.getDefaultAttributeValue();

			setting.addComponentAttributeValue(cav);
			cav.setSetting(setting);
			logger.warn("Default Value:" + (ca.getDefaultAttributeValue().getValue()));
		}
		return setting;
	}

	/**
	 * returns the profile with given id
	 * @param profileID
	 * @return
	 * @throws ControllerException
	 */
	public SmartRoomProfile getProfile(Long profileID) throws ControllerException {
		return (SmartRoomProfile) DBAccessController
		.getObjectByID(DBAccessController.GET_PROFILE_BY_ID, profileID);
	}

	/**
	 * Returns the profiles with the given ids.
	 * @param profileIDs
	 * @return
	 * @throws ControllerException
	 */
	//List
	public List getProfiles(List profileIDs) throws ControllerException {
		return DBAccessController.getObjectsByIDs(DBAccessController.GET_PROFILE_BY_ID, profileIDs);
	}

	public static SmartRoomProfileController getInstance() {
		if (SmartRoomProfileController._instance == null){
			SmartRoomProfileController._instance = new SmartRoomProfileController() ;
		}
		return SmartRoomProfileController._instance;	
	}

	/**
	 * Returns a profile with all components set to their default-properties.
	 * @return
	 * @throws MarsException
	 */
	public SmartRoomProfile createDefaultProfileForUser(Long userID) throws MarsException {
		
		User user = UserController.getInstance().getUser(userID);

		SmartRoomProfile profile = new SmartRoomProfile();
		profile.setUser(user);
		profile.setLastChange(new Date());
		profile.setName("Profile"+ DateFormat.getInstance().format(profile.getLastChange()));

		List components = getAllComponents();
		
		for (Iterator i = components.iterator(); i.hasNext();) {
			SmartRoomComponent c = (SmartRoomComponent)i.next();
			ComponentSetting s = makeSetting(c);
			profile.addComponentSetting(s);
		}
        DatabaseAccess.currentSession().getTransaction().rollback();    

        removeIds(profile);
        
		return profile; 
	}

	/**
	 * Returnes all components
	 * @return
	 */

	//List
	public List getAllComponents() {
		return DBAccessController.getAllObjectsFromQuery(DBAccessController.GET_ALL_COMPONENTS);
	}
	
	public void deleteSmartRoomProfile(Long profileId) throws ControllerException {
		SmartRoomProfile p = (SmartRoomProfile)DBAccessController.getObjectByID(SmartRoomProfile.class,profileId); 
		SmartRoomProfile def = p.getUser().getDefaultProfile();
        if(def != null && def.getId().equals(p.getId()))
        {
            p.getUser().setDefaultProfile(null);
        }
        p.getUser().removeSmartRoomProfile(p);
		DBAccessController.delete(p);
        DatabaseAccess.currentSession().flush();
	}
	
//START_AUTO_GEN
	public ModuleDescription addCommands(ModuleDescription module) {
		Command createProfile = new Command("createProfile", new ObjectParameter(SmartRoomProfile.class), this);

		createProfile.addParameter(new ObjectParameter("profile", SmartRoomProfile.class));
		createProfile.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(createProfile);
		Command updateProfile = new Command("updateProfile", new BooleanParameter(), this);

		updateProfile.addParameter(new ObjectParameter("profile", SmartRoomProfile.class));
		updateProfile.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(updateProfile);
		Command getProfile = new Command("getProfile", new ObjectParameter(SmartRoomProfile.class), this);

		getProfile.addParameter(new ObjectParameter("profileID", Long.class));

		module.addInterface(getProfile);
		Command getProfiles = new Command("getProfiles", new ObjectParameter(List.class), this);

		getProfiles.addParameter(new ListParameter("profileIDs", String.class));

		module.addInterface(getProfiles);
		Command createDefaultProfileForUser = new Command("createDefaultProfileForUser", new ObjectParameter(SmartRoomProfile.class), this);

		createDefaultProfileForUser.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(createDefaultProfileForUser);
		Command getAllComponents = new Command("getAllComponents", new ObjectParameter(List.class), this);


		module.addInterface(getAllComponents);
		Command deleteSmartRoomProfile = new Command("deleteSmartRoomProfile", null, this);

		deleteSmartRoomProfile.addParameter(new ObjectParameter("profileId", Long.class));

		module.addInterface(deleteSmartRoomProfile);
		return module;
	}
//STOP_AUTO_GEN
}
