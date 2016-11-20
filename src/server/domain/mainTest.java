package server.domain;

import server.DatabaseAccess;
import server.controller.exceptions.ElementUpdateException;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

// logging
import org.apache.log4j.Logger;

public class mainTest {

// Testklasse zum einfügen von Testdaten
	public static void main(String[] args) throws ElementUpdateException {
		try {
			DatabaseAccess.init();
		} catch(Throwable ex) {
		}
		 User user1 = new User();
		 user1.setFirstName("daniel");
		 save(user1);
		
	}
	
	
	public static void save(Object o) throws ElementUpdateException{
		try {
		    // aktuelle session holen und transaktion starten
		    Session s = DatabaseAccess.currentSession();
		    Transaction tx = s.beginTransaction();
	
		    // objekt speichern
		    s.saveOrUpdate(o);
		    System.out.println("Object saved:" + o);
		    // transaktion ausführen und session wieder schliessen
		    tx.commit();
		}catch (HibernateException e) {
			throw new ElementUpdateException(e.getMessage());
		}
	}

}
