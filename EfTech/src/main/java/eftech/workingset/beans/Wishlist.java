package eftech.workingset.beans;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceUser;
import eftech.workingset.beans.intefaces.InterfaceWishlist;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public class Wishlist implements InterfaceWishlist{
	private int id;
	private InterfaceUser user;
	private InterfaceGood good;
	
	public Wishlist(){
		id=0;
		user=new User();
	}

	public Wishlist(int id, InterfaceUser user, InterfaceGood good) {
		super();
		this.id = id;
		this.user = user;
		this.good = good;
	}
	
	public Wishlist(int id, int user, int goodId, String goodPrefix) {
		super();
		this.id = id;

		User currentUser=new User();
		currentUser.setId(user);
		this.user = currentUser;

		InterfaceGood currentGood=null;
		if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
			currentGood=new BrakingFluid();
		}else if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
			currentGood=new MotorOil();
		}else if (Service.GEARBOX_OIL_PREFIX.equals(goodPrefix)){
			currentGood=new GearBoxOil();
		}
		currentGood.setId(goodId);
		this.good = currentGood;
	}

	public Wishlist(int user, int goodId, String goodPrefix) {
		super();
		this.id = 0;

		User currentUser=new User();
		currentUser.setId(user);
		this.user = currentUser;

		InterfaceGood currentGood=null;
		if (Service.BRAKING_FLUID_PREFIX.equals(goodPrefix)){
			currentGood=new BrakingFluid();
		}else if (Service.MOTOR_OIL_PREFIX.equals(goodPrefix)){
			currentGood=new MotorOil();
		}else if (Service.GEARBOX_OIL_PREFIX.equals(goodPrefix)){
			currentGood=new GearBoxOil();
		}
		currentGood.setId(goodId);
		this.good = currentGood;
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
	 * @return the user
	 */
	public InterfaceUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(InterfaceUser user) {
		this.user = user;
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
}
