package eftech.workingset.beans;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceCountry;

public class Country implements InterfaceCountry {
	private int id;
	private String name;
	
	public Country() {
		name=Service.EMPTY;
	}

	public Country(int id, String name) {
		super();
		this.id = id;
		name=(name.length()==0?Service.EMPTY:name);
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	

}
