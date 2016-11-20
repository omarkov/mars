package web.java;

import server.domain.SmartRoomProfile;

public class ProfileTableDecoratorMobile extends org.displaytag.decorator.TableDecorator
{
	public String getLink()
	{
        SmartRoomProfile profile = (SmartRoomProfile)getCurrentRowObject();
        Long profileID = profile.getId();
		
		return    "<a href=\"../pages/manageProfilesShow-submit.do?currentProfileID=" + profileID
			+ "&submit=use\">Use</a>";
	}
}
