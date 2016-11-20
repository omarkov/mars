package web.java;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ExceptionConfig;

/**
 * Writes exceptions to the console. This Handler is defined in the struts-config. 
 *
 */
public class ExceptionHandler extends org.apache.struts.action.ExceptionHandler {

	
	/**
	 * Implementation of the execute method of the ExceptionHandler.
	 * Used to handle global exceptions
	 */
	public ActionForward execute(Exception exception, 
								 ExceptionConfig exceptionConfig, 
								 ActionMapping mapping, 
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws ServletException {
		//print the exception stack to the console
		exception.printStackTrace();
		return super.execute(exception, exceptionConfig, mapping, form, request, response);
	}

}
