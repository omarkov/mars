package light;

import java.io.*;
import java.util.*;
import gnu.io.*;

import net.*;


public class LightControl implements NetworkEventListener {
    private NetworkFactory net;
    private Properties config = new Properties();

    InputStream inputStream;
    OutputStream outputStream;
    SerialPort serialPort;


    private void loadConfiguration(String file) {
	// set default values
	config.setProperty("serial_port", "/dev/ttyS1");
	config.setProperty("ROOM_ID", "0");
	config.setProperty("RED_CHANNEL", "1");
	config.setProperty("GREEN_CHANNEL", "2"); 
	config.setProperty("BLUE_CHANNEL", "3");

	try {
	    // load the configuration file
	    config.load(new FileInputStream(file));
	} catch (Exception ex) {
	    System.err.println("Couldn't load configuration: " + ex.getMessage());
	    System.err.println("Using defaults.");
	}
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

    public static void main(String[] args) {
	// we absolutely need one argument
	if (args.length == 0) {
	    System.err.println("No configuration file specified");
	    return;
	}

	// construct the main class
	try {
	    LightControl lc = new LightControl(args);
	    lc.daemonize();
	} catch (Exception ex) {
	    System.err.println("*** Error during initialization");
	    System.err.println(" " + ex.getMessage());
	    return;
	}

	// go into the main loop
	System.out.println("Running...");
	while(true) {
	    try {
		Thread.sleep(100000);
	    } catch (Exception ex) {
		System.err.println("*** Error during execution; continuing");
		System.err.println(" " + ex.getMessage());
	    }
	}
    }

    public void initNetwork() {
        net = NetworkFactory.getInstance();
        
        ModuleDescription module = new ModuleDescription("LIGHT", "V1.0");
	module.setWebModule(true);

        NumericParameter red   = new NumericParameter("Red", new Integer(50), "rw", 0, 255, 10, this);
        NumericParameter green = new NumericParameter("Green", new Integer(50), "rw", 0, 255, 10, this);
        NumericParameter blue  = new NumericParameter("Blue", new Integer(50), "rw", 0, 255, 10, this);
        NumericParameter mood  = new NumericParameter("Mood", new Integer(0), "rws", 0, 5, 1, this);

	Command moodCMD = new Command("mood", new StringParameter(), this);
	moodCMD.addParameter(new NumericParameter("nr", null));

	Command fade = new Command("fade", new BooleanParameter(), this);
	Command update = new Command("update", new BooleanParameter(), this);
	Command beforeUpdate = new Command("beforeUpdate", new BooleanParameter(), this);
	Command enterRoom = new Command("enterRoom", new BooleanParameter(), this);
	Command exitRoom  = new Command("exitRoom", new BooleanParameter(), this);

        module.addInterface(moodCMD);
        module.addInterface(fade);
        module.addInterface(update);
        module.addInterface(beforeUpdate);
        module.addInterface(enterRoom);
        module.addInterface(exitRoom);
        module.addInterface(red);
        module.addInterface(green);
        module.addInterface(blue);
        module.addInterface(mood);

        net.setModuleDescription(module);
        net.startModule("localhost", 1234);
    }

    // module krimskrams
    private int RED_CHANNEL     = 1;
    private int GREEN_CHANNEL   = 2;
    private int BLUE_CHANNEL    = 3;
    private int ROOM_ID         = 3;

    public LightControl(String args[]) throws Exception {
	loadConfiguration(args[0]);

    	initNetwork();

        ROOM_ID       = Integer.parseInt(config.getProperty("ROOM_ID").trim());
        RED_CHANNEL   = Integer.parseInt(config.getProperty("RED_CHANNEL").trim());
        GREEN_CHANNEL = Integer.parseInt(config.getProperty("GREEN_CHANNEL").trim());
        BLUE_CHANNEL  = Integer.parseInt(config.getProperty("BLUE_CHANNEL").trim());

	String portName = config.getProperty("serial_port").trim();
    
        try {
            System.out.println("Opening serial port " + portName);

	    CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);

            serialPort = (SerialPort) portId.open("LightControl", 2000);
            serialPort.setSerialPortParams(9600,
					   SerialPort.DATABITS_7,
					   SerialPort.STOPBITS_1,
					   SerialPort.PARITY_EVEN);
            serialPort.sendBreak(1000);
        } catch (PortInUseException e) {
	    throw new Exception("Port is already in use");
	} catch (NoSuchPortException e) {
	    throw new Exception("No such port: " + portName);
        } catch (UnsupportedCommOperationException e) {
	    throw new Exception("Unsupported Operation: " + e.getMessage());
        }

	inputStream = serialPort.getInputStream();
	outputStream = serialPort.getOutputStream();

	LMTLRConnection c = LMTLRConnection.getInstance();
	c.setStreams(inputStream, outputStream);
    }

    private Integer red = new Integer(0);
    private Integer blue = new Integer(0);
    private Integer green = new Integer(0);
    private Integer mood = new Integer(1);

    public Boolean fade()
    {
        LMTLRConnection.getInstance().fade(ROOM_ID);
        return Boolean.TRUE;
    }

    public Boolean update()
    {
       System.out.println("UPDATE");
       return Boolean.TRUE;
    }

    public Boolean beforeUpdate()
    {
       System.out.println("BEFORE UPDATE");
       fade();
       return Boolean.TRUE;
    }

    public Boolean enterRoom()
    {
       System.out.println("ENTER ROOM");
       return Boolean.TRUE;
    }

    public Boolean exitRoom()
    {
        System.out.println("EXIT ROOM");
        setRed(new Integer(0));
        setBlue(new Integer(0));
        setGreen(new Integer(0));
        setMood(new Integer(1));
        // fade();
        return Boolean.TRUE;
    }

    public Integer getRed()
    {
        return red;
    }

    public boolean setRed(Integer val)
    {
        int value = val.intValue();
        if(value > 255) value = 255;
        if(value < 0) value = 0;

        red = new Integer(value);
        LMTLRConnection.getInstance().setValue(value, ROOM_ID, RED_CHANNEL);
        return true;
    }


    public Integer getGreen()
    {
        return green;
    }

    public boolean setGreen(Integer val)
    {
        int value = val.intValue();
        if(value > 255) value = 255;
        if(value < 0) value = 0;

      	green = new Integer(value);
       	LMTLRConnection.getInstance().setValue(value, ROOM_ID, GREEN_CHANNEL);
       	return true;
    }


    public Integer getBlue()
    {
        return blue;
    }

    public boolean setBlue(Integer val)
    {
        int value = val.intValue();
        if(value > 255) value = 255;
        if(value < 0) value = 0;

        blue = new Integer(value);
        LMTLRConnection.getInstance().setValue(value, ROOM_ID, BLUE_CHANNEL);
        return true;
    }

   public Integer getMood()
    {
        return mood;
    }

    public String mood(Integer _nr)
    {
	int nr = _nr.intValue();

	if(nr == 1) return "Arctic Winter";
	else if(nr == 2) return "Moulin Rouge";
	else if(nr == 3) return "Saphire Ocean";
	else if(nr == 4) return "Seven of Nine";
	else if(nr == 5) return "Disco Fever";
	else return " -- ";
    }

    public boolean setMood(Integer val)
    {
        mood = val;
        if(val.intValue() > 0)
            LMTLRConnection.getInstance().stimmung(val.intValue(), ROOM_ID);
        return true;
    }
}
