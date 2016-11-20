package ops;

import java.io.*;
import java.util.*;
import gnu.io.*;

import net.*;


public class SimpleRead implements Runnable, SerialPortEventListener, NetworkEventListener {
    private NetworkFactory net;

    Hashtable ht = new Hashtable();
    DeleteThread delThread = new DeleteThread(ht);
    
    LoggedTags loggedTags = new LoggedTags(ht);
    
    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;

    public Properties config = new Properties();


    public static void main(String[] args) {
	if (args.length == 0) {
	    System.err.println("no configuration file specified");
	    return;
	}

	// construct the main classes
	try {
	    SimpleRead reader = new SimpleRead(args);
	    reader.daemonize();
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

		// delete possible pidfile
		File pidfile = getPIDFile();

		if (pidfile != null)
		    pidfile.delete();
	    }
	}
    }

    private static File getPIDFile() {
	String pidfile_path = System.getProperty("daemon.pidfile");

	if (pidfile_path == null)
	    return null;

	return new File(pidfile_path);
    }

    private void daemonize() {
	File pidfile = getPIDFile();

	// if there is no pidfile, we don't want to daemonize
	if (pidfile == null)
	    return;

	pidfile.deleteOnExit();

	// close stdout & stderr
	System.out.close();
	System.err.close();
    }

    private void loadConfiguration(String file) {
	// set default values
	config.setProperty("serial_port", "/dev/ttyS1");
	config.setProperty("logoff_timeout", "10");

	try {
	    // load the configuration file
	    config.load(new FileInputStream(file));
	} catch (Exception ex) {
	    System.err.println("Couldn't load configuration: " + ex.getMessage());
	    System.err.println("Using defaults.");
	}
    }

    public void initNetwork() {
	net = NetworkFactory.getInstance();

	ModuleDescription module = new ModuleDescription("OPS", "V1.0");
	NumericParameter timeout = new NumericParameter("timeout", new Integer(5), "rw", 0, 10000, 1, this);

	module.addInterface(timeout);

	net.setModuleDescription(module);
	net.startModule("localhost", 1234);

        // create login system "OPS"
        try {
            net.call("system", "createLogInSystem", new Object[]{"OPS"});
        } catch (NetworkException ex) {
	    System.err.println("Couldn't create new login system in server");
	}
    }

    public SimpleRead(String args[]) throws Exception {
	loadConfiguration(args[0]);
    	initNetwork();

	String portName = config.getProperty("serial_port").trim();

	// Open the serial port given in the configuration file
        try {
            System.out.println("Opening serial port " + portName);

	    CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);

            serialPort = (SerialPort) portId.open("OPS", 2000);
            serialPort.setSerialPortParams(115200,
					   SerialPort.DATABITS_8,
					   SerialPort.STOPBITS_1,
					   SerialPort.PARITY_NONE);
	    serialPort.notifyOnDataAvailable(true);
            serialPort.sendBreak(1000);
        } catch (PortInUseException e) {
	    throw new Exception("Port is already in use");
	} catch (NoSuchPortException e) {
	    throw new Exception("No such port: " + portName);
        } catch (UnsupportedCommOperationException e) {
	    throw new Exception("Unsupported Operation: " + e.getMessage());
        }

	// try to get the input stream
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {
	    throw new Exception("Couldn't get input from port: " + e.getMessage());
	}

	// add an listener to listen for events on the port
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {
	    throw new Exception("Couldn't add any more listeners");
	}

        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {    	
        try {
	    while(true) {
                Thread.sleep(100000);
	    }
        } catch (InterruptedException e) {
	    // don't care
	}
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;

        case SerialPortEvent.DATA_AVAILABLE:
	    try {
		while(inputStream.available() > 0) {
		    OPSResponsePacket p = new OPSResponsePacket(inputStream, loggedTags);
		}
	    } catch(Exception e) {
		System.err.println("Error reading comport!!!");
		System.err.println(e);
	    }
        }
    }
}
