package eftech.workingset.beans;

import javax.validation.constraints.Size;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceGearBoxType;

public class GearBoxType implements InterfaceGearBoxType{
	private int id;
	
	@Size(min = 0, max = 45)
	private String name;
	boolean selected;
	
	public GearBoxType() {
		name=Service.EMPTY;
		selected=false;
	}

	public GearBoxType(int id, String name) {
		super();
		this.id = id;
		name=(name.length()==0?Service.EMPTY:name);
		setName(name);
		selected=false;
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
		this.name = Service.validateString(name,45);
	}
	
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
