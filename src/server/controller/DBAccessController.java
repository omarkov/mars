package server.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

import server.DatabaseAccess;
import server.controller.exceptions.ControllerException;
import server.controller.exceptions.ElementDeletionException;
import server.controller.exceptions.ElementNotFoundException;
import server.controller.exceptions.ElementUpdateException;
import server.controller.exceptions.MulitpleElementsWithPKException;
import server.domain.ComponentSetting;
import server.domain.Department;
import server.domain.GroupMars;
import server.domain.LogInSystem;
import server.domain.SmartRoomComponent;
import server.domain.SmartRoomProfile;
import server.domain.User;



/**
 * @author Rajkumar
 *
 * Created on 27.07.2005
 *
 * Date 		Author 		Change
 * 07/08/05		RDE			created
 * 09/08/05		RDE			getObjsByIDs
 */
public class DBAccessController {
	
	private static Logger logger = Logger.getLogger(DBAccessController.class);

	
	public static final String GET_LOGINSYSTEM_BY_ID = "get.system.by.id";
	public static final String GET_LOGINSYSTEM_BY_NAME = "get.system.by.name";
	public static final String GET_ALL_LOGINSYSTEMS = "get.all.systems";

	public static final String GET_IDENTIFICATION_BY_ID= "get.identification.by.id";
	public static final String GET_ALL_IDENTIFICATION = "get.all.identifications";
	
	public static final String GET_IDENTIFICATION_BY_SYSTEMID_TAG= "get.identification.by.systemid.tag";
	public static final String GET_IDENTIFICATION_BY_SYSTEMNAME_TAG= "get.identification.by.systemname.tag";

	public static final String GET_GROUP_BY_ID= "get.group.by.id";
	public static final String GET_GROUP_BY_NAME= "get.group.by.name";
	public static final String GET_ALL_GROUPS="get.all.groups";

	public static final String GET_DEPT_BY_ID = "get.department.by.id";
	public static final String GET_DEPT_BY_NAME = "get.department.by.name";
	public static final String GET_ALL_DEPTS = "get.all.departments";

	public static final String GET_USER_BY_ID= "get.user.by.id";
	public static final String GET_USER_BY_USRID = "get.user.by.userid";
	public static final String GET_ALL_USERS= "get.all.users";

	public static final String GET_USERS_BY_IDS= "get.user.by.id";
	public static final String GET_USERS_BY_USRIDS = "get.user.by.userid";
	public static final String GET_USER_BY_USRID_AND_PASSWD = "get.user.by.loginid.and.passwd";
	
	public static final String GET_COMPONENT_BY_ID= "get.component.by.id";
	public static final String GET_COMPONENT_BY_NAME= "get.component.by.name";
	public static final String GET_ALL_COMPONENTS = "get.all.components";
	
	public static final String GET_PROFILE_BY_ID="get.profile.by.id";
	public static final String GET_ALL_PROFILES = "get.all.profiles";
	
	public static final String GET_ATTIBUTE_BY_COMPID_NAME= "get.attribute.by.compid.name";
	public static final String GET_ATTIBUTES_BY_COMPID= "get.attributes.by.compid";
	public static final String GET_ALL_ATTRIBUTES = "get.all.attributes";

	public static final String GET_ALL_USERS_BY_DPTID = "get.users.by.deptid";
	public static final String GET_ALL_VALUES_BY_SETTINGID = "get.values.by.settingid";

	public static final String GET_ALL_ATTRIBUTETYPES = "get.all.attributeteypes";
	public static final String GET_SETTING_BY_ID = "get.setting.by.id";


	public static final String GET_USERS_NOT_IN_DEPT = "get.users.not.in.dept.with.id";



	

