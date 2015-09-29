package eftech.workingset.beans;

import eftech.workingset.beans.intefaces.InterfaceBrakingFluid;
import eftech.workingset.beans.intefaces.InterfaceUser;
import eftech.workingset.beans.intefaces.InterfaceWishlist;

public class Wishlist implements InterfaceWishlist{
	private int id;
	private InterfaceUser user;
	private InterfaceBrakingFluid brakingFluid;
	
	public Wishlist(){
		id=0;
		user=new User();
		brakingFluid=new BrakingFluid();
	}

	public Wishlist(int id, InterfaceUser user, InterfaceBrakingFluid brakingFluid) {
		super();
		this.id = id;
		this.user = user;
		this.brakingFluid = brakingFluid;
	}
	
	public Wishlist(int id, int user, int brakingFluid) {
		super();
		this.id = id;

		User currentUser=new User();
		currentUser.setId(user);
		this.user = currentUser;

		BrakingFluid currentBrakingFluid=new BrakingFluid();
		currentBrakingFluid.setId(brakingFluid);
		this.brakingFluid = currentBrakingFluid;
	}

	public Wishlist(int user, int brakingFluid) {
		super();
		this.id = 0;

		User currentUser=new User();
		currentUser.setId(user);
		this.user = currentUser;

		BrakingFluid currentBrakingFluid=new BrakingFluid();
		currentBrakingFluid.setId(brakingFluid);
		this.brakingFluid = currentBrakingFluid;
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
	public InterfaceBrakingFluid getBrakingFluid() {
		return brakingFluid;
	}

	/**
	 * @param brakingFluid the brakingFluid to set
	 */
	public void setBrakingFluid(InterfaceBrakingFluid brakingFluid) {
		this.brakingFluid = brakingFluid;
	}	
}
