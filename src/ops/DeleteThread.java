package ops;

import java.util.Enumeration;
import java.util.*;
import net.*;

public class DeleteThread extends Thread implements Runnable
{	
    Hashtable ht;

    public DeleteThread (Hashtable ht)
    {
	this.ht = ht;
	start();
    }

    public void run()
    {
	try {
	    while(true) {
		Thread.sleep(5000);
		check();
	    }
	} catch (InterruptedException ie) {
	}
    }


    public void check() throws InterruptedException
    {
	

	
	MyDate actualTime = new MyDate();
		
	Enumeration e = ht.keys();
	while(e.hasMoreElements()) {
		
	    Integer i = (Integer) e.nextElement();
	    Tag htTag = (Tag) ht.get(i);
	    MyDate tagTime = htTag.getDate();

	   
	    if (actualTime.after(tagTime)) {
	    	System.out.println(actualTime.getTime()+" and "+ tagTime.getTime());
		ht.remove(i);
		System.out.println("Tag removed");

		// call network
		try {
		    NetworkFactory.getInstance().call("system", "exitRoom", new Object[]{"OPS", htTag.getTagID()});
		} catch (NetworkException ex) {
		 
		}
	    }
	}
	
    }
}
