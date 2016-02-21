package eftech.vasil.greenCM;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.*; 
import java.io.*; 
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.itextpdf.text.DocumentException;
import com.paypal.api.payments.Address;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.CreditCard;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.FundingInstrument;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;

import eftech.workingset.DAO.templates.BrakingFluidTemplate;
import eftech.workingset.DAO.templates.ClientTemplate;
import eftech.workingset.DAO.templates.CountryTemplate;
import eftech.workingset.DAO.templates.CustomerTemplate;
import eftech.workingset.DAO.templates.DemandTemplate;
import eftech.workingset.DAO.templates.EngineTypeTemplate;
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.GearBoxOilTemplate;
import eftech.workingset.DAO.templates.GearBoxTypeTemplate;
import eftech.workingset.DAO.templates.InfoTemplate;
import eftech.workingset.DAO.templates.LogTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.MotorOilTemplate;
import eftech.workingset.DAO.templates.OfferStatusTemplate;
import eftech.workingset.DAO.templates.OfferTemplate;
import eftech.workingset.DAO.templates.OilStuffTemplate;
import eftech.workingset.DAO.templates.PayTemplate;
import eftech.workingset.DAO.templates.PriceTemplate;
import eftech.workingset.DAO.templates.ReviewTemplate;
import eftech.workingset.DAO.templates.RoleTemplate;
import eftech.workingset.DAO.templates.UserTemplate;
import eftech.workingset.DAO.templates.WishlistTemplate;
import eftech.workingset.Services.WorkWithExcel;
import eftech.workingset.Services.GenerateAccessToken;
import eftech.workingset.Services.ResultPrinter;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.Basket;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;
import eftech.workingset.beans.intefaces.base.InterfaceGood;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.Customer;
import eftech.workingset.beans.Demand;
import eftech.workingset.beans.EngineType;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.FluidClassSelected;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.ManufacturerSelected;
import eftech.workingset.beans.MotorOil;
import eftech.workingset.beans.Offer;
import eftech.workingset.beans.OilStuff;
import eftech.workingset.beans.Pay;
import eftech.workingset.beans.Price;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.Role;

/**
 * Handles requests for the application home page.
 */
@Controller
@SessionAttributes({"user", "adminpanel", "basket", "wishlist", "compare", "manufacturersFilterBrF", "fluidClassFilterBrF", "elementsInListBrF"
	, "currentPriceFilterBrF" , "currentBoilingTemperatureDryFilterBrF" , "currentBoilingTemperatureWetFilterBrF" , "currentValueFilterBrF" 
	, "currentViscosity40FilterBrF" , "currentViscosity100FilterBrF", "currentJudgementFilterBrF"
	,"dateBeginFilterOffer", "dateEndFilterOffer", "dateBeginFilterDemand", "dateEndFilterDemand", "Payment_Amount", "Payment_Option"})
public class HomeController{
	
	Map<String, String> map = new HashMap<String, String>();  //for paypal
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
	
	@Autowired
	PriceTemplate priceDAO;
	
	@Autowired
	OfferStatusTemplate offerStatusDAO;
	
	
	@Autowired
	OfferTemplate offerDAO;
	
	@Autowired
	DemandTemplate demandDAO;
	
	@Autowired
	CustomerTemplate customerDAO;


	@Autowired
	PayTemplate payDAO;
	
	@Autowired
	OilStuffTemplate oilStuffDAO;

	@Autowired
	EngineTypeTemplate engineTypeDAO;

	@Autowired
	MotorOilTemplate motorOilDAO;
	
	@Autowired
	GearBoxTypeTemplate gearBoxTypeDAO;
	
	@Autowired
	GearBoxOilTemplate gearBoxOilDAO;	
	
	private LinkedList<ManufacturerSelected> fillSelectedManufacturers(int[] manufacturerSelections, LinkedList<ManufacturerSelected> listManufacturerSelected){
		for (ManufacturerSelected manufacturer:listManufacturerSelected){
			manufacturer.setSelected(false);
			if (manufacturerSelections!=null){
				for (int j=0;j<manufacturerSelections.length; j++){
					if (manufacturerSelections[j]==manufacturer.getId()){
						manufacturer.setSelected(true);
						break;
					}
				}
			}
		}
		return listManufacturerSelected;
	}
	
	private LinkedList<FluidClassSelected> fillSelectedFluidClasses(int[] fluidClassSelections, LinkedList<FluidClassSelected> listFluidClassSelected){
		for (FluidClassSelected fluidClass:listFluidClassSelected){
			fluidClass.setSelected(false);
			if (fluidClassSelections!=null){
				for (int j=0;j<fluidClassSelections.length; j++){
					if (fluidClassSelections[j]==fluidClass.getId()){
						fluidClass.setSelected(true);
						break;
					}
				}
			}
		}		

		return listFluidClassSelected;
	}	
	
