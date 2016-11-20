package light;

import java.io.*;

public class LMTLRConnection
{
    private static LMTLRConnection instance;
    private static int TIMEOUT  = 3000;
    private static int POLLTIME = 300;
    private static int STX = 2;
    private static int ETX = 3;

    InputStream in;
    OutputStream out;
    
    private LMTLRConnection()
    {
    }

    public void setStreams(InputStream _in, OutputStream _out)
    {
        in = _in;
        out = _out;
    }

    public static synchronized LMTLRConnection getInstance()
    {
        if(instance == null)
        {
            instance = new LMTLRConnection();
        }
        return instance;
    }

    private int available(int timeout) throws IOException
    {
        int time = 0;
        while(in.available() == 0 && time < timeout)
        {
            try
            {
                Thread.sleep(POLLTIME);
                time += POLLTIME;
            }catch(Exception e)
            {
            }
        }

        return in.available();
    }

    private String readData() throws IOException
    {
        int b;

        String data = "";

        b = in.read();

        if(b != STX)
            throw new RuntimeException("Illegal position in stream");

        while((b = in.read()) != ETX)
        {
            data += (char)b;
        }

        return data;
    }

    private boolean readAck() throws RuntimeException, IOException
    {
        if(available(TIMEOUT) >= 3)
        {
            int b1 = in.read(); 
            int b2 = in.read(); 
            int b3 = in.read(); 

            if(b1 == 2 && b2 == 6 && b3 == 3)
            {
                System.out.println("ACK");
                return true;
            }else
            {
                System.out.println("NO ACK");
                return false;
            }
        }else
        {
            throw new RuntimeException("No ACK/NACK in timeout");
        }
    }

    private String intToString(int nr)
    {
        return Integer.toString(nr).trim();
    }

    private String sendCommand(String addr, String type, String cmd) throws RuntimeException
    {
        String msg = addr + type + cmd;

        try
        {
            System.out.println("Send " + msg);
            out.write(STX);
            out.write(msg.getBytes());
            out.write(ETX);
            
            if(readAck() && available(300) > 0)
            {
                String data = readData();
                if(data.equalsIgnoreCase("NACK!"))
                {
                    System.out.println("NACK");
                }
                return data;
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setFadeTime(int ticks)
    {
        sendCommand("TLR0R0G0", "T1", "FADE!"  + intToString(ticks));
    }

    public void fade(int r)
    {
        sendCommand("TLR0R" + intToString(r) + "G0", "T1", "FADE");
    }

    public void stimmung(int nr, int r)
    {
        
        // sendCommand("TLR1R1G1", "T1", "S" + intToString(nr) + "!");
        sendCommand("TLR0R" + intToString(r) + "G0", "T1", "S" + intToString(nr) + "!");
    }

    public void setValue(int value, int r, int g)
    {
        sendCommand("TLR0R" + intToString(r) + "G" + intToString(g), "T2", "W" + intToString(value) + "!");
    }

    public void fadeUp(int sec, int ra, int gr)
    {
        sendCommand("TLR0R" + intToString(ra) +"G" + intToString(gr), "T2", "DP" + intToString(sec));
    }

    public void brighter(int ticks)
    {
        sendCommand("TLR0R0G0", "T2", "D+" + intToString(ticks));
    }

    public void darker(int ticks)
    {
        sendCommand("TLR0R0G0", "T2", "D-" + intToString(ticks));
    }

}