	/**
	 * Saves the given Object in DB
	 * 
	 * @param o
	 * @throws ElementUpdateException
	 */
	public static Object save(Object o) throws ElementUpdateException{
	    // aktuelle session holen und transaktion starten
		try {
		    // aktuelle session holen
		    Session s = DatabaseAccess.currentSession();
	
		    Object o2 = s.merge(o);
		    logger.info("Object saved:" + o);
		    return o2;
		} catch (HibernateException e) {
			throw new ElementUpdateException(e.getMessage());
		}
	}

	/**
	 * Deletes the Object from DB
	 * 
	 * @param o
	 * @throws ElementDeletionException
	 */
	public static void delete(Object o) throws ElementDeletionException{
		try {
		    // aktuelle session holen
		    Session s = DatabaseAccess.currentSession();
	
		    // objekt speichern
		    s.delete(o);
            // s.flush();
	
		    logger.info("Element deleted:" + o);
		} catch (HibernateException e) {
			throw new ElementDeletionException(e.getMessage());
		}
	}
		
	
	/**
	 * Returns a List of all elements of a type. Should be 
	 * called with the type-specific select query.
	 * 
	 * @param aQuery
	 * @return
	 */
	public static List getAllObjectsFromQuery(String aQuery) {
		Session session = DatabaseAccess.currentSession();
		Query q = session.getNamedQuery(aQuery);
		return q.list();
		
	}
	
	/**
	 * Returns the Object that sattisfies the select-query with the given
	 * attributes.
	 * 
	 * @param query
	 * @param values
	 * @return
	 * @throws ElementNotFoundException
	 * @throws MulitpleElementsWithPKException
	 */
	public static Object getObjectByAttributes(Class cls, String[] keys, Object[] values)
	throws MulitpleElementsWithPKException {
				
			List elements = getObjectsByAttributes(cls,keys, values);
		
			if (elements == null || elements.size() == 0) {
				return null;
			} else if (elements.size() == 1) {
				return elements.get(0);
			} else {
				throw new MulitpleElementsWithPKException("");
			}
	}	

	public static Object getObjectByAttributes(String query, Object[] values)
	throws MulitpleElementsWithPKException {
				
		List elements = getObjectsByAttributes(query,values);
		
			if (elements == null || elements.size() == 0) {
				return null;
			} else if (elements.size() == 1) {
				return elements.get(0);
			} else {
				throw new MulitpleElementsWithPKException("");
			}
	}	
	
	/**
	 * Returns a List of Objects satisfying the select-query with the given
	 * values.
	 * 
	 * 
	 * @param query
	 * @param values
	 * @return
	 */
	public static List getObjectsByAttributes(Class cls, String[] keys, Object[] values) {

		Session session = DatabaseAccess.currentSession();
		Criteria criteria = session.createCriteria(cls);
		for (int i = 0; i < keys.length; i++) {
			if(values[i] instanceof Object[])
			{
				Criterion c = null;
				Object[] objs = (Object[])values[i];
				
				for (int j=0; j < objs.length; j++ ) {
					if(c == null)
						c = Expression.eq(keys[i], objs[j]);
					else
						c = Expression.or(c, Expression.eq(keys[i], objs[j]));
				}
				criteria.add(c);
			}else
				criteria.add(Expression.eq(keys[i], values[i]));
		}
		System.out.println(criteria.toString());
		List elements = criteria.list();
	
		if (elements == null || elements.size() == 0) {
			return null;
		} else {
			return elements;
		}
	}

	public static List getObjectsByAttributes(String query, Object[] values) {

		Session session = DatabaseAccess.currentSession();
		Query q = session.getNamedQuery(query);
		for (int i = 0; i < values.length; i++) {
			if(values[i] instanceof String)
				q.setString(i, (String)values[i]);
			else if(values[i] instanceof Long)
				q.setLong(i, (Long)values[i]);
		}
		List elements = q.list();
	
		if (elements == null || elements.size() == 0) {
			return new ArrayList();
		} else {
			return elements;
		}
	}
	
