package ops;

public class Tag {
	
	private int status = 0;
	private int tagID;
	//private int found = 0;
	private MyDate date;
	
	public Tag(int tID){
		
		this.tagID = tID;
		date = new MyDate();
		date.increaseLogOnTime();
		
	}
	
	
	/*public int getFound(){
		return found;
	}*/
	
	public int getTagID(){
		return tagID;
	}
	
	public MyDate getDate(){
		return date;
	}
	
	public void setDate(MyDate sDate){
		
		date = sDate;
	}
	
	public void setStatus(int i){
		status = i;
		
	}
	
	public int getStatus(){
	    return status;
	}
	
	
}
