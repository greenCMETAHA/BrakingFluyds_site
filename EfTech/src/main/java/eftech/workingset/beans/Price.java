package eftech.workingset.beans;

import java.util.Date;
import eftech.workingset.beans.intefaces.InterfacePrice;
import eftech.workingset.beans.intefaces.InterfaceUser;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public class Price implements InterfacePrice{
	private int id;
	private Date time;
	private double price;
	private InterfaceGood good;
	private InterfaceUser user;
	
	public Price(){
		
	}
	
	public Price(int id, Date date, double price, InterfaceGood good, User user) {
		super();
		this.id = id;
		this.time = date;
		this.price = price;
		this.user = user;
		this.good = good;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	public String showDate() {
		return time.toLocaleString();
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
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
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
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
