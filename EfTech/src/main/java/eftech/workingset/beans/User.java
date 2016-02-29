package eftech.workingset.beans;

import javax.validation.constraints.Size;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceRole;
import eftech.workingset.beans.intefaces.InterfaceUser;

public class User implements InterfaceUser{
	private int id;
	
	@Size(min = 0, max = 45)
	private String name;
	
	@Size(min = 0, max = 45)
	private String email;
	
	@Size(min = 0, max = 45)
	private String login;
	
	@Size(min = 0, max = 45)
	private String password;
	private InterfaceRole role;
	
	public User(){
		id=0;
		name="";
		email="";
		login="";
		password="";
		role = new Role();
	}
	
	public User(int id, String name, String email, String login, String password, Role role) {
		super();
		this.id = id;
		setName(name);
		setEmail(email);
		setLogin(login);
		setPassword(password);
		this.role = (InterfaceRole)role;
	}
	
	public User(int id, String name, String email, String login) {
		super();
		this.id = id;
		setName(name);
		setEmail(email);
		setLogin(login);
	}	
	
	public User(String login, String password) {
		super();
		setLogin(login);
		setPassword(password);
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
		this.name = Service.validateString(name,45);
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
		this.email = Service.validateString(email,45);
	}
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = Service.validateString(login,45);
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = Service.validateString(password,45);
	}
	
	/**
	 * @return the role
	 */
	public InterfaceRole getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(InterfaceRole role) {
		this.role = role;
	}
	
	public boolean isEmpty(){
		boolean result=false;
		
		result=(name.length()>0?false:true);
		
		return result; 
	}
	
	public boolean canAddNewElement(){
		boolean result=false;
		
		Role currentRole=(Role)role;
		if (currentRole.getId()>0){
			if ("admin".equals(currentRole.getName()) || ("saler".equals(currentRole.getName()) || ("product".equals(currentRole.getName())))){
				result=true;
			}
		}
		
		return result; 
	}
	
	public boolean canChangePrice(){
		boolean result=false;
		
		Role currentRole=(Role)role;
		if (currentRole.getId()>0){
			if (("ROLE_OFFERPRISE".equals(currentRole.getName()))   || ("ROLE_DESTR".equals(currentRole.getName()))
					|| ("ROLE_PRICE".equals(currentRole.getName())) || ("ROLE_ADMIN".equals(currentRole.getName())) 
						|| ("admin".equals(currentRole.getName()))  || ("price".equals(currentRole.getName()))){
				result=true;
			}
		}
		
		return result; 
	}	
	
	public boolean isSalesManager(){
		boolean result=false;
		
		Role currentRole=(Role)role;
		if (currentRole.getId()>0){
			if (("admin".equals(currentRole.getName())) || ("distribution".equals(currentRole.getName()))){
				result=true;
			}
		}
		
		return result; 
	}
	
	public boolean isWithoutLogin(){
		boolean result=false;
		
		Role currentRole=(Role)role;
		if (currentRole.getId()>0){
			if (("admin".equals(currentRole.getName())) || (currentRole.getName().length()==0) || ("offer".equals(currentRole.getName()))){
				result=true;
			}
		}
		
		return result; 
	}	
	
	public boolean isOfferPrice(){
		boolean result=false;
		
		Role currentRole=(Role)role;
		if (currentRole.getId()>0){
			if (("admin".equals(currentRole.getName())) || ("offer+price".equals(currentRole.getName()))){
				result=true;
			}
		}
		
		return result; 
	}		
	
	public boolean showSelection(){
		boolean result=false;
		
		Role currentRole=(Role)role;
		if (currentRole.getId()>0){
			result=true;
		}
		
		return result; 
	}	
	
	public boolean createBussinessOffer(){
		boolean result=false;
		
		Role currentRole=(Role)role;
		if (currentRole.getId()>0){
			if (("admin".equals(currentRole.getName())) || ("saler".equals(currentRole.getName()))){
				result=true;
			}
		}
		
		return result; 
	}	
	
	public boolean createDemand(){
		boolean result=false;
		
		Role currentRole=(Role)role;
		if (currentRole.getId()>0){
			if (("admin".equals(currentRole.getName())) || ("dealer".equals(currentRole.getName()))){
				result=true;
			}
		}
		
		return result; 
	}	
	
	public boolean loadData(){
		boolean result=false;
		
		Role currentRole=(Role)role;
		if (currentRole.getId()>0){
			if ("admin".equals(currentRole.getName())){
				result=true;
			}
		}
		
		return result; 
	}	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", login=" + login + "]";
	}
}
