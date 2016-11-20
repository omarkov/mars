package web.java;

import java.util.List;

import web.java.controller.ChangePasswordException;
import web.java.controller.ControllerConnection;
import web.java.controller.NotAuthorizedException;
import web.java.controller.UnknownLoginException;
import net.NetworkException;

public class UserDataBean {
	public List allUsers = null;
	public UserDataBean() {
/*		try {
			//TODO Change parameter of the constructor call to dynamic ones
			ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
			allUsers = controllerConnection.getAllUsers();
		} catch (NetworkException e) {
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
*/	}
	public List getAllUsers() {
		return allUsers;
	}
	public void setAllUsers(List allUsers) {
		this.allUsers = allUsers;
	}
}
