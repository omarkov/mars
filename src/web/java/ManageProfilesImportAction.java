package web.java;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;

import server.domain.GroupMars;
import server.domain.User;
import web.java.controller.ControllerConnection;

public final class ManageProfilesImportAction extends Action{
	
	public ActionForward execute(
    		ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
			throws Exception {

		ManageProfilesImportForm mpif = (ManageProfilesImportForm) form;

		if (mpif.getDispatch().equals("import")) 
		{
			FormFile file = mpif.getFile();
			//String fname = file.getFileName();
			//InputStream streamIn = file.getInputStream();
			//TODO do something with the file
			//streamIn.close();
			return (mapping.findForward("importdone"));
		}
		
		return (mapping.findForward("reload"));
	}
}
