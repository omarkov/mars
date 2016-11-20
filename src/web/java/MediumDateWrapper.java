package web.java;

import server.domain.GroupMars;
import java.util.*;
import java.text.DateFormat;


public class MediumDateWrapper implements org.displaytag.decorator.ColumnDecorator
{
    DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH); 
            
    public final String decorate(Object columnValue) {        
            Date expirationDate = (Date) columnValue;
            if (expirationDate != null)
                return this.df.format(expirationDate);
            else
                return "No expiration.";
    }       
        
}