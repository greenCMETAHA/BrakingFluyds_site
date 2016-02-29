package eftech.workingset.beans;

import javax.validation.constraints.Size;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceClient;
import eftech.workingset.beans.intefaces.InterfaceCountry;

public class Client implements InterfaceClient {
	private int id;
	@Size(min = 0, max = 100)
	private String name;
	@Size(min = 0, max = 45)
	private String email;
	@Size(min = 0, max = 45)
	private String address;
	private InterfaceCountry country;
	
	public Client() {
		super();
		name="";
		id=Service.ID_EMPTY_CLIENT;
	}
	
	public Client(int id, String name, String email, String address, InterfaceCountry country) {
		super();
		this.id = id;
		setName(name);
		setEmail(email);
		setAddress(address);
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
		this.name = Service.validateString(name,100);
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = Service.validateString(email,45);
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = Service.validateString(address,45);
	}
	/**
	 * @return the country
	 */
	public InterfaceCountry getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(InterfaceCountry country) {
		this.country = country;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + "]";
	}
	
	

}
