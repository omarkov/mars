package server.mail;

import java.util.*;



public class MailMessage
{
    private String sender;
    private List recipients;
    private String subject;
    private String body;


    public void setSender(String sender)
    {
	this.sender = sender;
    }

    public String getSender()
    {
	return sender;
    }


    public void setRecipient(String recipient)
    {
	recipients = new ArrayList();
	recipients.add(recipient);
    }

    public void setRecipients(List recipients)
    {
	this.recipients = recipients;
    }

    public List getRecipients()
    {
	return recipients;
    }


    public void setSubject(String subject)
    {
	this.subject = subject;
    }

    public String getSubject()
    {
	return subject;
    }


    public void setBody(String body)
    {
	this.body = body;
    }

    public String getBody()
    {
	return body;
    }


    public String construct()
    {
	String msg = "";

	// From:
	msg += "From: Mar-S Administrator <" + sender + ">\n";

	// Subject:
	msg += "Subject: " + subject + "\n";

	// X-Mailer:
	msg += "X-Mailer: Mar-S Automatic Mailing System X3000\n";

	// To:
	msg += "To: ";
	for (Iterator i = recipients.iterator(); i.hasNext(); )
	    msg += (String)i.next() + ", ";

	msg += "\n\n";

	msg += body;

	return msg;
    }
}