	private void createBussinessOffer(int id, int clientId, String variant, User user, LinkedList<Basket>  basket, HttpSession session){
		if (variant.equals("Show")){
			File pdfFile=null;
			try {
				pdfFile=Service.createPDF_BussinessOffer(basket, session.getServletContext().getRealPath("/"), user);
				logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создано бизнес-предложение"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (pdfFile!=null){
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().open(pdfFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.println("Awt Desktop is not supported!");
				}
			}
			
		}		
		if  (variant.equals("Send")){
			File pdfFile=null;
			try {
				pdfFile=Service.createPDF_BussinessOffer(basket, session.getServletContext().getRealPath("/"), user);
				logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создано бизнес-предложение"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//отошлем по электронной почте
			if (pdfFile!=null){
				if (clientId>0){
					Client currentClient=(Client)clientDAO.getClient(clientId);
					if (currentClient.getEmail().length()>0){
					 	Properties props = new Properties();			//ssl для яндекса
					 	props.put("mail.smtp.host", "smtp.yandex.ru");
					 	props.put("mail.smtp.socketFactory.port", "465");
					 	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
					 	props.put("mail.smtp.auth", "true");
					 	props.put("mail.smtp.port", "465");
//					        
//					     //   Session session = Session.getInstance(props,null);
					        Session sessionEmail = Session.getInstance(props, new Authenticator() {
					            protected PasswordAuthentication getPasswordAuthentication() {
					            	return new PasswordAuthentication("locomotions2@yandex.ru", "1z2x3c4v5b");
					            }
					        });
				 
				        try {
				            Message message = new MimeMessage(sessionEmail);
				            //от кого
				            message.setFrom(new InternetAddress("locomotions2@yandex.ru","Васильченко"));
				            //кому
				            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(currentClient.getEmail()));
				            //Заголовок письма
				            message.setSubject("Бизнес-предложение");
				            //Содержимое
				            
				            // Create the message part
				            BodyPart messageBodyPart = new MimeBodyPart();

				            // Now set the actual message
				            messageBodyPart.setText("Бизнес предложение (тестовое задание) для "+currentClient.getName());

				            // Create a multipar message
				            Multipart multipart = new MimeMultipart();

				            // Set text message part
				            multipart.addBodyPart(messageBodyPart);

				            // Part two is attachment
				            
				          //now write the PDF content to the output stream
				            messageBodyPart = new MimeBodyPart();
				            DataSource source = new FileDataSource(pdfFile);
				            messageBodyPart.setDataHandler(new DataHandler(source));
				            messageBodyPart.setFileName(pdfFile.getName());
				            multipart.addBodyPart(messageBodyPart);
				            
				            message.setContent(messageBodyPart.getParent());

				            // Send the complete message parts
				            // Session mailSession = Session.getDefaultInstance(props, null);
				            //Transport = mailSession.getTransport();
				            //Отправляем сообщение
				            Transport.send(message);
				            logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime()
				            			, null, "Создано бизнес-предложение, отослали на email: "+currentClient.getEmail()));
				        } catch (MessagingException e) {
				            throw new RuntimeException(e);
				        } catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	
					}
			        
				}
			}
			
		}
	}
	

	@RequestMapping(value = {"/searchThing"}, method ={RequestMethod.GET, RequestMethod.POST})   //хз, почему он не отрабатывает на searchGood
	public String searchThing(
			@RequestParam(value = "searchField", defaultValue="", required=false) String searchField
			,@RequestParam(value = "searchButton", defaultValue="", required=false) String searchButton
			,HttpServletRequest request,Locale locale, Model model) {
		
		if (searchField.length()>0) {
			model.addAttribute("searchField",searchField);
		}
		
		String strRedirectTo=null;
		if ("Моторные масла".equals(searchButton)){
			strRedirectTo="motorOil";
		} else if ("Трансмиссионные масла".equals(searchButton)){
			strRedirectTo="gearBoxOil";
		} else {
			strRedirectTo="home";
		}
		
		return ("redirect:"+strRedirectTo);

	}
		 	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = {"/","/index","/adminpanel/index"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String index(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "adminpanel", defaultValue="false", required=false) boolean adminpanel
			,HttpServletRequest request,Locale locale, Model model) {

		HttpSession session=request.getSession();
		
		session.setAttribute("adminpanel", adminpanel);

		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		if (user.getId()!=0){
			logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), user, "Пользователь зашел в систему"));
		}
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session, 0,0, 0, 1, new Customer());
		
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
			
		//model.addAttribute("user", user);
		model.addAttribute("user", user);
		model=Service.createHeader(model, user, basket,wishlist, compare, infoDAO, wishlistDAO);

		return "index";
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = {"/About"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String about(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "adminpanel", defaultValue="false", required=false) boolean adminpanel
			,HttpServletRequest request,Locale locale, Model model) {

		HttpSession session=request.getSession();
		
		session.setAttribute("adminpanel", adminpanel);

		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		if (user.getId()!=0){
			logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), user, "Пользователь зашел в систему"));
		}
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session, 0,0, 0, 1, new Customer());
		
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
			
		//model.addAttribute("user", user);
		model.addAttribute("user", user);
		model=Service.createHeader(model, user, basket,wishlist, compare, infoDAO, wishlistDAO);

		return "About";
	}	
	
	@RequestMapping(value = {"/home","/adminpanel/home"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String _home(
			@RequestParam(value = "good", defaultValue="", required=false) String good
			
			,@RequestParam(value = "selections", required=false ) int[] manufacturerSelections  //base (BrakingFluids)
			,@RequestParam(value = "fluidClassselections", required=false ) int[] fluidClassselections
			,@RequestParam(value = "currentPage", defaultValue="1", required=false) int currentPage
			,@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "currentPriceFilter", defaultValue="0,0", required=false) String currentPriceFilter
			,@RequestParam(value = "currentBoilingTemperatureDryFilter", defaultValue="0,0", required=false) String currentBoilingTemperatureDryFilter
			,@RequestParam(value = "currentBoilingTemperatureWetFilter", defaultValue="0,0", required=false) String currentBoilingTemperatureWetFilter
			,@RequestParam(value = "currentValueFilter", defaultValue="0,0", required=false) String currentValueFilter
			,@RequestParam(value = "currentViscosity40Filter", defaultValue="0,0", required=false) String currentViscosity40Filter
			,@RequestParam(value = "currentViscosity100Filter", defaultValue="0,0", required=false) String currentViscosity100Filter
			,@RequestParam(value = "currentJudgementFilter", defaultValue="0,0", required=false) String currentJudgementFilter
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "adminpanel", defaultValue="false", required=false) boolean adminpanel
			,@RequestParam(value = "searchField", defaultValue="", required=false) String searchField
			
			,@RequestParam(value = "viscositySelections", required=false ) int[] viscositySelections  //MotorOils
			,@RequestParam(value = "engineTypeSelections", required=false ) int[] engineTypeSelections
			,@RequestParam(value = "oilStuffSelections", required=false ) int[] oilStuffSelections
			,HttpServletRequest request
			,Locale locale, Model model) {
		
 		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		session.setAttribute("adminpanel", adminpanel);
		
//		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if (variant.compareTo("Заявка")==0){
			variant="Demand";
		}
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		LinkedList<ManufacturerSelected>  manufacturersFilter = (LinkedList<ManufacturerSelected>) session.getAttribute("manufacturersFilterBrF");
		if (manufacturersFilter==null){
			manufacturersFilter = createManufacturersFilter();
		}
		LinkedList<FluidClassSelected>  fluidClassFilter = (LinkedList<FluidClassSelected>) session.getAttribute("fluidClassFilterBrF");
		if (fluidClassFilter==null){
			fluidClassFilter = createFluidClassFilter();
		}
		if (currentPriceFilter.toString().equals("0,0")){
			if (session.getAttribute("currentPriceFilterBrF")!=null){
				currentPriceFilter =(String) session.getAttribute("currentPriceFilterBrF");
			}
		}
		if (currentBoilingTemperatureDryFilter.toString().equals("0,0")){
			if (session.getAttribute("currentBoilingTemperatureDryFilterBrF")!=null){
				currentBoilingTemperatureDryFilter =(String) session.getAttribute("currentBoilingTemperatureDryFilterBrF");
			}
		}
		if (currentBoilingTemperatureWetFilter.toString().equals("0,0")){
			if (session.getAttribute("currentBoilingTemperatureWetFilterBrF")!=null){
				currentBoilingTemperatureWetFilter =(String) session.getAttribute("currentBoilingTemperatureWetFilterBrF");
			}
		}
		if (currentValueFilter.toString().equals("0,0")){	
			if (session.getAttribute("currentValueFilterBrF")!=null){
				currentValueFilter =(String) session.getAttribute("currentValueFilterBrF");
			}
		}
		if (currentViscosity40Filter.toString().equals("0,0")){
			if (session.getAttribute("currentViscosity40FilterBrF")!=null){
				currentViscosity40Filter =(String) session.getAttribute("currentViscosity40FilterBrF");
			}
		}
		if (currentViscosity100Filter.toString().equals("0,0")){
			if (session.getAttribute("currentViscosity100FilterBrF")!=null){
				currentViscosity100Filter =(String) session.getAttribute("currentViscosity100FilterBrF");
			}
		}
		if (currentJudgementFilter.toString().equals("0,0")){
			if (session.getAttribute("currentJudgementFilterBrF")!=null){
				currentJudgementFilter =(String) session.getAttribute("currentJudgementFilterBrF");
			}
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInListBrF")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInListBrF");
		}
 
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				,logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0, 0, 0, 1, new Customer());

		elementsInList=(elementsInList==0?Service.ELEMENTS_IN_LIST:elementsInList);
	
		//--1
		int minPrice=new Double(brakingFluidDAO.minData("Price")).intValue();
		double maxPriceDouble=brakingFluidDAO.maxData("Price");
		int maxPrice=new Double(maxPriceDouble).intValue();
		if (maxPrice<maxPriceDouble){
			maxPrice++;
		}
		int[] priceFilter=Service.createFilterForSlider("Price", Service.BRAKING_FLUID_PREFIX, minPrice, maxPrice
				, currentPriceFilter, model, session);
		

		//--2
		int minBoilingTemperatureDry=new Double(brakingFluidDAO.minData("BoilingTemperatureDry")).intValue();
		double maxBoilingTemperatureDryDouble=brakingFluidDAO.maxData("BoilingTemperatureDry");
		int maxBoilingTemperatureDry=new Double(maxBoilingTemperatureDryDouble).intValue();
		if (maxBoilingTemperatureDry<maxBoilingTemperatureDryDouble){
			maxBoilingTemperatureDry++;
		}
		
		int[] boilingTemperatureDryFilter=Service.createFilterForSlider("BoilingTemperatureDry", Service.BRAKING_FLUID_PREFIX
				, minBoilingTemperatureDry, maxBoilingTemperatureDry, currentBoilingTemperatureDryFilter, model, session);
		
		//--3
		int minBoilingTemperatureWet=new Double(brakingFluidDAO.minData("BoilingTemperatureWet")).intValue();
		double maxBoilingTemperatureWetDouble=brakingFluidDAO.maxData("BoilingTemperatureWet");
		int maxBoilingTemperatureWet=new Double(maxBoilingTemperatureWetDouble).intValue();
		if (maxBoilingTemperatureWet<maxBoilingTemperatureWetDouble){
			maxBoilingTemperatureWet++;
		}
		
		int[] boilingTemperatureWetFilter=Service.createFilterForSlider("BoilingTemperatureWet", Service.BRAKING_FLUID_PREFIX
				, minBoilingTemperatureWet, maxBoilingTemperatureWet, currentBoilingTemperatureWetFilter, model, session);
		
		//--4
		int minValue=new Double(brakingFluidDAO.minData("Value")*1000).intValue();
		double maxValueDouble=brakingFluidDAO.maxData("Value")*1000;
		int maxValue=new Double(maxValueDouble).intValue();
		if (maxValue<maxValueDouble){
			maxValue++;
		}
		
		int[] valueFilter=Service.createFilterForSlider("Value", Service.BRAKING_FLUID_PREFIX, minValue, maxValue
				, currentValueFilter, model, session);

		//--5
		int minViscosity40=new Double(brakingFluidDAO.minData("Viscosity40")).intValue();
		double maxViscosity40Double=brakingFluidDAO.maxData("Viscosity40");
		int maxViscosity40=new Double(maxViscosity40Double).intValue();
		if (maxViscosity40<maxViscosity40Double){
			maxViscosity40++;
		}
		
		int[] viscosity40Filter=Service.createFilterForSlider("Viscosity40", Service.BRAKING_FLUID_PREFIX, minViscosity40, maxViscosity40
				, currentViscosity40Filter, model, session);
		
		//--6
		int minViscosity100=new Double(brakingFluidDAO.minData("Viscosity100")).intValue();
		double maxViscosity100Double=brakingFluidDAO.maxData("Viscosity100");
		int maxViscosity100=new Double(maxViscosity100Double).intValue();
		if (maxViscosity100<maxViscosity100Double){
			maxViscosity100++;
		}
		
		int[] viscosity100Filter=Service.createFilterForSlider("Viscosity100", Service.BRAKING_FLUID_PREFIX, minViscosity100, maxViscosity100
				, currentViscosity100Filter, model, session);
		
		//--7
		int minJudgement=new Double(brakingFluidDAO.minData("Judgement")).intValue();  //пока отключим. Бо не получается позиционировать на [0:5], вываливает на [100:0]
		double maxJudgementDouble=brakingFluidDAO.maxData("Judgement");
		int maxJudgement=new Double(maxJudgementDouble).intValue();
		if (maxJudgement<maxJudgementDouble){
			maxJudgement++;
		}
		
		int[] judgementFilter=Service.createFilterForSlider("Judgement", Service.BRAKING_FLUID_PREFIX, minJudgement, maxJudgement
				, currentJudgementFilter, model, session);
			
		manufacturersFilter=fillSelectedManufacturers(manufacturerSelections,manufacturersFilter); //method
		fluidClassFilter=fillSelectedFluidClasses(fluidClassselections,fluidClassFilter); //method
