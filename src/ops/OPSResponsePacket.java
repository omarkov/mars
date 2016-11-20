package ops;

import java.io.IOException;
import java.io.InputStream;


public class OPSResponsePacket {	
    private int dataLength;
    private int networkID;
    private int receiverID;
    private int nodeID;
    private int command;
    private int checksum;

    private OPSDataPacket data;

    public OPSResponsePacket(InputStream s, LoggedTags l) throws OPSPacketException {	
	try {
		
	    // wait for header
	    int b = 0;
	    while ((b = s.read()) != 0x55);

	    dataLength = s.read();
	    networkID = s.read();
	    receiverID = s.read();
	    nodeID = s.read();
	    command = s.read();

	    if (dataLength == 32 && command == 6) {
		data = new OPSDataPacket(s);
		System.out.println("TAGID: " + data.getTagID());
		l.insertTag(data.getTagID());
	    } else if(dataLength > 0)
	    {
	//	System.out.println("Package: " + dataLength + " / " + command);
		/* System.out.println("Skipping: " + dataLength);
		System.out.println("Skipped: " + s.skip(dataLength));*/
		s.skip(dataLength);
	    }

	    checksum = s.read();	
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }
	
    public String toString() {
	String s = "OPSResponsePacket\n";

	s += "Data length: " + dataLength + "\n";
	s += "Network ID: " + networkID + "\n";
	s += "Receiver ID: " + receiverID + "\n";
	s += "Node ID: " + nodeID + "\n";
	s += "Command: " + command + "\n";
	s += "Checksum: " + checksum + "\n";
	
	return s;
    }
}
