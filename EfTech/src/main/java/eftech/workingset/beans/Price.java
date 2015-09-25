package eftech.workingset.beans;

import java.util.GregorianCalendar;

import eftech.workingset.beans.intefaces.InterfacePrice;

public class Price implements InterfacePrice{
	private int id;
	private GregorianCalendar date;
	private double price;
	
	public Price(){
		
	}
	
	public Price(int id, GregorianCalendar date, double price) {
		super();
		this.id = id;
		this.date = date;
		this.price = price;
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
	 * @return the date
	 */
	public GregorianCalendar getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(GregorianCalendar date) {
		this.date = date;
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
