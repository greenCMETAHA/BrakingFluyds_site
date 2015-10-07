package eftech.vasil.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.NoHandlerFoundException;

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
import eftech.workingset.beans.Basket;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.FluidClassSelected;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.ManufacturerSelected;
import eftech.workingset.beans.Review;

/**
 * Handles requests for the application home page.
 */
@Controller
@SessionAttributes({"user", "basket", "wishlist", "compare", "manufacturersFilter", "fluidClassFilter", "elementsInList"
	, "currentPriceFilter" , "currentBoilingTemperatureDryFilter" , "currentBoilingTemperatureWetFilter" , "currentValueFilter" 
	, "currentViscosity40Filter" , "currentViscosity100Filter", "currentJudgementFilter"})
public class HomeController{
	
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
	
	
	LinkedList<FluidClassSelected> globalFluidClassSelected = new LinkedList<FluidClassSelected>();
	LinkedList<ManufacturerSelected> globalManufacturerSelected = new LinkedList<ManufacturerSelected>();

	
	private Model createHeader(Model model, User user, LinkedList<Basket>  basket, LinkedList<Wishlist>  wishlist, LinkedList<BrakingFluid>  compare){
		model.addAttribute("phone", infoDAO.getInfo(Service.PHONE));
		model.addAttribute("email_part1", "");
		model.addAttribute("email_part2", "");
		String email=infoDAO.getInfo(Service.EMAIL).trim();
		if (email.length()>0){
			if (email.indexOf("@")>0){
				model.addAttribute("email_part1", email.substring(0, email.indexOf("@")));
				model.addAttribute("email_part2", email.substring(email.indexOf("@")+1, email.length()));
			}
		}

		wishlist = wishlistDAO.getWishList(user.getId());
		model.addAttribute("wishlist", wishlist);
		double totalBasket=0;	
		for (Basket current:basket){
			BrakingFluid brFluid=current.getBrakingFluid();
			totalBasket+=(brFluid.getPrice()*current.getQauntity());
		}
		model.addAttribute("totalBasket", totalBasket);  //ограничить 2 знаками после запятой.
		return model;
	}
	
	private LinkedList<ManufacturerSelected> fillSelectedManufacturers(int[] manufacturerSelections, LinkedList<ManufacturerSelected> listManufacturerSelected){
		for (ManufacturerSelected manufacturer:globalManufacturerSelected){
			if (manufacturerSelections!=null){
				manufacturer.setSelected(false);
			}
		}
		if (manufacturerSelections!=null){
			for (ManufacturerSelected manufacturer:globalManufacturerSelected){
				if (manufacturerSelections!=null){
					for (int j=0;j<manufacturerSelections.length; j++){
						if (manufacturerSelections[j]==manufacturer.getId()){
							manufacturer.setSelected(true);
							//break;
						}else{
							//manufacturer.setSelected(false);
						}
					}
				}
			}
			return globalManufacturerSelected;
		}else{
			globalManufacturerSelected=listManufacturerSelected;
			return listManufacturerSelected;
		}
	}
	
	private LinkedList<FluidClassSelected> fillSelectedFluidClasses(int[] fluidClassSelections, LinkedList<FluidClassSelected> listFluidClassSelected){
		for (FluidClassSelected fluidClass:globalFluidClassSelected){
			if (fluidClassSelections!=null){
				fluidClass.setSelected(false);
			}
		}		
		if (fluidClassSelections!=null){
			for (FluidClassSelected fluidClass:globalFluidClassSelected){
				if (fluidClassSelections!=null){
					for (int j=0;j<fluidClassSelections.length; j++){
						if (fluidClassSelections[j]==fluidClass.getId()){
							//fluidClass.setSelected(!fluidClass.isSelected());
							//break;
							fluidClass.setSelected(true);
						}else{
							//fluidClass.setSelected(false);
						}
					}
				}
			}
			return globalFluidClassSelected;
		}else{
			globalFluidClassSelected=listFluidClassSelected;
			return listFluidClassSelected;
		}
		
	}	
	
