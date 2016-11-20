/*
 * Created on 13.06.2005
 */
package net;

/**
 * @author Dominik Rössler <dominik@freshx.de>
 */
public interface NetworkInterface extends Cloneable{
	public void unmarshall(String mstring);
	public String marshall();
	public String getName();
	public void setName(String name);
	public void setModuleDescription(ModuleDescription module);
	public ModuleDescription getModuleDescription();
	public NetworkEventListener getListener();
	public void setListener(NetworkEventListener listener);
}
