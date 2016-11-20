package server.domain;

import java.util.HashSet;
import java.util.Set;




/**
 * @author Rajkumar
 * 
 * Change History 
 * Date 		Author 		Change
 * 04/08/05		RDE			attribute korrigiert			
 * 
 * 			
 */
public class SmartRoomComponent {

    private Long id;
    private String name;
    public Set componentAttributes = new HashSet();
    
    public Long getId(){
    	return this.id;
    }
    
    public void setId(Long id){
    	this.id = id;
    }
    
    public String getName(){
    	return this.name;
    }
    
    public void setName(String name){
    	this.name = name;
    }
    
    public Set getComponentAttributes(){
    	return this.componentAttributes;
    }
    
    public void setComponentAttributes(Set componentAttributes){
    	this.componentAttributes = componentAttributes;
    }
    
    public void addComponentAttribute(ComponentAttribute cp){
    	this.componentAttributes.add(cp);
    }
    
    public void removeComponentAttribute(ComponentAttribute cp){
    	this.componentAttributes.remove(cp);
    }

 }
