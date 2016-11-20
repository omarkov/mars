package ops;

import java.util.*;

public class MyDate extends GregorianCalendar
{
   
    final int LOGONINCREASE = 30;//LoginTimes.logoffTime;

 

    public void increaseLogOnTime() {
	add(SECOND, LOGONINCREASE);
    }
}
