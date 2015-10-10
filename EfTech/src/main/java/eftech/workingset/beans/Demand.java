package eftech.workingset.beans;

import java.util.Date;

import eftech.workingset.beans.intefaces.InterfaceBrakingFluid;
import eftech.workingset.beans.intefaces.InterfaceDemand;

public class Demand implements InterfaceDemand {
	private int id;
	private Date time;
	private User user;
	private String demand_id;
	private InterfaceBrakingFluid brakingFluid;
	private int quantity;
	private double price;

	public Demand(int id, Date time, User user, String demand_id, InterfaceBrakingFluid brakingFluid, int quantity,	double price) {
		super();
		this.id = id;
		this.time = time;
		this.user = user;
		this.demand_id = demand_id;
		this.brakingFluid = brakingFluid;
		this.quantity = quantity;
		this.price = price;
	}

	public Demand() {
		super();
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
	 * @return the time
	 */
	public Date getTime() {
		return time;
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
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the offer_id
	 */
	public String getDemand_id() {
		return demand_id;
	}

	/**
	 * @param offer_id the offer_id to set
	 */
	public void setDemand_id(String demand_id) {
		this.demand_id = demand_id;
	}

	/**
	 * @return the brekingFluid
	 */
	public InterfaceBrakingFluid getBrakingFluid() {
		return brakingFluid;
	}

	/**
	 * @param brekingFluid the brekingFluid to set
	 */
	public void setBrakingFluid(InterfaceBrakingFluid brakingFluid) {
		this.brakingFluid = brakingFluid;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
}

