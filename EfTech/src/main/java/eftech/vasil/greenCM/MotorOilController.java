package eftech.vasil.greenCM;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import eftech.workingset.DAO.templates.BrakingFluidTemplate;
import eftech.workingset.DAO.templates.ClientTemplate;
import eftech.workingset.DAO.templates.CountryTemplate;
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
import eftech.workingset.Services.Service;
import eftech.workingset.beans.Basket;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.EngineType;
import eftech.workingset.beans.GearBoxOil;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.ManufacturerSelected;
import eftech.workingset.beans.MotorOil;
import eftech.workingset.beans.OilStuff;
import eftech.workingset.beans.Price;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

@Controller
@SessionAttributes({"user", "adminpanel", "basket", "wishlist", "compare", "manufacturersFilterOil", "engineTypeFilterOil", "oilStuffFilterOil"
	, "viscosityFilterOil", "elementsInListOil", "currentPriceFilterOil" , "currentValueFilterOil", "currentJudgementFilterOil"})
public class MotorOilController {
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
	
	private LinkedList<ManufacturerSelected> fillSelectedManufacturers(int[] selections, LinkedList<ManufacturerSelected> list){
		for (ManufacturerSelected current:list){
			current.setSelected(false);
			if (selections!=null){
				for (int j=0;j<selections.length; j++){
					if (selections[j]==current.getId()){
						current.setSelected(true);
						break;
					}
				}
			}
		}
		return list;
	}
	
	private LinkedList<OilStuff> fillSelectedOilStuff(int[] selections, LinkedList<OilStuff> list){
		for (OilStuff current:list){
			current.setSelected(false);
			if (selections!=null){
				for (int j=0;j<selections.length; j++){
					if (selections[j]==current.getId()){
						current.setSelected(true);
						break;
					}
				}
			}
		}
		return list;
	}
	
	private LinkedList<EngineType> fillSelectedEngineType(int[] selections, LinkedList<EngineType> list){
		for (EngineType current:list){
			current.setSelected(false);
			if (selections!=null){
				for (int j=0;j<selections.length; j++){
					if (selections[j]==current.getId()){
						current.setSelected(true);
						break;
					}
				}
			}
		}
		return list;
	}
	
	private HashMap<String,Boolean> fillSelectedViscosity(int[] selections, HashMap<String,Boolean> list){
		int numValue=0;
		for(Entry<String,Boolean> current : list.entrySet()){
			boolean bFind=false;
			if (selections!=null){
				for (int i=0;i<selections.length;i++){
					//if (current.getKey().equals(selections[i])){
					if (numValue==selections[i]){
						bFind=true;
						break;
					}
					
				}
			}
			numValue+=1;
			current.setValue(bFind);
		}
		
		return list;
	}		

