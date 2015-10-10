package eftech.workingset.beans;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceFluidClass;
import eftech.workingset.beans.intefaces.InterfaceOfferStatus;

public class OfferStatus implements InterfaceOfferStatus{
	private int id;
	private String name;
	
	public OfferStatus() {
		super();
		name=Service.EMPTY;
	}

	public OfferStatus(int id, String name) {
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
		name=(name.length()==0?Service.EMPTY:name);
		this.name = name;
	}
	
	

}
