package server.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.omg.IOP.MultipleComponentProfileHelper;

import net.*;

import server.ServerMain;
import server.controller.exceptions.ControllerException;
import server.controller.exceptions.ElementCreationException;
import server.controller.exceptions.ElementDeletionException;
import server.controller.exceptions.ElementUpdateException;
import server.controller.exceptions.LogInException;
import server.controller.exceptions.LogOutException;
import server.controller.exceptions.RoomOccupiedException;
import server.domain.*;


/**
 * @author Rajkumar
 *
 * Created on 27.07.2005
 *
 * Date 		Author 		Change
 * 10/08/05		RDE			created
 */

public class SmartRoomComponentController implements NetworkEventListener {

	private static SmartRoomComponentController _instance;
	private User activeUser;
	private Set usersInRoom = new HashSet();
	private Logger logger = Logger.getLogger(ServerMain.class);

	private SmartRoomComponentController() {
		
	}
	 
	public static SmartRoomComponentController getInstance() {
		if (SmartRoomComponentController._instance == null){
			SmartRoomComponentController._instance = new SmartRoomComponentController() ;
		}
		return SmartRoomComponentController._instance;	
	}

	public SmartRoomComponent createSmartRoomComponent(ModuleDescription m) 	{
		if(!m.isWebModule())
		{
			return null;	
		}
		try
		{
			SmartRoomComponent c = null;
			try
			{
				c = getComponentByName(m.getName());
			}catch(ControllerException e)
			{
			}

			System.out.println("createSmartRoomComponent" + c);

			if(c == null)
			{
				c = new SmartRoomComponent();
	                	c.setName(m.getName());

				System.out.println("Component: " + c.getName());
	
				for(int i=0; i < m.getInterfaceCount(); i++)
				{
					NetworkInterface ni = m.getInterface(i);
					if(ni instanceof Parameter)
					{
						Parameter p = (Parameter)ni;
	                			ComponentAttribute ca = new ComponentAttribute();
	
						if(p.getType() == StringParameter.TYPE)
						{
							StringType t = new StringType();
	                				ca.setAttributeType(t);
						}
						else if(p.getType() == NumericParameter.TYPE)	
						{
							NumericType t = new NumericType();
							NumericParameter np = (NumericParameter)p;
	
							t.setMin(np.getMin());
							t.setMax(np.getMax());
							t.setStep(np.getStep());
	                		ca.setAttributeType(t);
						}
						else if(p.getType() == BooleanParameter.TYPE)
						{
							BooleanType t = new BooleanType();
	                				ca.setAttributeType(t);
						}
						else if(p.getType() == ListParameter.TYPE)
						{	
							ListType t = new ListType();
                            ListParameter lp = (ListParameter)p;
                            String info = lp.getInfo();
                            t.setContentType(info);

	                		ca.setAttributeType(t);
						}
					
						ca.setFlag(p.getFlags());
						ca.setName(p.getName());
						ca.setDefaultValue(p.getDefault());
						ca.setSmartRoomComponent(c);
					}
				}
				return createSmartRoomComponent(c);
			}
		}catch(ControllerException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	protected SmartRoomComponent createSmartRoomComponent(SmartRoomComponent c) throws ElementCreationException {
		try {
			return (SmartRoomComponent)DBAccessController.save(c);
		} catch(ElementUpdateException e) {
			throw new ElementCreationException(e.getMessage());
		}
	}
	
	/**
	 * Deletes the component with the given id.
	 * @param id
	 * @throws ElementDeletionException
	 */
	public void deleteSmartRoomComponent(Long id) throws ElementDeletionException {
		try {
			SmartRoomComponent c = getComponent(id);
			DBAccessController.delete(c);
		} catch (ControllerException e) { 
			throw new ElementDeletionException(e.getMessage());
		}
		
	}
	
	
	/**
	 * returns the component with the given ID
	 * @param componentID
	 * @return
	 * @throws ControllerException
	 */
	public SmartRoomComponent getComponent(Long componentID) throws ControllerException {
		return (SmartRoomComponent)DBAccessController.getObjectByID(SmartRoomComponent.class,componentID);
	}

	/**
	 * returns the component with the given name
	 * @param name
	 * @return
	 * @throws ControllerException
	 */
	public SmartRoomComponent getComponentByName(String name) throws ControllerException {
		return (SmartRoomComponent)DBAccessController.getObjectByID(DBAccessController.GET_COMPONENT_BY_NAME,name);
	}
	
	/**
	 * Retruns all components
	 * @return
	 */
	public List getAllComponents() {
		return DBAccessController.getAllObjectsFromQuery(DBAccessController.GET_ALL_COMPONENTS);
	}

	/**
	 * Returns all Attributes of all Components
	 * @return
	 */
	public List getAllAttributes() {
		return DBAccessController.getAllObjectsFromQuery(DBAccessController.GET_ALL_ATTRIBUTES);
	}
	
	/**
	 * Returns the Attrbutes of the components with the given ID
	 * @param compID
	 * @return
	 */
	public List getAttributesByComponent(Long compID) {
		Object[] id = new Object[] {compID}; 
		return DBAccessController.getObjectsByAttributes(DBAccessController.GET_ATTIBUTES_BY_COMPID,id);
	}
	
	
}
