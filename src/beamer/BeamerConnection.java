package beamer;

import java.io.*;

public class BeamerConnection
{
    private static BeamerConnection instance;
    private static int TIMEOUT  = 3000;
    private static int POLLTIME = 300;
    private static int STX = 2;
    private static int ETX = 3;

    InputStream in;
    OutputStream out;
    
    private BeamerConnection()
    {
    }

    public void setStreams(InputStream _in, OutputStream _out)
    {
        in = _in;
        out = _out;
    }

    public static synchronized BeamerConnection getInstance()
    {
        if(instance == null)
            instance = new BeamerConnection();

        return instance;
    }

    private void sendCommand(String cmd) {
	System.out.println("Sending: " + cmd);

	try {
	    out.write(cmd.getBytes());

	    in.read();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

    public void powerOn() {
	sendCommand("POWER ON");
    }

    public void powerOff() {
	sendCommand("POWER OFF");
    }

}
