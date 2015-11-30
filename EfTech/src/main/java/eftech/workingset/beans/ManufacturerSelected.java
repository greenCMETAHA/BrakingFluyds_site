package eftech.workingset.beans;

public class ManufacturerSelected extends Manufacturer{
	boolean selected;

	public ManufacturerSelected() {
		super();
		this.selected = false;
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