//		System.out.println(manufacturerSelections);
//		System.out.println(fluidClassselections);
	
		ArrayList<BrakingFluid> listBakingFluids=brakingFluidDAO.getBrakingFluids(currentPage,elementsInList,manufacturersFilter,fluidClassFilter
				,priceFilter[0],priceFilter[1]
				,boilingTemperatureDryFilter[0],boilingTemperatureDryFilter[1]
				,boilingTemperatureWetFilter[0],boilingTemperatureWetFilter[1]
				,valueFilter[0]/1000,valueFilter[1]/1000
				,viscosity40Filter[0],viscosity40Filter[1]
				,viscosity100Filter[0],viscosity100Filter[1]
				,judgementFilter[0],judgementFilter[1], searchField); 
		

		
		model.addAttribute("listBrakFluids", listBakingFluids);
		int totalProduct=brakingFluidDAO.getCountRows(currentPage,elementsInList,manufacturersFilter,fluidClassFilter
				,priceFilter[0],priceFilter[1]
				,boilingTemperatureDryFilter[0],boilingTemperatureDryFilter[1]
				,boilingTemperatureWetFilter[0],boilingTemperatureWetFilter[1]
				,valueFilter[0]/1000,valueFilter[1]/1000
				,viscosity40Filter[0],viscosity40Filter[1]
				,viscosity100Filter[0],viscosity100Filter[1]
				,judgementFilter[0],judgementFilter[1], searchField); 
		int totalPages = (int)(totalProduct/elementsInList)+(totalProduct%elementsInList>0?1:0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		
		model.addAttribute("manufacturersFilter", manufacturersFilter);
		model.addAttribute("fluidClassFilter", fluidClassFilter);
		model.addAttribute("recommendedBrakFluids", brakingFluidDAO.getBrakingFluidsRecommended());
		 
		model.addAttribute("paginationString_part1", ""+((currentPage-1)*elementsInList+1)+"-"+(((currentPage-1)*elementsInList)+elementsInList));
		model.addAttribute("paginationString_part2", totalProduct);

		model.addAttribute("user", user);
		
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);	 //method
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("manufacturersFilterBrF", manufacturersFilter);
		session.setAttribute("fluidClassFilterBrF", fluidClassFilter);
		session.setAttribute("elementsInListBrF", elementsInList);
		
		return Service.isAdminPanel(session,request)+"home";
	}
	
	@RequestMapping(value = {"/Comparison","/adminpanel/Comparison"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String compare(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "task", defaultValue="BrF", required=false ) String task
			,@RequestParam(value = "selections", required=false ) int[] fluidsSelection
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {

		
//		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		
		String result=Service.isAdminPanel(session,request)+"Comparison";
		
		if (variant.compareTo("Сравнить")==0){
			if (Service.BRAKING_FLUID_PREFIX.equals(task)){
				if (fluidsSelection!=null){
					basket.clear();
					for (int i=0;i<fluidsSelection.length; i++){
						basket.add(new Basket(brakingFluidDAO.getBrakingFluid(fluidsSelection[i])));
					}
					session.setAttribute("basket", basket);
				}
			
				result=Service.isAdminPanel(session,request)+"Comparison";
			}else if (Service.MOTOR_OIL_PREFIX.equals(task)){
				if (fluidsSelection!=null){
					basket.clear();
					for (int i=0;i<fluidsSelection.length; i++){
						basket.add(new Basket(motorOilDAO.getMotorOil(fluidsSelection[i])));
					}
					session.setAttribute("basket", basket);
				}
			
				result="MotorOilComparison";
			}else if (Service.GEARBOX_OIL_PREFIX.equals(task)){
				if (fluidsSelection!=null){
					basket.clear();
					for (int i=0;i<fluidsSelection.length; i++){
						basket.add(new Basket(gearBoxOilDAO.getGearBoxOil(fluidsSelection[i])));
					}
					session.setAttribute("basket", basket);
				}
			
				result="GearBoxOilComparison";
			}
		}
		if (variant.compareTo("На главную")==0){
			if ("BrF".equals(task)){
				model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
				result=Service.isAdminPanel(session,request)+"home";
			}else if (Service.MOTOR_OIL_PREFIX.equals(task)){
				model.addAttribute("list", motorOilDAO.getMotorOils());
				result="MotorOilhome";
			}else if (Service.GEARBOX_OIL_PREFIX.equals(task)){
				model.addAttribute("list", gearBoxOilDAO.getGearBoxOils());
				result="GearBoxOilhome";
			}
		}
		
		
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		Service.workWithList(id, task, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO, logDAO, clientDAO
				, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		if (compare.size()>0){
			task=compare.get(0).getGoodName();
		}
			
		if (Service.BRAKING_FLUID_PREFIX.equals(task)){
			result=Service.isAdminPanel(session,request)+"Comparison";
		}else if (Service.MOTOR_OIL_PREFIX.equals(task)){
			result="MotorOilComparison";
		}else if (Service.GEARBOX_OIL_PREFIX.equals(task)){
			result="GearBoxOilComparison";
		}
		
		return result;
	}	
	
	@RequestMapping(value = "/Wishlist", method = {RequestMethod.POST, RequestMethod.GET})
	public String wishlist(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "goodPrefix", defaultValue="BrF", required=false) String goodPrefix
			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		Service.workWithList(id, goodPrefix, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO, logDAO, clientDAO
				, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "Wishlist";
	}	
	
	
	@RequestMapping(value = {"/Basket","/adminpanel/Basket"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String basket(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "goodPrefix", defaultValue="BrF", required=false) String goodPrefix
			,@RequestParam(value = "client", defaultValue="0", required=false) int client
			,@RequestParam(value = "quantity", defaultValue="0", required=false) int quantity
			,HttpServletRequest request,Locale locale, Model model) {

//		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		HttpSession session=request.getSession();
		
		if (variant.compareTo("Заявка")==0){
			return Service.isAdminPanel(session,request)+"makeDemand";
		}
		if (variant.compareTo("На главную")==0){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
			return Service.isAdminPanel(session,request)+"home";
		}		
		
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		Service.workWithList(id, goodPrefix, quantity, true, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO, logDAO, clientDAO
				, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		
		createBussinessOffer(id, client, variant, user, basket, session);

		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		model.addAttribute("listClients", clientDAO.getClients());
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		//model.addAttribute("totalBasket", Service.countBasket(basket));  //ограничить 2 знаками после запятой.
		
		return Service.isAdminPanel(session,request)+"Basket";
	}	
	
	@RequestMapping(value = {"/Download","/adminpanel/Download"}, headers = "content-type=multipart/*", method = {RequestMethod.POST, RequestMethod.GET})
	public String download(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value="fileExcel",  required=false) MultipartFile fileExcel
			,@RequestParam(value = "good", defaultValue="", required=false) String good
			,@RequestParam(value = "task", defaultValue="", required=false) String task
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {

//		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		HttpSession session=request.getSession();
		
		if (variant.compareTo("На главную")==0){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
			return Service.isAdminPanel(session,request)+"home";
		}
		if (variant.compareTo("Загрузка")==0){
			variant="Download";
			return Service.isAdminPanel(session,request)+"home";
		}
		
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		ArrayList<String> errors=new ArrayList<String>();
		if (variant!="download"){
			errors=WorkWithExcel.downloadExcel(variant,user, good, fileExcel, countryDAO, manufacturerDAO, fluidClassDAO, brakingFluidDAO,
					oilStuffDAO, engineTypeDAO, motorOilDAO, gearBoxTypeDAO, gearBoxOilDAO, logDAO, priceDAO, session);
		}
		
		model.addAttribute("variant", variant);
		model.addAttribute("task", variant);
		model.addAttribute("errors", errors);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return Service.isAdminPanel(session,request)+"Download";
	}		
		
	@RequestMapping(value = {"/ShowOne","/adminpanel/ShowOne"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String showOne(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "goodPrefix", defaultValue="BrF", required=false) String goodPrefix   //!!!!!!!!  Обработать
			,@RequestParam(value = "userLogin", defaultValue="", required=false) String userLogin
			,@RequestParam(value = "userEmail", defaultValue="", required=false) String userEmail
			,@RequestParam(value = "userReviw", defaultValue="", required=false) String userReviw
			,@RequestParam(value = "score", defaultValue="0", required=false) double judgement

			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		
		if ((userLogin.trim().isEmpty()) || (userEmail.trim().isEmpty())){ 
		}else{
//			try {
//				userLogin=new String(userLogin.getBytes("iso-8859-1"), "UTF-8");
//				userEmail=new String(userEmail.getBytes("iso-8859-1"), "UTF-8");
//				userReviw= new String(userReviw.getBytes("iso-8859-1"), "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}			
			Review review = new Review();
			review.setEmail(userEmail);
			review.setName(userLogin);
			review.setJudgement(judgement);
			review.setReview(userReviw);
			BrakingFluid brFluid=new BrakingFluid();
			brFluid.setId(id);
			review.setGood(brFluid);
			reviewDAO.createReview(review,Service.BRAKING_FLUID_PREFIX);
		}		
		
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		if ((request.isUserInRole("ROLE_ADMIN")) || (request.isUserInRole("ROLE_PRODUCT"))
				|| (request.isUserInRole("ROLE_PRICE"))){
			
			if (variant.compareTo("insert")==0){
				model.addAttribute("pageInfo", "Введите новую номенклатуру: ");
				model.addAttribute("currentBrakFluid", new BrakingFluid());
			}else{
				BrakingFluid currentBR = brakingFluidDAO.getBrakingFluid(id);
				model.addAttribute("pageInfo", "Отредактируйте номенклатуру:");
				model.addAttribute("currentBrakFluid", currentBR);
				String photo="";
				if (currentBR.hasPhoto()){
					photo=currentBR.getPhoto();
				}
				model.addAttribute("Photo", photo);
				model.addAttribute("photoBackUp", photo);
			}
			model.addAttribute("buttonInto", "Сохранить");
			model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
			model.addAttribute("combobox_FluidClasses", fluidClassDAO.getFluidClassis());
			model.addAttribute("errors", new ArrayList<String>());
			model.addAttribute("prices", priceDAO.getPrices(id, Service.BRAKING_FLUID_PREFIX));
			
			return Service.isAdminPanel(session,request)+"InsertUpdate";
		}else{
			model.addAttribute("currentBrakFluid", brakingFluidDAO.getBrakingFluid(id));
			model.addAttribute("reviews", reviewDAO.getReviews(id,Service.BRAKING_FLUID_PREFIX));
			model.addAttribute("prices", priceDAO.getPrices(id, Service.BRAKING_FLUID_PREFIX));
			
			return Service.isAdminPanel(session,request)+"ShowOne";   //!!!! somwhere here is a mistake for a admin InsertUpdate
		}
		
	}	
	
	public String defaultHome(HttpSession session, Model model, User user, LinkedList<Basket> basket
			, LinkedList<Wishlist> wishlist, LinkedList<InterfaceGood> compare, String searchField){
		LinkedList<ManufacturerSelected>  manufacturersFilter = (LinkedList<ManufacturerSelected>) session.getAttribute("manufacturersFilterBrF");
		if (manufacturersFilter==null){
			manufacturersFilter = createManufacturersFilter();
		}
		LinkedList<FluidClassSelected>  fluidClassFilter = (LinkedList<FluidClassSelected>) session.getAttribute("fluidClassFilterBrF");
		if (fluidClassFilter==null){
			fluidClassFilter = createFluidClassFilter();
		}
		String currentPriceFilter = createCurrentPriceFilter();
		if (session.getAttribute("currentPriceFilterBrF")!=null){
			currentPriceFilter =(String) session.getAttribute("currentPriceFilterBrF");
		}else{
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInListBrF")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInListBrF");
		}
		
		elementsInList=(elementsInList==0?Service.ELEMENTS_IN_LIST:elementsInList);
		double currentMinPriceFilter=brakingFluidDAO.minData("Price");
		double currentMaxPriceFilter=brakingFluidDAO.maxData("Price");
		model.addAttribute("MinPrice", currentMinPriceFilter);
		model.addAttribute("MaxPrice", currentMaxPriceFilter);
		model.addAttribute("currentMinPriceFilter", currentMinPriceFilter);
		model.addAttribute("currentMaxPriceFilter", currentMaxPriceFilter);		

		
		double currentMinBoilingTemperatureDryFilter=brakingFluidDAO.minData("BoilingTemperatureDry");
		double currentMaxBoilingTemperatureDryFilter=brakingFluidDAO.maxData("BoilingTemperatureDry");
		model.addAttribute("MinBoilingTemperatureDry", currentMinBoilingTemperatureDryFilter);
		model.addAttribute("MaxBoilingTemperatureDry", currentMaxBoilingTemperatureDryFilter);
		model.addAttribute("currentMinBoilingTemperatureDryFilter", currentMinBoilingTemperatureDryFilter);
		model.addAttribute("currentMaxBoilingTemperatureDryFilter", currentMaxBoilingTemperatureDryFilter);		

		
		double currentMinBoilingTemperatureWetFilter=brakingFluidDAO.minData("BoilingTemperatureWet");
		double currentMaxBoilingTemperatureWetFilter=brakingFluidDAO.maxData("BoilingTemperatureWet");
		model.addAttribute("MinBoilingTemperatureWet", currentMinBoilingTemperatureWetFilter);
		model.addAttribute("MaxBoilingTemperatureWet", currentMaxBoilingTemperatureWetFilter);
		model.addAttribute("currentMinBoilingTemperatureWetFilter", currentMinBoilingTemperatureWetFilter);
		model.addAttribute("currentMaxBoilingTemperatureWetFilter", currentMaxBoilingTemperatureWetFilter);		

		
		double currentMinValueFilter=brakingFluidDAO.minData("Value")*1000;
		double currentMaxValueFilter=brakingFluidDAO.maxData("Value")*1000;
		model.addAttribute("MinValue", currentMinValueFilter);
		model.addAttribute("MaxValue", currentMaxValueFilter);
		model.addAttribute("currentMinValueFilter", currentMinValueFilter);
		model.addAttribute("currentMaxValueFilter", currentMaxValueFilter);		
		
		
		double currentMinViscosity40Filter=brakingFluidDAO.minData("Viscosity40");
		double currentMaxViscosity40Filter=brakingFluidDAO.maxData("Viscosity40");
		model.addAttribute("MinViscosity40", currentMinViscosity40Filter);
		model.addAttribute("MaxViscosity40", currentMaxViscosity40Filter);
		model.addAttribute("currentMinViscosity40Filter", currentMinViscosity40Filter);
		model.addAttribute("currentMaxViscosity40Filter", currentMaxViscosity40Filter);		
		
		
		double currentMinViscosity100Filter=brakingFluidDAO.minData("Viscosity100");
		double currentMaxViscosity100Filter=brakingFluidDAO.maxData("Viscosity100");
		model.addAttribute("MinViscosity100", currentMinViscosity100Filter);
		model.addAttribute("MaxViscosity100", currentMaxViscosity100Filter);
		model.addAttribute("currentMinViscosity100Filter", currentMinViscosity100Filter);
		model.addAttribute("currentMaxViscosity100Filter", currentMaxViscosity100Filter);		

		
		double currentMinJudgementFilter=brakingFluidDAO.minData("Judgement");
		double currentMaxJudgementFilter=brakingFluidDAO.maxData("Judgement");
		model.addAttribute("MinJudgement", currentMinJudgementFilter);
		model.addAttribute("MaxJudgement", currentMaxJudgementFilter);
		model.addAttribute("currentMinJudgementFilter", currentMinJudgementFilter);
		model.addAttribute("currentMaxJudgementFilter", currentMaxJudgementFilter);		

		
		int count=0;												//восстановим существующий фильтр по производителю -->
		for (ManufacturerSelected currentMan:manufacturersFilter){
			if (currentMan.isSelected()){
				count++;
			}
		}
		
		int manufacturerSelections[]=new int[count];
		count=0;
		for (ManufacturerSelected currentMan:manufacturersFilter){
			if (currentMan.isSelected()){
				manufacturerSelections[count]=currentMan.getId();
				count++;
			}
		}															//восстановим существующий фильтр по производителю <--
		
		count=0;												//восстановим существующий фильтр по классу жидкости -->
		for (FluidClassSelected currentFC:fluidClassFilter){
			if (currentFC.isSelected()){
				count++;
			}
		}
		
		int fluidClassSelections[]=new int[count];
		count=0;
		for (FluidClassSelected currentFC:fluidClassFilter){
			if (currentFC.isSelected()){
				manufacturerSelections[count]=currentFC.getId();
				count++;
			}
		}															//восстановим существующий фильтр по  классу жидкости  <--
		
		
		
		ArrayList<BrakingFluid> listBakingFluids=brakingFluidDAO.getBrakingFluids(1,elementsInList
				,manufacturersFilter, fluidClassFilter
				,currentMinPriceFilter,currentMaxPriceFilter
				,currentMinBoilingTemperatureDryFilter,currentMaxBoilingTemperatureDryFilter
				,currentMinBoilingTemperatureWetFilter,currentMaxBoilingTemperatureWetFilter
				,currentMinValueFilter/1000,currentMaxValueFilter/1000
				,currentMinViscosity40Filter,currentMaxViscosity40Filter
				,currentMinViscosity100Filter,currentMaxViscosity100Filter
				,currentMinJudgementFilter,currentMaxJudgementFilter,searchField); 

		model.addAttribute("listBrakFluids", listBakingFluids);
		int totalProduct=brakingFluidDAO.getCountRows(1,elementsInList
				,manufacturersFilter, fluidClassFilter
				,currentMinPriceFilter,currentMaxPriceFilter
				,currentMinBoilingTemperatureDryFilter,currentMaxBoilingTemperatureDryFilter
				,currentMinBoilingTemperatureWetFilter,currentMaxBoilingTemperatureWetFilter
				,currentMinValueFilter/1000,currentMaxValueFilter/1000
				,currentMinViscosity40Filter,currentMaxViscosity40Filter
				,currentMinViscosity100Filter,currentMaxViscosity100Filter
				,currentMinJudgementFilter,currentMaxJudgementFilter,searchField); 

		int totalPages = (int)(totalProduct/elementsInList)+(totalProduct%elementsInList>0?1:0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", 1);
		
		model.addAttribute("manufacturersFilter", manufacturersFilter);
		model.addAttribute("recommendedBrakFluids", brakingFluidDAO.getBrakingFluidsRecommended());
		model.addAttribute("currentPriceFilter", currentPriceFilter);
		
		model.addAttribute("paginationString_part1", ""+(1)+"-"+elementsInList);
		model.addAttribute("paginationString_part2", totalProduct);

		model.addAttribute("user", user);
		
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("currentPriceFilterBrF", currentPriceFilter);
		session.setAttribute("manufacturersFilterBrF", manufacturersFilter);
		session.setAttribute("elementsInListBrF", elementsInList);
		
		return "home";
	}
	
	@RequestMapping(value = {"/InsertUpdate","/adminpanel/InsertUpdate"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String insertUpdate(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "id_BrakeFluid" , defaultValue="0", required=false) int id_BrakeFluid
			,@RequestParam(value = "name_BrakeFluid", defaultValue="", required=false) String name_BrakeFluid
			,@RequestParam(value = "Manufacturer", defaultValue="", required=false) String manufacturer
			,@RequestParam(value = "FluidClass", defaultValue="", required=false) String fluidClass
			,@RequestParam(value = "BoilingTemperatureDry", defaultValue="0", required=false) String boilingTemperatureDry  //double
			,@RequestParam(value = "BoilingTemperatureWet", defaultValue="0", required=false) String boilingTemperatureWet  //double
			,@RequestParam(value = "Value", defaultValue="0", required=false) String value	//double
			,@RequestParam(value = "Price", defaultValue="0", required=false) String price	//double
			,@RequestParam(value = "Photo", required=false) MultipartFile formPhoto
			,@RequestParam(value="photoBackUp", defaultValue="", required=false) String photoBackUp   //в MultipartFile невозможно получить значение, оказывается. Старое сохраним в элементе формы
			,@RequestParam(value = "Description", defaultValue="", required=false) String description
			,@RequestParam(value = "Viscosity40", defaultValue="0", required=false) String Viscosity40	//double
			,@RequestParam(value = "Viscosity100", defaultValue="0", required=false) String Viscosity100	//double
			,@RequestParam(value = "Specification", defaultValue="", required=false) String specification
			,@RequestParam(value="Judgement", defaultValue="0", required=false) String judgement		//double
			,@RequestParam(value="InStock", defaultValue="0", required=false) String inStock		//int
			,@RequestParam(value="Discount", defaultValue="0", required=false) String discount		//double
			,@RequestParam(value="pageInfo", defaultValue="0", required=false) String pageInfo		//double

			,HttpServletRequest request,Locale locale, Model model) {
		
//		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		if (variant.compareTo("Сохранить")==0){
			variant="Save";
		}
		if (variant.compareTo("Обновить")==0){
			variant="Refresh";
		}
		HttpSession session=request.getSession();
		
		if (variant.compareTo("На главную")==0){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
			return Service.isAdminPanel(session,request)+"home";
		}				
		
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		
		
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		String result="home";
//		try {
//			pageInfo=new String(pageInfo.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
		
		if ("Excel".equals(variant)){
			BrakingFluid good = brakingFluidDAO.getBrakingFluid(id_BrakeFluid);
			
			WorkWithExcel.createGoodCard(good, session);
			
			String photo="";
			if (good.hasPhoto()){
				photo=good.getPhoto();
			}
			model.addAttribute("Photo", photo);
			model.addAttribute("photoBackUp", photo);
			
			model.addAttribute("errors", new ArrayList<String>());
			model.addAttribute("currentBrakFluid", good);
			model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
			model.addAttribute("combobox_FluidClasses", fluidClassDAO.getFluidClassis());
			
			result= "InsertUpdate";	
		
		}else if ("New".equals(variant)){
			model.addAttribute("pageInfo", "Введите новую номенклатуру: ");
			model.addAttribute("errors", new ArrayList<String>());
			result = "InsertUpdate";
		}else{
			String fieldName="", fieldManufacturer="", fieldFluidClass="", fieldBoilingTemperatureDry="", fieldBoilingTemperatureWet="",
					fieldValue="", fieldPrice="", fieldPhoto="", fieldDescription="", fieldViscosity40="", fieldViscosity100="", fieldSpecification="", fieldJudgement="";
			//try {
				fieldName=name_BrakeFluid; //new String(name_BrakeFluid.getBytes("iso-8859-1"), "UTF-8");
				fieldManufacturer=manufacturer; // new String(manufacturer.getBytes("iso-8859-1"), "UTF-8");
				fieldFluidClass = fluidClass; // new String(fluidClass.getBytes("iso-8859-1"), "UTF-8");
				fieldBoilingTemperatureDry = boilingTemperatureDry; //new String(boilingTemperatureDry.getBytes("iso-8859-1"), "UTF-8");
				fieldBoilingTemperatureWet = boilingTemperatureWet; //new String(boilingTemperatureWet.getBytes("iso-8859-1"), "UTF-8");
				fieldValue = value; //new String(value.getBytes("iso-8859-1"), "UTF-8");
				fieldPrice = price; //new String(price.getBytes("iso-8859-1"), "UTF-8");
				fieldPhoto = (formPhoto.getOriginalFilename().length()>0?formPhoto.getOriginalFilename():photoBackUp); //new String(photoBackUp.getBytes("iso-8859-1"), "UTF-8"));
				fieldDescription = description; //new String(description.getBytes("iso-8859-1"), "UTF-8"); 
				fieldViscosity40 = Viscosity40; //new String(Viscosity40.getBytes("iso-8859-1"), "UTF-8");
				fieldViscosity100 = Viscosity100; //new String(Viscosity100.getBytes("iso-8859-1"), "UTF-8");
				fieldSpecification = specification; //new String(specification.getBytes("iso-8859-1"), "UTF-8");
				fieldJudgement = judgement; //new String(judgement.getBytes("iso-8859-1"), "UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}			
			
			ArrayList<String> errors=new ArrayList<String>();
			if (fieldName.length()==0){
				errors.add("Поле \"Наименование\" должно быть заполнено");
			}
			if ((request.isUserInRole("ROLE_PRODUCT")) || (request.isUserInRole("ROLE_ADMIN"))){
				try{
					Double param = new Double(fieldBoilingTemperatureDry);
				}catch (NumberFormatException e){
					fieldBoilingTemperatureDry="0.0";
					errors.add("Поле \"Температура кипения (сухая)\" должно быть заполнено правильно");
				};
				try{
					Double param = new Double(fieldBoilingTemperatureWet);
				}catch (NumberFormatException e){
					fieldBoilingTemperatureWet="0.0";
					errors.add("Поле \"Температура кипения (влажная)\" должно быть заполнено правильно");
				};	
				try{
					Double param = new Double(fieldValue);
				}catch (NumberFormatException e){
					fieldValue="0.0";
					errors.add("Поле \"Объём\" должно быть заполнено правильно");
				};				
				try{
					Double param = new Double(fieldViscosity40);
				}catch (NumberFormatException e){
					fieldViscosity40="0.0";
					errors.add("Поле \"Вязкость (40)\" должно быть заполнено правильно");
				};
				try{
					Double param = new Double(fieldViscosity100);
				}catch (NumberFormatException e){
					fieldViscosity100="0.0";
					errors.add("Поле \"Вязкость (100)\" должно быть заполнено правильно");
				};			
				try{
					Double param = new Double(fieldJudgement);
				}catch (NumberFormatException e){
					fieldJudgement="0.0";
					errors.add("Поле \"Оценка пользователей\" должно быть заполнено правильно");
				};
			}else if ((request.isUserInRole("ROLE_PRICE")) || (request.isUserInRole("ROLE_ADMIN"))){
				
				try{
					Double param = new Double(fieldPrice);
				}catch (NumberFormatException e){
					fieldPrice="0.0";
					errors.add("Поле \"Цена\" должно быть заполнено правильно");
				};
			}
			
			BrakingFluid brFluid = new BrakingFluid(id_BrakeFluid,fieldName, fieldManufacturer, fieldFluidClass, new Double(fieldBoilingTemperatureDry)
					,new Double(fieldBoilingTemperatureWet), new Double(fieldValue), new Double(fieldPrice),fieldPhoto,fieldDescription
					,new Double(fieldViscosity40),new Double(fieldViscosity100), fieldSpecification,new Double(fieldJudgement)
					,new Double(discount), new Integer(inStock));
			
			model.addAttribute("pageInfo", pageInfo);
			if (("Refresh".equals(variant)) || (errors.size()>0)){
				if (formPhoto.getOriginalFilename().length()>0){
					brFluid.setPhoto(Service.copyPhoto(WorkWithExcel.convertMultipartFile(formPhoto).getAbsolutePath(), request.getSession().getServletContext().getRealPath("/")));
				}
				String photo="";
				if (brFluid.hasPhoto()){
					photo=brFluid.getPhoto();
				}
				model.addAttribute("Photo", photo);
				model.addAttribute("photoBackUp", photo);
				
				model.addAttribute("errors", errors);
				model.addAttribute("currentBrakFluid", brFluid);
				model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
				model.addAttribute("combobox_FluidClasses", fluidClassDAO.getFluidClassis());
				
				result= "InsertUpdate";
				
			}else if(("Update".equals(variant)) || ("Save".equals(variant))){
				if (request.isUserInRole("ROLE_PRODUCT")){
					Manufacturer currentManufacturer=(Manufacturer)brFluid.getManufacturer();
					FluidClass currentFluidClass=(FluidClass)brFluid.getFluidClass();
					Country country=(Country)currentManufacturer.getCountry();
					if (country.getId()==0){
						country=countryDAO.createCountry(country.getName());
						currentManufacturer.setCountry(country);
					}
					if (currentManufacturer.getId()==0){
						currentManufacturer=manufacturerDAO.createManufacturer(currentManufacturer.getName(),country.getId());
							brFluid.setManufacturer(currentManufacturer);
					}
					if (currentFluidClass.getId()==0){
						currentFluidClass=fluidClassDAO.createFluidClass(currentFluidClass.getName());
						brFluid.setFluidClass(currentFluidClass);
					}	
				
					if (brFluid.getId()==0){
						BrakingFluid currentBrakingFluid = null;
						if (request.isUserInRole("ROLE_PRICE") || request.isUserInRole("ROLE_ADMIN")){
							currentBrakingFluid = brakingFluidDAO.createBrakingFluid(brFluid);
						}else{
							currentBrakingFluid = brakingFluidDAO.createBrakingFluidWithoutPrice(brFluid);
						}
						logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentBrakingFluid, "Создан товар"));
						if (currentBrakingFluid.getPrice()>0){
							Price currentPrice = priceDAO.createPrice(new Price(currentBrakingFluid.getId(),new GregorianCalendar().getTime()
									,brFluid.getPrice(),currentBrakingFluid,user), Service.BRAKING_FLUID_PREFIX);
							logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentPrice
									, "Создана цена для "+currentBrakingFluid.getId()+". "+currentBrakingFluid.getName()));
						}
					}else{

						BrakingFluid currentBrakingFluid = brakingFluidDAO.createBrakingFluidWithoutPrice(brFluid);
						logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentBrakingFluid, "Изменён товар"));
					}
				}else if (request.isUserInRole("ROLE_PRICE")){
					BrakingFluid oldBrakingFluid = brakingFluidDAO.getBrakingFluid(brFluid.getId());
					brakingFluidDAO.fillPrices(brFluid);
					BrakingFluid currentBrakingFluid = brakingFluidDAO.getBrakingFluid(brFluid.getId());
					
					if (oldBrakingFluid.getPrice()!=currentBrakingFluid.getPrice()){
						Price currentPrice = priceDAO.createPrice(new Price(0,new GregorianCalendar().getTime()
								,brFluid.getPrice(),currentBrakingFluid,user), Service.BRAKING_FLUID_PREFIX);
						logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentPrice
								, "Создана цена для "+currentBrakingFluid.getId()+". "+currentBrakingFluid.getName()));
					}

					
				}
				
				result=defaultHome(session, model, user, basket, wishlist, compare, "");
			}
		}
		return Service.isAdminPanel(session,request)+result;
	}			
	
	@RequestMapping(value = { "/login", "/greenCM/login"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String login(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "login";
	}	
	
	@RequestMapping(value = { "/logout", "/adminpanel/logout"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String logout(HttpServletRequest request, HttpServletResponse response, Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null) {
        	logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), user, "Пользователь вышел из системы"));
            (new SecurityContextLogoutHandler()).logout(request, response, auth);
        }		
		
		return "redirect:index";
	}			
	
	@RequestMapping(value = "/menu", method = {RequestMethod.POST, RequestMethod.GET})
	public String menuAdmin(
			@RequestParam(value = "selections", required=false ) int[] manufacturerSelections
			,@RequestParam(value = "task", defaultValue="", required=false) String task
			,@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,HttpServletRequest request
			,Locale locale, Model model) {
		
		String result="home";
				
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);

		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
							
				
		basket.clear();
		if (manufacturerSelections!=null){
			for (int i=0;i<manufacturerSelections.length; i++){
				basket.add(new Basket(brakingFluidDAO.getBrakingFluid(manufacturerSelections[i])));
			}
		}
		session.setAttribute("basket", basket);
				
