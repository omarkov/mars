package ops;

import java.util.Hashtable;

public class MyHashtable extends Hashtable
{	
    boolean locked= false;

    public void lock() {
	locked = true;
    }

    public void unlock() {
	locked = false;
    }
}
