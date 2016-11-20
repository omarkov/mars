// -*- indent-tabs-mode: nil -*-

package server.controller;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;

import net.*;
import server.controller.exceptions.*;
import server.domain.*;
import server.osi.*;
import server.DatabaseAccess;


public class SmartRoomController implements NetworkEventListener
{
    private Logger logger = Logger.getLogger(SmartRoomController.class);

    private List usersInRoom = new LinkedList();
    private Long activeUser;
    private boolean ready;

    private Long currentProfileID;


    // ------------------------------------------------------------------------
    // Singleton shit
    // ------------------------------------------------------------------------

    private static SmartRoomController _instance;

    public static SmartRoomController getInstance() {
        if (_instance == null)
            _instance = new SmartRoomController();

        return _instance;
    }

    public Long getActiveUser()
    {
        return activeUser;
    }


    // ------------------------------------------------------------------------
    // Network-callable commands
    // ------------------------------------------------------------------------

    public Boolean enterRoom(String loginSystemName, String tagID) throws ControllerException {
        Identification ident = IdentificationController.getInstance().getIdentificationByLSNameAndTag(loginSystemName, tagID);

        // do we know this tag?
        if(ident == null)
            throw new LogInException("Unknown identification: " + loginSystemName + "/" + tagID);


        User user = ident.getUser();
        logger.info(user.getFirstName() + " " + user.getLastName() + " entered the room.");

        if(usersInRoom.contains(user))
        {
            return Boolean.FALSE;
        }

        // are there any other users in the room?
        if (usersInRoom.isEmpty())
        {
            loginUser(user);

        }

        usersInRoom.add(user);
        return Boolean.TRUE;
    }

