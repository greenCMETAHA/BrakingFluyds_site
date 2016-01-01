package eftech.workingset.beans;

import eftech.workingset.beans.intefaces.InterfaceBasket;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public class Basket implements InterfaceBasket {
	private InterfaceGood good;
	private int qauntity;
	
	public Basket() {
		super();
		good=null;
		qauntity=0;
	}

	public Basket(InterfaceGood good, int qauntity) {
		super();
		this.good = good;
		this.qauntity = qauntity;
	}
	
	public Basket(InterfaceGood good) {
		super();
		this.good = good;
		this.qauntity = 1;
	}	
	
	/**
	 * @return the brakingFluid
	 */
	public InterfaceGood getGood() {
		return good;
	}

	/**
	 * @param brakingFluid the brakingFluid to set
	 */
	public void setGood(InterfaceGood good) {
		this.good = good;
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