	@RequestMapping(value = {"/motorOil","/adminpanel/motorOil"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String _home(
			@RequestParam(value = "good", defaultValue="", required=false) String good
			
			,@RequestParam(value = "selections", required=false ) int[] manufacturerSelections  //base (BrakingFluids)
			,@RequestParam(value = "viscositySelections", required=false ) int[] viscositySelections
			,@RequestParam(value = "engineTypeSelections", required=false ) int[] engineTypeSelections
			,@RequestParam(value = "oilStuffSelections", required=false ) int[] oilStuffSelections
			,@RequestParam(value = "currentPage", defaultValue="1", required=false) int currentPage
			,@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "currentPriceFilter", defaultValue="0,0", required=false) String currentPriceFilter
			,@RequestParam(value = "currentValueFilter", defaultValue="0,0", required=false) String currentValueFilter
			,@RequestParam(value = "currentJudgementFilter", defaultValue="0,0", required=false) String currentJudgementFilter
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "adminpanel", defaultValue="false", required=false) boolean adminpanel
			,@RequestParam(value = "searchField", defaultValue="", required=false) String searchField
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
		LinkedList<ManufacturerSelected>  manufacturersFilter = (LinkedList<ManufacturerSelected>) session.getAttribute("manufacturersFilterOil");
		if (manufacturersFilter==null){
			manufacturersFilter = createManufacturersFilter();
		}

		LinkedList<EngineType>  engineTypeFilter = (LinkedList<EngineType>) session.getAttribute("engineTypeFilterOil");
		if (engineTypeFilter==null){
			engineTypeFilter = createEngineTypeFilter();
		}

		LinkedList<OilStuff>  oilStuffFilter = (LinkedList<OilStuff>) session.getAttribute("oilStuffFilterOil");
		if (oilStuffFilter==null){
			oilStuffFilter = createOilStuffFilter();
		}
		
		HashMap<String,Boolean> viscosityFilter = (HashMap<String,Boolean>) session.getAttribute("viscosityFilterOil");
		if (viscosityFilter==null){
			viscosityFilter = createViscosityFilter(); 
		}
		
		if (currentPriceFilter.toString().equals("0,0")){
			if (session.getAttribute("currentPriceFilterOil")!=null){
				currentPriceFilter =(String) session.getAttribute("currentPriceFilterOil");
			}
		}
		if (currentValueFilter.toString().equals("0,0")){	
			if (session.getAttribute("currentValueFilterOil")!=null){
				currentValueFilter =(String) session.getAttribute("currentValueFilterOil");
			}
		}
		if (currentJudgementFilter.toString().equals("0,0")){
			if (session.getAttribute("currentJudgementFilterOil")!=null){
				currentJudgementFilter =(String) session.getAttribute("currentJudgementFilterOil");
			}
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInListOil")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInListOil");
		}
 
		Service.workWithList(id, Service.MOTOR_OIL_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0, 0, 0, 1);

		elementsInList=(elementsInList==0?Service.ELEMENTS_IN_LIST:elementsInList);
	
		//--1
		int minPrice=new Double(motorOilDAO.minData("Price")).intValue();
		int currentMinPriceFilter = new Double(minPrice).intValue();
		double maxPriceDouble=motorOilDAO.maxData("Price");
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
		int minValue=new Double(motorOilDAO.minData("Value")*1000).intValue();
		int currentMinValueFilter = new Double(minValue).intValue();
		double maxValueDouble=motorOilDAO.maxData("Value")*1000;
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
		
		//--3
		int minJudgement=new Double(motorOilDAO.minData("Judgement")).intValue();  //пока отключим. Бо не получается позиционировать на [0:5], вываливает на [100:0]
		int currentMinJudgementFilter = new Double(minJudgement).intValue();
		double maxJudgementDouble=motorOilDAO.maxData("Judgement");
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
		engineTypeFilter=fillSelectedEngineType(engineTypeSelections,engineTypeFilter); //method
		oilStuffFilter=fillSelectedOilStuff(oilStuffSelections,oilStuffFilter); //method
		viscosityFilter=fillSelectedViscosity(viscositySelections, viscosityFilter); //method
//		System.out.println(manufacturerSelections);
//		System.out.println(fluidClassselections);
	
		ArrayList<MotorOil> list=motorOilDAO.getMotorOils(currentPage,elementsInList,manufacturersFilter,engineTypeFilter
				,oilStuffFilter,viscosityFilter,currentMinPriceFilter,currentMaxPriceFilter
				,currentMinValueFilter/1000,currentMaxValueFilter/1000
				,currentMinJudgementFilter,currentMaxJudgementFilter, searchField); 
		
		model.addAttribute("listMotorOils", list);
		int totalProduct=motorOilDAO.getCountRows(currentPage,elementsInList,manufacturersFilter,engineTypeFilter
				,oilStuffFilter,viscosityFilter,currentMinPriceFilter,currentMaxPriceFilter
				,currentMinValueFilter/1000,currentMaxValueFilter/1000
				,currentMinJudgementFilter,currentMaxJudgementFilter, searchField);
		int totalPages = (int)(totalProduct/elementsInList)+(totalProduct%elementsInList>0?1:0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		
		model.addAttribute("manufacturersFilter", manufacturersFilter);
		model.addAttribute("engineTypeFilter", engineTypeFilter);
		model.addAttribute("oilStuffFilter", oilStuffFilter);
		model.addAttribute("viscosityFilter", viscosityFilter);
		model.addAttribute("recommended", motorOilDAO.getMotorOilsRecommended());
		model.addAttribute("currentPriceFilter", currentMinPriceFilter+","+currentMaxPriceFilter); 
		 
		model.addAttribute("paginationString_part1", ""+((currentPage-1)*elementsInList+1)+"-"+(((currentPage-1)*elementsInList)+elementsInList));
		model.addAttribute("paginationString_part2", totalProduct);

		model.addAttribute("user", user);
		
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("manufacturersFilter", manufacturersFilter);
		session.setAttribute("engineTypeFilter", engineTypeFilter);
		session.setAttribute("oilStuffFilter", oilStuffFilter);
		session.setAttribute("viscosityFilter", viscosityFilter);
		session.setAttribute("elementsInList", elementsInList);
		
		//return Service.isAdminPanel(session,request)+"home";
		return "MotorOilhome";
	}
	
