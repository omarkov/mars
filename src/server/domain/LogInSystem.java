package server.domain;

import java.util.HashSet;
import java.util.Set;

public class LogInSystem {
	
	private Long id;
	private String name;
	
	public LogInSystem() {
	}
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}	
	
	/**
	 * @param name
	 */
	public LogInSystem(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the identifications.
	 */
/*
	public Set getIdentifications() {
		return identifications;
	}
	
	public void setIdentifications(Set identifications){
		this.identifications = identifications;
	}

	public void addIdentification(Identification ident) {
		this.getIdentifications().add(ident);
	}
	
	public void removeIdentification(Identification ident) {
		this.getIdentifications().remove(ident);
	}
*/
}
