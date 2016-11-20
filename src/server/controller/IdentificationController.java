package server.controller;

import java.util.List;

import org.apache.log4j.Logger;

import net.*;
import server.controller.exceptions.ControllerException;
import server.controller.exceptions.ElementCreationException;
import server.controller.exceptions.ElementDeletionException;
import server.controller.exceptions.ElementNotFoundException;
import server.controller.exceptions.ElementValidationException;
import server.controller.exceptions.MulitpleElementsWithPKException;
import server.domain.Identification;
import server.domain.LogInSystem;


/**
 * @author Rajkumar
 *
 * Created on 27.07.2005
 *
 * Date 		Author 		Change
 * 10/08/05		RDE			created
 * 14/08/05		RDE			commented
 */
public class IdentificationController  implements NetworkEventListener {

	private static IdentificationController _instance;

	private static Logger logger = Logger.getLogger(IdentificationController.class);

	private IdentificationController() {
		
	}

	/**
	 * Creates an Identification. An Identification consists of a LogInSystem
	 * and a LogInSystem-wide unique tagID.
	 * 
	 * @param system
	 * @param tagID
	 * @throws ElementCreationException
	 */
	protected Identification createIdentification(LogInSystem system, String tagID)
			throws ElementCreationException {

		try {
			Identification ident = new Identification(system, tagID);
			validateIdentification(ident);
			return (Identification) DBAccessController.save(ident);
		} catch (ControllerException e) {
			throw new ElementCreationException(e.getMessage());
		}

	}

	public Boolean updateIdentification(Long idID, String tagID)
			throws ElementCreationException {

		try {
			Identification ident = this.getIdentification(idID);
			ident.setTagID(tagID);
			validateIdentification(ident);
			DBAccessController.save(ident);
		} catch (ControllerException e) {
			throw new ElementCreationException(e.getMessage());
		}
        return Boolean.TRUE;
	}

	/**
	 * Creates an Identification. An Identification consists of a LogInSystem
	 * and a LogInSystem-wide unique tagID.
	 * 
	 * @param system
	 * @param tagID
	 * @throws ElementCreationException
	 */
	public Identification createIdentification(Long lsID, String tagID)
			throws ElementCreationException {

		try {
			LogInSystem system = this.getLogInSystem(lsID);
			Identification ident = new Identification(system, tagID);
			validateIdentification(ident);
			return (Identification)DBAccessController.save(ident);
		} catch (ControllerException e) {
			throw new ElementCreationException(e.getMessage());
		}

	}
	/**
	 * Creates an Identification. An Identification consists of a LogInSystem
	 * and a LogInSystem-wide unique tagID.
	 * 
	 * @param system
	 * @param tagID
	 * @throws ElementCreationException
	 */
	public Identification createIdentificationForLogInSystemNamed(String lsName, String tagID)
			throws ElementCreationException {

		try {
			LogInSystem system = this.getLogInSystemByName(lsName);
			Identification ident = new Identification(system, tagID);
			validateIdentification(ident);
			return (Identification)DBAccessController.save(ident);
		} catch (ControllerException e) {
			throw new ElementCreationException(e.getMessage());
		}

	}

	/**
	 * Deletes the Identification with the given ID from DB.
	 * 
	 * @param id
	 * @throws ElementDeletionException
	 */
	public void deleteIdentification(Long id) throws ElementDeletionException {
		try {
			deleteIdentification(this.getIdentification(id));
		} catch (ControllerException e) {
			throw new ElementDeletionException(e.getMessage());
		}
	}
	
	/**
	 * Deletes an Identification from DB
	 * 
	 * @param ident
	 * @throws ElementDeletionException
	 */
	protected void deleteIdentification(Identification ident)
			throws ElementDeletionException {
		DBAccessController.delete(ident);
	}

	/**
	 * Validates an Identification.
	 * 
	 * @param ident
	 * @throws ElementValidationException
	 */
	public void validateIdentification(Identification ident) throws ElementValidationException {
		if (ident == null) {
			throw new ElementValidationException("Element may not be null");
		} else if (ident.getLogInSystem() == null) {
			throw new ElementValidationException("LogInSystem may not be null");
		} else if ((ident.getTagID() == null) || (ident.getTagID().equals(""))) {
			throw new ElementValidationException("TagID may not be empty");
		}
	}
	
