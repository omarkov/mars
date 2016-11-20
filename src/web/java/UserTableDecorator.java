package web.java;

import server.domain.User;

public class UserTableDecorator extends org.displaytag.decorator.TableDecorator
{
	public String getLink()
	{
                User user = (User)getCurrentRowObject();
                Long id = user.getId();


		return    "<a href=\"manageUser-submit.do?currentUserID=" + id
			+ "&submit=edit\">Edit</a> | "
			+ "<a href=\"manageUser-submit.do?currentUserID=" + id
			+ "&submit=delete\">Delete</a> | "
                        + "<a href=\"manageUser-submit.do?currentUserID=" + id
			+ "&submit=showProfiles\">Show Profiles</a>";    
	}
}
