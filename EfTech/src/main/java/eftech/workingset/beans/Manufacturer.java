package eftech.workingset.beans;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceCountry;
import eftech.workingset.beans.intefaces.InterfaceManufacturer;

public class Manufacturer implements InterfaceManufacturer{
	private int id;
	private String name;
	private String fullName;
	private InterfaceCountry country;
	
	public Manufacturer() {
		super();
		name=Service.EMPTY;
		fullName=name;
		country=new Country();
	}

	public Manufacturer(int id, String name, InterfaceCountry country) {
		super();
		this.id = id;
		name=(name.length()==0?Service.EMPTY:name);
		this.name = name;
		this.fullName=name;
		this.country = country;
	}
	
	public Manufacturer(int id, String name, String fullName, InterfaceCountry country) {
		super();
		this.id = id;
		name=(name.length()==0?Service.EMPTY:name);
		this.name = name;
		this.fullName=fullName;
		this.country = country;
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
		name=(name.length()==0?Service.EMPTY:name);
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the country
	 */
	public Country getCountry() {
		return (Country)country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(InterfaceCountry country) {
		this.country = country;
	}
	
	
	
}
