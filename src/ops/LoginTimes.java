package ops;
import java.io.*;
import java.util.*;

public class LoginTimes{

    public static int logoffTime;
    
    String str = "";
    private Properties config = new Properties();

    public LoginTimes(){
	    loadResource("login.properties", config);
	    //LoginTimes.logoffTime = Integer.parseInt(config.getProperty("logoffTime").trim());
	    LoginTimes.logoffTime = 300;
    }
	
	
	private static void loadResource(String resources, Properties props) {
	    InputStream in = SimpleRead.class.getResourceAsStream(resources);
	    if (props == null) {
		System.err.println("Unable to locate resources: " + resources);
		System.err.println("Using default resources.");
	    } else {
		try {
		    props.load(in);
		} catch (java.io.IOException e) {
		    System.err.println("Caught IOException loading resources: "
                        + resources);
		    System.err.println("Using default resources.");
		}
	    }
	}
}
