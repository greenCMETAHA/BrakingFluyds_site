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
		
		if (task.compareTo("На главную")==0){
			ArrayList<BrakingFluid> basket = new ArrayList<BrakingFluid>();
			basket=brakingFluidDAO.getBrakingFluids();
			page="home";
		}else if (task.compareTo("Создать новый элемент")==0){
			model.addAttribute("variant",variant);
			model.addAttribute("id",0);
			page="AddEdit";
		}else if (task.compareTo("Delete")==0){
			if (variant.compareTo("User")==0){
				userDAO.deleteUser(id);
			}else if (variant.compareTo("Manufacturer")==0){
				manufacturerDAO.deleteManufacturer(id);
			}else if (variant.compareTo("Country")==0){
				countryDAO.deleteCountry(id);
			}else if (variant.compareTo("FluidClass")==0){
				fluidClassDAO.deleteFluidClass(id);
			}else if (variant.compareTo("Client")==0){
				clientDAO.deleteClient(id);
			}else if (variant.compareTo("Wishlist")==0){
				wishlistDAO.deleteFromWishlist(id);
			}else if (variant.compareTo("Log")==0){
				logDAO.deleteLog(id);
			}
			page=Service.createAdminEdit(model, variant, currentPage, manufacturerDAO, fluidClassDAO, countryDAO, clientDAO, userDAO, logDAO, new LinkedList<String>());
		} else{
			int totalRows = 0;
			if (variant.compareTo("User")==0){
				User current=userDAO.getUser(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
				model.addAttribute("currentRoles",roleDAO.getRolesForUser(current.getLogin()));
				model.addAttribute("roles",roleDAO.getRoles());
			}else if (variant.compareTo("Manufacturer")==0){
				Manufacturer current=manufacturerDAO.getManufacturer(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
				model.addAttribute("combobox_countris",countryDAO.getCountries());
			}else if (variant.compareTo("Country")==0){
				Country current=countryDAO.getCountry(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
			}else if (variant.compareTo("FluidClass")==0){
				FluidClass current=fluidClassDAO.getFluidClass(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
			}else if (variant.compareTo("Client")==0){
				Client current=clientDAO.getClient(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
				model.addAttribute("combobox_countris",countryDAO.getCountries());
			}else if (variant.compareTo("Wishlist")==0){
				Wishlist current=wishlistDAO.getWishById(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
			}else if (variant.compareTo("Log")==0){
				Log current=logDAO.getLogById(id);
				model.addAttribute("current",current);
				model.addAttribute("id",current.getId());
			}
		}
		return "adminpanel/"+page;	
	}		

	@RequestMapping(value = "/AddEdit", method = RequestMethod.POST)
	public String home(@ModelAttribute User user
			,@RequestParam(value="id_current", defaultValue="0", required=false) int id
			,@RequestParam(value = "selections", required=false ) int[] roleSelections
			,@RequestParam(value="variant", defaultValue="", required=false ) String variant
			,@RequestParam(value="name_current", defaultValue="", required=false ) String name
			,@RequestParam(value="email", defaultValue="", required=false ) String email
			,@RequestParam(value="country", defaultValue="", required=false ) int country_id
			,@RequestParam(value="login", defaultValue="", required=false ) String login
			,@RequestParam(value="password", defaultValue="", required=false ) String password
			,@RequestParam(value="address", defaultValue="", required=false ) String address
			,@RequestParam(value="info", defaultValue="", required=false ) String info
			,HttpServletRequest request
			, Locale locale, Model model) {
			
		String page="home";
		
		try {
			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LinkedList<String> errors=new LinkedList<String>();
		
		if (variant.compareTo("На главную")==0){
			LinkedList<BrakingFluid> basket = new LinkedList<BrakingFluid>();
			basket.add(brakingFluidDAO.getBrakingFluid(id));
		}else if (variant.compareTo("К списку")==0){
			page=Service.createAdminEdit(model, variant, 1, manufacturerDAO, fluidClassDAO, countryDAO, clientDAO, userDAO, logDAO, errors);
		}else {
			synchronized (this) {
				if (variant.compareTo("User")==0){
					User currentUser= new User();
					currentUser.setEmail(email);
					currentUser.setName(name);
					currentUser.setPassword(password);
					currentUser.setLogin(login);
					ArrayList<Role> existingRoles = roleDAO.getRolesForUser(login);
					ArrayList<Role> roles=new ArrayList<Role>();
					for (int i=0;i<roleSelections.length;i++){
						Role currentRole=roleDAO.getRole(roleSelections[i]);
						if (!existingRoles.contains(currentRole)){
							roles.add(currentRole);
							existingRoles.remove(currentRole);
						}
					}
					for (Role current:existingRoles){
						userDAO.deleteUser(currentUser, current);
						Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "Админ удалил Роль у пользователя "+currentUser.getLogin()));
					}
					if (roles.size()>0){
						for (Role current:roles){
							userDAO.createUserWithRole(user, current);
							Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "Админ добавил Пользователя/Роль пользователю "+currentUser.getLogin()));
						}
					}
					userDAO.updateUsers(currentUser);
					Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Админ изменил данные Пользователей с логином "+currentUser.getLogin()));
				}else if (variant.compareTo("Manufacturer")==0){
					Manufacturer current = manufacturerDAO.createManufacturer(name, country_id);
					Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "Админ добавил/исправил Производитея"));
				}else if (variant.compareTo("Country")==0){
					Country current=countryDAO.createCountry(name);
					Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "Админ добавил/исправил Страну"));
				}else if (variant.compareTo("FluidClass")==0){
					FluidClass current=fluidClassDAO.createFluidClass(name);
					Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "Админ добавил/исправил Класс жидкости"));
				}else if (variant.compareTo("Client")==0){
					Client current=clientDAO.createClient(new Client(id,name,email,address,new Country(id,"")));
					Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "Админ добавил/исправил Клиента"));
				}else if (variant.compareTo("Log")==0){
					Log current=logDAO.createLog(new Log(id,user,new GregorianCalendar().getTime(),null,info));
					Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), current, "Админ добавил/исправил Историю"));
				}
			}
			page=Service.createAdminEdit(model, variant, 1, manufacturerDAO, fluidClassDAO, countryDAO, clientDAO, userDAO, logDAO, errors);
		}
		return "adminpanel/"+page;	
	}	

}
