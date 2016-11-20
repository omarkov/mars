package beamer;

import java.io.*;
import java.util.*;
import gnu.io.*;

import net.*;


public class BeamerControl implements NetworkEventListener {
    static CommPortIdentifier portId;
    static Enumeration portList;

    private NetworkFactory net;

    InputStream inputStream;
    OutputStream outputStream;
    SerialPort serialPort;

    private Properties config = new Properties();

    private static void loadResource(String resources, Properties props) {
        InputStream in = BeamerControl.class.getResourceAsStream(resources);
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


    public void initNetwork() {
        System.out.print("Starting network...");

        net = NetworkFactory.getInstance();
        
        ModuleDescription module = new ModuleDescription("BeamerControl", "V1.0");
	module.setWebModule(false);

	Command enterRoom = new Command("enterRoom", new BooleanParameter(), this);
	Command exitRoom  = new Command("exitRoom", new BooleanParameter(), this);
        module.addInterface(enterRoom);
        module.addInterface(exitRoom);

        net.setModuleDescription(module);
        net.startModule("localhost", 1234);

        System.out.println("done");
    }


    public BeamerControl() {
        loadResource("beamercontrol-config.properties", config);
    	initNetwork();

        try {
            System.out.print("Opening serial port...");

            serialPort = (SerialPort) portId.open("BeamerControl", 2000);
            serialPort.setSerialPortParams(9600,
                                           SerialPort.DATABITS_7,
                                           SerialPort.STOPBITS_1,
                                           SerialPort.PARITY_EVEN);
            serialPort.sendBreak(1000);
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }

        try {
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();

            BeamerConnection c = BeamerConnection.getInstance();
            c.setStreams(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean enterRoom()
    {
        BeamerConnection c = BeamerConnection.getInstance();
        c.powerOn();
        return Boolean.TRUE;
    }

    public Boolean exitRoom()
    {
        BeamerConnection c = BeamerConnection.getInstance();
        c.powerOff();
        return Boolean.TRUE;
    }

    public static void main(String[] args) {
    	System.out.println("Starting...");
        portList = CommPortIdentifier.getPortIdentifiers();
        System.out.println(portList.hasMoreElements());

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            System.out.println("Considering: " + portId.getName());
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals("/dev/ttyS1")) {
                    BeamerControl reader = new BeamerControl();
                    while(true) {
                        try {
                            Thread.sleep(100000);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
