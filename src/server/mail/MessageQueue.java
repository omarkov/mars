package server.mail;

import java.util.*;
import net.*;


public class MessageQueue implements NetworkEventListener
{
    private static MessageQueue instance;
    private List queue = new LinkedList();


    public static MessageQueue instance()
    {
	if (instance == null)
	    instance = new MessageQueue();

	return instance;
    }

    public void insert(MailMessage msg)
    {
	queue.add(msg);
    }

    public int pending()
    {
	return queue.size();
    }

    public void clear()
    {
	queue.clear();
    }

    public void composeMessage(String sender, List recipients, String subject, String body)
    {
	MailMessage msg = new MailMessage();
	msg.setSender(sender);
	msg.setRecipients(recipients);
	msg.setSubject(subject);
	msg.setBody(body);

	insert(msg);
    }

    public void sendAllMessages() throws MailException
    {
	// open a connection
	SMTPSender mailer = new SMTPSender();
	mailer.open(server.Config.get("mailer_server"),
		    server.Config.get("mailer_username"),
		    server.Config.get("mailer_password"));

	// send all pending messages
	for (Iterator it = queue.iterator(); it.hasNext(); )
	    mailer.send((MailMessage)it.next());

	// we're finished
	mailer.close();
    }

    public ModuleDescription addCommands(ModuleDescription module)
    {
	Command composeMessage = new Command("composeMessage", null, this);

	composeMessage.addParameter(new StringParameter("sender", null));
	composeMessage.addParameter(new ListParameter("sender", String.class));
	composeMessage.addParameter(new StringParameter("subject", null));
	composeMessage.addParameter(new StringParameter("body", null));

	module.addInterface(composeMessage);

	return module;
    }
}
