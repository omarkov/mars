/*
 * Created on 13.06.2005
 */
package net;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public class Lichtanlage implements NetworkEventListener {

	private Integer rotwert = new Integer(3);
	private String programm = "easy";
	private Boolean status  = new Boolean(true);
	private List list  = new ArrayList();

	public static void main(String[] args) {
		new Lichtanlage();

		while(true)
		{
			try
			{
				Thread.sleep(1000);
			}catch(Exception e)
			{}
		}
	}
	
	private NetworkFactory net;
	
	public Lichtanlage()
	{
		net = NetworkFactory.getInstance();

		ModuleDescription myModule = new ModuleDescription("lichtanlageV3", "V1.0");
		myModule.setWebModule(true);

		NumericParameter n = new NumericParameter("rotwert", new Integer(0), "rw", 0, 100, 10, this);
		StringParameter p = new StringParameter("programm", "easy", "rw", this);
		BooleanParameter p2 = new BooleanParameter("status", Boolean.FALSE, "ro", this);
		ListParameter   p3 = new ListParameter("soneliste", new Integer(0), "rw", "audio", this, String.class);

		myModule.addInterface(p);
		myModule.addInterface(p2);
		myModule.addInterface(p3);
		myModule.addInterface(n);

		net.setModuleDescription(myModule);
		net.startModule("localhost", 1234);
		// Bonjour 
		// net.startModule(); 
	}

	public boolean setSoneliste(List list)
	{
		this.list = list;
		return true;
	}
	public List getSoneliste()
	{
		return this.list;
	}

	public boolean setStatus(Boolean status)
	{
		this.status = status;
		return true;
	}
	public Boolean getStatus()
	{
		return this.status;
	}


	public String getProgramm()
	{
		return programm;
	}
	
	public boolean setProgramm(String programm)
	{
		this.programm = programm;
		System.out.println("Programm: " + programm);
		return true;
	}

	public Object getRotwert()
	{
		return rotwert;
	}
	
	public boolean setRotwert(Integer val)
	{
		rotwert = val;
		return true;
	}
}
