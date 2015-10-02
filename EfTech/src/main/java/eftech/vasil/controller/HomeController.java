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
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.ReviewTemplate;
import eftech.workingset.DAO.templates.UserTemplate;
import eftech.workingset.DAO.templates.WishlistTemplate;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.ManufacturerSelected;
import eftech.workingset.beans.Review;

/**
 * Handles requests for the application home page.
 */
@Controller
@SessionAttributes({"user", "basket", "wishlist", "compare", "currentPriceFilter", "manufacturersFilter", "elementsInList"})
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
	
	private Model createHeader(Model model, User user, LinkedList<BrakingFluid>  basket, LinkedList<Wishlist>  wishlist, LinkedList<BrakingFluid>  compare){
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
		for (BrakingFluid brFluid:basket){
			totalBasket+=brFluid.getPrice();
		}
		model.addAttribute("totalBasket", totalBasket);  //ограничить 2 знаками после запятой.
		return model;
	}
	
	private LinkedList<ManufacturerSelected> fillSelectedManufacturers(int[] manufacturerSelections){
		LinkedList<ManufacturerSelected> result=new LinkedList<ManufacturerSelected>();
		
		for (Manufacturer manufacturer:manufacturerDAO.getManufacturers()){
			ManufacturerSelected manSelected=new ManufacturerSelected();
			manSelected.setId(manufacturer.getId());
			manSelected.setName(manufacturer.getName());
			manSelected.setCountry(manufacturer.getCountry());
			if (manufacturerSelections!=null){
				if (manufacturerSelections.length==0){
					manSelected.setSelected(true); //если не было отбора - выберем по всем
				}else{
					for (int j=0;j<manufacturerSelections.length; j++){
						if (manufacturerSelections[j]==manufacturer.getId()){
							manSelected.setSelected(true);
						}
					}
				}
			}else{
				manSelected.setSelected(true); //если не было отбора - выберем по всем
			}
			result.add(manSelected);
		}
		
		return result;
	}
	
	private void creadeBussinessOffer(int id, int clientId, String variant, User user, LinkedList<BrakingFluid>  basket, HttpSession session){
		if (variant.equals("Show")){
			File pdfFile=null;
			try {
				pdfFile=Service.createPDF_BussinessOffer(basket, session.getServletContext().getRealPath("/"), user);
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
	
	private void workWithList(int id, String variant, User user, LinkedList<BrakingFluid>  basket
			, LinkedList<Wishlist>  wishlist, LinkedList<BrakingFluid>  compare, HttpSession session){
	
		if (variant.compareTo("Demand")==0){
			LinkedList<BrakingFluid>  listBrakingFluid = null; 
			if (id==0){ 
				listBrakingFluid = (LinkedList<BrakingFluid>) session.getAttribute("basket");
			}else{
				listBrakingFluid=new LinkedList<BrakingFluid>();
				listBrakingFluid.add(brakingFluidDAO.getBrakingFluid(id));
			} 
		
			try {
				Service.createPDF_Demand(listBrakingFluid, session.getServletContext().getRealPath("/"), user);
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
			for(BrakingFluid current: basket){
				if (current.getId()==id){
					basket.remove(current);
					break;
				}
			}
		}
		if (variant.compareTo("inBasket")==0){
//			boolean bFind=false;                   //в корзине может быть несколько товаров одного вида 
//			for(BrakingFluid current: basket){
//				if (current.getId()==id){
//					bFind=true;
//					break;
//				}
//			}
//			if (!bFind){
				basket.add(brakingFluidDAO.getBrakingFluid(id));
//			}
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
					Wishlist currentWish=wishlistDAO.addToWishlist(new Wishlist(user.getId(), id));
					wishlist.add(currentWish);
				}
			}  
		}
		if (variant.compareTo("deleteFromWishlist")==0){
			for(Wishlist current: wishlist){
				if (((BrakingFluid)current.getBrakingFluid()).getId()==id){
					wishlist.remove(current);
					wishlistDAO.deleteFromWishlist(current);
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
	@RequestMapping(value = {"/","/index",""}, method = {RequestMethod.POST, RequestMethod.GET})
	public String index(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {

		HttpSession session=request.getSession();

		User user=(User) session.getAttribute("user");
		Principal userPrincipal = request.getUserPrincipal();
		if (userPrincipal!=null){
			user=userDAO.getUser(userPrincipal.getName());		//сделал так чтобы выцепить реальное имя пользователя, а не логин
		}else{
			if (user==null){
				user=createUser();
			}
		}
		
		LinkedList<BrakingFluid>  basket =  (LinkedList<BrakingFluid>) session.getAttribute("basket");
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
		double currentPriceFilter =0.0;
		if (session.getAttribute("currentPriceFilter")!=null){
			currentPriceFilter =(Double) session.getAttribute("currentPriceFilter");
		}else{
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInList")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInList");
		}
		
		workWithList(id, variant, user, basket, wishlist, compare, session);
		
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("currentPriceFilter", currentPriceFilter);
		session.setAttribute("manufacturersFilter", manufacturersFilter);
		session.setAttribute("elementsInList", elementsInList);
			
		//model.addAttribute("user", user);
		model.addAttribute("user", user);
		model=createHeader(model, user, basket,wishlist, compare);

		return "index";
	}
	
	@RequestMapping(value = "/home", method = {RequestMethod.POST, RequestMethod.GET})
	public String home(
			@RequestParam(value = "selections", required=false ) int[] manufacturerSelections
			,@RequestParam(value = "currentPage", defaultValue="1", required=false) int currentPage
			,@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request
			,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Principal userPrincipal = request.getUserPrincipal();
		if (userPrincipal!=null){
			user=userDAO.getUser(userPrincipal.getName());		//сделал так чтобы выцепить реальное имя пользователя, а не логин
		}else{
			if (user==null){
				user=createUser();
			}
		}
		
		LinkedList<BrakingFluid>  basket =  (LinkedList<BrakingFluid>) session.getAttribute("basket");
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
		double currentPriceFilter =0.0;
		if (session.getAttribute("currentPriceFilter")!=null){
			currentPriceFilter =(Double) session.getAttribute("currentPriceFilter");
		}else{
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInList")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInList");
		}
 
		workWithList(id, variant, user, basket, wishlist, compare, session);

		elementsInList=(elementsInList==0?Service.ELEMENTS_IN_LIST:elementsInList);
		double currentMinPriceFilter=brakingFluidDAO.minPrice();
		double currentMaxPriceFilter=brakingFluidDAO.maxPrice();
		model.addAttribute("currentMinPriceFilter", currentMinPriceFilter);
		model.addAttribute("currentMaxPriceFilter", currentMaxPriceFilter);
		
		LinkedList<ManufacturerSelected> manufacturersSelected=null;
		if (manufacturerSelections==null){
			if (manufacturersFilter.size()>0){
				manufacturersSelected=manufacturersFilter;
			}else{
				manufacturersSelected=fillSelectedManufacturers(manufacturerSelections); //method
			}
		}else{
			manufacturersSelected=fillSelectedManufacturers(manufacturerSelections); //method
		}

		ArrayList<BrakingFluid> listBakingFluids=brakingFluidDAO.getBrakingFluids(currentPage,elementsInList,currentMinPriceFilter,currentMaxPriceFilter,manufacturerSelections); 
		model.addAttribute("listBrakFluids", listBakingFluids);
		int totalProduct=brakingFluidDAO.getCountRows(currentPage,elementsInList,currentMinPriceFilter,currentMaxPriceFilter,manufacturerSelections);
		int totalPages = (int)(totalProduct/elementsInList)+(totalProduct%elementsInList>0?1:0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		
		model.addAttribute("manufacturersFilter", manufacturersSelected);
		model.addAttribute("recommendedBrakFluids", brakingFluidDAO.getBrakingFluidsRecommended());
		model.addAttribute("currentPriceFilter", (currentPriceFilter==0?currentMaxPriceFilter:currentPriceFilter)); //если текущая цена в фильтре не задана - возьмём максимум
		
		model.addAttribute("paginationString_part1", ""+((currentPage-1)*elementsInList+1)+"-"+(((currentPage-1)*elementsInList)+elementsInList));
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
		
		return "home";
	}
	
	@RequestMapping(value = "/Comparison", method = {RequestMethod.POST, RequestMethod.GET})
	public String compare(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Principal userPrincipal = request.getUserPrincipal();
		if (user==null){
			user=createUser();
		}
		
		LinkedList<BrakingFluid>  basket =  (LinkedList<BrakingFluid>) session.getAttribute("basket");
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
		workWithList(id, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "Comparison";
	}	
	
	@RequestMapping(value = "/Wishlist", method = {RequestMethod.POST, RequestMethod.GET})
	public String wishlist(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Principal userPrincipal = request.getUserPrincipal();
		if (user==null){
			user=createUser();
		}
		
		LinkedList<BrakingFluid>  basket =  (LinkedList<BrakingFluid>) session.getAttribute("basket");
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
		workWithList(id, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "Wishlist";
	}	
	
	
	@RequestMapping(value = "/Basket", method = {RequestMethod.POST, RequestMethod.GET})
	public String basket(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "client", defaultValue="0", required=false) int client
			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Principal userPrincipal = request.getUserPrincipal();
		if (user==null){
			user=createUser();
		}
		
		LinkedList<BrakingFluid>  basket =  (LinkedList<BrakingFluid>) session.getAttribute("basket");
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
		workWithList(id, variant, user, basket, wishlist, compare, session);
		
		creadeBussinessOffer(id, client, variant, user, basket, session);

		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		model.addAttribute("listClients", clientDAO.getClients());
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "Basket";
	}	
	
	
	public ArrayList<String> downloadExcel(String variant, MultipartFile fileEcxel, HttpSession session) {
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
    		    				currentManufacturer.setCountry(country);
    		    			}
    		    			if (currentManufacturer.getId()==0){
    		    				currentManufacturer=manufacturerDAO.createManufacturer(currentManufacturer.getName(),country.getId());
    		    				currentBF.setManufacturer(currentManufacturer);
    		    			}
    		    			if (currentFluidClass.getId()==0){
    		    				currentFluidClass=fluidClassDAO.createFluidClass(currentFluidClass.getName());
    		    				currentBF.setFluidClass(currentFluidClass);
    		    			}		    			
    		        		BrakingFluid value = brakingFluidDAO.createBrakingFluid(currentBF); //breakingFluidDAO			
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
			        	}
					}			
    			}
    		}
		}
		
		return errors;
	}
	
	@RequestMapping(value = "/Download", headers = "content-type=multipart/*", method = {RequestMethod.POST, RequestMethod.GET})
	public String download(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value="fileEcxel", defaultValue="", required=false) MultipartFile fileEcxel
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Principal userPrincipal = request.getUserPrincipal();
		if (user==null){
			user=createUser();
		}
		
		LinkedList<BrakingFluid>  basket =  (LinkedList<BrakingFluid>) session.getAttribute("basket");
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
		workWithList(id, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		ArrayList<String> errors=new ArrayList<String>();
		if (variant!=""){
			errors=downloadExcel(variant,fileEcxel,session);
		}
		
		model.addAttribute("errors", errors);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "Download";
	}		
		
	@RequestMapping(value = "/ShowOne", method = {RequestMethod.POST, RequestMethod.GET})
	public String showOne(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "userLogin", defaultValue="", required=false) String userLogin
			,@RequestParam(value = "userEmail", defaultValue="", required=false) String userEmail
			,@RequestParam(value = "userReviw", defaultValue="", required=false) String userReviw
			,@RequestParam(value = "judgement", defaultValue="0", required=false) double judgement

			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Principal userPrincipal = request.getUserPrincipal();
		if (user==null){
			user=createUser();
		}
		
		LinkedList<BrakingFluid>  basket =  (LinkedList<BrakingFluid>) session.getAttribute("basket");
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
		
		workWithList(id, variant, user, basket, wishlist, compare, session);
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
			
			return "InsertUpdate";
		}else{
			model.addAttribute("currentBrakFluid", brakingFluidDAO.getBrakingFluid(id));
			model.addAttribute("reviews", reviewDAO.getReviews(id));
			
			return "ShowOne";
		}
		
	}	
	
	@RequestMapping(value = "/InsertUpdate", method = {RequestMethod.POST, RequestMethod.GET})
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
		
		System.out.println(judgement);
		
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Principal userPrincipal = request.getUserPrincipal();
		if (user==null){
			user=createUser();
		}
		
		LinkedList<BrakingFluid>  basket =  (LinkedList<BrakingFluid>) session.getAttribute("basket");
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
		
		
		workWithList(id, variant, user, basket, wishlist, compare, session);
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
				double currentPriceFilter =0.0;
				if (session.getAttribute("currentPriceFilter")!=null){
					currentPriceFilter =(Double) session.getAttribute("currentPriceFilter");
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
				brakingFluidDAO.createBrakingFluid(brFluid);
				
				elementsInList=(elementsInList==0?Service.ELEMENTS_IN_LIST:elementsInList);
				double currentMinPriceFilter=brakingFluidDAO.minPrice();
				double currentMaxPriceFilter=brakingFluidDAO.maxPrice();
				model.addAttribute("currentMinPriceFilter", currentMinPriceFilter);
				model.addAttribute("currentMaxPriceFilter", currentMaxPriceFilter);
				
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
				
				
				ArrayList<BrakingFluid> listBakingFluids=brakingFluidDAO.getBrakingFluids(1,elementsInList,currentMinPriceFilter,currentMaxPriceFilter,manufacturerSelections); 
				model.addAttribute("listBrakFluids", listBakingFluids);
				int totalProduct=brakingFluidDAO.getCountRows(1,elementsInList,currentMinPriceFilter,currentMaxPriceFilter,manufacturerSelections);
				int totalPages = (int)(totalProduct/elementsInList)+(totalProduct%elementsInList>0?1:0);
				model.addAttribute("totalPages", totalPages);
				model.addAttribute("currentPage", 1);
				
				model.addAttribute("manufacturersFilter", manufacturersFilter);
				model.addAttribute("recommendedBrakFluids", brakingFluidDAO.getBrakingFluidsRecommended());
				model.addAttribute("currentPriceFilter", (currentPriceFilter==0?currentMaxPriceFilter:currentPriceFilter)); //если текущая цена в фильтре не задана - возьмём максимум
				
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
		return result;
	}			
	
	@RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
	public String login(
			@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,HttpServletRequest request,Locale locale, Model model) {
		
		HttpSession session=request.getSession();
		User user=(User) session.getAttribute("user");
		Principal userPrincipal = request.getUserPrincipal();
		if (user==null){
			user=createUser();
		}
		
		LinkedList<BrakingFluid>  basket =  (LinkedList<BrakingFluid>) session.getAttribute("basket");
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
		workWithList(id, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "login";
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
		User user=(User) session.getAttribute("user");
		Principal userPrincipal = request.getUserPrincipal();
		if (user==null){
			user=createUser();
		}
		
		LinkedList<BrakingFluid>  basket =  (LinkedList<BrakingFluid>) session.getAttribute("basket");
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
		workWithList(id, variant, user, basket, wishlist, compare, session);
		model=createHeader(model, user, basket, wishlist,compare);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		return "error404";
	}		
	
	@ModelAttribute("basket")
	public LinkedList<BrakingFluid> createBasket(){
		return new LinkedList<BrakingFluid>();
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
	
	@ModelAttribute("currentPriceFilter")
	public double createCurrentPriceFilter(){
		return 0.0;
	}
	
	@ModelAttribute("manufacturersFilter")
	public LinkedList<ManufacturerSelected> createManufacturersFilter(){
		return new LinkedList<ManufacturerSelected>();
	}	
	
	@ModelAttribute("elementsInList")  //количество элементов, выводимых одновременно в списке. Используется в педжинации
	public int createElementsInList(){
		return Service.ELEMENTS_IN_LIST;
	}		
	
}