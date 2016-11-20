package web.java;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.NetworkException;
import net.NetworkFactory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import server.domain.User;
import web.java.controller.ControllerConnection;
/*
*NOT YET IMPLEMENTED !!!
*/
public class MobileLightingForm extends ActionForm {
   String red;
   String green;
   String blue;
   String mood;
   String dispatch;

   public void setRed  (String _red)  {red   = _red;}
   public void setGreen(String _green){green = _green;}
   public void setBlue (String _blue) {blue  = _blue;}
   public void setMood (String _mood) {mood  = _mood;}
   public void setDispatch (String _dispatch) {dispatch  = _dispatch;}

   public String getDispatch  (){return dispatch;}
   public String getRed  (){return red;}
   public String getGreen(){return green;}
   public String getBlue (){return blue;}
   public String getMood (){return mood;}

   public void reset(ActionMapping mapping, HttpServletRequest request)
   {
        ControllerConnection controllerConnection = (ControllerConnection)
        request.getSession().getAttribute(Constants.CONNECTION);
        NetworkFactory net = controllerConnection.getNetworkFactory();

        try{
            setRed(((Integer)net.get("LIGHT", "Red")).toString());
            setBlue(((Integer)net.get("LIGHT", "Blue")).toString());
            setGreen(((Integer)net.get("LIGHT", "Green")).toString());
            setMood(((Integer)net.get("LIGHT", "Mood")).toString());
    	} catch (NetworkException ex) {
    	    ex.printStackTrace();
    	}
   }
}
