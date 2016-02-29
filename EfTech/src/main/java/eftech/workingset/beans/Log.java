package eftech.workingset.beans;

import java.util.Date;

import javax.validation.constraints.Size;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceLog;

public class Log implements InterfaceLog{
	private int id;
	private User user;
	private Date time;
	private Object object;
	
	@Size(min = 0, max = 249)
	private String info;
	
	public Log(int id, User user, Date time, Object object, String info) {
		super();
		this.id = id;
		this.user = user;
		this.time = time;
		this.object = object;
		setInfo(info);
	}
	
	public Log() {
		super();
		this.id = 0;
		this.user = new User();
		this.time = new Date();
		this.object = new String("<не определено>");
		this.info = "";
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
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}
	
	public String getObjectName(){
		String result="<не определен>";
		
		if (object!=null){
			Class c=object.getClass();
			result=c.getSimpleName();
		}
		
		return result;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = Service.validateString(info,250);
	}	
	
}
