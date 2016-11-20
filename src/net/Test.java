/*
 * Created on 13.06.2005
 */
package net;

import java.text.DateFormat;
import java.util.List;
import java.util.Vector;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class Test implements NetworkEventListener {

	public static void main(String[] args) {
		new Test();
	}
	
	private NetworkFactory net;
	
	public Test()
	{
		net = NetworkFactory.getInstance();

		ModuleDescription myModule = new ModuleDescription("test", "V1.0");
		net.setModuleDescription(myModule);
		// Bonjour
		// net.startModule();
		net.startModule("localhost", 1234);

		System.out.println("WebModuleCount: " + net.getWebModulesCount());
		
		for(int i=0; i < net.getWebModulesCount(); i++)
		{
			ModuleDescription m = net.getWebModuleDescription(i);
			System.out.println(m.marshall());
			
			for(int j=0; j < m.getInterfaceCount(); j++)
			{
				NetworkInterface ni = m.getInterface(j);
				/*
				 * Generic Parameter (Web)
				 * 
				 * if(ni instanceof Parameter)
				 * {
				 * 		Parameter p = (Parameter) ni;
				 * 		if(p.getType() == StringParameter.TYPE)
				 * 		{
				 * 			String value = (String)net.get(p);
				 * 			p.setValue("bla");
				 * 			net.set(p);
				 * 			....
				 * 		}
				 * }else if(ni instanceof Command)
				 * {
				 * 		Command c = (Command) c;
				 * 		for(int k=0; k < c.getParameterCount(); k++)
				 * 		{
				 * 			Parameter p = c.getParameter();
				 * 			p.setValue("bla");
				 * 		}
				 *		String ret = net.call(c);
				 *		...
				 * }
				 * ...
				 */
			}
		}
		
        try
        {
		    System.out.println("lichtspiel: " + net.call("lichtanlage", "lichtspiel", new Object[]{"test"}));
		    System.out.println("login:");
		    
		    TestData data = (TestData)net.call("system", "login", new Object[]{"dominik","pwd",new TestData()});
		    System.out.println("Data: " + data.getBla() + " " + data.getX() + " " 
		    			+ data.getY() + " " + data.isJo() + " " 
		    			+ DateFormat.getDateInstance().format(data.getDate()));
        }catch(NetworkException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
	/*

		TestData data = (TestData)net.get("system", "testData");
		System.out.println("getTestData:" + data.getBla() + " " + data.getX() +" " + data.getY() +" " + data.isJo());
		net.set("system", "testData", data);
		
		List l = (List)net.get("system", "testList");
		net.set("system", "testList", l);

		l = (List)net.get("system", "playlist");
		System.out.println("Current Playlist:");
		
		for(int i=0; i < l.size(); i++)
		{
			System.out.println(l.get(i));
		}

		Vector v = new Vector();
		v.addElement("Song " + Math.round(Math.random() * 100));
		v.addElement("Song " + Math.round(Math.random() * 100));
		v.addElement("Song " + Math.round(Math.random() * 100));
		v.addElement("Song " + Math.round(Math.random() * 100));

		net.set("system", "playlist", v);

		l = (List)net.get("system", "playlist");
		System.out.println("Current Playlist:");
		
		for(int i=0; i < l.size(); i++)
		{
			System.out.println(l.get(i));
		}
		
		v.clear();
		v.addElement(new TestData());
		v.addElement(new TestData());
		try {
			System.out.println(net.call("system", "doSomething", new Object[]{"test", v}));
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		net.stopModule();
	*/
	}
}
