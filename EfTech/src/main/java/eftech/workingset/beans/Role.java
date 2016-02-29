package eftech.workingset.beans;

import javax.validation.constraints.Size;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceRole;

public class Role implements InterfaceRole{
	private int id;
	
	@Size(min = 0, max = 45)
	private String name;
	
	public Role() {
		super();
		id=0;
		name=Service.EMPTY;
	}

	public Role(int id, String name) {
		super();
		this.id = id;
		setName(name);
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

	

}
