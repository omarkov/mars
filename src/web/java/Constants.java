package web.java;

public final class Constants {

    public static final String CONNECTION = "connection";

    /**
     * The package name for this application.
     */
    public static final String Package = "web.java";


    /**
     * The session scope attribute under which the Username
     * for the currently logged in user is stored.
     */
    public static final String USER_KEY = "user";

    /**
     * The session scope attribute under which the Username
     * for the currently logged in user is stored.
     */
    public static final String GROUPNAME_KEY = "groupname";
    

    /**
     * The token that represents a nominal outcome
     * in an ActionForward.
     */
    public static final String SUCCESS = "success";


    /**
     * The token that represents the logon activity
     * in an ActionForward.
     */
    public static final String LOGIN = "login";


    /**
     * The token that represents the welcome activity
     * in an ActionForward.
     */
    public static final String WELCOME = "welcome";


    /**
     * The value to indicate debug logging.
     */
    public static final int DEBUG = 1;


    /**
     * The value to indicate normal logging.
     */
    public static final int NORMAL = 0;
	
	public static final String EXPIRATION_DATE_UNLIMITED = "unlimited";
	public static final String EXPIRATION_DATE_LIMITED = "limited";
	
	public static final String NO_DEPARTMENT_SELECTED = "No Department";

}
