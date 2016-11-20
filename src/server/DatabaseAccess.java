package server;

// hibernate
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// logging
import org.apache.log4j.Logger;
 

public class DatabaseAccess
{
    private static Configuration configuration;
    private static SessionFactory sessionFactory;
    private static Logger logger;

    public static final ThreadLocal session = new ThreadLocal();


    public static void init() throws Throwable
    {
	try {
	    logger = Logger.getLogger(DatabaseAccess.class);
	    configuration = new Configuration()
		.addClass(server.domain.AttributeType.class)
		.addClass(server.domain.ComponentAttribute.class)
		.addClass(server.domain.ComponentAttributeValue.class)
		.addClass(server.domain.SmartRoomComponent.class)
		.addClass(server.domain.SmartRoomProfile.class)
		.addClass(server.domain.User.class)
		.addClass(server.domain.Department.class)
		.addClass(server.domain.GroupMars.class)
		.addClass(server.domain.ComponentSetting.class)
		.addClass(server.domain.Identification.class)
		.addClass(server.domain.LogInSystem.class)
		.setProperty("hibernate.current_session_context_class", "thread")
		.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
		.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
		.setProperty("hibernate.connection.url", server.Config.get("db_url"))
		.setProperty("hibernate.connection.username", server.Config.get("db_username"))
		.setProperty("hibernate.connection.password", server.Config.get("db_password"))
		.setProperty("hibernate.cglib.use_reflection_optimizer", "false");

	    sessionFactory = configuration.buildSessionFactory();
	} catch (Throwable ex) {
	    logger.error("Failed to initialize DatabaseAccess.", ex);
	    throw ex;
	}
    }

    public static void shutdown()
    {
	    DatabaseAccess.closeSession();
	    sessionFactory.close();
    }

    public static Session startSession()
    {
        Session s = DatabaseAccess.currentSession();
        s.beginTransaction();
        return s;
    }

    public static void rollback()
    {
        Session s = DatabaseAccess.currentSession();
        try
        {
            s.getTransaction().rollback();
        }
        catch(Exception e)
        { 
            // BLUB 
        }
        s.close();
    }

    public static void commitSession()
    {
        Session s = DatabaseAccess.currentSession();
        Transaction tx = s.getTransaction();
        if(tx != null)
        {
            try
            {
                tx.commit();
            }catch(Exception e)
            {
                //BLUB
            }
        }
        s.close();
    }

    public static Session currentSession()
    {
/*        Session s = (Session)session.get();

        if (s == null) {
            s = sessionFactory.openSession();
            session.set(s);
        }else if(!s.isOpen())
	{
  	    s.close();
            s = sessionFactory.openSession();
            session.set(s);
	}

        return s;*/
        return sessionFactory.getCurrentSession();
    }

    public static void closeSession()
    {
	Session s = (Session)session.get();

	if (s != null)
	    s.close();

	session.set(null);
    }
}
