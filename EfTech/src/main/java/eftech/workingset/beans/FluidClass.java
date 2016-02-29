package eftech.workingset.beans;

import javax.validation.constraints.Size;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceFluidClass;

public class FluidClass implements InterfaceFluidClass{
	private int id;
	
	@Size(min = 0, max = 30)
	private String name;
	
	public FluidClass() {
		super();
		name=Service.EMPTY;
	}

	public FluidClass(int id, String name) {
		super();
		this.id = id;
		name=(name.length()==0?Service.EMPTY:name);
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
		this.name = Service.validateString(name,30);
	}
	
	

}