	@RequestMapping(value = {"/ShowOneMotorOil","/adminpanel/ShowOneMotorOil"}, method = {RequestMethod.POST, RequestMethod.GET})
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
			MotorOil current=new MotorOil();
			current.setId(id);
			review.setGood(current);
			reviewDAO.createReview(review,Service.MOTOR_OIL_PREFIX);
		}		
		
		Service.workWithList(id, Service.MOTOR_OIL_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1);
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		if ((request.isUserInRole("ROLE_ADMIN")) || (request.isUserInRole("ROLE_PRODUCT"))
				|| (request.isUserInRole("ROLE_PRICE"))){
			
			if (variant.compareTo("insert")==0){
				model.addAttribute("pageInfo", "Введите новую номенклатуру: ");
				model.addAttribute("currentMotorOil", new MotorOil());
			}else{
				MotorOil current = motorOilDAO.getMotorOil(id);
				model.addAttribute("pageInfo", "Отредактируйте номенклатуру:");
				model.addAttribute("currentMotorOil", current);
				String photo="";
				if (current.hasPhoto()){
					photo=current.getPhoto();
				}
				model.addAttribute("Photo", photo);
				model.addAttribute("photoBackUp", photo);
			}
			model.addAttribute("buttonInto", "Сохранить");
			model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
			model.addAttribute("combobox_EngineType", engineTypeDAO.getEngineTypes());
			model.addAttribute("combobox_OilStuff", oilStuffDAO.getOilStuffs());
			model.addAttribute("errors", new ArrayList<String>());
			model.addAttribute("prices", priceDAO.getPrices(id, Service.MOTOR_OIL_PREFIX));
			
			//return Service.isAdminPanel(session,request)+"InsertUpdate";
			return "MotorOilInsertUpdate";
		}else{
			model.addAttribute("currentMotorOil", motorOilDAO.getMotorOil(id));
			model.addAttribute("reviews", reviewDAO.getReviews(id,Service.MOTOR_OIL_PREFIX));
			model.addAttribute("prices", priceDAO.getPrices(id, Service.MOTOR_OIL_PREFIX));
			
			return "MotorOilShowOne";
		}
		
	}
	
	public String defaultHome(HttpSession session, Model model, User user, LinkedList<Basket> basket
			, LinkedList<Wishlist> wishlist, LinkedList<InterfaceGood> compare, String searchField){
	
		LinkedList<ManufacturerSelected>  manufacturersFilter = (LinkedList<ManufacturerSelected>) session.getAttribute("manufacturersFilterOil");
		if (manufacturersFilter==null){
			manufacturersFilter = createManufacturersFilter();
		}
		LinkedList<EngineType>  engineTypeFilter = (LinkedList<EngineType>) session.getAttribute("engineTypeFilterOil");
		if (engineTypeFilter==null){
			engineTypeFilter = createEngineTypeFilter();
		}

		LinkedList<OilStuff>  oilStuffFilter = (LinkedList<OilStuff>) session.getAttribute("oilStuffFilterOil");
		if (oilStuffFilter==null){
			oilStuffFilter = createOilStuffFilter();
		}
		
		HashMap<String,Boolean> viscosityFilter = (HashMap<String,Boolean>) session.getAttribute("viscosityFilterOil");
		if (viscosityFilter==null){
			viscosityFilter = createViscosityFilter();
		}

		String currentPriceFilter = createCurrentPriceFilter();
		if (session.getAttribute("currentPriceFilterOil")!=null){
			currentPriceFilter =(String) session.getAttribute("currentPriceFilterOil");
		}else{
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInListOil")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInListOil");
		}
		
		elementsInList=(elementsInList==0?Service.ELEMENTS_IN_LIST:elementsInList);
		double currentMinPriceFilter=motorOilDAO.minData("Price");
		double currentMaxPriceFilter=motorOilDAO.maxData("Price");
		model.addAttribute("MinPrice", currentMinPriceFilter);
		model.addAttribute("MaxPrice", currentMaxPriceFilter);
		model.addAttribute("currentMinPriceFilter", currentMinPriceFilter);
		model.addAttribute("currentMaxPriceFilter", currentMaxPriceFilter);		

		double currentMinValueFilter=motorOilDAO.minData("Value")*1000;
		double currentMaxValueFilter=motorOilDAO.maxData("Value")*1000;
		model.addAttribute("MinValue", currentMinValueFilter);
		model.addAttribute("MaxValue", currentMaxValueFilter);
		model.addAttribute("currentMinValueFilter", currentMinValueFilter);
		model.addAttribute("currentMaxValueFilter", currentMaxValueFilter);		
		
		double currentMinJudgementFilter=motorOilDAO.minData("Judgement");
		double currentMaxJudgementFilter=motorOilDAO.maxData("Judgement");
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
		for (EngineType current:engineTypeFilter){
			if (current.isSelected()){
				count++;
			}
		}
		
		int engineTyprSelections[]=new int[count];
		count=0;
		for (EngineType current:engineTypeFilter){
			if (current.isSelected()){
				manufacturerSelections[count]=current.getId();
				count++;
			}
		}															//восстановим существующий фильтр по  классу жидкости  <--
		
		count=0;												//восстановим существующий фильтр по классу жидкости -->
		for (OilStuff current:oilStuffFilter){
			if (current.isSelected()){
				count++;
			}
		}
		
		int oilStuffSelections[]=new int[count];		count=0;
		for (OilStuff current:oilStuffFilter){
			if (current.isSelected()){
				oilStuffSelections[count]=current.getId();
				count++;
			}
		}															//восстановим существующий фильтр по  классу жидкости  <--
	
		ArrayList<MotorOil> list=motorOilDAO.getMotorOils(1,elementsInList,manufacturersFilter,engineTypeFilter
				,oilStuffFilter,viscosityFilter,currentMinPriceFilter,currentMaxPriceFilter
				,currentMinValueFilter/1000,currentMaxValueFilter/1000
				,currentMinJudgementFilter,currentMaxJudgementFilter, searchField); 
		
		model.addAttribute("listMotorOils", list);
		int totalProduct=motorOilDAO.getCountRows(1,elementsInList,manufacturersFilter,engineTypeFilter
				,oilStuffFilter,viscosityFilter,currentMinPriceFilter,currentMaxPriceFilter
				,currentMinValueFilter/1000,currentMaxValueFilter/1000
				,currentMinJudgementFilter,currentMaxJudgementFilter, searchField);		
		
		

		int totalPages = (int)(totalProduct/elementsInList)+(totalProduct%elementsInList>0?1:0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", 1);
		
		model.addAttribute("manufacturersFilter", manufacturersFilter);
		model.addAttribute("engineTypeFilter", engineTypeFilter);
		model.addAttribute("oilStuffFilter", oilStuffFilter);
		model.addAttribute("viscosityFilter", viscosityFilter);
		model.addAttribute("recommended", motorOilDAO.getMotorOilsRecommended());
		model.addAttribute("currentPriceFilter", currentPriceFilter);
		
		model.addAttribute("paginationString_part1", ""+(1)+"-"+elementsInList);
		model.addAttribute("paginationString_part2", totalProduct);

		model.addAttribute("user", user);
		
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("currentPriceFilter", currentPriceFilter);
		session.setAttribute("manufacturersFilter", manufacturersFilter);
		session.setAttribute("elementsInList", elementsInList);
		
		return "MotorOilhome";
	}	
	
	@RequestMapping(value = {"/InsertUpdateMotorOil","/adminpanel/InsertUpdateMotorOil"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String insertUpdateMotorOil(HttpServletRequest request,Locale locale, Model model
			,@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "id_MotorOil" , defaultValue="0", required=false) int id_MotorOil
			,@RequestParam(value = "name_MotorOil", defaultValue="", required=false) String name_MotorOil
			,@RequestParam(value = "Manufacturer", defaultValue="", required=false) String manufacturer
			,@RequestParam(value = "EngineType", defaultValue="", required=false) String fieldEngineType
			,@RequestParam(value = "OilStuff", defaultValue="", required=false) String fieldOilStuff
			,@RequestParam(value = "Viscosity", defaultValue="", required=false) String fieldViscosity
			,@RequestParam(value = "Value", defaultValue="0", required=false) String value	//double
			,@RequestParam(value = "Price", defaultValue="0", required=false) String price	//double
			,@RequestParam(value = "Photo", required=false) MultipartFile formPhoto
			,@RequestParam(value="photoBackUp", defaultValue="", required=false) String photoBackUp   //в MultipartFile невозможно получить значение, оказывается. Старое сохраним в элементе формы
			,@RequestParam(value = "Description", defaultValue="", required=false) String description
			,@RequestParam(value = "Specification", defaultValue="", required=false) String specification
			,@RequestParam(value="score", defaultValue="0", required=false) String judgement		//double
			,@RequestParam(value="InStock", defaultValue="0", required=false) String inStock		//int
			,@RequestParam(value="Discount", defaultValue="0", required=false) String discount		//double
			,@RequestParam(value="pageInfo", defaultValue="0", required=false) String pageInfo		//double
			) {
		
//		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		fieldEngineType=("Выберите тип двигателя".equals(fieldEngineType)?Service.EMPTY:fieldEngineType);
		manufacturer   =("Выберите производителя".equals(manufacturer)?Service.EMPTY:manufacturer);
		fieldOilStuff  =("Выберите тип масла".equals(fieldOilStuff)?Service.EMPTY:fieldOilStuff);
		
		if (variant.compareTo("Сохранить")==0){
			variant="Save";
		}
		if (variant.compareTo("Обновить")==0){
			variant="Refresh";
		}
		HttpSession session=request.getSession();
		
		if (variant.compareTo("На главную")==0){
			model.addAttribute("list", brakingFluidDAO.getBrakingFluids());
			//return Service.isAdminPanel(session,request)+"MotorOil";
			return "MotorOilhome";
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
		
		
		Service.workWithList(id, Service.MOTOR_OIL_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1);
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		String result="MotorOilhome";
//		try {
//			pageInfo=new String(pageInfo.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		if ("Excel".equals(variant)){
			MotorOil good = motorOilDAO.getMotorOil(id_MotorOil);
			
			WorkWithExcel.createGoodCard(good, session);
			
			String photo="";
			if (good.hasPhoto()){
				photo=good.getPhoto();
			}
			model.addAttribute("Photo", photo);
			model.addAttribute("photoBackUp", photo);
			
			model.addAttribute("errors", new ArrayList<String>());
			model.addAttribute("currentMotorOil", good);
			model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
			model.addAttribute("combobox_EngineType", engineTypeDAO.getEngineTypes());
			model.addAttribute("combobox_OilStuff", oilStuffDAO.getOilStuffs());
			
			result= "MotorOilInsertUpdate";			
		}else if ("New".equals(variant)){
			model.addAttribute("pageInfo", "Введите новую номенклатуру: ");
			model.addAttribute("errors", new ArrayList<String>());
			model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
			model.addAttribute("combobox_EngineType", engineTypeDAO.getEngineTypes());
			model.addAttribute("combobox_OilStuff", oilStuffDAO.getOilStuffs());
			
			result = "MotorOilInsertUpdate";
		}else{
			String fieldName="", fieldManufacturer="", fieldValue="", fieldPrice="", fieldPhoto="", fieldDescription="", fieldSpecification="", fieldJudgement="";
			//try {
				fieldName=name_MotorOil; //new String(name_BrakeFluid.getBytes("iso-8859-1"), "UTF-8");
				fieldManufacturer=manufacturer; // new String(manufacturer.getBytes("iso-8859-1"), "UTF-8");
				fieldValue = value; //new String(value.getBytes("iso-8859-1"), "UTF-8");
				fieldPrice = price; //new String(price.getBytes("iso-8859-1"), "UTF-8");
				fieldPhoto = (formPhoto.getOriginalFilename().length()>0?formPhoto.getOriginalFilename():photoBackUp); //new String(photoBackUp.getBytes("iso-8859-1"), "UTF-8"));
				fieldDescription = description; //new String(description.getBytes("iso-8859-1"), "UTF-8"); 
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
					Double param = new Double(fieldValue);
				}catch (NumberFormatException e){
					fieldValue="0.0";
					errors.add("Поле \"Объём\" должно быть заполнено правильно");
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
			
			MotorOil motorOil= new MotorOil(id_MotorOil,fieldName, fieldManufacturer, fieldOilStuff
					,fieldEngineType, fieldViscosity, new Double(fieldValue),new Double(fieldPrice), fieldPhoto, fieldDescription, fieldSpecification
					,new Double(fieldJudgement), new Double(discount), new Integer(inStock));
					
			model.addAttribute("pageInfo", pageInfo);
			if (("Refresh".equals(variant)) || (errors.size()>0)){
				if (formPhoto.getOriginalFilename().length()>0){
					motorOil.setPhoto(Service.copyPhoto(WorkWithExcel.convertMultipartFile(formPhoto).getAbsolutePath(), request.getSession().getServletContext().getRealPath("/")));
				}
				String photo="";
				if (motorOil.hasPhoto()){
					photo=motorOil.getPhoto();
				}
				model.addAttribute("Photo", photo);
				model.addAttribute("photoBackUp", photo);
				
				model.addAttribute("errors", errors);
				model.addAttribute("currentMotorOil", motorOil);
				model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
				model.addAttribute("combobox_EngineType", engineTypeDAO.getEngineTypes());
				model.addAttribute("combobox_OilStuff", oilStuffDAO.getOilStuffs());
				
				result= "MotorOilInsertUpdate";
				
			}else if(("Update".equals(variant)) || ("Save".equals(variant))){
				if (request.isUserInRole("ROLE_PRODUCT") || request.isUserInRole("ROLE_ADMIN")){
					Manufacturer currentManufacturer=(Manufacturer)motorOil.getManufacturer();
					EngineType currentEngineType=(EngineType)motorOil.getEngineType();
					OilStuff currentOilStuff=(OilStuff)motorOil.getOilStuff();
					
					Country country=(Country)currentManufacturer.getCountry();
					if (country.getId()==0){
						country=countryDAO.createCountry(country.getName());
						currentManufacturer.setCountry(country);
					}
					if (currentManufacturer.getId()==0){
						currentManufacturer=manufacturerDAO.createManufacturer(currentManufacturer.getName(),country.getId());
						motorOil.setManufacturer(currentManufacturer);
					}
					if (currentEngineType.getId()==0){
						currentEngineType=engineTypeDAO.createEngineType(currentEngineType.getName());
						motorOil.setEngineType(currentEngineType);
					}	
					if (currentOilStuff.getId()==0){
						currentOilStuff=oilStuffDAO.createOilStuff(currentOilStuff.getName());
						motorOil.setOilStuff(currentOilStuff);
					}	
				
					if (motorOil.getId()==0){
						MotorOil currentMotorOil = null;
						if (request.isUserInRole("ROLE_PRICE") || request.isUserInRole("ROLE_ADMIN")){
							currentMotorOil = motorOilDAO.createMotorOil(motorOil);
						}else{
							currentMotorOil = motorOilDAO.createMotorOilWithoutPrice(motorOil);
						}
						
						logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentMotorOil, "Создан товар: моторное масло"));
						if (currentMotorOil.getPrice()>0){
							Price currentPrice = priceDAO.createPrice(new Price(currentMotorOil.getId(),new GregorianCalendar().getTime()
									,motorOil.getPrice(),currentMotorOil,user), Service.MOTOR_OIL_PREFIX);
							logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentPrice
									, "Создана цена для "+currentMotorOil.getId()+". "+currentMotorOil.getName()));
						}
					}else{

						MotorOil currentMotorOil = motorOilDAO.createMotorOilWithoutPrice(motorOil);
						logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentMotorOil, "Изменён товар: моторное масло"));
					}
				}else if (request.isUserInRole("ROLE_PRICE")){
					MotorOil oldMotorOil = motorOilDAO.getMotorOil(motorOil.getId());
					motorOilDAO.fillPrices(motorOil);
					MotorOil currentMotorOil = motorOilDAO.getMotorOil(motorOil.getId());
					
					if (oldMotorOil.getPrice()!=currentMotorOil.getPrice()){
						Price currentPrice = priceDAO.createPrice(new Price(0,new GregorianCalendar().getTime()
								,motorOil.getPrice(),currentMotorOil,user), Service.MOTOR_OIL_PREFIX);
						logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentPrice
								, "Создана цена для "+currentMotorOil.getId()+". "+currentMotorOil.getName()));
					}

					
				}
				
				result=defaultHome(session, model, user, basket, wishlist, compare, "");
			}
		}
		//return Service.isAdminPanel(session,request)+result;
		return result;
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

	@ModelAttribute("viscosityFilterOil")
	public HashMap<String,Boolean> createViscosityFilter(){
		HashMap<String,Boolean> map = new HashMap<String,Boolean>();
		for (String current: motorOilDAO.getMotorOilViscosities()){
			map.put(current,false);
		}
		
		return map;
	}
	
	@ModelAttribute("engineTypeFilterOil")
	public LinkedList<EngineType> createEngineTypeFilter(){
		LinkedList<EngineType> list = new LinkedList<EngineType>(engineTypeDAO.getEngineTypes());
		
		return list;
	}
	
	@ModelAttribute("oilStuffFilterOil")
	public LinkedList<OilStuff> createOilStuffFilter(){
		LinkedList<OilStuff> list = new LinkedList<OilStuff>(oilStuffDAO.getOilStuffs());
		
		return list;
	}
	
	
	@ModelAttribute("manufacturersFilterOil")
	public LinkedList<ManufacturerSelected> createManufacturersFilter(){
		LinkedList<ManufacturerSelected> listManufacturerSelected = new LinkedList<ManufacturerSelected>();
		for (Manufacturer currentManufacturer:manufacturerDAO.getManufacturers(Service.MOTOR_OIL_PREFIX)){
			ManufacturerSelected man=new ManufacturerSelected();
			man.setId(currentManufacturer.getId());
			man.setName(currentManufacturer.getName());
			man.setCountry(currentManufacturer.getCountry());
			man.setSelected(false);
			listManufacturerSelected.add(man);
		}
		
		return listManufacturerSelected;
	}
	
	private String createFilterValue(String param){
		double minPrice=motorOilDAO.minData(param);  //если текущая цена в фильтре не задана - возьмём максимум
		int i=new Double(minPrice).intValue();
		double maxPrice=motorOilDAO.maxData(param);
		int j=new Double(maxPrice).intValue();
		if (j<maxPrice){
			j++;
		}
		return i+","+j;
	}
	
	@ModelAttribute("currentPriceFilterOil")
	public String createCurrentPriceFilter(){
		return createFilterValue("price");
	}	
	
	@ModelAttribute("currentValueFilterOil")
	public String createCurrentValueFilter(){
		double minPrice=motorOilDAO.minData("Value")*1000;  //если текущая цена в фильтре не задана - возьмём максимум
		int i=new Double(minPrice).intValue();
		double maxPrice=motorOilDAO.maxData("Value")*1000;
		int j=new Double(maxPrice).intValue();
		if (j<maxPrice){
			j++;
		}
		return i+","+j;
	}
	
	@ModelAttribute("elementsInListOil")  //количество элементов, выводимых одновременно в списке. Используется в педжинации
	public int createElementsInList(){
		return Service.ELEMENTS_IN_LIST;
	}		
}
