package server;

import java.util.*;
import org.hibernate.*;
import server.mail.*;
import server.domain.*;
import server.controller.*;
import server.controller.exceptions.*;
import server.DatabaseAccess;


public class MaintenanceTask
{
    Timer purgeTimer;
    Timer mailTimer;

    class PurgeTask extends TimerTask
    {
        public void run()
        {
            System.out.println("Doing maintenance...");

            Session session = DatabaseAccess.startSession();

            // purge users, whose expiration date is in the past
            try {
                List expiredUserIds = session.createQuery("select expired_user.id " +
                                                          "from server.domain.User as expired_user " +
                                                          "where expired_user.expirationDate < current_date()").list();

                if (expiredUserIds.size() > 0)
                    System.out.println("Deleting " + expiredUserIds.size() + " expired users.");

                for (int i = 0; i < expiredUserIds.size(); i++) {
                    try {
                        UserController.getInstance().deleteUser((Long)expiredUserIds.get(i));
                    } catch (ElementDeletionException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }

            // notify all users, who are about to expire in 5 days
            try {
                List pendingUsers = session.createQuery("from server.domain.User as pending_user " +
                                                        "where pending_user.expirationDate > current_date() " +
                                                        "and pending_user.expirationDate < current_date() + 7" + 
                                                        "and pending_user.expirationNotified is false").list();

                for (int i = 0; i < pendingUsers.size(); i++) {
                    User user = (User)pendingUsers.get(i);
                    String body = "";

                    // write a message body
                    body += "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n";
                    body += "\n";
                    body += "Your account in the Mar-S system is expiring on " + user.getExpirationDate().toString() + ".\n";
                    body += "If you think this message is in error, please contact the administrator.\n";
                    body += "\n";
                    body += "DO NOT REPLY TO THIS MESSAGE.\n";

                    // construct an email message
                    MailMessage msg = new MailMessage();
                    msg.setSender("Mar-S Administrator");
                    msg.setRecipient(user.getEmail());
                    msg.setSubject("Account Expiration Notice");
                    msg.setBody(body);

                    MessageQueue.instance().insert(msg);

                    user.setExpirationNotified(true);
                }

                session.getTransaction().commit();
            } catch (RuntimeException ex) {
                // rollback the transaction
                session.getTransaction().rollback();
                ex.printStackTrace();
            }

            DatabaseAccess.closeSession();
        }
    }

    class MailerTask extends TimerTask
    {
        public void run()
        {
            // check if there are any pending messages
            if (MessageQueue.instance().pending() > 0) {
                System.out.println("Sending " + MessageQueue.instance().pending() + " messages.");

                try {
                    MessageQueue.instance().sendAllMessages();
                } catch (MailException ex) {
                    System.out.println("Error in mailer task: " + ex.getMessage());
                }
            }
        }
    }

    public void schedule()
    {
        // schedule the timer for purging expired users
        purgeTimer = new Timer();
        purgeTimer.schedule(new PurgeTask(), 0, 12 * 60 * 60 * 1000); // 12 hours

        // schedule the timer for the mailing task
        mailTimer = new Timer();
        mailTimer.schedule(new MailerTask(), 0, 5 * 60 * 1000); // 5 minutes
    }
}
