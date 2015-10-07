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
import eftech.workingset.DAO.templates.UserTemplate;
import eftech.workingset.DAO.templates.WishlistTemplate;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.User;

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
	
	@RequestMapping(value = "/ShowList", method = RequestMethod.GET)
	public String showList(@ModelAttribute User user
			,@RequestParam("variant") String formButton
			,@RequestParam(value="variant", defaultValue="1", required=false ) int numPage
			,HttpServletRequest request
			, Locale locale, Model model) {
		
		try {
			formButton=new String(formButton.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String page="home";
		
		if (formButton.compareTo("На главную")==0){
			ArrayList<BrakingFluid> basket = new ArrayList<BrakingFluid>();
			basket=brakingFluidDAO.getBrakingFluids();
		}else{
			int totalRows = 0;
			if (formButton.compareTo("User")==0){
				//totalRows=userDAO.getCountRows();
				

			}else if (formButton.compareTo("Manufacturer")==0){
			
			}else if (formButton.compareTo("Country")==0){
				
			}else if (formButton.compareTo("FluidClass")==0){
				
			}else if (formButton.compareTo("Client")==0){
				
			}else if (formButton.compareTo("Wishlist")==0){
				
			}else if (formButton.compareTo("Log")==0){
				totalRows=logDAO.getCountRows();
				
				
			}
		}
		return "adminpanel/"+page;	
	}		

	@RequestMapping(value = "/EditList", method = RequestMethod.GET)
	public String home(@ModelAttribute User user
			,@RequestParam("variant") String formButton
			,@RequestParam("id") int id
			,HttpServletRequest request
			, Locale locale, Model model) {
			
		try {
			formButton=new String(formButton.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (formButton.compareTo("На главную")==0){
			LinkedList<BrakingFluid> basket = new LinkedList<BrakingFluid>();
			basket.add(brakingFluidDAO.getBrakingFluid(id));
		}else if (formButton.compareTo("Новый элемент")==0){

		}else if (formButton.compareTo("Новый элемент")==0){
			
		}
		return "adminpanel/home";	
	}	

}