	/**
	 * returns a List of Objects of the select-query-specific type.
	 *  
	 * 
	 * @param query
	 * @param ids
	 * @return
	 * @throws MulitpleElementsWithPKException
	 */
	public static List getObjectsByIDs(String query, List ids)
	throws MulitpleElementsWithPKException {
	Set objs = new HashSet();
	if (ids == null) {
		return new ArrayList();
	}
	for(Iterator i = ids.iterator(); i.hasNext();) {
		objs.add(i.next());
	}
	return getObjectsByIDs(query,objs);
}
	public static List getObjectsByIDs(Class cls, List ids)
		throws MulitpleElementsWithPKException {
		Set objs = new HashSet();
		if (ids == null) {
			return new ArrayList();
		}
		for(Iterator i = ids.iterator(); i.hasNext();) {
			objs.add(i.next());
		}
		return getObjectsByIDs(cls,objs);
	}
	
	/**
	 * Receives a lsit of Objects of the same type. 
	 * TODO: Optimieren!!! Das geht wesentlich schneller direkt über Hibernate!
	 * 
	 * @param query
	 * @param ids
	 * @return
	 * @throws ElementNotFoundException
	 * @throws MulitpleElementsWithPKException
	 */
	public static List getObjectsByIDs(Class cls, Set ids)
			throws MulitpleElementsWithPKException {
		if (ids == null) {
			return new ArrayList();
		}
		
		Session session = DatabaseAccess.currentSession(); 
		Criteria criteria = session.createCriteria(cls);
		Criterion c = Expression.isNull("id");
		
		for (Iterator i = ids.iterator(); i.hasNext(); ) {
			c = Expression.or(c, Expression.eq("id", i.next()));
		}
		criteria = criteria.add(c);
		//System.out.println(criteria.toString());
		return criteria.list();
	}

	public static List getObjectsByIDs(String query, Set ids)
			throws MulitpleElementsWithPKException {
		if (ids == null) {
			return new ArrayList();
		}

		ArrayList list = new ArrayList();
		
		for (Iterator i = ids.iterator(); i.hasNext();) {
			Object id = i.next();
			if(id instanceof Long)
			{
				list.add(getObjectByID(query, (Long)id));
			}else
			{
				list.add(getObjectByID(query, (String)id));
			}
		}

		return list;
	}
	

	
	/**
	 * Empties the DB.
	 * @throws ControllerException
	 */
	public static void deleteDivers() throws ControllerException {
		
		removeAllObjectsByQuery(DBAccessController.GET_ALL_PROFILES);
        removeAllObjectsByQuery(DBAccessController.GET_ALL_USERS);
        removeAllObjectsByQuery(DBAccessController.GET_ALL_GROUPS);
        removeAllObjectsByQuery(DBAccessController.GET_ALL_COMPONENTS);
        removeAllObjectsByQuery(DBAccessController.GET_ALL_DEPTS);
        removeAllObjectsByQuery(DBAccessController.GET_ALL_IDENTIFICATION);
        removeAllObjectsByQuery(DBAccessController.GET_ALL_LOGINSYSTEMS);
        
	    logger.warn("DB CONTENT DELETED");
	}

