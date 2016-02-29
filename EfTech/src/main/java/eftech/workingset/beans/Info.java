package eftech.workingset.beans;

import javax.validation.constraints.Size;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceInfo;

public class Info implements InterfaceInfo{
	private int id;
	
	@Size(min = 0, max = 20)
	private String name;
	
	@Size(min = 0, max = 100)
	private String value;
	
	public Info() {
		id=0;
	}
	public Info(int id, String name, String value) {
		super();
		this.id = id;
		setName(name);
		setValue(value);
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
		this.name = Service.validateString(name,20);
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = Service.validateString(value,100);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Info [id=" + id + ", name=" + name + ", value=" + value + "]";
	}
	
	
	
	

}