	/**
	 * Creates a new LogInSystem with the given name. 
	 * There can't be LogInSystems with the same name.
	 * 
	 * @param logInSystemName
	 * @throws ElementCreationException
	 */
	public LogInSystem createLogInSystem(String logInSystemName)
			throws ElementCreationException {

		try {
			LogInSystem system = new LogInSystem(logInSystemName);
			return createLogInSystem(system);
		} catch (ControllerException e) {
			throw new ElementCreationException(e.getMessage());
		}

	}

	/**
	 * Creates a new LogInSystem with the given name. 
	 * There can't be LogInSystems with the same name.
	 * 
	 * @param logInSystemName
	 * @throws ElementCreationException
	 */
    protected LogInSystem createLogInSystem(LogInSystem logInSystem)
	throws ElementCreationException
    {
	try {
	    validatLogInSystem(logInSystem);

	    // check for dupe
	    LogInSystem system = getLogInSystemByName(logInSystem.getName());
	    if (system != null)
		return system;
	    else
		return (LogInSystem)DBAccessController.save(logInSystem);

	} catch (ControllerException e) {
	    throw new ElementCreationException(e.getMessage());
	}
    }


	/**
	 * Deletes a LogInSystem from DB
	 * @param logInSystemID
	 * @throws ElementDeletionException
	 */
	public void deleteLogInSystem(Long logInSystemID) throws ElementDeletionException {
		try {
			LogInSystem system = this.getLogInSystem(logInSystemID);
			deleteLogInSystem(system);
		} catch (ControllerException e) {
			throw new ElementDeletionException(e.getMessage());
		}
	}
	
	/**
	 * Deletes a LogInSystem from DB 
	 * @param system
	 * @throws ElementDeletionException
	 */
	protected void deleteLogInSystem(LogInSystem system)
			throws ElementDeletionException {
		DBAccessController.delete(system);
	}

	/**
	 * Validates a LogInSystem. A LogInSystem is only valid if it's name is unique
	 * 
	 * @param system
	 * @throws ElementValidationException
	 */
	public  void validatLogInSystem(LogInSystem system) throws ElementValidationException {
		if(system == null) {
			throw new ElementValidationException("Element may not be null");
		} else if(system.getName() == null || system.getName().length()== 0) {
			throw new ElementValidationException("LoginSystem.Name may not be empty");
		}
		
	}
	
	
	/**
	 * @return Returns the _instance.
	 */
	public static IdentificationController getInstance() {
		if (IdentificationController._instance == null){
			IdentificationController._instance = new IdentificationController() ;
		}
		return IdentificationController._instance;	
	}

	/**
	 * Loads all LogInSystems from DB
	 * @return
	 */
	//LogInSystem
	public List getAllLogInSystems() {
		return DBAccessController.getAllObjectsFromQuery(DBAccessController.GET_ALL_LOGINSYSTEMS);
	}
	
	/**
	 * Returns the LogInSystem with the given ID
	 * @param lsID
	 * @return
	 * @throws ControllerException
	 */
	public LogInSystem getLogInSystem(Long lsID) throws ControllerException {
		return (LogInSystem)DBAccessController.getObjectByID(DBAccessController.GET_LOGINSYSTEM_BY_ID,lsID);
	}
	
