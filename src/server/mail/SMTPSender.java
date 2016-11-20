package server.mail;

import java.util.*;
import java.net.*;
import java.io.*;


public class SMTPSender
{
    private String login;
    private String password;

    private Socket socket;
    private BufferedWriter out;
    private BufferedReader in; 

    private void sendLine(String cmd)
    {
	try {
	    out.write(cmd);
	    out.newLine();
	    out.flush();
	} catch (IOException ex) {
	    ex.printStackTrace();
	}
    }

    private String receiveLine()
    {
	try {
	    return in.readLine();
	} catch (IOException ex) {
	    ex.printStackTrace();
	    return "boo";
	}
    }

    private boolean isPositiveResponseCode(int code)
    {
	// check first digit of response code
	switch(code % 100) {
      	    case 1: // positive preliminary
	    case 2: // positive completion reply
	    case 3: // positive intermediate reply
		return true;

	    case 4: // transient negative completion
 	    case 5: // permanent negative completion reply
		return false;

            default: // unknown
		return false;
	}
    }

    private String checkResponse()
    {
	String response = receiveLine();
	int code = Integer.parseInt(response.substring(0, 3));
	boolean is_multiline = response.charAt(3) == '-';

	// read first message
	String message = response.substring(4, response.length());

	if (is_multiline) {
	    message += "\n";

	    while(is_multiline) {
		response = receiveLine();
		message += response.substring(4, response.length()) + "\n";
		is_multiline = response.charAt(3) == '-';
	    }
	}

	System.out.println(message);

	return message;
    }


    public void open(String host, String login, String password) throws MailException
    {
	try {
	    socket = new Socket(host, 25);
	    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	    checkResponse();

	    // send hello
	    sendLine("EHLO " + InetAddress.getLocalHost().getCanonicalHostName());
	    checkResponse();

	    // request LOGIN authentication
	    sendLine("AUTH LOGIN");
	    checkResponse();

	    // authenticate
	    sendLine(Base64.encodeBytes(login.getBytes()));
	    checkResponse();
	    sendLine(Base64.encodeBytes(password.getBytes()));
	    checkResponse();
	} catch (Exception ex) {
	    throw new MailException(ex.getMessage());
	}
    }

    public void close()
    {
	try {
	    socket.close();
	} catch(IOException ex) {
	    ex.printStackTrace();
	}
    }

    public void send(MailMessage msg) throws MailException
    {
	// set sender
	sendLine("MAIL FROM:<" + msg.getSender() + ">");
	checkResponse();

	// set recipients
	for (Iterator i = msg.getRecipients().iterator(); i.hasNext();) {
	    String rcpt = (String)i.next();
	    sendLine("RCPT TO:<" + rcpt + ">");
	    checkResponse();
	}

	// set body
	sendLine("DATA");
	checkResponse();

	sendLine(msg.construct() + "\n.");
	checkResponse();
    }
}