	/**
	 * Deletes all Objects that are retrieved by the select query.
	 * 
	 * @param aQuery
	 * @throws ElementUpdateException
	 */
	public static void removeAllObjectsByQuery(String aQuery) throws ElementUpdateException {
		try {
			if(aQuery == GET_ALL_GROUPS){
				List objs = DBAccessController.getAllObjectsFromQuery(aQuery);
				for(Iterator i = objs.iterator(); i.hasNext();) {
					GroupMars groupTest = (GroupMars)i.next();
					GroupController.getInstance().deleteGroup(groupTest.getId());
				}
				logger.info("removeAllObjects(): Alle Gruppen gelöscht");
			}else if(aQuery == GET_ALL_USERS){
				List objs = DBAccessController.getAllObjectsFromQuery(aQuery);
				for(Iterator i = objs.iterator(); i.hasNext();) {
					User userTest = (User)i.next();
					UserController.getInstance().deleteUser(userTest.getId());
				}
				logger.info("removeAllObjects(): Alle User gelöscht");
			}else if(aQuery == GET_ALL_COMPONENTS){
				List objs = DBAccessController.getAllObjectsFromQuery(aQuery);
				for(Iterator i = objs.iterator(); i.hasNext();) {
					SmartRoomComponent srpTest = (SmartRoomComponent)i.next();
					SmartRoomComponentController.getInstance().deleteSmartRoomComponent(srpTest.getId());
				}
				logger.info("removeAllObjects(): Alle SmartRoomComponents gelöscht");
			}else if(aQuery == GET_ALL_DEPTS){
				List objs = DBAccessController.getAllObjectsFromQuery(aQuery);
				for(Iterator i = objs.iterator(); i.hasNext();) {
					Department depTest = (Department)i.next();
					DepartmentController.getInstance().deleteDepartment(depTest.getId());
				}
				logger.info("removeAllObjects(): Alle Departments gelöscht");
			}else if(aQuery == GET_ALL_IDENTIFICATION){
				List objs = DBAccessController.getAllObjectsFromQuery(aQuery);
				for(Iterator i = objs.iterator(); i.hasNext();) {
					DBAccessController.delete(i.next());
				}
				logger.warn("removeAllObjects(): Alle Identifications gelöscht");
			}else if(aQuery == GET_ALL_LOGINSYSTEMS){
				List objs = DBAccessController.getAllObjectsFromQuery(aQuery);
				for(Iterator i = objs.iterator(); i.hasNext();) {
					LogInSystem logTest = (LogInSystem)i.next();
					DBAccessController.delete(logTest);
				}
				logger.info("removeAllObjects(): Alle LogInSystems gelöscht");
			}else if(aQuery == GET_ALL_PROFILES){
				List objs = DBAccessController.getAllObjectsFromQuery(aQuery);
				for(Iterator i = objs.iterator(); i.hasNext();) {
					SmartRoomProfile srp = (SmartRoomProfile)i.next();
					logger.warn("Profile.settings.size()" + srp.getComponentSettings().size());
					SmartRoomProfileController.getInstance().deleteSmartRoomProfile(srp.getId());
				}	
			}
			
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}
	}

    public static Object getObjectByID(String query, String id)
			throws MulitpleElementsWithPKException {

		Session session = DatabaseAccess.currentSession();
		Query q = session.getNamedQuery(query);
		System.out.println(q.getQueryString());
		q.setString(0, id);
		List elements = q.list();
		if (elements == null || elements.size() == 0) {
			return null;
		} else if (elements.size() == 1) {
			return elements.get(0);
		} else {
			throw new MulitpleElementsWithPKException(id);
		}
	}

    public static void closeSession()
    {
        DatabaseAccess.closeSession();
    }
	
	
    public static Object getObjectByID(String query, Long id)
			throws MulitpleElementsWithPKException {

		Session session = DatabaseAccess.currentSession();
		Query q = session.getNamedQuery(query);
		System.out.println(q.getQueryString());
		q.setLong(0, id);
		List elements = q.list();
		if (elements == null || elements.size() == 0) {
			return null;
		} else if (elements.size() == 1) {
			return elements.get(0);
		} else {
			throw new MulitpleElementsWithPKException(id.toString());
		}
	}
	
	public static Object getObjectByID(Class cls, Long id) throws ControllerException {
			Session session = DatabaseAccess.currentSession();
            try {
                    // return session.load(cls, id);
                    return session.get(cls, id);
            } catch (HibernateException e)  {
                    throw new ControllerException(e.getMessage());
            }
    }
	
	
	
	/**
	 * Executes the given query.
	 * 
	 * @param aQuery
	 * @return
	 */
	public static int perform(String aQuery) {
	    Session s = DatabaseAccess.currentSession();
        int deletedEntities = s.createQuery(aQuery).executeUpdate();
		return deletedEntities;
	}
}
