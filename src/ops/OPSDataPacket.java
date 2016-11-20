package ops;

import java.io.IOException;
import java.io.InputStream;


public class OPSDataPacket {
    private int interval;
    private int counter;
    private int version;
    private int movementCounter;
    private int lifeCycleCounter;
    private int siteCode;
    private int tagID;
    private int tagType;
    private int baseAddress;
    private int checksum;
    private int alarm;
    private int originalReader;
    private int softwareVersion;

    public OPSDataPacket(InputStream s) throws OPSPacketException {	
	// wait for header "!**"
	try {
	  /*  while(true) {
		if ((char)s.read() == '!')
		    if ((char)s.read() == '*')
			if ((char)s.read() == '*')
			    break;
	    }*/
		if (!((char)s.read() == '!' && (char)s.read() == '*' && (char)s.read() == '*'))
			throw new OPSPacketException("No header (!**) found");

	} catch (IOException ex) {
	    throw new OPSPacketException("Caught IOException while waiting for header: " + ex.getMessage());
	}

	readFromStream(s);
    }

    private void readLifeCycleCounter(InputStream s) throws OPSPacketException {
	try {
	    int b0 = s.read();
	    int b1 = s.read();
	    int b2 = s.read();
	    int b3 = s.read();
	
	    lifeCycleCounter = (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
	} catch (IOException ex) {
	    throw new OPSPacketException("Caught IOException while reading life cycle counter: " + ex.getMessage());
	}
    }

    private void readSiteCode(InputStream s) throws OPSPacketException {
	try {
	    int b0 = s.read();
	    int b1 = s.read();
	    int b2 = s.read();
	
	    siteCode = (b0 << 16) | (b1 << 8) | b2;
	} catch (IOException ex) {
	    throw new OPSPacketException("Caught IOException while reading site code: " + ex.getMessage());
	}
    }
	
    private void readTagID(InputStream s) throws OPSPacketException {
	try {
	    int b0 = s.read();
	    int b1 = s.read();
	    int b2 = s.read();
	    int b3 = s.read();

	    tagID = (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
	} catch (IOException ex) {
	    throw new OPSPacketException("Caught IOException while reading life tag ID: " + ex.getMessage());
	}
    }

    private void readFromStream(InputStream s) throws OPSPacketException
    {
	if (s == null)
	    throw new OPSPacketException("Got NULL InputStream");

	try {
	    // byte 4: time interval between transmissions
	    interval = s.read();

	    // byte 5: Reed switch open/close and counter
	    counter = s.read();

	    // byte 6: software version
	    version = s.read();
	
	    // byte 7-8: reserved bytes
	    s.skip(2);
	
	    // byte 9: movement counter
	    movementCounter = s.read();
	
	    // byte 10-13: life cycle counter
	    readLifeCycleCounter(s);
			
	    // byte 14-16: site code
	    readSiteCode(s);

	    // byte 17-20: tag id
	    readTagID(s);

	    // byte 21: tag type
	    tagType = s.read();

	    // byte 22: base address of multifunction reader
	    baseAddress = s.read();
	
	    // byte 23: reserved
	    s.skip(1);
	
	    // byte 24: checksum
	    checksum = s.read();

	    // byte 25: reserved
	    s.skip(1);

	    // byte 26: alarm indication byte
	    alarm = s.read();

	    // byte 27: ?
	    originalReader = s.read();

	    // byte 28-29: reserved for repeater communication
	    s.skip(2);

	    // byte 30: software version
	    softwareVersion = s.read();

	    // byte 31-32: post-amble
	    if (((char)s.read() != 10) && ((char)s.read() != 13))
		    throw new OPSPacketException("Wrong post-amble");
	} catch (IOException ex) {
	    throw new OPSPacketException("Caught IOException: " + ex.getMessage());
	}
    }
	
    public String toString() {
	String s = "OPSPacket:\n";
		
	s += "Interval: " + interval + "\n";
	s += "Counter: " + counter + "\n";
	s += "Version: " + version + "\n";
	s += "Movement Counter: " + movementCounter + "\n";
	s += "Lifecycle Counter: " + lifeCycleCounter + "\n";
	s += "Site code: " + siteCode + "\n";
	s += "Tag ID: " + tagID + "\n";
	s += "Tag Type: " + tagType + "\n";
	s += "Base Address: " + baseAddress + "\n";
	s += "Checksum: " + checksum + "\n";
	s += "Alarm bit: " + alarm + "\n";
	s += "Original Reader: " + originalReader + "\n";
	s += "Software Version: " + softwareVersion + "\n";

	return s;
    }

    public int getTagID(){
	return tagID;
    }
}
