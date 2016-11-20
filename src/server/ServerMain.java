package server;


import java.util.Date;
import java.util.List;
import java.io.*;

import net.ModuleDescription;
import net.ModuleListener;
import net.NetworkFactory;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import server.mail.MessageQueue;
import server.controller.*;
import server.domain.*;
import server.*;
import server.controller.exceptions.ControllerException;



public class ServerMain extends Thread implements ModuleListener
{
    private Logger logger = Logger.getLogger(ServerMain.class);
    private NetworkFactory net;
    private boolean isRunning = false;

    private MaintenanceTask maintenanceTask;

    private void init(String[] args) throws Throwable {
	if (args.length == 0)
	    throw new Exception("configuration file not specified");
        // initialize log4j
        BasicConfigurator.configure();
        logger.info("Initializing controller.");

        // setup a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    quit();
                }
            });

        // load configuration
        Config.load(args[0]);

        // initialize the connection to the database
        DatabaseAccess.init();

        //Initialize the System-Module
        ModuleDescription module = new ModuleDescription("system", "V1.0");
        module = DepartmentController.getInstance().addCommands(module);
        module = UserController.getInstance().addCommands(module);
        module = GroupController.getInstance().addCommands(module);
        module = SmartRoomProfileController.getInstance().addCommands(module);
        module = IdentificationController.getInstance().addCommands(module);
        module = SmartRoomController.getInstance().addCommands(module);
        module = MessageQueue.instance().addCommands(module);

        DatabaseAccess.startSession();

        List<User> users = (List<User>)UserController.getInstance().getAllUsers();

        boolean adminExists = false;

        for(User user: users)
            if(user.isAdministrator())
                adminExists = true;

        if(!adminExists)
            UserController.getInstance().createUser( "admin",
                                                    "admin",
                                                    "ad",
                                                    "min",
                                                    "root@localhost.localdomain",
                                                    "Admin",
                                                    true,
                                                    false,
                                                    null);

        // create login system "PDA"
        try {
            IdentificationController.getInstance().createLogInSystem("PDA");
        } catch (MarsException ex) {
            ex.printStackTrace();
        }

        DatabaseAccess.commitSession();


        // start the maintenance task
        maintenanceTask = new MaintenanceTask();
        maintenanceTask.schedule();

        // start the networking
        net = NetworkFactory.getInstance();

        net.setModuleDescription(module);
        net.addModuleListener(this);
                
        logger.warn("starting Controller");
        net.startController(1234);
                                
        isRunning = true;
        logger.info("Running...");
    }
   
    private void shutdown() {
        logger.info("Shutting down the controller.");

        // stop networking
        net.stopController();

        // close connection to the database
        DatabaseAccess.shutdown();

        logger.info("Shutdown complete.");
        logger.info("Have a nice day!");
    }

    public void quit() {
        isRunning = false;
        logger.info("Quitting.");
    }

    public void daemonize() {
	String pidfile_path = System.getProperty("daemon.pidfile");

	// if there is no pidfile, we don't want to daemonize
	if (pidfile_path == null)
	    return;

	// close the pid file on exit
	File pidfile = new File(pidfile_path);
	pidfile.deleteOnExit();

	// close stdout & stderr
	System.out.close();
	System.err.close();
    }

    public void run() {
	while(isRunning) {
	    try {
		Thread.sleep(100000);
	    } catch(Exception e) {
		e.printStackTrace();
	    }  
	}    
    }
        
    public static void main(String[] args) {
        ServerMain application = new ServerMain();

        try {
            application.init(args);
	    application.daemonize();
            application.run();
        } catch (Throwable ex) {
	    System.err.println("*** Error during initialization.");
	    System.err.println(ex.getMessage());
	}
    }
        
    public void registerModule(ModuleDescription d) {
        SmartRoomComponentController.getInstance().createSmartRoomComponent(d);                
    }
}
