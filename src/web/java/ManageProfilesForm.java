package web.java;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.NetworkException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import server.domain.User;
import web.java.controller.ControllerConnection;

public class ManageProfilesForm extends ActionForm {
	private List users;
	private String userLoginId = null;
	private int currentUserIndex; //this is set in the jsp
	
	
	public ManageProfilesForm() {
		
	}

	public List getUsers() {
		return users;
	}

	public void setUsers(List users) {
		this.users = users;
	}

	public int getCurrentUserIndex() {
		return currentUserIndex;
	}
	public void setCurrentUserIndex(int currentUserIndex) {
		this.currentUserIndex = currentUserIndex;
	}

	public User getSelectedUser() {
		Object userArray[] = null;
		userArray = users.toArray();
		return (User)userArray[currentUserIndex];
	}
	public String getUserLoginId() {
		return userLoginId;
	}

	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	//Get all users to display in the jsp
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
			users = controllerConnection.getAllUsers();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.reset(mapping, request);
	}

}
