package web.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

import java.util.ArrayList;
import java.util.Collection;

import net.NetworkException;
import web.java.controller.NotAuthorizedException;
import web.java.controller.*;

/*Here the menu validation for the User or admin should
 *be implement
 *TODO !
 */
public class UserAction  {
    
    private Log log = LogFactory.getFactory().getInstance(this.getClass().getName());
    private Collection users;
    private int currentUserIndex;

    private UserAction() throws NotAuthorizedException {
        BasicConfigurator.configure(); //Configure logging
        
		//try {
			log.debug("UserAction.java:");
			
                       
			//ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
			//users = new ArrayList(controllerConnection.getAllUsers());                        
/*		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownLoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ChangePasswordException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotAuthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/    }

} // end UserAction