	private void createBussinessOffer(int id, int clientId, String variant, User user, LinkedList<Basket>  basket, HttpSession session){
		if (variant.equals("Show")){
			File pdfFile=null;
			try {
				pdfFile=Service.createPDF_BussinessOffer(basket, session.getServletContext().getRealPath("/"), user);
				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создано бизнес-предложение"));
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
				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создано бизнес-предложение"));
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
					 	props.put("mail.smtp.auth", "true");
					 	props.put("mail.transport.protocol", "smtp");

//						 	props.put("mail.smtp.host", "smtp.beget.ru");
//						 	props.put("mail.smtp.port", "25");
					 	
					 	
					 	props.put("mail.smtp.host", "smtp.mail.ru");
				 	props.put("mail.smtp.port", "465");
//					 	props.put("mail.user", "test@locomotions.ru");
//						 	props.put("mail.password" , "12345678qa");					 	
				        
//						 		props.put("mail.smtp.auth", "true");
					        props.put("mail.smtp.starttls.enable", "true");
//						        props.put("mail.smtp.host", "smtp.gmail.com");
//						        props.put("mail.smtp.port", "587");
//				        
//					        
//					     //   Session session = Session.getInstance(props,null);
					        Session sessionEmail = Session.getInstance(props, new Authenticator() {
					            protected PasswordAuthentication getPasswordAuthentication() {
					                return new PasswordAuthentication("phylife@mail.ru", "cbcmrb2000"); //phylife@mail.ru
					            }
					        });
				 
				        try {
				            Message message = new MimeMessage(sessionEmail);
				            //от кого
				            message.setFrom(new InternetAddress("glebas@tut.by","Васильченко"));
				            //кому
				            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(currentClient.getEmail()));
				            //Заголовок письма
				            message.setSubject("Тестовое письмо");
				            //Содержимое
				            
//					            // Create the message part
//					            BodyPart messageBodyPart = new MimeBodyPart();
//
//					            // Now set the actual message
//					            messageBodyPart.setText("Бизнес предложение (тестовое задание) для "+currentClient.getName());
//
//					            // Create a multipar message
//					            Multipart multipart = new MimeMultipart();
//
//					            // Set text message part
//					            multipart.addBodyPart(messageBodyPart);
//
//					            // Part two is attachment
//					            messageBodyPart = new MimeBodyPart();
//					            DataSource source = new FileDataSource(pdfFile);
//					            messageBodyPart.setDataHandler(new DataHandler(source));
//					            messageBodyPart.setFileName(pdfFile.getName());
//					            multipart.addBodyPart(messageBodyPart);

				            // Send the complete message parts
				            // Session mailSession = Session.getDefaultInstance(props, null);
				            //Transport = mailSession.getTransport();
				            //Отправляем сообщение
				            Transport.send(message);
				            Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime()
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
	
	//isChanging - это для Корзины. если = false - просто устанавливаем quantity. Если нет - смещаем на +/-1. если <=0 - удалим из корзины
	private void workWithList(int id, int quantity, boolean isChanging, String variant, User user, LinkedList<Basket>  basket
			, LinkedList<Wishlist>  wishlist, LinkedList<BrakingFluid>  compare, HttpSession session){  
	
		if (variant.compareTo("Demand")==0){
			LinkedList<Basket>  listBrakingFluid = null; 
			if (id==0){ 
				listBrakingFluid = (LinkedList<Basket>) session.getAttribute("basket");
			}else{
				listBrakingFluid=new LinkedList<Basket>();
				listBrakingFluid.add(new Basket(brakingFluidDAO.getBrakingFluid(id)));
			} 
		
			try {
				Service.createPDF_Demand(listBrakingFluid, session.getServletContext().getRealPath("/"), user);
				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создана заявка"));
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
			
		}
		
		if (variant.compareTo("deleteFromBasket")==0){  
			for(Basket current: basket){
				if (current.getBrakingFluid().getId()==id){
					basket.remove(current);
					break;
				}
			}
		}
		if (variant.compareTo("deleteQuantityFromBasket")==0){  
			for(Basket current: basket){
				if (current.getBrakingFluid().getId()==id){
					current.setQauntity((isChanging?current.getQauntity():0)+quantity);
					if (current.getQauntity()<=0){
						basket.remove(current);
					}
					break;
				}
			}
		}
		if (variant.compareTo("inBasket")==0){
			boolean bFind=false;                   //в корзине может быть несколько товаров одного вида 
			for(Basket current: basket){
				if (current.getBrakingFluid().getId()==id){
					bFind=true;
					current.setQauntity((isChanging?current.getQauntity():0)+quantity);
					if (current.getQauntity()<=0){
						basket.remove(current);
					}
					break;
				}
			}
			if (!bFind){
				basket.add(new Basket(brakingFluidDAO.getBrakingFluid(id),(quantity==0?1:quantity)));
			}
		}
		if (variant.compareTo("checkout")==0){
			basket.clear();  //здесь нужно обработать выдачу кассового чека и формирование закаха на склад 
		}
		if (variant.compareTo("inWishlist")==0){
			if (user.getId()!=0){
				boolean bFind=false;
				for(Wishlist current: wishlist){
					if (((BrakingFluid)current.getBrakingFluid()).getId()==id){
						bFind=true;
						break;
					}
				}
				if (!bFind){
					synchronized (this) {
						Wishlist currentWish=wishlistDAO.addToWishlist(new Wishlist(user.getId(), id));
						Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentWish, "Добавили в избранное"));
						wishlist.add(currentWish);
					}
				}
			}  
		}
		if (variant.compareTo("deleteFromWishlist")==0){
			for(Wishlist current: wishlist){
				if (((BrakingFluid)current.getBrakingFluid()).getId()==id){
					wishlist.remove(current);
					synchronized (this) {
						wishlistDAO.deleteFromWishlist(current);
						Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), wishlist, "Удалили из избранного"));
					}
					break;
				}
			}
		}

		
		if (variant.compareTo("inCompare")==0){
			boolean bFind=false;
			for(BrakingFluid current: compare){
				if (current.getId()==id){
					bFind=true;
					break;
				}
			}
			if (!bFind){
				compare.add(brakingFluidDAO.getBrakingFluid(id));
			}
		}
		if (variant.compareTo("deleteFromCompare")==0){
			for(BrakingFluid current: compare){
				if (current.getId()==id){
					compare.remove(current);
					break;
				}
			}
		}			
	}
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = {"/","/index","/adminpanel/index"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String index(
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
		
		LinkedList<BrakingFluid> compare = (LinkedList<BrakingFluid>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		
		workWithList(id, 0, false, variant, user, basket, wishlist, compare, session);
		
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
			
		//model.addAttribute("user", user);
		model.addAttribute("user", user);
		model=createHeader(model, user, basket,wishlist, compare);

		return "index";
	}
	
	@RequestMapping(value = {"/home","/adminpanel/home"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String home(
			@RequestParam(value = "selections", required=false ) int[] manufacturerSelections
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
			,HttpServletRequest request
			,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		try {
			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		
		LinkedList<BrakingFluid> compare = (LinkedList<BrakingFluid>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		LinkedList<ManufacturerSelected>  manufacturersFilter = (LinkedList<ManufacturerSelected>) session.getAttribute("manufacturersFilter");
		if (manufacturersFilter==null){
			manufacturersFilter = createManufacturersFilter();
		}
		LinkedList<FluidClassSelected>  fluidClassFilter = (LinkedList<FluidClassSelected>) session.getAttribute("fluidClassFilter");
		if (fluidClassFilter==null){
			fluidClassFilter = createFluidClassFilter();
		}
		if (currentPriceFilter.toString().equals("0,0")){
			if (session.getAttribute("currentPriceFilter")!=null){
				currentPriceFilter =(String) session.getAttribute("currentPriceFilter");
			}
		}
		if (currentBoilingTemperatureDryFilter.toString().equals("0,0")){
			if (session.getAttribute("currentBoilingTemperatureDryFilter")!=null){
				currentBoilingTemperatureDryFilter =(String) session.getAttribute("currentBoilingTemperatureDryFilter");
			}
		}
		if (currentBoilingTemperatureWetFilter.toString().equals("0,0")){
			if (session.getAttribute("currentBoilingTemperatureWetFilter")!=null){
				currentBoilingTemperatureWetFilter =(String) session.getAttribute("currentBoilingTemperatureWetFilter");
			}
		}
		if (currentValueFilter.toString().equals("0,0")){	
			if (session.getAttribute("currentValueFilter")!=null){
				currentValueFilter =(String) session.getAttribute("currentValueFilter");
			}
		}
		if (currentViscosity40Filter.toString().equals("0,0")){
			if (session.getAttribute("currentViscosity40Filter")!=null){
				currentViscosity40Filter =(String) session.getAttribute("currentViscosity40Filter");
			}
		}
		if (currentViscosity100Filter.toString().equals("0,0")){
			if (session.getAttribute("currentViscosity100Filter")!=null){
				currentViscosity100Filter =(String) session.getAttribute("currentViscosity100Filter");
			}
		}
		if (currentJudgementFilter.toString().equals("0,0")){
			if (session.getAttribute("currentJudgementFilter")!=null){
				currentJudgementFilter =(String) session.getAttribute("currentJudgementFilter");
			}
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInList")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInList");
		}
 
		workWithList(id, 0, false, variant, user, basket, wishlist, compare, session);

		elementsInList=(elementsInList==0?Service.ELEMENTS_IN_LIST:elementsInList);
	
		//--1
		int minPrice=new Double(brakingFluidDAO.minData("Price")).intValue();
		int currentMinPriceFilter = new Double(minPrice).intValue();
		double maxPriceDouble=brakingFluidDAO.maxData("Price");
		int maxPrice=new Double(maxPriceDouble).intValue();
		if (maxPrice<maxPriceDouble){
			maxPrice++;
		}
		int currentMaxPriceFilter=maxPrice;
		
		currentMinPriceFilter = Math.max(new Integer(currentPriceFilter.substring(0, currentPriceFilter.indexOf(","))), minPrice);
		int oldValue=new Integer(currentPriceFilter.substring(currentPriceFilter.indexOf(",")+1, currentPriceFilter.length()));
		currentMaxPriceFilter = Math.min((oldValue==0?maxPrice:currentMaxPriceFilter),maxPrice);  //нужно взять минимум из верхнего интервала. Но может быть 0, его надо убрать.
			
		currentPriceFilter=currentMinPriceFilter+","+currentMaxPriceFilter;

		model.addAttribute("MinPrice", minPrice);
		model.addAttribute("MaxPrice", maxPrice);	
		model.addAttribute("currentMinPriceFilter", currentMinPriceFilter);
		model.addAttribute("currentMaxPriceFilter", currentMaxPriceFilter);		

			
		session.setAttribute("currentPriceFilter", currentPriceFilter);
		model.addAttribute("currentPriceFilter",currentPriceFilter);


		//--2
		int minBoilingTemperatureDry=new Double(brakingFluidDAO.minData("BoilingTemperatureDry")).intValue();
		int currentMinBoilingTemperatureDryFilter = new Double(minBoilingTemperatureDry).intValue();
		double maxBoilingTemperatureDryDouble=brakingFluidDAO.maxData("BoilingTemperatureDry");
		int maxBoilingTemperatureDry=new Double(maxBoilingTemperatureDryDouble).intValue();
		if (maxBoilingTemperatureDry<maxBoilingTemperatureDryDouble){
			maxBoilingTemperatureDry++;
		}
		int currentMaxBoilingTemperatureDryFilter=maxBoilingTemperatureDry;
		
		currentMinBoilingTemperatureDryFilter = Math.max(new Integer(currentBoilingTemperatureDryFilter.substring(0, currentBoilingTemperatureDryFilter.indexOf(","))), minBoilingTemperatureDry);
		oldValue=new Integer(currentBoilingTemperatureDryFilter.substring(currentBoilingTemperatureDryFilter.indexOf(",")+1, currentBoilingTemperatureDryFilter.length()));
		currentMaxBoilingTemperatureDryFilter = Math.min((oldValue==0?maxBoilingTemperatureDry:currentMaxBoilingTemperatureDryFilter),maxBoilingTemperatureDry);
		
		model.addAttribute("MinBoilingTemperatureDry", minBoilingTemperatureDry);
		model.addAttribute("MaxBoilingTemperatureDry", maxBoilingTemperatureDry);
		model.addAttribute("currentMinBoilingTemperatureDryFilter", currentMinBoilingTemperatureDryFilter);
		model.addAttribute("currentMaxBoilingTemperatureDryFilter", currentMaxBoilingTemperatureDryFilter);		
		
		
		currentBoilingTemperatureDryFilter=currentMinBoilingTemperatureDryFilter+","+currentMaxBoilingTemperatureDryFilter;
		session.setAttribute("currentBoilingTemperatureDryFilter", currentBoilingTemperatureDryFilter);
		model.addAttribute("currentBoilingTemperatureDryFilter",currentBoilingTemperatureDryFilter);
		
		//--3
		int minBoilingTemperatureWet=new Double(brakingFluidDAO.minData("BoilingTemperatureWet")).intValue();
		int currentMinBoilingTemperatureWetFilter = new Double(minBoilingTemperatureWet).intValue();
		double maxBoilingTemperatureWetDouble=brakingFluidDAO.maxData("BoilingTemperatureWet");
		int maxBoilingTemperatureWet=new Double(maxBoilingTemperatureWetDouble).intValue();
		if (maxBoilingTemperatureWet<maxBoilingTemperatureWetDouble){
			maxBoilingTemperatureWet++;
		}
		int currentMaxBoilingTemperatureWetFilter=maxBoilingTemperatureWet;
		
		currentMinBoilingTemperatureWetFilter = Math.max(new Integer(currentBoilingTemperatureWetFilter.substring(0, currentBoilingTemperatureWetFilter.indexOf(",")))
				, minBoilingTemperatureWet);
		oldValue=new Integer(currentBoilingTemperatureWetFilter.substring(currentBoilingTemperatureWetFilter.indexOf(",")+1, currentBoilingTemperatureWetFilter.length()));
		currentMaxBoilingTemperatureWetFilter = Math.min((oldValue==0?maxBoilingTemperatureWet:currentMaxBoilingTemperatureWetFilter),maxBoilingTemperatureWet);
		model.addAttribute("MinBoilingTemperatureWet", minBoilingTemperatureWet);
		model.addAttribute("MaxBoilingTemperatureWet", maxBoilingTemperatureWet);
		model.addAttribute("currentMinBoilingTemperatureWetFilter", currentMinBoilingTemperatureWetFilter);
		model.addAttribute("currentMaxBoilingTemperatureWetFilter", currentMaxBoilingTemperatureWetFilter);		
		
			
		currentBoilingTemperatureWetFilter=currentMinBoilingTemperatureWetFilter+","+currentMaxBoilingTemperatureWetFilter;
		session.setAttribute("currentBoilingTemperatureWetFilter", currentBoilingTemperatureWetFilter);	
		model.addAttribute("currentBoilingTemperatureWetFilter",currentBoilingTemperatureWetFilter);
						
		//--4
		int minValue=new Double(brakingFluidDAO.minData("Value")*1000).intValue();
		int currentMinValueFilter = new Double(minValue).intValue();
		double maxValueDouble=brakingFluidDAO.maxData("Value")*1000;
		int maxValue=new Double(maxValueDouble).intValue();
		if (maxValue<maxValueDouble){
			maxValue++;
		}
		int currentMaxValueFilter=maxValue;
		
		currentMinValueFilter = Math.max(new Integer(currentValueFilter.substring(0, currentValueFilter.indexOf(","))), minValue);
		oldValue=new Integer(currentValueFilter.substring(currentValueFilter.indexOf(",")+1, currentValueFilter.length()));
		currentMaxValueFilter = Math.min((oldValue==0?maxValue:currentMaxValueFilter),maxValue);
		model.addAttribute("MinValue", minValue);
		model.addAttribute("MaxValue", maxValue);		
		model.addAttribute("currentMinValueFilter", currentMinValueFilter);
		model.addAttribute("currentMaxValueFilter", currentMaxValueFilter);		
			
		currentValueFilter=currentMinValueFilter+","+currentMaxValueFilter;
		session.setAttribute("currentValueFilter", currentValueFilter);	
		model.addAttribute("currentValueFilter",currentValueFilter);
		
		//--5
		int minViscosity40=new Double(brakingFluidDAO.minData("Viscosity40")).intValue();
		int currentMinViscosity40Filter = new Double(minViscosity40).intValue();
		double maxViscosity40Double=brakingFluidDAO.maxData("Viscosity40");
		int maxViscosity40=new Double(maxViscosity40Double).intValue();
		if (maxViscosity40<maxViscosity40Double){
			maxViscosity40++;
		}
		int currentMaxViscosity40Filter=maxViscosity40;
		
		currentMinViscosity40Filter = Math.max(new Integer(currentViscosity40Filter.substring(0, currentViscosity40Filter.indexOf(","))), minViscosity40);
		oldValue=new Integer(currentViscosity40Filter.substring(currentViscosity40Filter.indexOf(",")+1, currentViscosity40Filter.length()));
		currentMaxViscosity40Filter = Math.min((oldValue==0?maxViscosity40:currentMaxViscosity40Filter),maxViscosity40);
		model.addAttribute("MinViscosity40", minViscosity40);
		model.addAttribute("MaxViscosity40", maxViscosity40);	
		model.addAttribute("currentMinViscosity40Filter", currentMinViscosity40Filter);
		model.addAttribute("currentMaxViscosity40Filter", currentMaxViscosity40Filter);		
		
			
		currentViscosity40Filter=currentMinViscosity40Filter+","+currentMaxViscosity40Filter;
		session.setAttribute("currentViscosity40Filter", currentViscosity40Filter);	
		model.addAttribute("currentViscosity40Filter",currentViscosity40Filter);
		
		//--6
		int minViscosity100=new Double(brakingFluidDAO.minData("Viscosity100")).intValue();
		int currentMinViscosity100Filter = new Double(minViscosity100).intValue();
		double maxViscosity100Double=brakingFluidDAO.maxData("Viscosity100");
		int maxViscosity100=new Double(maxViscosity100Double).intValue();
		if (maxViscosity100<maxViscosity100Double){
			maxViscosity100++;
		}
		int currentMaxViscosity100Filter=maxViscosity100;
		
		currentMinViscosity100Filter = Math.max(new Integer(currentViscosity100Filter.substring(0, currentViscosity100Filter.indexOf(","))), minViscosity100);
		oldValue=new Integer(currentViscosity100Filter.substring(currentViscosity100Filter.indexOf(",")+1, currentViscosity100Filter.length()));
		currentMaxViscosity100Filter = Math.min((oldValue==0?maxViscosity100:currentMaxViscosity100Filter),maxViscosity100);
		model.addAttribute("MinViscosity100", minViscosity100);
		model.addAttribute("MaxViscosity100", maxViscosity100);	
		model.addAttribute("currentMinViscosity100Filter", currentMinViscosity100Filter);
		model.addAttribute("currentMaxViscosity100Filter", currentMaxViscosity100Filter);		
		
			
		currentViscosity100Filter=currentMinViscosity100Filter+","+currentMaxViscosity100Filter;
		session.setAttribute("currentViscosity100Filter", currentViscosity100Filter);
		model.addAttribute("currentViscosity100Filter",currentViscosity100Filter);

		//--7
		int minJudgement=new Double(brakingFluidDAO.minData("Judgement")).intValue();  //пока отключим. Бо не получается позиционировать на [0:5], вываливает на [100:0]
		int currentMinJudgementFilter = new Double(minJudgement).intValue();
		double maxJudgementDouble=brakingFluidDAO.maxData("Judgement");
		int maxJudgement=new Double(maxJudgementDouble).intValue();
		if (maxJudgement<maxJudgementDouble){
			maxJudgement++;
		}
		int currentMaxJudgementFilter=maxJudgement;
		
		currentMinJudgementFilter = Math.max(new Integer(currentJudgementFilter.substring(0, currentJudgementFilter.indexOf(","))), minJudgement);
		oldValue=new Integer(currentJudgementFilter.substring(currentJudgementFilter.indexOf(",")+1, currentJudgementFilter.length()));
		currentMaxJudgementFilter = Math.min((oldValue==0?maxJudgement:currentMaxJudgementFilter),maxJudgement);
		model.addAttribute("MinJudgement", minJudgement);
		model.addAttribute("MaxJudgement", maxJudgement);
		model.addAttribute("currentMinJudgementFilter", currentMinJudgementFilter);
		model.addAttribute("currentMaxJudgementFilter", currentMaxJudgementFilter);		
			
		currentJudgementFilter=currentMinJudgementFilter+","+currentMaxJudgementFilter;
		session.setAttribute("currentJudgementFilter", currentJudgementFilter);
		model.addAttribute("currentJudgementFilter",currentJudgementFilter);
		
		manufacturersFilter=fillSelectedManufacturers(manufacturerSelections,manufacturersFilter); //method
		fluidClassFilter=fillSelectedFluidClasses(fluidClassselections,fluidClassFilter); //method
//		System.out.println(manufacturerSelections);
//		System.out.println(fluidClassselections);
	
		ArrayList<BrakingFluid> listBakingFluids=brakingFluidDAO.getBrakingFluids(currentPage,elementsInList,manufacturersFilter,fluidClassFilter
				,currentMinPriceFilter,currentMaxPriceFilter
				,currentMinBoilingTemperatureDryFilter,currentMaxBoilingTemperatureDryFilter
				,currentMinBoilingTemperatureWetFilter,currentMaxBoilingTemperatureWetFilter
				,currentMinValueFilter/1000,currentMaxValueFilter/1000
				,currentMinViscosity40Filter,currentMaxViscosity40Filter
				,currentMinViscosity100Filter,currentMaxViscosity100Filter
				,currentMinJudgementFilter,currentMaxJudgementFilter); 
		

		
		model.addAttribute("listBrakFluids", listBakingFluids);
		int totalProduct=brakingFluidDAO.getCountRows(currentPage,elementsInList,manufacturersFilter,fluidClassFilter
				,currentMinPriceFilter,currentMaxPriceFilter
				,currentMinBoilingTemperatureDryFilter,currentMaxBoilingTemperatureDryFilter
				,currentMinBoilingTemperatureWetFilter,currentMaxBoilingTemperatureWetFilter
				,currentMinValueFilter/1000,currentMaxValueFilter/1000
				,currentMinViscosity40Filter,currentMaxViscosity40Filter
				,currentMinViscosity100Filter,currentMaxViscosity100Filter
				,currentMinJudgementFilter,currentMaxJudgementFilter);
		int totalPages = (int)(totalProduct/elementsInList)+(totalProduct%elementsInList>0?1:0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		
		model.addAttribute("manufacturersFilter", manufacturersFilter);
		model.addAttribute("fluidClassFilter", fluidClassFilter);
		model.addAttribute("recommendedBrakFluids", brakingFluidDAO.getBrakingFluidsRecommended());
		model.addAttribute("currentPriceFilter", currentMinPriceFilter+","+currentMaxPriceFilter); 
		 
		model.addAttribute("paginationString_part1", ""+((currentPage-1)*elementsInList+1)+"-"+(((currentPage-1)*elementsInList)+elementsInList));
		model.addAttribute("paginationString_part2", totalProduct);

		model.addAttribute("user", user);
		
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("manufacturersFilter", manufacturersFilter);
		session.setAttribute("fluidClassFilter", fluidClassFilter);
		session.setAttribute("elementsInList", elementsInList);
		
		return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"home";
	}
	
	@RequestMapping(value = {"/Comparison","/adminpanel/Comparison"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String compare(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "selections", required=false ) int[] fluidsSelection
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {

		
		try {
			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HttpSession session=request.getSession();
		User user=Service.getUser(request.getUserPrincipal(), logDAO, userDAO);
		
		LinkedList<Basket>  basket =  (LinkedList<Basket>) session.getAttribute("basket");
		if (basket==null){
			basket=createBasket();
		}
		
		if (variant.compareTo("Сравнить")==0){
			if (fluidsSelection!=null){
				basket.clear();
				for (int i=0;i<fluidsSelection.length; i++){
					basket.add(new Basket(brakingFluidDAO.getBrakingFluid(fluidsSelection[i])));
				}
				session.setAttribute("basket", basket);
			}
		
			return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"Comparison";
		}
		if (variant.compareTo("На главную")==0){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
			return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"home";
		}
		
		
		LinkedList<Wishlist>  wishlist =  (LinkedList<Wishlist>) session.getAttribute("wishlist");
		if (wishlist==null){
			wishlist=createWishlist();
		}
		if (user.getId()>0){
			wishlist=wishlistDAO.getWishList(user.getId());
		}
		
		LinkedList<BrakingFluid> compare = (LinkedList<BrakingFluid>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		workWithList(id, 0, false, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"Comparison";
	}	
	
	@RequestMapping(value = "/Wishlist", method = {RequestMethod.POST, RequestMethod.GET})
	public String wishlist(
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
		
		LinkedList<BrakingFluid> compare = (LinkedList<BrakingFluid>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		workWithList(id, 0, false, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "Wishlist";
	}	
	
	
	@RequestMapping(value = {"/Basket","/adminpanel/Basket"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String basket(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "client", defaultValue="0", required=false) int client
			,@RequestParam(value = "quantity", defaultValue="0", required=false) int quantity
			,HttpServletRequest request,Locale locale, Model model) {

		try {
			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (variant.compareTo("Заявка")==0){
			return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"makeDemand";
		}
		if (variant.compareTo("На главную")==0){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
			return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"home";
		}		
		
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
		
		LinkedList<BrakingFluid> compare = (LinkedList<BrakingFluid>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		workWithList(id, quantity, true, variant, user, basket, wishlist, compare, session);
		
		createBussinessOffer(id, client, variant, user, basket, session);

		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		model.addAttribute("listClients", clientDAO.getClients());
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"Basket";
	}	
	
	
	public ArrayList<String> downloadExcel(String variant,User user, MultipartFile fileEcxel, HttpSession session) {
		ArrayList<String> errors=new ArrayList<String>();
		
		 if ("Product".equals(variant)){
			String productFile=fileEcxel.getOriginalFilename().trim();
			if (!Service.isFileExist(productFile)){
				errors.add("Указанный Вами файл с номенклатурой не существует");
    		}else{
    			if (!productFile.contains(".xlsx")){
    				errors.add("Указанный Вами файл с номенклатурой не соответствует формату. Используйте Excel-файл с расширением *.xlsx");
    			}else{
    				ArrayList<BrakingFluid> listBrakingFluids = Service.importFromExcelProduct(Service.convertMultipartFile(fileEcxel)
    							,session.getServletContext().getRealPath("/"));
    				
    			    synchronized (listBrakingFluids){
    		        	for (BrakingFluid currentBF:listBrakingFluids){
    		        		if (!Service.isFileExist(currentBF.getPhoto())){
    		        			errors.add("Указанный Вами файл c изображением не существует");
    		        		}
    		        		currentBF.setPhoto(Service.copyPhoto(currentBF.getPhoto(), session.getServletContext().getRealPath("/")));
    		        		Manufacturer currentManufacturer=(Manufacturer)currentBF.getManufacturer();
    		    			FluidClass currentFluidClass=(FluidClass)currentBF.getFluidClass();
    		    			Country country=(Country)currentManufacturer.getCountry();
    		    			if (country.getId()==0){
    		    				country=countryDAO.createCountry(country.getName());
    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),country, "Загрузили из Excel"));
    		    				currentManufacturer.setCountry(country);
    		    			}
    		    			if (currentManufacturer.getId()==0){
    		    				currentManufacturer=manufacturerDAO.createManufacturer(currentManufacturer.getName(),country.getId());
    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentManufacturer, "Загрузили из Excel"));
    		    				currentBF.setManufacturer(currentManufacturer);
    		    			}
    		    			if (currentFluidClass.getId()==0){
    		    				currentFluidClass=fluidClassDAO.createFluidClass(currentFluidClass.getName());
    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentFluidClass, "Загрузили из Excel"));
    		    				currentBF.setFluidClass(currentFluidClass);
    		    			}		    			
    		        		BrakingFluid value = brakingFluidDAO.createBrakingFluid(currentBF); //breakingFluidDAO		
    		        		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),value, "Загрузили из Excel"));
		    			}
		    		}
					
	        	}
			}
		}else if ("Price".equals(variant)){
			String priceFile=fileEcxel.getOriginalFilename().trim();
			if (!Service.isFileExist(priceFile)){
				errors.add("Указанный Вами файл с ценами не существует");
    		}else{
    			if (!priceFile.contains(".xlsx")){
    				errors.add("Указанный Вами файл с ценами не соответствует формату. Используйте Excel-файл с расширением *.xlsx");
    			}else{
					ArrayList<BrakingFluid> listBrakingFluids = Service.importFromExcelPrices(Service.convertMultipartFile(fileEcxel));
		 	        synchronized (listBrakingFluids){
			         	for (BrakingFluid currentBF:listBrakingFluids){
			        		BrakingFluid value = brakingFluidDAO.fillPrices(currentBF);
			        		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), value, "Загрузили из Excel, изменение цен"));
			        	}
					}			
    			}
    		}
		}
		
		return errors;
	}
	
	@RequestMapping(value = {"/Download","/adminpanel/Download"}, headers = "content-type=multipart/*", method = {RequestMethod.POST, RequestMethod.GET})
	public String download(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value="fileEcxel", defaultValue="", required=false) MultipartFile fileExcel
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {

		try {
			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (variant.compareTo("На главную")==0){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
			return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"home";
		}
		if (variant.compareTo("Загрузка")==0){
			variant="Download";
			return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"home";
		}
		
		
		
		
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
		
		LinkedList<BrakingFluid> compare = (LinkedList<BrakingFluid>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		workWithList(id, 0, false, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		ArrayList<String> errors=new ArrayList<String>();
		if (variant!=""){
			errors=downloadExcel(variant,user,fileExcel,session);
		}
		
		model.addAttribute("errors", errors);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"Download";
	}		
		
	@RequestMapping(value = {"/ShowOne","/adminpanel/ShowOne"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String showOne(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
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
		
		LinkedList<BrakingFluid> compare = (LinkedList<BrakingFluid>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		
		if ((userLogin.trim().isEmpty()) || (userEmail.trim().isEmpty())){ 
		}else{
			try {
				userLogin=new String(userLogin.getBytes("iso-8859-1"), "UTF-8");
				userEmail=new String(userEmail.getBytes("iso-8859-1"), "UTF-8");
				userReviw= new String(userReviw.getBytes("iso-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			Review review = new Review();
			review.setEmail(userEmail);
			review.setName(userLogin);
			review.setJudgement(judgement);
			review.setReview(userReviw);
			BrakingFluid brFluid=new BrakingFluid();
			brFluid.setId(id);
			review.setBrakingFluid(brFluid);
			reviewDAO.createReview(review);
		}		
		
		workWithList(id, 0, false, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
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
			
			return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"InsertUpdate";
		}else{
			model.addAttribute("currentBrakFluid", brakingFluidDAO.getBrakingFluid(id));
			model.addAttribute("reviews", reviewDAO.getReviews(id));
			
			return "ShowOne";
		}
		
	}	
	
	@RequestMapping(value = {"/InsertUpdate","/adminpanel/InsertUpdate"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String insertUpdate(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "id_BrakeFluid" , defaultValue="", required=false) int id_BrakeFluid
			,@RequestParam(value = "name_BrakeFluid", defaultValue="", required=false) String name_BrakeFluid
			,@RequestParam("Manufacturer") String manufacturer
			,@RequestParam("FluidClass") String fluidClass
			,@RequestParam("BoilingTemperatureDry") String boilingTemperatureDry  //double
			,@RequestParam("BoilingTemperatureWet") String boilingTemperatureWet  //double
			,@RequestParam("Value") String value	//double
			,@RequestParam("Price") String price	//double
			,@RequestParam("Photo") MultipartFile formPhoto
			,@RequestParam(value="photoBackUp", defaultValue="", required=false) String photoBackUp   //в MultipartFile невозможно получить значение, оказывается. Старое сохраним в элементе формы
			,@RequestParam("Description") String description
			,@RequestParam("Viscosity40") String Viscosity40	//double
			,@RequestParam("Viscosity100") String Viscosity100	//double
			,@RequestParam("Specification") String specification
			,@RequestParam(value="Judgement", defaultValue="0", required=false) String judgement		//double
			,@RequestParam(value="pageInfo", defaultValue="0", required=false) String pageInfo		//double

			,HttpServletRequest request,Locale locale, Model model) {
		
		try {
			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (variant.compareTo("Сохранить")==0){
			variant="Save";
		}
		if (variant.compareTo("Обновить")==0){
			variant="Refresh";
		}
		if (variant.compareTo("На главную")==0){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
			return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+"home";
		}				
		
		System.out.println(judgement);
		
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
		
		LinkedList<BrakingFluid> compare = (LinkedList<BrakingFluid>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		
		
		workWithList(id, 0, false, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		String result="home";
		try {
			pageInfo=new String(pageInfo.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		if ("New".equals(variant)){
			model.addAttribute("pageInfo", "Введите новую номенклатуру: ");
			model.addAttribute("errors", new ArrayList<String>());
			result = "InsertUpdate";
		}else{
			String fieldName="", fieldManufacturer="", fieldFluidClass="", fieldBoilingTemperatureDry="", fieldBoilingTemperatureWet="",
					fieldValue="", fieldPrice="", fieldPhoto="", fieldDescription="", fieldViscosity40="", fieldViscosity100="", fieldSpecification="", fieldJudgement="";
			try {
				fieldName=new String(name_BrakeFluid.getBytes("iso-8859-1"), "UTF-8");
				fieldManufacturer= new String(manufacturer.getBytes("iso-8859-1"), "UTF-8");
				fieldFluidClass = new String(fluidClass.getBytes("iso-8859-1"), "UTF-8");
				fieldBoilingTemperatureDry = new String(boilingTemperatureDry.getBytes("iso-8859-1"), "UTF-8");
				fieldBoilingTemperatureWet = new String(boilingTemperatureWet.getBytes("iso-8859-1"), "UTF-8");
				fieldValue = new String(value.getBytes("iso-8859-1"), "UTF-8");
				fieldPrice = new String(price.getBytes("iso-8859-1"), "UTF-8");
				fieldPhoto = (formPhoto.getOriginalFilename().length()>0?formPhoto.getOriginalFilename():new String(photoBackUp.getBytes("iso-8859-1"), "UTF-8"));
				fieldDescription = new String(description.getBytes("iso-8859-1"), "UTF-8"); 
				fieldViscosity40 = new String(Viscosity40.getBytes("iso-8859-1"), "UTF-8");
				fieldViscosity100 = new String(Viscosity100.getBytes("iso-8859-1"), "UTF-8");
				fieldSpecification = new String(specification.getBytes("iso-8859-1"), "UTF-8");
				fieldJudgement = new String(judgement.getBytes("iso-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			ArrayList<String> errors=new ArrayList<String>();
			if (fieldName.length()==0){
				errors.add("Поле \"Наименование\" должно быть заполнено");
			}
			{
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
					Double param = new Double(fieldPrice);
				}catch (NumberFormatException e){
					fieldPrice="0.0";
					errors.add("Поле \"Цена\" должно быть заполнено правильно");
				};
				try{
					Double param = new Double(fieldViscosity40);
				}catch (NumberFormatException e){
					fieldViscosity40="0.0";
					errors.add("Поле \"Вязкозть (40)\" должно быть заполнено правильно");
				};
				try{
					Double param = new Double(fieldViscosity100);
				}catch (NumberFormatException e){
					fieldViscosity100="0.0";
					errors.add("Поле \"Вязкозть (100)\" должно быть заполнено правильно");
				};			
				try{
					Double param = new Double(fieldJudgement);
				}catch (NumberFormatException e){
					fieldJudgement="0.0";
					errors.add("Поле \"Оценка пользователей\" должно быть заполнено правильно");
				};
			}
			
			BrakingFluid brFluid = new BrakingFluid(id_BrakeFluid,fieldName, fieldManufacturer, fieldFluidClass, new Double(fieldBoilingTemperatureDry)
					,new Double(fieldBoilingTemperatureWet), new Double(fieldValue), new Double(fieldPrice),fieldPhoto,fieldDescription
					,new Double(fieldViscosity40),new Double(fieldViscosity100), fieldSpecification,new Double(fieldJudgement));
			
			model.addAttribute("pageInfo", pageInfo);
			if (("Refresh".equals(variant)) || (errors.size()>0)){
				if (formPhoto.getOriginalFilename().length()>0){
					brFluid.setPhoto(Service.copyPhoto(Service.convertMultipartFile(formPhoto).getAbsolutePath(), request.getSession().getServletContext().getRealPath("/")));
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
				LinkedList<ManufacturerSelected>  manufacturersFilter = (LinkedList<ManufacturerSelected>) session.getAttribute("manufacturersFilter");
				if (manufacturersFilter==null){
					manufacturersFilter = createManufacturersFilter();
				}
				LinkedList<FluidClassSelected>  fluidClassFilter = (LinkedList<FluidClassSelected>) session.getAttribute("fluidClassFilter");
				if (fluidClassFilter==null){
					fluidClassFilter = createFluidClassFilter();
				}
				String currentPriceFilter = createCurrentPriceFilter();
				if (session.getAttribute("currentPriceFilter")!=null){
					currentPriceFilter =(String) session.getAttribute("currentPriceFilter");
				}else{
				}
				int elementsInList = Service.ELEMENTS_IN_LIST;
				if (session.getAttribute("elementsInList")!=null){
					elementsInList = (Integer) session.getAttribute("elementsInList");
				}
				
				
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
				BrakingFluid currentBrakingFluid = brakingFluidDAO.createBrakingFluid(brFluid);
  
				
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
						,currentMinJudgementFilter,currentMaxJudgementFilter); 
 
				model.addAttribute("listBrakFluids", listBakingFluids);
				int totalProduct=brakingFluidDAO.getCountRows(1,elementsInList
						,manufacturersFilter, fluidClassFilter
						,currentMinPriceFilter,currentMaxPriceFilter
						,currentMinBoilingTemperatureDryFilter,currentMaxBoilingTemperatureDryFilter
						,currentMinBoilingTemperatureWetFilter,currentMaxBoilingTemperatureWetFilter
						,currentMinValueFilter/1000,currentMaxValueFilter/1000
						,currentMinViscosity40Filter,currentMaxViscosity40Filter
						,currentMinViscosity100Filter,currentMaxViscosity100Filter
						,currentMinJudgementFilter,currentMaxJudgementFilter); 

				int totalPages = (int)(totalProduct/elementsInList)+(totalProduct%elementsInList>0?1:0);
				model.addAttribute("totalPages", totalPages);
				model.addAttribute("currentPage", 1);
				
				model.addAttribute("manufacturersFilter", manufacturersFilter);
				model.addAttribute("recommendedBrakFluids", brakingFluidDAO.getBrakingFluidsRecommended());
				model.addAttribute("currentPriceFilter", currentPriceFilter);
				
				model.addAttribute("paginationString_part1", ""+(1)+"-"+elementsInList);
				model.addAttribute("paginationString_part2", totalProduct);

				model.addAttribute("user", user);
				
				model=createHeader(model, user, basket, wishlist,compare);		 //method
				session.setAttribute("user", user);
				session.setAttribute("basket", basket);
				session.setAttribute("wishlist", wishlist);
				session.setAttribute("compare", compare);
				session.setAttribute("currentPriceFilter", currentPriceFilter);
				session.setAttribute("manufacturersFilter", manufacturersFilter);
				session.setAttribute("elementsInList", elementsInList);
				
				
				result="home";
			}
		}
		return (request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"")+result;
	}			
	
	@RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
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
		
		LinkedList<BrakingFluid> compare = (LinkedList<BrakingFluid>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		workWithList(id, 0, false, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "login";
	}		
	
	@RequestMapping(value = "/menu", method = {RequestMethod.POST, RequestMethod.GET})
	public String menuAdmin(
			@RequestParam(value = "selections", required=false ) int[] manufacturerSelections
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
				
		try {
			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		if (variant.compareTo("Сравнить")==0){
			result="adminpanel/Comparison";
		}else if(variant.compareTo("В корзину")==0){
			result="adminpanel/Basket";
		}else if (variant.compareTo("Загрузить номенклатуру")==0){
			model.addAttribute("variantDownload", Service.VARIANT_PRODUCT);
			model.addAttribute("errors", new ArrayList<String>());
			result="adminpanel/Download";
		}else if (variant.compareTo("Загрузить цены")==0){
			model.addAttribute("variantDownload", Service.VARIANT_PRICES);
			model.addAttribute("errors", new ArrayList<String>());
			result="adminpanel/Download";
		}else if(variant.compareTo("Коммерческое приложение")==0){
			model.addAttribute("listBrakFluids", basket);
			model.addAttribute("listClients", clientDAO.getClients());
			result="adminpanel/BussinessOffer";
		}else{
			result=Service.createAdminEdit(model,variant, 1, manufacturerDAO,fluidClassDAO,countryDAO,clientDAO,userDAO,logDAO, new LinkedList<String>());
		}
		return result;
	}
		
	 @RequestMapping(value = "/{name}", method = RequestMethod.GET)
	 public String viewEdit(@PathVariable("name") final String name, Model model) {
		 if ("Download".equals(name)){  
			 return "Download";
		 }
          return "error404";
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
		
		LinkedList<BrakingFluid> compare = (LinkedList<BrakingFluid>) session.getAttribute("compare");
		if (compare==null){
			compare=createComparement();
		}
		workWithList(id, 0, false, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "error404";
	}		
	
	@ModelAttribute("basket")
	public LinkedList<Basket> createBasket(){
		return new LinkedList<Basket>();
	}
	
	@ModelAttribute("compare")
	public LinkedList<BrakingFluid> createComparement(){
		return new LinkedList<BrakingFluid>();
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
	
	@ModelAttribute("currentPriceFilter")
	public String createCurrentPriceFilter(){
		return createFilterValue("price");
	}
	
	@ModelAttribute("currentBoilingTemperatureDryFilter")
	public String createCurrentTemperatureDryFilter(){
		return createFilterValue("boilingTemperatureDry");
	}
	
	@ModelAttribute("currentBoilingTemperatureWetFilter")
	public String createCurrentTemperatureWetFilter(){
		return createFilterValue("boilingTemperatureWet");
	}	
	
	@ModelAttribute("currentValueFilter")
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
	
	@ModelAttribute("currentViscosity40Filter")
	public String createCurrentViscosity40Filter(){
		return createFilterValue("Viscosity40");
	}			
	
	@ModelAttribute("currentViscosity100Filter")
	public String createCurrentViscosity100Filter(){
		return createFilterValue("Viscosity100");
	}
	
	@ModelAttribute("currentJudgementFilter")
	public String createCurrentJudgementFilter(){
		return createFilterValue("Judgement");
	}		
	
	@ModelAttribute("manufacturersFilter")
	public LinkedList<ManufacturerSelected> createManufacturersFilter(){
		LinkedList<ManufacturerSelected> listManufacturerSelected = new LinkedList<ManufacturerSelected>();
		globalManufacturerSelected.clear();
		for (Manufacturer currentManufacturer:manufacturerDAO.getManufacturers()){
			ManufacturerSelected man=new ManufacturerSelected();
			man.setId(currentManufacturer.getId());
			man.setName(currentManufacturer.getName());
			man.setCountry(currentManufacturer.getCountry());
			man.setSelected(false);
			listManufacturerSelected.add(man);
			man=new ManufacturerSelected();
			man.setId(currentManufacturer.getId());
			man.setName(currentManufacturer.getName());
			man.setCountry(currentManufacturer.getCountry());
			man.setSelected(false);
			globalManufacturerSelected.add(man);
		}
		
		return listManufacturerSelected;
	}
	
	@ModelAttribute("fluidClassFilter")
	public LinkedList<FluidClassSelected> createFluidClassFilter(){
		LinkedList<FluidClassSelected> listFluidClassSelected = new LinkedList<FluidClassSelected>();
		globalFluidClassSelected.clear();
		for (FluidClass currentFluidClass:fluidClassDAO.getFluidClassis()){
			FluidClassSelected fc=new FluidClassSelected();
			fc.setId(currentFluidClass.getId());
			fc.setName(currentFluidClass.getName());
			fc.setSelected(false);
			listFluidClassSelected.add(fc);
			fc=new FluidClassSelected();
			fc.setId(currentFluidClass.getId());
			fc.setName(currentFluidClass.getName());
			fc.setSelected(false);			
			globalFluidClassSelected.add(fc);
		}
		
		return listFluidClassSelected;
	}		
	
	@ModelAttribute("elementsInList")  //количество элементов, выводимых одновременно в списке. Используется в педжинации
	public int createElementsInList(){
		return Service.ELEMENTS_IN_LIST;
	}		
	
}