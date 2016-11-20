package web.java;

import server.domain.GroupMars;


public class GroupTableDecorator extends org.displaytag.decorator.TableDecorator
{
	public String getLink()
	{
		GroupMars group= (GroupMars)getCurrentRowObject();
		Long id = group.getId();

		return    "<a href=\"manageGroup-submit.do?currentGroupID=" + id
			+ "&submit=edit\">Edit</a> | "
			+ "<a href=\"manageGroup-submit.do?currentGroupID=" + id
			+ "&submit=delete\">Delete</a>";
	}
}