	/**
	 * Returns the loginSystem with the given Name
	 * @param lsName
	 * @return
	 * @throws ControllerException
	 */
	public LogInSystem getLogInSystemByName(String lsName) throws ControllerException {
		return (LogInSystem)DBAccessController.getObjectByID(DBAccessController.GET_LOGINSYSTEM_BY_NAME,lsName);
	}
	
	
	/**
	 * returns the Identification with the given ID
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	public Identification getIdentification(Long id) throws ControllerException {
		return (Identification)DBAccessController.getObjectByID(Identification.class,id);		
	}

	/**
	 * Returns the identifications with the given ids
	 * @param ids
	 * @return
	 * @throws ControllerException
	 */
	//Identification
	public List getIdentifications(List ids) throws ControllerException {
		return DBAccessController.getObjectsByIDs(DBAccessController.GET_IDENTIFICATION_BY_ID,ids);		
	}
	
	
	/**
	 * returns the identification by loginsystemname and tagID
	 * @param loginSystemName
	 * @param tagID
	 * @return
	 * @throws ControllerException
	 */
	public Identification getIdentificationByLSNameAndTag(String loginSystemName, String tagID) throws ControllerException {
		String[] attribs = {loginSystemName,tagID};
		return (Identification)DBAccessController
			.getObjectByAttributes(DBAccessController.GET_IDENTIFICATION_BY_SYSTEMNAME_TAG, attribs);
		
	}
//START_AUTO_GEN
	public ModuleDescription addCommands(ModuleDescription module) {
		Command updateIdentification = new Command("updateIdentification", new BooleanParameter(), this);

		updateIdentification.addParameter(new ObjectParameter("idID", Long.class));
		updateIdentification.addParameter(new StringParameter("tagID", null));

		module.addInterface(updateIdentification);
		Command createIdentification = new Command("createIdentification", new ObjectParameter(Identification.class), this);

		createIdentification.addParameter(new ObjectParameter("lsID", Long.class));
		createIdentification.addParameter(new StringParameter("tagID", null));

		module.addInterface(createIdentification);
		Command createIdentificationForLogInSystemNamed = new Command("createIdentificationForLogInSystemNamed", new ObjectParameter(Identification.class), this);

		createIdentificationForLogInSystemNamed.addParameter(new StringParameter("lsName", null));
		createIdentificationForLogInSystemNamed.addParameter(new StringParameter("tagID", null));

		module.addInterface(createIdentificationForLogInSystemNamed);
		Command deleteIdentification = new Command("deleteIdentification", null, this);

		deleteIdentification.addParameter(new ObjectParameter("id", Long.class));

		module.addInterface(deleteIdentification);
		Command validateIdentification = new Command("validateIdentification", null, this);

		validateIdentification.addParameter(new ObjectParameter("ident", Identification.class));

		module.addInterface(validateIdentification);
		Command createLogInSystem = new Command("createLogInSystem", new ObjectParameter(LogInSystem.class), this);

		createLogInSystem.addParameter(new StringParameter("logInSystemName", null));

		module.addInterface(createLogInSystem);
		Command deleteLogInSystem = new Command("deleteLogInSystem", null, this);

		deleteLogInSystem.addParameter(new ObjectParameter("logInSystemID", Long.class));

		module.addInterface(deleteLogInSystem);
		Command validatLogInSystem = new Command("validatLogInSystem", null, this);

		validatLogInSystem.addParameter(new ObjectParameter("system", LogInSystem.class));

		module.addInterface(validatLogInSystem);
		Command getAllLogInSystems = new Command("getAllLogInSystems", new ListParameter(LogInSystem.class), this);


		module.addInterface(getAllLogInSystems);
		Command getLogInSystem = new Command("getLogInSystem", new ObjectParameter(LogInSystem.class), this);

		getLogInSystem.addParameter(new ObjectParameter("lsID", Long.class));

		module.addInterface(getLogInSystem);
		Command getLogInSystemByName = new Command("getLogInSystemByName", new ObjectParameter(LogInSystem.class), this);

		getLogInSystemByName.addParameter(new StringParameter("lsName", null));

		module.addInterface(getLogInSystemByName);
		Command getIdentification = new Command("getIdentification", new ObjectParameter(Identification.class), this);

		getIdentification.addParameter(new ObjectParameter("id", Long.class));

		module.addInterface(getIdentification);
		Command getIdentifications = new Command("getIdentifications", new ListParameter(Identification.class), this);

		getIdentifications.addParameter(new ListParameter("ids", String.class));

		module.addInterface(getIdentifications);
		Command getIdentificationByLSNameAndTag = new Command("getIdentificationByLSNameAndTag", new ObjectParameter(Identification.class), this);

		getIdentificationByLSNameAndTag.addParameter(new StringParameter("loginSystemName", null));
		getIdentificationByLSNameAndTag.addParameter(new StringParameter("tagID", null));

		module.addInterface(getIdentificationByLSNameAndTag);
		return module;
	}
//STOP_AUTO_GEN
}
