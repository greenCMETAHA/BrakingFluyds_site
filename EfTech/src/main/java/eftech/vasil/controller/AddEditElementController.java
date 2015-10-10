package eftech.vasil.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.itextpdf.text.DocumentException;

import eftech.workingset.DAO.templates.BrakingFluidTemplate;
import eftech.workingset.DAO.templates.ClientTemplate;
import eftech.workingset.DAO.templates.CountryTemplate;
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.InfoTemplate;
import eftech.workingset.DAO.templates.LogTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.ReviewTemplate;
import eftech.workingset.DAO.templates.RoleTemplate;
import eftech.workingset.DAO.templates.UserTemplate;
import eftech.workingset.DAO.templates.WishlistTemplate;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.Role;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;

@Controller
@SessionAttributes({"user", "basket", "wishlist", "compare", "manufacturersFilter", "fluidClassFilter", "elementsInList"
	, "currentPriceFilter" , "currentBoilingTemperatureDryFilter" , "currentBoilingTemperatureWetFilter" , "currentValueFilter" 
	, "currentViscosity40Filter" , "currentViscosity100Filter", "currentJudgementFilter"})

public class AddEditElementController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	BrakingFluidTemplate brakingFluidDAO;

	@Autowired
	UserTemplate userDAO;
	
	@Autowired
	RoleTemplate roleDAO;
	
	
	@Autowired
	ManufacturerTemplate manufacturerDAO;

	@Autowired
	FluidClassTemplate fluidClassDAO;
	
	@Autowired
	ClientTemplate clientDAO;

	@Autowired
	CountryTemplate countryDAO;

	@Autowired
	InfoTemplate infoDAO;

	@Autowired
	ReviewTemplate reviewDAO;

	@Autowired
	WishlistTemplate wishlistDAO;	
	
	@Autowired
	LogTemplate logDAO;
	
	@RequestMapping(value = "/ShowList", method = {RequestMethod.GET,RequestMethod.POST})
	public String showList(@ModelAttribute User user
			,@RequestParam(value="id", defaultValue="0", required=false) int id
			,@RequestParam(value="login", defaultValue="", required=false) String login
			,@RequestParam(value="task", defaultValue="", required=false) String task
			,@RequestParam(value="page", defaultValue="1", required=false) int currentPage
			,@RequestParam(value="variant", defaultValue="", required=false ) String variant
			,HttpServletRequest request
			, Locale locale, Model model) {
		
		try {
			task=new String(task.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String page="AddEdit";
		
		if (task.isEmpty()){
			page=Service.createAdminEdit(model, variant, currentPage, manufacturerDAO, fluidClassDAO, countryDAO, clientDAO, userDAO, logDAO, new LinkedList<String>());
			
		}else if (task.compareTo("�� �������")==0){
			ArrayList<BrakingFluid> basket = new ArrayList<BrakingFluid>();
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
			page="home";
		}else if ((task.compareTo("�����")==0) || (task.compareTo("������� �����")==0)){
			model.addAttribute("variant",variant);
			model.addAttribute("pageInfo",task);
			model.addAttribute("id",0);
			model.addAttribute("combobox_countris",countryDAO.getCountries());
			if ((variant.compareTo("user")==0) || (variant.compareTo("������������")==0)){	
				model.addAttribute("roles",roleDAO.getRoles());
				model.addAttribute("currentRoles",new ArrayList<Role>());
			}
			page="AddEdit";
		}else if ((task.compareTo("��������� ������������")==0) || (variant.compareTo("DownloadProduct")==0)){
			model.addAttribute("variantDownload", Service.VARIANT_PRODUCT);
			model.addAttribute("errors", new ArrayList<String>());
			page="Download";
		}else if ((task.compareTo("��������� ����")==0) || (variant.compareTo("DownloadProduct")==0)){
			model.addAttribute("variantDownload", Service.VARIANT_PRICES);
			model.addAttribute("errors", new ArrayList<String>());
			page="Download";
		}else if (task.compareTo("Delete")==0){
			if ((variant.compareTo("user")==0) || (variant.compareTo("������������")==0)){
				userDAO.deleteUser(login);
			}else if ((variant.compareTo("manufacturer")==0) || (variant.compareTo("�������������")==0)) {
				manufacturerDAO.deleteManufacturer(id);
			}else if ((variant.compareTo("country")==0) || (variant.compareTo("������")==0)){
				countryDAO.deleteCountry(id);
			}else if ((variant.compareTo("fluidClass")==0) || (variant.compareTo("������ ��������")==0)){
				fluidClassDAO.deleteFluidClass(id);
			}else if ((variant.compareTo("client")==0) || (variant.compareTo("�������")==0)){
				clientDAO.deleteClient(id);
			}else if ((variant.compareTo("wishlist")==0) || (variant.compareTo("���������")==0)){
				wishlistDAO.deleteFromWishlist(id);
			}else if ((variant.compareTo("log")==0) || (variant.compareTo("�����������")==0)){
				logDAO.deleteLog(id);
			}
			page=Service.createAdminEdit(model, variant, currentPage, manufacturerDAO, fluidClassDAO, countryDAO, clientDAO, userDAO, logDAO, new LinkedList<String>());
		} else{
			int totalRows = 0;
			if ((variant.compareTo("user")==0) || (variant.compareTo("������������")==0)){
				User current=userDAO.getUser(login);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
				model.addAttribute("currentRoles",roleDAO.getRolesForUser(current.getLogin()));
				model.addAttribute("roles",roleDAO.getRoles());
			}else if ((variant.compareTo("manufacturer")==0) || (variant.compareTo("�������������")==0)) {
				Manufacturer current=manufacturerDAO.getManufacturer(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
				model.addAttribute("combobox_countris",countryDAO.getCountries());
			}else if ((variant.compareTo("country")==0) || (variant.compareTo("������")==0)){
				Country current=countryDAO.getCountry(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
			}else if ((variant.compareTo("fluidClass")==0) || (variant.compareTo("������ ��������")==0)){
				FluidClass current=fluidClassDAO.getFluidClass(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
			}else if ((variant.compareTo("client")==0) || (variant.compareTo("�������")==0)){
				Client current=clientDAO.getClient(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
				model.addAttribute("combobox_countris",countryDAO.getCountries());
			}else if ((variant.compareTo("wishlist")==0) || (variant.compareTo("���������")==0)){
				Wishlist current=wishlistDAO.getWishById(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
			}else if ((variant.compareTo("log")==0) || (variant.compareTo("�����������")==0)){
				Log current=logDAO.getLogById(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
			}
			model.addAttribute("pageInfo","�������� �������");
			model.addAttribute("variant",variant);
			model.addAttribute("currentUser",user);
		}
		return "adminpanel/"+page;	
	}		

	@RequestMapping(value = "/AddEdit", method = {RequestMethod.POST,RequestMethod.GET})
	public String home(@ModelAttribute User user
			,@RequestParam(value="id_current", defaultValue="0", required=false) int id_current
			,@RequestParam(value = "selections", required=false ) int[] roleSelections
			,@RequestParam(value="variant", defaultValue="", required=false ) String variant
			,@RequestParam(value="task", defaultValue="", required=false ) String task
			,@RequestParam(value="name_current", defaultValue="", required=false ) String name
			,@RequestParam(value="email", defaultValue="", required=false ) String email
			,@RequestParam(value="country", defaultValue="", required=false ) String country_name
			,@RequestParam(value="login", defaultValue="", required=false ) String login
			,@RequestParam(value="login_current", defaultValue="", required=false ) String login_current
			,@RequestParam(value="password_current", defaultValue="", required=false ) String password
			,@RequestParam(value="address", defaultValue="", required=false ) String address
			,@RequestParam(value="info", defaultValue="", required=false ) String info
			,HttpServletRequest request
			, Locale locale, Model model) {
			
		String page="home";
		int id=new Integer(id_current);
		
		try {
			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8"); 
			task = new String(task.getBytes("iso-8859-1"), "UTF-8");
			name =new String(name.getBytes("iso-8859-1"), "UTF-8");
			email =new String(email.getBytes("iso-8859-1"), "UTF-8");
			country_name =new String(country_name.getBytes("iso-8859-1"), "UTF-8");
			login =new String(login.getBytes("iso-8859-1"), "UTF-8");
			login_current =new String(login_current.getBytes("iso-8859-1"), "UTF-8");
			password =new String(password.getBytes("iso-8859-1"), "UTF-8");
			address =new String(address.getBytes("iso-8859-1"), "UTF-8");
			info =new String(info.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<String> errors=new LinkedList<String>();
		
		if (task.compareTo("� ������")==0){
			page=Service.createAdminEdit(model, variant, 1, manufacturerDAO, fluidClassDAO, countryDAO, clientDAO, userDAO, logDAO, errors);
		}else {
			synchronized (this) {
				if (variant.compareTo("user")==0){
					login=(login.isEmpty()?login_current:login); //�.�. ����� ���� ��������� ������� � ������� ������ ��� 1 ����� - ���� �� ������. ���� ������� ����� - ��� ����� ����� ����� � login_current 
					User current= new User();
					current.setEmail(email);
					current.setName(name);
					current.setPassword(password);
					current.setLogin(login);
					
					if (login.isEmpty()){
						errors.add("����� ����������� ��������� �����!");
					}
					if (email.isEmpty()){
						errors.add("����� ����������� ��������� e-mail!");
					}
					if (roleSelections==null){
						errors.add("H���� ����������� ���� ������������ �����-���� �����!");
					}
					
					if (errors.size()>0){
						model.addAttribute("current",current);
						model.addAttribute("id",current.getId());
						model.addAttribute("currentRoles",new ArrayList<Role>());
						model.addAttribute("roles",roleDAO.getRoles());
					}else{
						
						ArrayList<Role> existingRoles = roleDAO.getRolesForUser(login);
						ArrayList<Role> roles=new ArrayList<Role>();
						for (int i=0;i<roleSelections.length;i++){
							boolean bFind=false;							//���� ���� �� ��������� ����� � ������ ������ �����
							for (Role existingRole: existingRoles){
								if (roleSelections[i]==existingRole.getId()){
									bFind=true;
									existingRoles.remove(existingRole);		//����� - ������� � � ����� 
									break;
								}
							}
							if (!bFind){									//�� ����� - �������� � ������� �� ���������� 
								Role currentRole=roleDAO.getRole(roleSelections[i]);							
								if (!existingRoles.contains(currentRole)){
									roles.add(currentRole);
								}	
							}
						}
						for (Role currentRole:existingRoles){				//������ ������
							userDAO.deleteUser(current, currentRole);
							Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "����� ������ ���� � ������������ "+current.getLogin()));
						}
						if (roles.size()>0){								//������� �����������
							for (Role currentRole:roles){
								userDAO.createUserWithRole(current, currentRole);
								Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "����� ������� ������������/���� ������������ "+current.getLogin()));
							}
						}
						userDAO.updateUsers(current);
						Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "����� ������� ������ ������������� � ������� "+current.getLogin()));
					}
				}else if (variant.compareTo("manufacturer")==0){
					Manufacturer current = new Manufacturer(id_current, name, countryDAO.getCountryByName(country_name));
					if (name.isEmpty()){
						errors.add("����� ����������� ��������� ������������!");
					}
					if (errors.size()>0){
						model.addAttribute("current",current);
						model.addAttribute("id",current.getId());
						model.addAttribute("combobox_countris",countryDAO.getCountries());
					}else{
						current = manufacturerDAO.createManufacturer(current);
						Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "����� �������/�������� ������������"));
					}
				}else if (variant.compareTo("country")==0){
					Country current=new Country(id_current,name);
					if (name.isEmpty()){
						errors.add("����� ����������� ��������� ������������!");
					}
					if (errors.size()>0){
						model.addAttribute("current",current);
						model.addAttribute("id",current.getId());
					}else{
						current=countryDAO.createCountry(current);
						Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "����� �������/�������� ������"));
					}
				}else if (variant.compareTo("fluidClass")==0){
					FluidClass current=new FluidClass(id_current,name);
					if (name.isEmpty()){
						errors.add("����� ����������� ��������� ������������!");
					}
					if (errors.size()>0){
						model.addAttribute("current",current);
						model.addAttribute("id",current.getId());
					}else{
						current=fluidClassDAO.createFluidClass(current);
						Log	log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "����� �������/�������� ����� ��������"));
					}
				}else if (variant.compareTo("client")==0){
					Client current=new Client(id,name,email,address,countryDAO.getCountryByName(country_name));
					if (name.isEmpty()){
						errors.add("����� ����������� ��������� ������������!");
					}
					if (email.isEmpty()){
						errors.add("����� ����������� ��������� ������������!");
					}
					if (errors.size()>0){
						model.addAttribute("current",current);
						model.addAttribute("id",current.getId());
					}else{
						current=clientDAO.createClient(current);
						Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "����� �������/�������� �������"));
					}
				}else if (variant.compareTo("log")==0){
					Log current=logDAO.createLog(new Log(id,user,new GregorianCalendar().getTime(),null,info));
					if (info.isEmpty()){
						errors.add("����� ����������� ��������� ����������!");
					}
					if (errors.size()>0){
						model.addAttribute("current",current);
						model.addAttribute("id",current.getId());
					}else{
						current=logDAO.createLog(current);
						Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "����� �������/�������� �������"));
					}
				}
				if (errors.size()>0){
					model.addAttribute("variant",variant);
					model.addAttribute("pageInfo",task);
					model.addAttribute("combobox_countris",countryDAO.getCountries());
					model.addAttribute("errors", errors);
					page="AddEdit";
				}
			}
			if (errors.size()==0){
				page=Service.createAdminEdit(model, variant, 1, manufacturerDAO, fluidClassDAO, countryDAO, clientDAO, userDAO, logDAO, errors);
			}
		}
		return "adminpanel/"+page;	
	}	

}
