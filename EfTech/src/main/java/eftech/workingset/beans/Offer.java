package eftech.workingset.beans;

import java.util.Date;

import eftech.workingset.beans.intefaces.InterfaceBrakingFluid;
import eftech.workingset.beans.intefaces.InterfaceOffer;
import eftech.workingset.beans.intefaces.InterfaceOfferStatus;

public class Offer implements InterfaceOffer {
	private int id;
	private Date time;
	private User user;
	private String offer_id;
	private InterfaceBrakingFluid brakingFluid;
	private int quantity;
	private double price;
	private InterfaceOfferStatus status;

	public Offer(int id, Date time, User user, String offer_id, InterfaceBrakingFluid brekingFluid, int quantity,
			double price, InterfaceOfferStatus status) {
		super();
		this.id = id;
		this.time = time;
		this.user = user;
		this.offer_id = offer_id;
		this.brakingFluid = brekingFluid;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
	}

	public Offer() {
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
	public String getOffer_id() {
		return offer_id;
	}

	/**
	 * @param offer_id the offer_id to set
	 */
	public void setOffer_id(String offer_id) {
		this.offer_id = offer_id;
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

	/**
	 * @return the status
	 */
	public InterfaceOfferStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(InterfaceOfferStatus status) {
		this.status = status;
	}

	
	
	
}