//		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//			task=new String(task.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
		model.addAttribute("showNew","");
		if (task.compareTo("Сравнить")==0){
			result="Comparison";
		}else if (task.compareTo("На главную")==0){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
		}else if(task.compareTo("В корзину")==0){
			result="Basket";
		}else if(task.compareTo("В начало")==0){
			return "index";																				//---return
		}else if ((task.compareTo("Загрузить номенклатуру")==0) || (task.compareTo("DownloadProduct")==0)){
			model.addAttribute("variantDownload", Service.VARIANT_PRODUCT);
			model.addAttribute("errors", new ArrayList<String>());
			result="Download";
		}else if ((task.compareTo("Загрузить цены")==0) || (task.compareTo("DownloadProduct")==0)){
			model.addAttribute("variantDownload", Service.VARIANT_PRICES);
			model.addAttribute("errors", new ArrayList<String>());
			result="Download";
		}else if ((task.compareTo("Отчеты")==0) || (task.compareTo("Reports")==0)){
			result="Reports";
		}else if(task.compareTo("Коммерческое приложение")==0){
			model.addAttribute("listBrakFluids", basket);
			model.addAttribute("listClients", clientDAO.getClients());
			result="BussinessOffer";
		}else if ((task.compareTo("Новый")==0) || (task.compareTo("Создать новый")==0)){
			model.addAttribute("variant",variant);
			model.addAttribute("showNew","showNew");
			model.addAttribute("pageInfo",task);
			model.addAttribute("id",0);
			model.addAttribute("combobox_countris",countryDAO.getCountries());
			if ((variant.compareTo("user")==0) || (variant.compareTo("Пользователи")==0)){	
				model.addAttribute("roles",roleDAO.getRoles());
				model.addAttribute("currentRoles",new ArrayList<Role>());
			}
			
			result="AddEdit";
			
		}else{
			model.addAttribute("showNew","showNew");
			result=Service.createAdminEdit(model,task, 1, manufacturerDAO,fluidClassDAO,countryDAO,clientDAO,userDAO,logDAO
					, oilStuffDAO, engineTypeDAO, gearBoxTypeDAO, new LinkedList<String>());
		}
		return "adminpanel/"+result;//Service.isAdminPanel(session,request)+result;
	}
		
	 @RequestMapping(value = {"/listDoc","/adminpanel/listDoc"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String listDoc(
			@RequestParam(value = "selections", required=false ) int[] manufacturerSelections
			,@RequestParam(value = "currentPage", defaultValue="1", required=false) int currentPage
			,@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "button", defaultValue="", required=false) String task
			,@RequestParam(value = "dateBeginFilterString" , defaultValue="2015-01-01", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date dateBeginFilter //, defaultValue="", required=false) String dateBeginFilterString
			,@RequestParam(value = "dateEndFilterString" , defaultValue="2015-12-31", required=false)  @DateTimeFormat(pattern="yyyy-MM-dd") Date dateEndFilter // defaultValue="", required=false) String dateEndFilterString
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request
			,Locale locale, Model model, Object ArrayList) {
		
		 HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO); 
		
//		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		if (variant.compareTo("Заявка")==0){
			variant="Demand";
		}
		String result="listDoc";
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInList")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInList");
		}
		
		if (dateBeginFilter==null){
			dateBeginFilter = (Date) session.getAttribute("dateBeginFilter"+variant);
			if (dateBeginFilter==null){
				dateBeginFilter=createDateBeginFilterDemand();
			}
		}
		if (dateEndFilter==null){
			dateEndFilter = (Date) session.getAttribute("dateEndFilter"+variant);
			if (dateEndFilter==null){
				dateEndFilter=createDateEndFilterDemand();
			}

		}
		
		if (!"Demand".equals(variant)){  //если только в шапке
			Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
					, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		}

		elementsInList=(elementsInList==0?Service.ELEMENTS_IN_LIST:elementsInList);
		int totalDoc=Service.showDocInListDoc(variant,task,dateBeginFilter,dateEndFilter,currentPage,elementsInList,user,request,model,
				basket,demandDAO,offerDAO,payDAO, manufacturerDAO,infoDAO,clientDAO);
		if ("Создать".equals(task)){
			result="InsertUpdateDoc";
		}

		model.addAttribute("dateBeginFilterString", Service.getFormattedDate(dateBeginFilter));
		model.addAttribute("dateEndFilterString", Service.getFormattedDate(dateEndFilter));
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("variant", variant);
	
		int totalPages = (int)(totalDoc/elementsInList)+(totalDoc%elementsInList>0?1:0);
		model.addAttribute("totalPages", totalPages);
		 
		model.addAttribute("paginationString_part1", ""+((currentPage-1)*elementsInList+1)+"-"+(((currentPage-1)*elementsInList)+elementsInList));
		model.addAttribute("paginationString_part2", totalDoc);

		model.addAttribute("user", user);
		
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("elementsInListBrF", elementsInList);
		session.setAttribute("dateBeginFilter", dateBeginFilter);
		session.setAttribute("dateEndFilter", dateEndFilter);
		
		return Service.isAdminPanel(session,request)+result;
	}	 
 
	 @RequestMapping(value = {"/InsertUpdateDoc","/adminpanel/InsertUpdateDoc"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String insertUpdateDoc(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "task", defaultValue="", required=false) String task
			,@RequestParam(value = "id", defaultValue="0", required=false) String id
			,@RequestParam(value = "time" , defaultValue="2015-01-01", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date time //,@RequestParam(value = "time", defaultValue="0", required=false) String time
			,@RequestParam(value = "doc_id", defaultValue="", required=false) String doc_id
			,@RequestParam(value = "status_id" , defaultValue="", required=false) String status_id
			,@RequestParam(value = "executer_id" , defaultValue="", required=false) String executer_id
			,@RequestParam(value = "login_name" , defaultValue="", required=false) String login_name
			,@RequestParam(value = "doc_summ" , defaultValue="0", required=false) double doc_summ
			,@RequestParam(value = "client_id" , defaultValue="0", required=false) int client_id
			,@RequestParam(value = "paid" , defaultValue="0", required=false) int paid
			,@RequestParam(value = "shipping" , defaultValue="1", required=false) int shipping
			,@RequestParam(value="pageInfo", defaultValue="0", required=false) String pageInfo		
			,HttpServletRequest request,Locale locale, Model model) {
		
		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//			task=new String(task.getBytes("iso-8859-1"), "UTF-8");
			doc_id=new String(doc_id.getBytes("iso-8859-1"), "UTF-8");
//			pageInfo=new String(pageInfo.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String result="home";
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		
		Date dateBeginFilter=new Date();
		if (session.getAttribute("dateBeginFilter")!=null){
			dateBeginFilter = (Date) session.getAttribute("dateBeginFilter");
		}
		
		Date dateEndFilter=new Date();
		if (session.getAttribute("dateEndFilter")!=null){
			dateEndFilter = (Date) session.getAttribute("dateEndFilter");
		}
 		
		if (task.length()==0){ //если task=="New/Open/Save" не нужно выводить pdf. Будет сформирован документ
			Service.workWithList(new Integer(id), Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare
					, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
					, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		}
		
		GregorianCalendar currentTime = new GregorianCalendar();
		
		model.addAttribute("variant", variant);
		model.addAttribute("statuslist", offerStatusDAO.getStatuses());
		if (task.compareTo("New")==0){
			if ("Demand".equals(variant)){
				model.addAttribute("pageInfo", "Создать новую заявку");
				model.addAttribute("user_name", user.getName());
				model.addAttribute("executer_id", user.getId());
				model.addAttribute("paid", 0);
				model.addAttribute("shipping", 1);
			}else if ("Offer".equals(variant)){
				model.addAttribute("pageInfo", "Создать новое коммерческое предложение");
				model.addAttribute("user_name", user.getName());
				model.addAttribute("executer_id", user.getId());
			}else if ("Pay".equals(variant)){
				model.addAttribute("pageInfo", "Создать новую оплату");
				model.addAttribute("currentManufacturer", manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MARKETING_FIRM))));
				model.addAttribute("user_login", user.getLogin());
				model.addAttribute("user_name", user.getName());
				model.addAttribute("listDemands", new ArrayList<Pay>()); //список заявок, на которые распределилась сумма + предоплаты
			}
			model.addAttribute("id", 0);
			model.addAttribute("time", Service.getFormattedDate(currentTime.getTime()));
			model.addAttribute("doc_id", variant+"_"+Service.getFormattedDate(currentTime.getTime())
				+":"+currentTime.getTime().getHours()+":"+currentTime.getTime().getMinutes()+":"+currentTime.getTime().getSeconds());
			model.addAttribute("currentStatus", 1);
			model.addAttribute("listClients", clientDAO.getClients());
			model.addAttribute("currentClient", Service.ID_EMPTY_CLIENT);
			model.addAttribute("listDoc", basket);
			model.addAttribute("task", "New");
			model.addAttribute("summ", 0);
			result="InsertUpdateDoc";
		}else if (task.compareTo("Open")==0){
			if ("Demand".equals(variant)){
				model.addAttribute("pageInfo", "Заявка");
			}else if ("Offer".equals(variant)){
				model.addAttribute("pageInfo", "Коммерческое предложение");
			}else if ("Pay".equals(variant)){
				model.addAttribute("pageInfo", "Оплата");
			}
			currentTime=new GregorianCalendar();
			model.addAttribute("id", doc_id);
			model.addAttribute("doc_id", doc_id);
			model.addAttribute("task", "Open");
			Service.showDocInInsertUpdateDoc(variant,task,doc_id, doc_summ, currentTime, user,request,model
					,demandDAO,offerDAO,payDAO,manufacturerDAO,infoDAO,clientDAO,userDAO,offerStatusDAO, customerDAO);
			result="InsertUpdateDoc";
		}else if (task.compareTo("home")==0){
			result=defaultHome(session, model, user, basket, wishlist, compare, "");
		}else {
			synchronized (this) {														//сохраняем
				if ("Demand".equals(variant)){
					ArrayList<Demand> listDoc=demandDAO.getDemand(doc_id);
					if (listDoc.size()==0){
						if (basket.size()>0){
							demandDAO.createDemand(doc_id, basket, user, offerStatusDAO.getOfferStatus(new Integer(status_id))
									,userDAO.getUser(executer_id.isEmpty()?Service.ID_EXECUTER:new Integer(executer_id))
									, clientDAO.getClient(client_id), paid, shipping, new Customer());
							logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создана заявка #"+doc_id));
							model.addAttribute("listDoc", basket);
						}						
					}else{
						demandDAO.changeStatus(doc_id, offerStatusDAO.getOfferStatus(new Integer(status_id)));
						demandDAO.changeExecuter(doc_id, userDAO.getUser(new Integer(executer_id)));
					}
					payDAO.clearPaysDemand_id(doc_id);
					Service.spreadDemand(doc_id, infoDAO, logDAO, payDAO,demandDAO, manufacturerDAO);
					model.addAttribute("time",  (listDoc.size()>0?listDoc.get(0).getTime():currentTime.getTime()));
				}else if ("Offer".equals(variant)){
					ArrayList<Offer> listDoc=offerDAO.getOffer(doc_id);
					if (listDoc.size()==0){
						if (basket.size()>0){
							offerDAO.createOffer(doc_id, basket, user);
							logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создано бизнес-предложение #"+doc_id));
						}						
					}
					model.addAttribute("time",  (listDoc.size()>0?listDoc.get(0).getTime():currentTime.getTime()));
					model.addAttribute("listDoc", listDoc);
				}else if ("Pay".equals(variant)){
					ArrayList<Pay> listDoc=payDAO.getPaysByNumDoc(doc_id);
					if (listDoc.size()==0){
						time=new GregorianCalendar().getTime();
					}else{
						time=listDoc.get(0).getTime();
					}
					payDAO.deletePaysWithNumDoc(doc_id);
					Client currentClient=clientDAO.getClient((client_id>0?client_id:Service.ID_EMPTY_CLIENT));
					Service.createPays(doc_id, time, currentClient, doc_summ, user, infoDAO, logDAO, payDAO, demandDAO,manufacturerDAO);
					model.addAttribute("time", time);
				}
			}
			model.addAttribute("id", id);
			model.addAttribute("doc_id", doc_id);
		
			if ((task.compareTo("Save")==0) || (task.compareTo("Сохранить")==0)){		//выводим на страницу
				Service.showDocInInsertUpdateDoc(variant,task,doc_id, doc_summ, currentTime, user,request,model
						,demandDAO,offerDAO,payDAO,manufacturerDAO,infoDAO,clientDAO,userDAO,offerStatusDAO, customerDAO);
				
				result="InsertUpdateDoc";
			}else if ((task.compareTo("SaveAndList")==0) || (task.compareTo("СохранитьКСписку")==0)){
				int elementsInList=Service.ELEMENTS_IN_LIST;
				int totalDoc=Service.showDocInListDoc(variant,task,dateBeginFilter,dateEndFilter,1,elementsInList,user,request,model,
						basket,demandDAO,offerDAO,payDAO, manufacturerDAO,infoDAO,clientDAO);
				result="listDoc";
				model.addAttribute("dateBeginFilterString", Service.getFormattedDate(dateBeginFilter));
				model.addAttribute("dateEndFilterString", Service.getFormattedDate(dateEndFilter));

			}
		}
		
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		session.setAttribute("user", user);
		model.addAttribute("combobox_executers", userDAO.getUsers());  
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("dateBeginFilter", dateBeginFilter);
		session.setAttribute("dateEndFilter", dateEndFilter);

		return Service.isAdminPanel(session,request)+result;
	}	
 
	 @RequestMapping(value = {"/report","/adminpanel/listDoc"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String report(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "button", defaultValue="", required=false) String task
			,@RequestParam(value = "goodPrefix", defaultValue="", required=false) String goodPrefix
			,@RequestParam(value = "dateBeginFilterString" , defaultValue="2015-01-01", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date dateBeginFilter //, defaultValue="", required=false) String dateBeginFilterString
			,@RequestParam(value = "dateEndFilterString" , defaultValue="2015-12-31", required=false)  @DateTimeFormat(pattern="yyyy-MM-dd") Date dateEndFilter // defaultValue="", required=false) String dateEndFilterString
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request, HttpServletResponse response
			,Locale locale, Model model, Object ArrayList) {
		
	 	HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO); 
		
		if (variant.compareTo("Заявка")==0){
			variant="Demand";
		}
		String result="Report";
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInList")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInList");
		}
		
		if (dateBeginFilter==null){
			dateBeginFilter = (Date) session.getAttribute("dateBeginFilter"+variant);
			if (dateBeginFilter==null){
				dateBeginFilter=createDateBeginFilterDemand();
			}
		}
		if (dateEndFilter==null){
			dateEndFilter = (Date) session.getAttribute("dateEndFilter"+variant);
			if (dateEndFilter==null){
				dateEndFilter=createDateEndFilterDemand();
			}

		}
		
		if (!"Demand".equals(variant)){  //если только в шапке
			Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
					, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		}
		
		if ("Pay".equals(variant)){
			ArrayList<Pay> listDoc = payDAO.getPaysForReport(dateBeginFilter,dateEndFilter
					, new Integer(infoDAO.getInfo("marketingFirm")), new Integer(infoDAO.getInfo("mainFirm")));
			model.addAttribute("listDoc", listDoc);
		}
		if (("Список поставщиков".equals(variant))  || ("ManufacturerList".equals(variant))){
			WorkWithExcel.listManufacturersExcel(variant,manufacturerDAO, logDAO,request.getSession(),request, response);
			result="index";
		} else if (("Прайс".equals(variant)) || ("Price".equals(variant))) {
			WorkWithExcel.PriceExcel(goodPrefix,brakingFluidDAO, motorOilDAO, gearBoxOilDAO, logDAO, request.getSession());
			result="index";
		} 
		

		model.addAttribute("dateBeginFilterString", Service.getFormattedDate(dateBeginFilter));
		model.addAttribute("dateEndFilterString", Service.getFormattedDate(dateEndFilter));
	
		model.addAttribute("user", user);
		model.addAttribute("variant", variant);
		
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("dateBeginFilter", dateBeginFilter);
		session.setAttribute("dateEndFilter", dateEndFilter);
		
		return Service.isAdminPanel(session,request)+result;
	}	 
	//------------------------------------------------------------------------------------------------------------Pay Pal
	 public String createPaymentPayPal(HttpServletRequest req, HttpServletResponse resp, User user, LinkedList<Basket>  basket, LogTemplate logDAO
			 ,Model model, int shipping) {
			Payment createdPayment = null;
			APIContext apiContext = null;
			String accessToken = null;
			String returnURL=null;
			String result="PayPal";

			try {
				accessToken = GenerateAccessToken.getAccessToken();
				apiContext = new APIContext(accessToken);
			} catch (PayPalRESTException e) {
				req.setAttribute("error", e.getMessage());
				System.out.println(e.getMessage());
				model.addAttribute("variant", "Error");
				model.addAttribute("errMessage", e.getMessage());
				logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null,e.getMessage()));

			}
			if (req.getParameter("PayerID") != null) {
				Payment payment = new Payment();
				if (req.getParameter("guid") != null) {
					payment.setId(map.get(req.getParameter("guid")));
				}

				PaymentExecution paymentExecution = new PaymentExecution();
				paymentExecution.setPayerId(req.getParameter("PayerID"));
				try {
					createdPayment = payment.execute(apiContext, paymentExecution);
					ResultPrinter.addResult(req, resp, "Executed The Payment", Payment.getLastRequest(), Payment.getLastResponse(), null);
				} catch (PayPalRESTException e) {
					ResultPrinter.addResult(req, resp, "Executed The Payment", Payment.getLastRequest(), null, e.getMessage());
					System.out.println(e.getMessage());
					model.addAttribute("variant", "Error");
					model.addAttribute("errMessage", e.getMessage());
					logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null,e.getMessage()));

				}
				
				GregorianCalendar currentTime = new GregorianCalendar();
				String doc_id="Demand_"+Service.getFormattedDate(currentTime.getTime())
					+":"+currentTime.getTime().getHours()+":"+currentTime.getTime().getMinutes()+":"+currentTime.getTime().getSeconds();
				if (basket.size()>0){
					demandDAO.createDemand(doc_id, basket, user, offerStatusDAO.getOfferStatus(1)
							,userDAO.getUser(Service.ID_EXECUTER), clientDAO.getClient(Service.ID_EMPTY_CLIENT), 1, shipping, new Customer());
					logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создана заявка #"+doc_id));
					model.addAttribute("listDoc", basket);
				}						
				payDAO.clearPaysDemand_id(doc_id);
				Service.spreadDemand(doc_id, infoDAO, logDAO, payDAO,demandDAO, manufacturerDAO);
				
				basket.clear();    //очистили корзину
			} else {

				Details details = new Details();
				details.setShipping("0");
				details.setSubtotal(Service.strCountBasket(basket));
				details.setTax("0");

				Amount amount = new Amount();
				amount.setCurrency("GBP");
				amount.setTotal(Service.strCountBasket(basket));
				amount.setDetails(details);

				Transaction transaction = new Transaction();
				transaction.setAmount(amount);
				transaction.setDescription("This is the payment transaction description.");
				
				List<Item> items = new ArrayList<Item>();
				for (Basket current:basket){
					Item item = new Item();
					DecimalFormat decimalFormat = new DecimalFormat("#.00");
					String strPrice = decimalFormat.format(current.getGood().getPriceWithDiscount());
					item.setName(current.getGood().getName()).setQuantity(""+current.getQauntity())
						.setCurrency("GBP").setPrice(strPrice.replace(",", "."));
					items.add(item);
				}
				ItemList itemList = new ItemList();
				itemList.setItems(items);
				
				transaction.setItemList(itemList);

				List<Transaction> transactions = new ArrayList<Transaction>();
				transactions.add(transaction);

				Payer payer = new Payer();
				payer.setPaymentMethod("paypal");
				
				Payment payment = new Payment();
				payment.setIntent("sale");
				payment.setPayer(payer);
				payment.setTransactions(transactions);

				// ###Redirect URLs
				RedirectUrls redirectUrls = new RedirectUrls();
				String guid = UUID.randomUUID().toString().replaceAll("-", "");
				redirectUrls.setCancelUrl(req.getScheme() + "://"
						+ req.getServerName() + ":" + req.getServerPort()
						+ req.getContextPath() + "/paymentwithpaypal?guid=" + guid);
				redirectUrls.setReturnUrl(req.getScheme() + "://"
						+ req.getServerName() + ":" + req.getServerPort()
						+ req.getContextPath() + "/paymentwithpaypal?guid=" + guid);
				payment.setRedirectUrls(redirectUrls);

				try {
					createdPayment = payment.create(apiContext); 
					logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null
							, "Создана оплата с id = " + createdPayment.getId() + " и статусом = "+ createdPayment.getState()));
					Iterator<Links> links = createdPayment.getLinks().iterator();
					while (links.hasNext()) {
						Links link = links.next();
						if (link.getRel().equalsIgnoreCase("approval_url")) {
							returnURL= link.getHref();
							req.setAttribute("redirectURL", link.getHref());
						} 
					}
					ResultPrinter.addResult(req, resp, "Payment with PayPal", Payment.getLastRequest(), Payment.getLastResponse(), null);
					map.put(guid, createdPayment.getId()); 
				} catch (PayPalRESTException e) {
					ResultPrinter.addResult(req, resp, "Payment with PayPal", Payment.getLastRequest(), null, e.getMessage());
					System.out.println(e.getMessage());
					model.addAttribute("variant", "Error");
					model.addAttribute("errMessage", e.getMessage());
					logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, e.getMessage()));


				}
			}
			if (returnURL!=null){
				try {
					resp.sendRedirect(returnURL);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					model.addAttribute("variant", "Error");
					model.addAttribute("errMessage", e.getMessage());

				}
			}else{
				model.addAttribute("variant", "Confirm");
			}
			
			return result;
		}	


		public String createPaymentCard(String lastName, String firstName, String city, String address, String countryCode, String stateCode
				, String zip, String card, String cardNumber, String cardMonth, String cardYear
				,HttpServletRequest req,HttpServletResponse resp, User user, LinkedList<Basket> basket, LogTemplate logDAO
				,Model model) {
			String returnURL=null;
			String result="PayPal";
			
			Address billingAddress = new Address();
			billingAddress.setCity(city.trim());
			billingAddress.setCountryCode(countryCode.trim());
			billingAddress.setLine1(address.trim());
			billingAddress.setPostalCode(zip.trim());
			billingAddress.setState(stateCode.trim());

			// ###CreditCard
			// A resource representing a credit card that can be
			// used to fund a payment.
			CreditCard creditCard = new CreditCard();
			creditCard.setBillingAddress(billingAddress);
			creditCard.setCvv2(111);
			creditCard.setExpireMonth(new Integer(cardMonth));
			creditCard.setExpireYear(new Integer(cardYear));
			creditCard.setFirstName(firstName);
			creditCard.setLastName(lastName.trim());
			creditCard.setNumber(cardNumber.trim());
			creditCard.setType(card);

			// ###Details
			// Let's you specify details of a payment amount.
			Details details = new Details();
			details.setShipping("0");
			details.setSubtotal(""+Service.strCountBasket(basket));
			details.setTax("0");

			// ###Amount
			// Let's you specify a payment amount.
			Amount amount = new Amount();
			amount.setCurrency("GBP");
			// Total must be equal to sum of shipping, tax and subtotal.
			amount.setTotal(""+Service.strCountBasket(basket));
			amount.setDetails(details);
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setDescription("This is the payment transaction description.");
			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);

			FundingInstrument fundingInstrument = new FundingInstrument();
			fundingInstrument.setCreditCard(creditCard);

			List<FundingInstrument> fundingInstrumentList = new ArrayList<FundingInstrument>();
			fundingInstrumentList.add(fundingInstrument);

			Payer payer = new Payer();
			payer.setFundingInstruments(fundingInstrumentList);
			payer.setPaymentMethod("credit_card");

			Payment payment = new Payment();
			payment.setIntent("sale");
			payment.setPayer(payer);
			payment.setTransactions(transactions);
			Payment createdPayment = null;
			try {
				String accessToken = GenerateAccessToken.getAccessToken();
				APIContext apiContext = new APIContext(accessToken);

				createdPayment = payment.create(apiContext);
				logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null
						, "Создана оплата с id = " + createdPayment.getId() + " и статусом = "+ createdPayment.getState()));
				ResultPrinter.addResult(req, resp, "Payment with Credit Card",
						Payment.getLastRequest(), Payment.getLastResponse(), null);
			} catch (PayPalRESTException e) {
				System.out.println(e.getMessage());
				ResultPrinter.addResult(req, resp, "Payment with Credit Card",
						Payment.getLastRequest(), null, e.getMessage());
				model.addAttribute("variant", "Error");
				model.addAttribute("errMessage", e.getMessage());
				logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null
						, e.getMessage()));

			}
			if (returnURL!=null){
				try {
					resp.sendRedirect(returnURL);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					model.addAttribute("variant", "Error");
					model.addAttribute("errMessage", e.getMessage());

				}
			}
			return result;
			
		}
		
	@RequestMapping(value = "/checkout", method = {RequestMethod.GET, RequestMethod.POST})
	public String checkout(
			@RequestParam(value = "variant", defaultValue="Prepare", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "fullName", defaultValue="", required=false) String fullName
			,@RequestParam(value = "lastName", defaultValue="", required=false) String lastName
			,@RequestParam(value = "companyName", defaultValue="", required=false) String companyName
			
			,@RequestParam(value = "town", defaultValue="", required=false) String town
			,@RequestParam(value = "address", defaultValue="", required=false) String address
			,@RequestParam(value = "countryCode", defaultValue="", required=false) String countryCode
			,@RequestParam(value = "stateCode", defaultValue="", required=false) String stateCode
			,@RequestParam(value = "zip", defaultValue="", required=false) String zip
			,@RequestParam(value = "email", defaultValue="", required=false) String email
			,@RequestParam(value = "phone", defaultValue="", required=false) String phone
			,@RequestParam(value = "shipping", defaultValue="1", required=false) int shipping
			,Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {

		String result="home";
	    HttpSession session = request.getSession(true);
	    
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session
				,Service.countBasket(basket),Service.ID_EMPTY_CLIENT, 0, shipping, new Customer());  
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		if ("Pay".equals(variant)){
			
			Customer customer=new Customer(0, fullName, lastName, companyName, address, town, zip, email, phone);
			customer=customerDAO.createCustomer(customer);
			logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), customer, "Записаны данные покупателя"+customer.getId()));
			
			GregorianCalendar currentTime = new GregorianCalendar();
			String doc_id="Demand_"+Service.getFormattedDate(currentTime.getTime())
				+":"+currentTime.getTime().getHours()+":"+currentTime.getTime().getMinutes()+":"+currentTime.getTime().getSeconds();
			if (basket.size()>0){
				demandDAO.createDemand(doc_id, basket, user, offerStatusDAO.getOfferStatus(1)
						,userDAO.getUser(Service.ID_EXECUTER), clientDAO.getClient(Service.ID_EMPTY_CLIENT), 0, shipping, customer);
				logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создана заявка #"+doc_id));
				model.addAttribute("listDoc", basket);
			}						
			payDAO.clearPaysDemand_id(doc_id);
			Service.spreadDemand(doc_id, infoDAO, logDAO, payDAO,demandDAO, manufacturerDAO);
			
			basket.clear();				
			
		}
		
		return "redirect:"+result;
		
	}
		
		
	@RequestMapping(value = {"/PayPal", "/paymentwithpaypal"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String systemPayPal(
			@RequestParam(value = "variant", defaultValue="New", required=false) String variant
			,@RequestParam(value = "task", defaultValue="", required=false) String task
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "lastName", defaultValue="", required=false) String lastName
			,@RequestParam(value = "firstName", defaultValue="", required=false) String firstName
			,@RequestParam(value = "city", defaultValue="", required=false) String city
			,@RequestParam(value = "address", defaultValue="", required=false) String address
			,@RequestParam(value = "countryCode", defaultValue="", required=false) String countryCode
			,@RequestParam(value = "stateCode", defaultValue="", required=false) String stateCode
			,@RequestParam(value = "zip", defaultValue="", required=false) String zip
			,@RequestParam(value = "card", defaultValue="PayPal", required=false) String card
			,@RequestParam(value = "cardNumber", defaultValue="", required=false) String cardNumber
			,@RequestParam(value = "cardMonth", defaultValue="", required=false) String cardMonth
			,@RequestParam(value = "cardYear", defaultValue="", required=false) String cardYear
			,@RequestParam(value = "shipping", defaultValue="1", required=false) int shipping
			//,@RequestParam(value = "paySumm", defaultValue="0.0", required=false) double paySumm
			,@RequestParam(value = "client_id", defaultValue="0", required=false) int client_id
			
			,Locale locale, Model model, HttpServletRequest request, HttpServletResponse response) {
		
		variant=("PayPal".equals(task)?"Confirm":variant);
		
	    HttpSession session = request.getSession(true);
	    
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session
				,Service.countBasket(basket),client_id, ("Nalik".equals(card)?0:1), shipping, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("shipping", shipping);
		
		session.setAttribute("Payment_Option", card);
		session.setAttribute("Payment_Amount", ""+Service.countBasket(basket));
		
		String result="PayPal";
		
		if ("Nalik".equals(card)) {
			session.setAttribute("wishlist", wishlist);
			session.setAttribute("compare", compare);
			session.setAttribute("shipping", shipping);
			
			model.addAttribute("lastName","Shopper");  					//для тестирования  //удалить потом  -->
			model.addAttribute("fullName","Joe");
			model.addAttribute("town","San Jose");
			model.addAttribute("companyName","USA");
			model.addAttribute("address","1 Main St");
			model.addAttribute("zip","95131");
			model.addAttribute("email","glebas@tut.by");
			model.addAttribute("phone","1234567");  					//для тестирования  //удалить потом  <--			
			
			result = "checkout";
		}else{
			InputStream is = HomeController.class.getResourceAsStream("/sdk_config.properties");
			try { 
				PayPalResource.initConfig(is); 
			}
			catch (PayPalRESTException e) {
				System.out.println(e.getMessage());
				//LOGGER.fatal(e.getMessage()); 
			}				
			
			if (("PayPal".equals(task)) || (task.length()==0)) {
			
			
				if ( "PayPal".equals(card)){
			    	createPaymentPayPal(request, response, user, basket, logDAO, model, shipping);
			    	

				}else if ( "visa".equals(card)){
					model.addAttribute("variant",variant);
					if ( !"New".equals(variant)){
						model.addAttribute("variant","Confirm");
						createPaymentCard(lastName, firstName, city, address, countryCode, stateCode, zip, card, cardNumber, cardMonth, cardYear
							,request, response, user, basket, logDAO, model);
						
						GregorianCalendar currentTime = new GregorianCalendar();
						String doc_id="Demand_"+Service.getFormattedDate(currentTime.getTime())
							+":"+currentTime.getTime().getHours()+":"+currentTime.getTime().getMinutes()+":"+currentTime.getTime().getSeconds();
						if (basket.size()>0){
							demandDAO.createDemand(doc_id, basket, user, offerStatusDAO.getOfferStatus(1)
									,userDAO.getUser(Service.ID_EXECUTER), clientDAO.getClient(Service.ID_EMPTY_CLIENT), ("Nalik".equals(card)?0:1)
									, shipping, new Customer());
							logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создана заявка #"+doc_id));
							model.addAttribute("listDoc", basket);
						}						
						payDAO.clearPaysDemand_id(doc_id);
						Service.spreadDemand(doc_id, infoDAO, logDAO, payDAO,demandDAO, manufacturerDAO);
						
						basket.clear();
					}else{															//для тестирования  //удалить потом  -->			
						model.addAttribute("lastName","Shopper");  
						model.addAttribute("firstName","Joe");
						model.addAttribute("city","San Jose");
						model.addAttribute("address","1 Main St");
						model.addAttribute("countryCode","US");
						model.addAttribute("stateCode","CA");
						model.addAttribute("zip","95131");
						model.addAttribute("cardNumber","4032038024150892");
						model.addAttribute("cardMonth","11");
						model.addAttribute("cardYear","2018");  					//для тестирования  //удалить потом  <--
					}
					 
				}
			}else{
				result=task;
			}
		}
		session.setAttribute("basket", basket);
		
		return Service.isAdminPanel(session,request)+result;
	}
		
	//------------------------------------------------------------------------------------------------------------Pay Pal 


	 @RequestMapping(value = "/{name}", method = RequestMethod.GET)
	 public String viewEdit(@PathVariable("name") final String name, Model model
			,@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "task", defaultValue="", required=false) String task

			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "goodPrefix", defaultValue="BrF", required=false) String goodPrefix
			,@RequestParam(value = "searchField", defaultValue="", required=false) String searchField
			,@RequestParam(value = "searchButton", defaultValue="", required=false) String searchButton
			
			,HttpServletRequest request,HttpServletResponse response) {
		
		String result="home";
		 
		if ("searchGood".equals(name)){
			if (searchField.length()>0) {
				model.addAttribute("searchField",searchField);
			}
			if (Service.MOTOR_OIL_PREFIX.equals(searchButton)){
				result = "redirect:MotorOil";
			}else if (Service.BRAKING_FLUID_PREFIX.equals(searchButton)){
				result = "redirect:home";
			}else if (Service.GEARBOX_OIL_PREFIX.equals(searchButton)){
				result = "redirect:GearBoxOil";
				
			}
		}else{
			HttpSession session=request.getSession();
			User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
			
			LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
			if (basket==null){
				basket=createBasket();
			}
			LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
			if (wishlist==null){
				wishlist=createWishlist();
			}
			if (user.getId()>0){
				wishlist=wishlistDAO.getWishList(user.getId());
			}
			
			LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
			if (compare==null){
				compare=createComparement();
			}
			Service.workWithList(id, goodPrefix, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO, logDAO, clientDAO
					, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
			model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
			
			session.setAttribute("basket", basket);
			session.setAttribute("wishlist", wishlist);
			session.setAttribute("compare", compare);
			
			 if ("Download".equals(name)){  
				 model.addAttribute("variant", variant);
				 model.addAttribute("task", task);
				 result = Service.isAdminPanel(session,request)+"Download";
			 }else{
				 model.addAttribute("errNumber", "404");
				 model.addAttribute("errMessage", "Извините, страница не может быть найдена.");
				
				 result = Service.isAdminPanel(session,request)+"errorPage";
			 }
		}
		return result;

     }
	 
	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception ex
			,@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			, HttpServletRequest request,Locale locale) {

		ModelAndView model = new ModelAndView("errorPage");
		model.addObject("errNumber", "Ошибка");
		model.addObject("errMessage", ex.getMessage());
		String strMessage=ex.getMessage();
		if (strMessage.length()>185){
			strMessage=strMessage.substring(0, 185);
		}

		try{
			Service.sendTheErrorToAdmin(ex.getMessage(),infoDAO.getInfo(Service.ADMIN_EMAIL));
		}catch(Exception e){
			Service.sendTheErrorToAdmin(ex.getMessage(),"phylife@mail.ru");
			
		}
		
		try{
			User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
			logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "!!!Ошибка: "+strMessage));
		}catch(Exception e){
			//---
		}

		return model;

	}
	
	@RequestMapping(value = "/error", method = {RequestMethod.POST, RequestMethod.GET})
	public String errorGlobal(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		model.addAttribute("errNumber", "403");
		model.addAttribute("errMessage", "У Вас нет доступа к этой странице.");
		
		
		return "errorPage";
	}			
	
	@RequestMapping(value = "/error403", method = {RequestMethod.POST, RequestMethod.GET})
	public String error403(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		model.addAttribute("errNumber", "403");
		model.addAttribute("errMessage", "У Вас нет доступа к этой странице.");
		
		
		return "errorPage";
	}		
	
	@RequestMapping(value = "/error404", method = {RequestMethod.POST, RequestMethod.GET})
	public String error404(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<InterfaceGood> compare = (LinkedList<InterfaceGood>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		Service.workWithList(id, Service.BRAKING_FLUID_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		model.addAttribute("errNumber", "404");
		model.addAttribute("errMessage", "Извините, страница не может быть найдена.");
		
		return "errorPage";
	}		
	
	@ModelAttribute("basket")
	public LinkedList<Basket> createBasket(){
		return new LinkedList<Basket>();
	}
	
	@ModelAttribute("compare")
	public LinkedList<InterfaceGood> createComparement(){
		return new LinkedList<InterfaceGood>();
	}	
	
	@ModelAttribute("Wishlist")
	public LinkedList<Wishlist> createWishlist(){
		return new LinkedList<Wishlist>();
	}	
	
	@ModelAttribute("user")
	public User createUser(){
		//return userDAO.getUser("admin", "111");
		return new User();
	}
	
	private String createFilterValue(String param){
		double minPrice=brakingFluidDAO.minData(param);  //если текущая цена в фильтре не задана - возьмём максимум
		int i=new Double(minPrice).intValue();
		double maxPrice=brakingFluidDAO.maxData(param);
		int j=new Double(maxPrice).intValue();
		if (j<maxPrice){
			j++;
		}
		return i+","+j;
	}
	
	@ModelAttribute("currentPriceFilterBrF")
	public String createCurrentPriceFilter(){
		return createFilterValue("price");
	}
	
	@ModelAttribute("currentBoilingTemperatureDryFilterBrF")
	public String createCurrentTemperatureDryFilter(){
		return createFilterValue("boilingTemperatureDry");
	}
	
	@ModelAttribute("currentBoilingTemperatureWetFilterBrF")
	public String createCurrentTemperatureWetFilter(){
		return createFilterValue("boilingTemperatureWet");
	}	
	
	@ModelAttribute("currentValueFilterBrF")
	public String createCurrentValueFilter(){
		double minPrice=brakingFluidDAO.minData("Value")*1000;  //если текущая цена в фильтре не задана - возьмём максимум
		int i=new Double(minPrice).intValue();
		double maxPrice=brakingFluidDAO.maxData("Value")*1000;
		int j=new Double(maxPrice).intValue();
		if (j<maxPrice){
			j++;
		}
		return i+","+j;
	}	
	
	@ModelAttribute("currentViscosity40FilterBrF")
	public String createCurrentViscosity40Filter(){
		return createFilterValue("Viscosity40");
	}			
	
	@ModelAttribute("currentViscosity100FilterBrF")
	public String createCurrentViscosity100Filter(){
		return createFilterValue("Viscosity100");
	}
	
	@ModelAttribute("currentJudgementFilterBrF")
	public String createCurrentJudgementFilter(){
		return createFilterValue("Judgement");
	}		
	
	@ModelAttribute("manufacturersFilterBrF")
	public LinkedList<ManufacturerSelected> createManufacturersFilter(){
		LinkedList<ManufacturerSelected> listManufacturerSelected = new LinkedList<ManufacturerSelected>();
		for (Manufacturer currentManufacturer:manufacturerDAO.getManufacturers(Service.BRAKING_FLUID_PREFIX)){
			ManufacturerSelected man=new ManufacturerSelected();
			man.setId(currentManufacturer.getId());
			man.setName(currentManufacturer.getName());
			man.setCountry(currentManufacturer.getCountry());
			man.setSelected(false);
			listManufacturerSelected.add(man);
		}
		
		return listManufacturerSelected;
	}
	
	@ModelAttribute("fluidClassFilterBrF")
	public LinkedList<FluidClassSelected> createFluidClassFilter(){
		LinkedList<FluidClassSelected> listFluidClassSelected = new LinkedList<FluidClassSelected>();
		for (FluidClass currentFluidClass:fluidClassDAO.getFluidClassis()){
			FluidClassSelected fc=new FluidClassSelected();
			fc.setId(currentFluidClass.getId());
			fc.setName(currentFluidClass.getName());
			fc.setSelected(false);
			listFluidClassSelected.add(fc);
		}
		
		return listFluidClassSelected;
	}		
	
	@ModelAttribute("elementsInListBrF")  //количество элементов, выводимых одновременно в списке. Используется в педжинации
	public int createElementsInList(){
		return Service.ELEMENTS_IN_LIST;
	}		
	
	@ModelAttribute("dateBeginFilterOffer") 
	public Date createDateBeginFilterOffer(){
		GregorianCalendar date=new GregorianCalendar();
		Date result=new Date(date.get(date.YEAR), date.get(date.MONTH), 1);
		
		return result;
	}	
	@ModelAttribute("dateEndFilterOffer") 
	public Date createDateEndFilterOffer(){
		GregorianCalendar date=new GregorianCalendar();
		Date result=new Date(date.get(date.YEAR), date.get(date.MONTH), date.get(date.DAY_OF_MONTH));
		
		return result;
	}
	
	@ModelAttribute("dateBeginFilterDemand") 
	public Date createDateBeginFilterDemand(){
		return createDateBeginFilterOffer();
	}
	
	@ModelAttribute("dateEndFilterDemand") 
	public Date createDateEndFilterDemand(){
		return createDateEndFilterOffer();
	}
	
	@ModelAttribute("adminpanel") 
	public boolean isAdminPanel(){
		return false;
	}
	
	@ModelAttribute("Payment_Amount") 
	public String setSummPayPal(){
		return "0";
	}			

	@ModelAttribute("Payment_Option") 
	public String setPaymentOption(){
		return "PayPal";
	}
/*	
	@ModelAttribute("viscosityFilter")
	public HashMap<String,Boolean> createViscosityFilter(){
		HashMap<String,Boolean> map = new HashMap<String,Boolean>();
		for (String current: motorOilDAO.getMotorOilViscosities()){
			map.put(current,false);
		}
		
		return map;
	}
	
	@ModelAttribute("engineTypeFilter")
	public LinkedList<EngineType> createEngineTypeFilter(){
		LinkedList<EngineType> list = new LinkedList<EngineType>(engineTypeDAO.getEngineTypes());
		
		return list;
	}
	
	@ModelAttribute("oilStuffFilter")
	public LinkedList<OilStuff> createOilStuffFilter(){
		LinkedList<OilStuff> list = new LinkedList<OilStuff>(oilStuffDAO.getOilStuffs());
		
		return list;
	}*/
	
}