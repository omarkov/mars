package ops;

import net.*;
import java.util.*;
public class LoggedTags {

	Hashtable ht;
	
	public LoggedTags(Hashtable ht){
		this.ht = ht;
		
	}

	public void insertTag(int tagID){
	       
		Tag RecTag = new Tag(tagID);
		Integer tagIDObject = new Integer(tagID);
		//wird das erste mal erkannt
		if(!ht.containsKey(tagIDObject)){
			System.out.println("logging in...2 ");
			MyDate latestLoginTime = new MyDate();
			latestLoginTime.increaseLogOnTime();
			RecTag.setDate(latestLoginTime);
			ht.put(tagIDObject,RecTag);
			
			
		}
		else{
			Tag htTag = (Tag) ht.get(tagIDObject);
			MyDate latestLoginTime = new MyDate();
			latestLoginTime.increaseLogOnTime();
			htTag.setDate(latestLoginTime);
			if(htTag.getStatus()==0){
			    htTag.setStatus(1);
			        // call network
				try {
				    NetworkFactory.getInstance().call("system", "enterRoom", new Object[]{"OPS", RecTag.getTagID()});
				} catch (NetworkException ex) {
				   
				}}

			
		}
		
	}
}
