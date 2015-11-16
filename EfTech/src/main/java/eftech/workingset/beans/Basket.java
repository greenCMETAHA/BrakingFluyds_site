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

	public Basket(BrakingFluid brakingFluid, int qauntity) {
		super();
		this.good = (InterfaceGood) brakingFluid;
		this.qauntity = qauntity;
	}
	
	public Basket(MotorOil motorOil, int qauntity) {
		super();
		this.good = motorOil;
		this.qauntity = qauntity;
	}	
	
	public Basket(BrakingFluid brakingFluid) {
		super();
		this.good = (InterfaceGood) brakingFluid;
		this.qauntity = 1;
	}	
	
	public Basket(MotorOil motorOil) {
		super();
		this.good = motorOil;
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
