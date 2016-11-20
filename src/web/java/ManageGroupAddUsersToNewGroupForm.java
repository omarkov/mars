package web.java;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.NetworkException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import web.java.controller.ControllerConnection;

public class ManageGroupAddUsersToNewGroupForm extends ActionForm{

	private String action = null;
    private Long[] selectedUsers = null;
	private List usersNotInGroup = null;
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		
		//TODO insert proper login data
		try {
			ControllerConnection controllerConnection = new ControllerConnection("schleidl", "123");
			//TODO: groupName ???
			usersNotInGroup = controllerConnection.getAllUsersNotInGroup((Long)request.getAttribute("groupName"));
			controllerConnection.close();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
                
		super.reset(mapping, request);
	}

	public Long[] getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(Long[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public List getUsersNotInGroup() {
		return usersNotInGroup;
	}

	public void setUsersNotInGroup(List usersNotInGroup) {
		this.usersNotInGroup = usersNotInGroup;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	} 

}
