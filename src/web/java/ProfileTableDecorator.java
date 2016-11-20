package web.java;

import server.domain.SmartRoomProfile;

public class ProfileTableDecorator extends org.displaytag.decorator.TableDecorator
{
	public String getLink()
	{
                SmartRoomProfile profile = (SmartRoomProfile)getCurrentRowObject();
                Long profileID = profile.getId();
		
		return    "<a href=\"manageProfilesShow-submit.do?currentProfileID=" + profileID
			+ "&submit=edit\">Edit</a> | "
			+ "<a href=\"manageProfilesShow-submit.do?currentProfileID=" + profileID
			+ "&submit=delete\">Delete</a> | "
			+ "<a href=\"manageProfilesShow-submit.do?currentProfileID=" + profileID
			+ "&submit=setAsDefault\">Set as Default</a>";
		
		//	+ "<a href=\"manageProfilesShow-submit.do?currentProfileID=" + profileID
		//	+ "&submit=export\">Export</a>";
	}
}