    public Boolean exitRoom(String loginSystemName, String tagID) throws ControllerException {
        // lade Identification und User aus DB
        Identification ident = IdentificationController.getInstance().getIdentificationByLSNameAndTag(loginSystemName, tagID);

        // ist uns dieses tag bekannt?
        if(ident == null)
            throw new LogInException("Unknown identification: " + loginSystemName + "/" + tagID);

        User user = ident.getUser();
        logger.info(user.getFirstName() + " " + user.getLastName() + " left the room.");

        usersInRoom.remove(user);

        // falls das der aktive benutzer war, wird der ausgeloggt
        if (activeUser != null && activeUser.equals(user.getId())) {
            logoutUser();

            System.out.println("Users in room: " + usersInRoom.size());

            // falls es noch benutzer im raum gibt, den nächstbesten einloggen
            if (!usersInRoom.isEmpty())
                loginUser((User)usersInRoom.get(0));
            else
            {
                NetworkFactory net = NetworkFactory.getInstance();
                try {
                    for(int i=0; i < net.getWebModulesCount(); i++)
                    { 
                        ModuleDescription d = net.getWebModuleDescription(i);
                        net.call(d.getName(), "exitRoom", new Object[]{});
                    }
                }catch(NetworkException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
        return Boolean.TRUE;
    }

    public Boolean getNextProfileMC() throws ControllerException {
        if(activeUser == null)
            return Boolean.FALSE;

	    final User user = UserController.getInstance().getUser(activeUser);

        if (user == null)
            return Boolean.FALSE;

        //SYNC
        Thread t = new Thread()
        {
           public void run()
           {
		try
		{
              DatabaseAccess.startSession();
              User currentUser = UserController.getInstance().getUser(activeUser);
              ArrayList<SmartRoomProfile> profiles = new ArrayList(currentUser.getSmartRoomProfiles());

              Collections.sort(profiles, new Comparator() {
                public int compare(Object o1, Object o2) 
                {
                    SmartRoomProfile profile1 = (SmartRoomProfile)o1;
                    SmartRoomProfile profile2 = (SmartRoomProfile)o2;
                    return profile1.getName().compareTo(profile2.getName());
                 }
              } );

              SmartRoomProfile currentProfile = SmartRoomProfileController.getInstance().getProfile(currentProfileID);
              setProfile(profiles.get((profiles.indexOf(currentProfile) + 1) % profiles.size()));
              DatabaseAccess.commitSession();
		}catch(Exception e)
		{
		     e.printStackTrace();
		}
           }
        };
        t.start();
        return Boolean.TRUE;
    }



    public Boolean changeUserMC(String name) throws ControllerException {
	final User user = UserController.getInstance().getUserByLoginID(name);

        if (activeUser != null && activeUser.equals(user.getId()))
            return Boolean.FALSE;

        //SYNC
        Thread t = new Thread()
        {
           public void run()
           {
		try
		{
                    DatabaseAccess.startSession();
                    logoutUser();
        	        loginUser(UserController.getInstance().getUser(user.getId()));
                    DatabaseAccess.commitSession();
		}catch(Exception e)
		{
		     e.printStackTrace();
		}
           }
        };
        t.start();
        return Boolean.TRUE;
    }

    public Boolean changeUser(User user) throws ControllerException {
        // ist der benutzer schon aktiv?
        if (activeUser != null && activeUser.equals(user.getId()))
            return Boolean.FALSE;

        loginUser(user);
        return Boolean.TRUE;
    }

    public List getUsersInRoom() throws ControllerException {
        List users = new ArrayList();
        for(User user: (List<User>)usersInRoom)
        {
            users.add(user.getUserLoginID());
        }
        return users;
    }


    // ------------------------------------------------------------------------
    // Login and logout
    // ------------------------------------------------------------------------

    private void loginUser(User user) throws LogInException {
        try {
            // wenn raum druch einen anderen Benutzer besetzt ist, exception
            if (activeUser != null && !activeUser.equals(user.getId())) {
                throw new RoomOccupiedException(UserController.getInstance().getUser(activeUser));
            } else if (activeUser != null && activeUser.equals(user)) {
                return; // nichts tun wenn man schon eingelogggt ist
            }

            activeUser = user.getId();

            SmartRoomProfile profile = user.getDefaultProfile();
            if(profile == null)
            {
                Set profiles = user.getSmartRoomProfiles();
                for(Iterator i=profiles.iterator(); i.hasNext();)
                {
                    profile = (SmartRoomProfile)i.next();
                    break;
                }
            }

            if(profile == null)
            {
                activeUser = null;
                return;
            }

            // set the default profile and start the media center
            startMediaCenter();

            NetworkFactory net = NetworkFactory.getInstance();
            try
            {
                for(int i=0; i < net.getWebModulesCount(); i++)
                { 
                    ModuleDescription d = net.getWebModuleDescription(i);
                    net.call(d.getName(), "enterRoom", new Object[]{});
                }
                // net.call("Mediacenter", "setRepeat", new Object[]{user.isSuperDuperRepeat()});
            }catch(NetworkException e)
            {
                System.out.println(e.getMessage());
            }


            setProfile(profile);
        } catch(ControllerException e) {
            throw new LogInException(e.getMessage());
        }
    }


    private void logoutUser() throws ControllerException {
        if (activeUser == null)
            return;

        // shutdown media center
        stopMediaCenter();

        // set all components to their inactive values

        activeUser = null;
    }


    // ------------------------------------------------------------------------
    // Profile management
    // ------------------------------------------------------------------------

    public Boolean setProfileMC(String name) throws ControllerException
    {
        if(activeUser == null)
            return Boolean.FALSE;

        User user = UserController.getInstance().getUser(activeUser);
        if(user != null)
        {
            Set<SmartRoomProfile> profiles = user.getSmartRoomProfiles();
            for(final SmartRoomProfile profile: profiles)
            {
                if(profile.getName().equalsIgnoreCase(name))
                {
                    //SYNC
                    Thread t = new Thread()
                    {
                       public void run()
                       {
                            try{ 

                    		DatabaseAccess.startSession();
                                setProfile(profile);
                    		DatabaseAccess.commitSession();
                            }catch(Exception e){}
                       }
                    };
                    t.start();
                }
            }
        }
        return Boolean.FALSE;
    }

    public Boolean setProfile(SmartRoomProfile profile) throws ControllerException {
        NetworkFactory net = NetworkFactory.getInstance();
        currentProfileID = profile.getId();

        User user = UserController.getInstance().getUser(activeUser);

        if(user != null)
        {
            try
            {
                net.call("Mediacenter", "setRepeat", new Object[]{user.isSuperDuperRepeat()});
            }catch(NetworkException e)
            {
                System.out.println(e.getMessage());
            }
        }

        // go through all components
        for (ComponentSetting setting : (Set<ComponentSetting>)profile.getComponentSettings()) {
            String moduleName = setting.getSmartRoomComponent().getName();

            // go through all parameter values
            try
            {
                net.call(moduleName, "beforeUpdate", new Object[]{});
            }catch(NetworkException e)
            {
                System.out.println(e.getMessage());
            }
            for (ComponentAttributeValue value : (Set<ComponentAttributeValue>)setting.getComponentAttributeValues()) {
                String parameterName = value.getComponentAttribute().getName();
                Object parameterValue = value.getValue();

                System.out.println("Setting " + moduleName + ": " + parameterName + " to " + parameterValue);

                // set the parameter
                try
                {
                    net.set(moduleName, parameterName, parameterValue);
                }catch(NetworkException e)
                {
                    e.printStackTrace();
                }
            }
            try
            {
                net.call(moduleName, "update", new Object[]{});
            }catch(NetworkException e)
            {
                System.out.println(e.getMessage());
            }
        }
        return Boolean.TRUE;
    }

    public List getProfilesMC() throws ControllerException {
        List profileNames = new LinkedList();

        try {
            List<SmartRoomProfile> profiles = UserController.getInstance().getProfilesForUser(activeUser);

            for (SmartRoomProfile profile : profiles)
                profileNames.add(profile.getName());

        } catch (Exception ex) {
            // *Gulp*
            ex.printStackTrace();
        }

	return profileNames;
    }

    // ------------------------------------------------------------------------
    // Media center management
    // ------------------------------------------------------------------------

    class StreamGobbler extends Thread
    {
        InputStream is;
        String type;
        
        StreamGobbler(InputStream is, String type)
        {
            this.is = is;
            this.type = type;
        }
        
        public void run()
        {
            try
            {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line=null;
                while ( (line = br.readLine()) != null)
                    System.out.println(type + ">" + line);    
                } catch (IOException ioe)
                  {
                    ioe.printStackTrace();  
                  }
        }
    }

    class MediaCenterThread extends Thread {
        private String login;

        public MediaCenterThread(String login) {
            this.login = login;
        }

        public void run() {
            String program = "freevo";
            String[] args = new String[0];

            try
            {
                Thread.sleep(2000);
            }catch(Exception e){}
/*
            try
            {
                Process proc = Runtime.getRuntime().exec(program); 
                StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
                StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
                errorGobbler.start();
                outputGobbler.start();
            }catch(IOException e)
            {
            }*/
            UserManager.runProgram(program, args, login);
        }
    }

    private MediaCenterThread mediaCenterThread;

    public Boolean readyMC() throws ControllerException
    {
        ready = true;
        return Boolean.TRUE;
    }

    private void startMediaCenter() {
        logger.info("Starting Media Center.");
        
        User user = null;

        try
        {
            user = UserController.getInstance().getUser(activeUser);
        }catch(Exception e)
        {
            return;
        }
        ready = false;
        mediaCenterThread = new MediaCenterThread(user.getUserLoginID());
        mediaCenterThread.start();
        // SYNC
        for(int i=0; i < 30 && !ready; i++)
        {
            try
            {
                Thread.sleep(1000);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            Thread.sleep(1500);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void stopMediaCenter() {
	try
	{
        NetworkFactory.getInstance().call("Mediacenter", "shutdown", new Object[]{});
	}catch(NetworkException e)
	{
	    e.printStackTrace();
 	}	
        // send message to stop
    }


    // ------------------------------------------------------------------------
    // Network module descriptions
    // ------------------------------------------------------------------------

    public ModuleDescription addCommands(ModuleDescription module) {
        Command enterRoom = new Command("enterRoom", new BooleanParameter(), this);
        enterRoom.addParameter(new StringParameter("loginSystem", null));
        enterRoom.addParameter(new StringParameter("tagID", null));
        module.addInterface(enterRoom);

        Command exitRoom = new Command("exitRoom", new BooleanParameter(), this);
        exitRoom.addParameter(new StringParameter("loginSystem", null));
        exitRoom.addParameter(new StringParameter("tagID", null));
        module.addInterface(exitRoom);

        Command changeUserMC = new Command("changeUserMC", new BooleanParameter(), this);
        changeUserMC.addParameter(new StringParameter("loginID", null));
        module.addInterface(changeUserMC);

        Command changeUser = new Command("changeUser", new BooleanParameter(), this);
        changeUser.addParameter(new ObjectParameter("user", User.class));
        module.addInterface(changeUser);

        Command getUsersInRoom = new Command("getUsersInRoom", new ListParameter(String.class), this);
        module.addInterface(getUsersInRoom);

        Command setProfile = new Command("setProfile", new BooleanParameter(), this);
        setProfile.addParameter(new ObjectParameter("profile", SmartRoomProfile.class));
        module.addInterface(setProfile);

        Command readyMC = new Command("readyMC", new BooleanParameter(), this);
        module.addInterface(readyMC);

        Command setProfileMC = new Command("setProfileMC", new BooleanParameter(), this);
        setProfileMC.addParameter(new StringParameter("profileName", null));
        module.addInterface(setProfileMC);

        Command getNextProfileMC = new Command("getNextProfileMC", new BooleanParameter(), this);
        module.addInterface(getNextProfileMC);

        Command getProfilesMC = new Command("getProfilesMC", new ListParameter(String.class), this);
        module.addInterface(getProfilesMC);

        return module;
    }
}
