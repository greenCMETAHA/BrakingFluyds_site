package eftech.workingset.beans;

import java.util.Date;

import eftech.workingset.beans.intefaces.InterfaceBrakingFluid;
import eftech.workingset.beans.intefaces.InterfaceOfferStatus;

public class DocRow {
	private String numDoc;
	private int quantity;
	private Date time;
	private double summ;
	private InterfaceBrakingFluid brakingFluid;
	private InterfaceOfferStatus status;
	private User executer;
	private boolean paid;  //оплачено
	
	public DocRow() {
		super();
	}

	public DocRow(String numDoc, Date time, InterfaceBrakingFluid interfaceBrakingFluid, int quantity, double summ
				, InterfaceOfferStatus status, User executer, boolean paid) {
		super();
		this.numDoc = numDoc;
		this.quantity = quantity;
		this.summ = summ;
		this.status = status;
		this.setTime(time);
		this.setBrakingFluid(interfaceBrakingFluid);
		this.setExecuter(executer);
		this.setPaid(paid);
		
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

	public InterfaceBrakingFluid getBrakingFluid() {
		return brakingFluid;
	}

	public void setBrakingFluid(InterfaceBrakingFluid brakingFluid) {
		this.brakingFluid = brakingFluid;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	public String showDate() {
		return time.toLocaleString();
	}

	public User getExecuter() {
		return executer;
	}

	public void setExecuter(User executer) {
		this.executer = executer;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}	

}
