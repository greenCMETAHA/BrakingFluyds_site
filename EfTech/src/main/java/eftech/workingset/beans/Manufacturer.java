package eftech.workingset.beans;

import javax.validation.constraints.Size;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceCountry;
import eftech.workingset.beans.intefaces.InterfaceManufacturer;

public class Manufacturer implements InterfaceManufacturer{
	private int id;
	
	@Size(min = 0, max = 45)
	private String name;
	
	@Size(min = 0, max = 150)
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
		setName(name);
		setFullName(name);
		this.country = country;
	}
	
	public Manufacturer(int id, String name, String fullName, InterfaceCountry country) {
		super();
		this.id = id;
		name=(name.length()==0?Service.EMPTY:name);
		setName(fullName);
		setFullName(fullName);
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
		this.name = Service.validateString(name,45);
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = Service.validateString(fullName,150);
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
