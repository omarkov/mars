/*
 * Created on 13.06.2005
 */
package net;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class ServerTest implements NetworkEventListener, ModuleListener {

	private Integer rotwert = new Integer(3);
	private List playlist = new Vector();

	public static void main(String[] args) {
		new ServerTest();
	}
	
	private NetworkFactory net;

	public void registerModule(ModuleDescription d)
	{
		System.out.println("Register Module: " + d.getName());
	}
	
	public ServerTest()
	{
		net = NetworkFactory.getInstance();
		net.addModuleListener(this);

		ModuleDescription myModule = new ModuleDescription("system", "V1.0");
		
		ObjectParameter p6 = new ObjectParameter("testData", "rw", this, TestData.class);
		ListParameter   p7 = new ListParameter("testList", 0, "rw", "generic", this, TestData.class);
		ListParameter    p = new ListParameter("playlist", 0, "rw", "generic", this, String.class);
		
		Command c    = new Command("login", new ObjectParameter(TestData.class), this);
		Parameter p1 = new StringParameter("user", null);
		Parameter p2 = new StringParameter("pwd" , null);
		Parameter p3 = new ObjectParameter("data", TestData.class);
		c.addParameter(p1);
		c.addParameter(p2);
		c.addParameter(p3);
		
		Command c2   = new Command("doSomething", new BooleanParameter(), this);
		Parameter p4 = new StringParameter("bla", null);
		Parameter p5 = new ListParameter("pwd" , null, TestData.class);
		c2.addParameter(p4);
		c2.addParameter(p5);
		
		myModule.addInterface(c2);
		myModule.addInterface(c);
		myModule.addInterface(p);
		myModule.addInterface(p6);
		myModule.addInterface(p7);

		net.setModuleDescription(myModule);
		
		System.out.println(myModule.marshall());
		// Bonjour
		// net.startController();
		System.out.println("Starting...");
		net.startController(1234);
	}
	
	public TestData getTestData()
	{
		return new TestData();
	}
	
	public Boolean setTestData(TestData data)
	{
		System.out.println("setTestData:" + data.getBla() + " " + data.getX() +" " + data.getY() +" " + data.isJo());
		return Boolean.TRUE;
	}
	
	public List getPlaylist()
	{
		return playlist;
	}
	
	public List getTestList()
	{
		ArrayList a = new ArrayList();
/*		a.add(new TestData());
		a.add(new TestData());
		a.add(new TestData());*/
		return a;
	}
	
	public Boolean setTestList(List list)
	{
		System.out.println("setTestList: ");
		for(int i=0; i < list.size(); i++)
		{
			TestData data = (TestData)list.get(i);
			System.out.println("Data:" + data.getBla() + " " + data.getX() +" " + data.getY() +" " + data.isJo());
		}
		return Boolean.TRUE;
	}
	
	public Boolean doSomething(String bla, List list)
	{
		System.out.println("doSomething: " + bla);
		for(int i=0; i < list.size(); i++)
		{
			TestData data = (TestData)list.get(i);
			System.out.println("Data:" + data.getBla() + " " + data.getX() +" " + data.getY() +" " + data.isJo());
		}
		return Boolean.FALSE;
	}
	
	public Boolean setPlaylist(List list)
	{
		System.out.println("Playlist:");
		for(int i=0; i < list.size(); i++)
		{
			System.out.println(list.get(i));
		}
		
		playlist = list;
		
		return Boolean.TRUE;
	}
	
	public TestData login(String user, String pwd, TestData data) throws Exception
	{
		System.out.println("Login:" + user + "/" + pwd);
		System.out.println("Data:" + data.getBla() + " " + data.getX() +" " + data.getY() +" " + data.isJo());
		data.setX(data.getX()+1);
		data.setY(data.getY()-1);
		if(data.getX() == 11)
			throw new Exception("bla");
		return data;
	}
}
