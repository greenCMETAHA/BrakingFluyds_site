package eftech.workingset.beans;

import eftech.workingset.beans.intefaces.InterfaceBasket;

public class Basket implements InterfaceBasket {
	private BrakingFluid brakingFluid;
	private int qauntity;
	
	public Basket() {
		super();
		brakingFluid=new BrakingFluid();
		qauntity=0;
	}

	public Basket(BrakingFluid brakingFluid, int qauntity) {
		super();
		this.brakingFluid = brakingFluid;
		this.qauntity = qauntity;
	}
	
	public Basket(BrakingFluid brakingFluid) {
		super();
		this.brakingFluid = brakingFluid;
		this.qauntity = 1;
	}	

	/**
	 * @return the brakingFluid
	 */
	public BrakingFluid getBrakingFluid() {
		return brakingFluid;
	}

	/**
	 * @param brakingFluid the brakingFluid to set
	 */
	public void setBrakingFluid(BrakingFluid brakingFluid) {
		this.brakingFluid = brakingFluid;
	}

	/**
	 * @return the qauntity
	 */
	public int getQauntity() {
		return qauntity;
	}

	/**
	 * @param qauntity the qauntity to set
	 */
	public void setQauntity(int qauntity) {
		this.qauntity = qauntity;
	}
	
	

}
