package eftech.workingset.beans;

import java.util.Date;

import eftech.workingset.beans.intefaces.InterfaceClient;
import eftech.workingset.beans.intefaces.InterfaceManufacturer;
import eftech.workingset.beans.intefaces.InterfacePay;

public class Pay implements InterfacePay {
	private int id;
	private Date time;
	private User user;
	private String numDoc;
	private String demand_id;
	private InterfaceManufacturer manufacturer;
	private boolean storno;
	private double summ;
	private InterfaceClient client;
	
	public Pay(){
		
	}
	
	public Pay(int id, Date time, User user, String numDoc, String demand_id, InterfaceManufacturer manufacturer,
			boolean storno, double summ, InterfaceClient client) {
		super();
		this.id = id;
		this.time = time;
		this.user = user;
		this.numDoc = numDoc;
		this.demand_id = demand_id;
		this.manufacturer = manufacturer;
		this.storno = storno;
		this.summ = summ;
		this.client = client;
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
	 * @return the numDoc
	 */
	public String getNumDoc() {
		return numDoc;
	}

	/**
	 * @param numDoc the numDoc to set
	 */
	public void setNumDoc(String numDoc) {
		this.numDoc = numDoc;
	}

	
	/**
	 * @return the demand_id
	 */
	public String getDemand_id() {
		return demand_id;
	}

	/**
	 * @param demand_id the demand_id to set
	 */
	public void setDemand_id(String demand_id) {
		this.demand_id = demand_id;
	}

	/**
	 * @return the manufacturer
	 */
	public InterfaceManufacturer getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(InterfaceManufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the storno
	 */
	public boolean isStorno() {
		return storno;
	}

	/**
	 * @param storno the storno to set
	 */
	public void setStorno(boolean storno) {
		this.storno = storno;
	}

	/**
	 * @return the summ
	 */
	public double getSumm() {
		return summ;
	}

	/**
	 * @param summ the summ to set
	 */
	public void setSumm(double summ) {
		this.summ = summ;
	}

	public InterfaceClient getClient() {
		return client;
	}

	public void setClient(InterfaceClient client) {
		this.client = client;
	}
}
