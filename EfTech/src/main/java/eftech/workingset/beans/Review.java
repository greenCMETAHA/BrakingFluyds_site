package eftech.workingset.beans;

import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig.Interface;

import eftech.workingset.beans.intefaces.InterfaceBrakingFluid;
import eftech.workingset.beans.intefaces.InterfaceReview;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public class Review implements InterfaceReview {
	private int id;
	private String name;
	private String email;
	private double judgement;
	private String review;
	private InterfaceGood good;
	
	public Review() {
		id=0;
		good=null;
	}

	public Review(int id, String name, String email, double judgement, String review, InterfaceGood good) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.judgement = judgement;
		this.review = review;
		this.good=good;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the judgement
	 */
	public double getJudgement() {
		return judgement;
	}

	/**
	 * @param judgement the judgement to set
	 */
	public void setJudgement(double judgement) {
		this.judgement = judgement;
	}

	/**
	 * @return the review
	 */
	public String getReview() {
		return review;
	}

	/**
	 * @param review the review to set
	 */
	public void setReview(String review) {
		this.review = review;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Review [id=" + id + ", name=" + name + ", email=" + email + ", judgement=" + judgement + ", review="
				+ review + "]";
	}	
}
