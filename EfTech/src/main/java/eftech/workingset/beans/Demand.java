package eftech.workingset.beans;

import java.util.Date;

import eftech.workingset.beans.intefaces.InterfaceBrakingFluid;
import eftech.workingset.beans.intefaces.InterfaceClient;
import eftech.workingset.beans.intefaces.InterfaceDemand;
import eftech.workingset.beans.intefaces.InterfaceOfferStatus;

public class Demand implements InterfaceDemand {
	private int id;
	private Date time;
	private User user;
	private String demand_id;
	private InterfaceBrakingFluid brakingFluid;
	private int quantity;
	private double price;
	private InterfaceOfferStatus status;
	private User executer;
	private InterfaceClient client;

	public Demand(int id, Date time, User user, String demand_id, InterfaceBrakingFluid brakingFluid, int quantity,	double price
			, InterfaceOfferStatus status, User executer, InterfaceClient client ) {
		super();
		this.id = id;
		this.time = time;
		this.user = user;
		this.demand_id = demand_id;
		this.brakingFluid = brakingFluid;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
		this.setExecuter(executer);
		this.setClient(client);
	}
	
	public Demand(int id, String demand_id, InterfaceOfferStatus status) {
		super();
		this.id = id;
		this.demand_id = demand_id;
		this.status = status;
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
	
	public String showDate() {
		return time.toLocaleString();
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

	public User getExecuter() {
		return executer;
	}

	public void setExecuter(User executer) {
		this.executer = executer;
	}

	public InterfaceClient getClient() {
		return client;
	}

	public void setClient(InterfaceClient client) {
		this.client = client;
	}
	
}

