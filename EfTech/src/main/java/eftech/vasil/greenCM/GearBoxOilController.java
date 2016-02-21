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
import eftech.workingset.DAO.templates.GearBoxTypeTemplate;
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.GearBoxTypeTemplate;
import eftech.workingset.DAO.templates.InfoTemplate;
import eftech.workingset.DAO.templates.LogTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.MotorOilTemplate;
import eftech.workingset.DAO.templates.GearBoxOilTemplate;
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
import eftech.workingset.beans.Customer;
import eftech.workingset.beans.GearBoxType;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.ManufacturerSelected;
import eftech.workingset.beans.MotorOil;
import eftech.workingset.beans.GearBoxOil;
import eftech.workingset.beans.OilStuff;
import eftech.workingset.beans.Price;
import eftech.workingset.beans.Review;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

@Controller
@SessionAttributes({"user", "adminpanel", "basket", "wishlist", "compare", "manufacturersFilterGrO", "gearBoxTypeFilterGrO", "oilStuffFilterGrO"
	, "viscosityFilterGrO", "elementsInListGrO", "currentPriceFilterGrO" , "currentValueFilterGrO", "currentJudgementFilterGrO"})
public class GearBoxOilController {
	@Autowired
	BrakingFluidTemplate brakingFluidDAO;

	@Autowired
	UserTemplate userDAO;
	
	@Autowired
	RoleTemplate roleDAO;
	
	
	@Autowired
	ManufacturerTemplate manufacturerDAO;

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
	
	private LinkedList<GearBoxType> fillSelectedGearBoxType(int[] selections, LinkedList<GearBoxType> list){
		for (GearBoxType current:list){
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

	@RequestMapping(value = {"/gearBoxOil","/adminpanel/gearBoxOil"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String _home(
			@RequestParam(value = "good", defaultValue="", required=false) String good
			
			,@RequestParam(value = "selections", required=false ) int[] manufacturerSelections  //base (BrakingFluids)
			,@RequestParam(value = "viscositySelections", required=false ) int[] viscositySelections
			,@RequestParam(value = "gearBoxTypeSelections", required=false ) int[] gearBoxTypeSelections
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
		LinkedList<ManufacturerSelected>  manufacturersFilter = (LinkedList<ManufacturerSelected>) session.getAttribute("manufacturersFilterGrO");
		if (manufacturersFilter==null){
			manufacturersFilter = createManufacturersFilter();
		}

		LinkedList<GearBoxType>  gearBoxTypeFilter = (LinkedList<GearBoxType>) session.getAttribute("gearBoxTypeFilterGrO");
		if (gearBoxTypeFilter==null){
			gearBoxTypeFilter = createGearBoxTypeFilter();
		}

		LinkedList<OilStuff>  oilStuffFilter = (LinkedList<OilStuff>) session.getAttribute("oilStuffFilterGrO");
		if (oilStuffFilter==null){
			oilStuffFilter = createOilStuffFilter();
		}
		
		HashMap<String,Boolean> viscosityFilter = (HashMap<String,Boolean>) session.getAttribute("viscosityFilterGrO");
		if (viscosityFilter==null){
			viscosityFilter = createViscosityFilter(); 
		}
		
		if (currentPriceFilter.toString().equals("0,0")){
			if (session.getAttribute("currentPriceFilterGrO")!=null){
				currentPriceFilter =(String) session.getAttribute("currentPriceFilterGrO");
			}
		}
		if (currentValueFilter.toString().equals("0,0")){	
			if (session.getAttribute("currentValueFilterGrO")!=null){
				currentValueFilter =(String) session.getAttribute("currentValueFilterGrO");
			}
		}
		if (currentJudgementFilter.toString().equals("0,0")){
			if (session.getAttribute("currentJudgementFilterGrO")!=null){
				currentJudgementFilter =(String) session.getAttribute("currentJudgementFilterGrO");
			}
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInListGrO")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInListGrO");
		}
 
		Service.workWithList(id, Service.GEARBOX_OIL_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0, 0, 0, 1, new Customer());
		

		elementsInList=(elementsInList==0?Service.ELEMENTS_IN_LIST:elementsInList);
	
		//--1
		int minPrice=new Double(gearBoxOilDAO.minData("Price")).intValue();
		double maxPriceDouble=gearBoxOilDAO.maxData("Price");
		int maxPrice=new Double(maxPriceDouble).intValue();
		if (maxPrice<maxPriceDouble){
			maxPrice++;
		}
		int[] priceFilter=Service.createFilterForSlider("Price", Service.GEARBOX_OIL_PREFIX, minPrice, maxPrice, currentPriceFilter, model, session);
		
		//--2
		int minValue=new Double(gearBoxOilDAO.minData("Value")*1000).intValue();
		int maxValue=new Double(gearBoxOilDAO.maxData("Value")*1000).intValue();
		
		int[] valueFilter=Service.createFilterForSlider("Value", Service.GEARBOX_OIL_PREFIX, minValue, maxValue, currentValueFilter, model, session);
		
		//--3
//		int minJudgement=new Double(gearBoxOilDAO.minData("Judgement")).intValue();  //пока отключим. Бо не получается позиционировать на [0:5], вываливает на [100:0]
		int currentMinJudgementFilter = 0; //new Double(minJudgement).intValue();
//		double maxJudgementDouble=gearBoxOilDAO.maxData("Judgement");
//		int maxJudgement=new Double(maxJudgementDouble).intValue();
//		if (maxJudgement<maxJudgementDouble){
//			maxJudgement++;
//		}
		int currentMaxJudgementFilter=5; //maxJudgement;
//		
//		currentMinJudgementFilter = Math.max(new Integer(currentJudgementFilter.substring(0, currentJudgementFilter.indexOf(","))), minJudgement);
//		oldValue=new Integer(currentJudgementFilter.substring(currentJudgementFilter.indexOf(",")+1, currentJudgementFilter.length()));
//		currentMaxJudgementFilter = Math.min((oldValue==0?maxJudgement:currentMaxJudgementFilter),maxJudgement);
		model.addAttribute("MinJudgement", 0); //minJudgement);
		model.addAttribute("MaxJudgement", 5); //maxJudgement);
		model.addAttribute("currentMinJudgementFilter", 0); //currentMinJudgementFilter);
		model.addAttribute("currentMaxJudgementFilter", 5); //currentMaxJudgementFilter);		
			
		currentJudgementFilter=currentMinJudgementFilter+","+currentMaxJudgementFilter;
		session.setAttribute("currentJudgementFilterGrO", currentJudgementFilter);
		model.addAttribute("currentJudgementFilter",currentJudgementFilter);
		
		manufacturersFilter=fillSelectedManufacturers(manufacturerSelections,manufacturersFilter); //method
		gearBoxTypeFilter=fillSelectedGearBoxType(gearBoxTypeSelections,gearBoxTypeFilter); //method
		oilStuffFilter=fillSelectedOilStuff(oilStuffSelections,oilStuffFilter); //method
		viscosityFilter=fillSelectedViscosity(viscositySelections, viscosityFilter); //method
//		System.out.println(manufacturerSelections);
//		System.out.println(fluidClassselections);
	
		ArrayList<GearBoxOil> list=gearBoxOilDAO.getGearBoxOils(currentPage,elementsInList,manufacturersFilter,gearBoxTypeFilter
				,oilStuffFilter,viscosityFilter,priceFilter[0],priceFilter[1]
				,valueFilter[0]/1000,valueFilter[1]/1000
				,currentMinJudgementFilter,currentMaxJudgementFilter, searchField); 
		
		model.addAttribute("listGearBoxOils", list);
		int totalProduct=gearBoxOilDAO.getCountRows(currentPage,elementsInList,manufacturersFilter,gearBoxTypeFilter
				,oilStuffFilter,viscosityFilter,priceFilter[0],priceFilter[1]
				,valueFilter[0]/1000,valueFilter[1]/1000
				,currentMinJudgementFilter,currentMaxJudgementFilter, searchField);
		int totalPages = (int)(totalProduct/elementsInList)+(totalProduct%elementsInList>0?1:0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		
		model.addAttribute("manufacturersFilter", manufacturersFilter);
		model.addAttribute("gearBoxTypeFilter", gearBoxTypeFilter);
		model.addAttribute("oilStuffFilter", oilStuffFilter);
		model.addAttribute("viscosityFilter", viscosityFilter);
		model.addAttribute("recommended", gearBoxOilDAO.getGearBoxOilsRecommended());
		 
		model.addAttribute("paginationString_part1", ""+((currentPage-1)*elementsInList+1)+"-"+(((currentPage-1)*elementsInList)+elementsInList));
		model.addAttribute("paginationString_part2", totalProduct);

		model.addAttribute("user", user);
		
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("manufacturersFilterGrO", manufacturersFilter);
		session.setAttribute("gearBoxTypeFilterGrO", gearBoxTypeFilter);
		session.setAttribute("oilStuffFilterGrO", oilStuffFilter);
		session.setAttribute("viscosityFilterGrO", viscosityFilter);
		session.setAttribute("elementsInListGrO", elementsInList);
		
		//return Service.isAdminPanel(session,request)+"home";
		return "GearBoxOilhome";
	}
	
	@RequestMapping(value = {"/ShowOneGearBoxOil","/adminpanel/ShowOneGearBoxOil"}, method = {RequestMethod.POST, RequestMethod.GET})
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
			GearBoxOil current=new GearBoxOil();
			current.setId(id);
			review.setGood(current);
			reviewDAO.createReview(review,Service.GEARBOX_OIL_PREFIX);
		}		
		
		Service.workWithList(id, Service.GEARBOX_OIL_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		if ((request.isUserInRole("ROLE_ADMIN")) || (request.isUserInRole("ROLE_PRODUCT"))
				|| (request.isUserInRole("ROLE_PRICE"))){
			
			if (variant.compareTo("insert")==0){
				model.addAttribute("pageInfo", "Введите новую номенклатуру: ");
				model.addAttribute("currentGearBoxOil", new GearBoxOil());
			}else{
				GearBoxOil current = gearBoxOilDAO.getGearBoxOil(id);
				model.addAttribute("pageInfo", "Отредактируйте номенклатуру:");
				model.addAttribute("currentGearBoxOil", current);
				String photo="";
				if (current.hasPhoto()){
					photo=current.getPhoto();
				}
				model.addAttribute("Photo", photo);
				model.addAttribute("photoBackUp", photo);
			}
			model.addAttribute("buttonInto", "Сохранить");
			model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
			model.addAttribute("combobox_GearBoxType", gearBoxTypeDAO.getGearBoxTypes());
			model.addAttribute("combobox_OilStuff", oilStuffDAO.getOilStuffs());
			model.addAttribute("errors", new ArrayList<String>());
			model.addAttribute("prices", priceDAO.getPrices(id, Service.GEARBOX_OIL_PREFIX));
			
			//return Service.isAdminPanel(session,request)+"InsertUpdate";
			return "GearBoxOilInsertUpdate";
		}else{
			model.addAttribute("currentGearBoxOil", gearBoxOilDAO.getGearBoxOil(id));
			model.addAttribute("reviews", reviewDAO.getReviews(id,Service.GEARBOX_OIL_PREFIX));
			model.addAttribute("prices", priceDAO.getPrices(id, Service.GEARBOX_OIL_PREFIX));
			
			return "GearBoxOilShowOne";
		}
		
	}
	
	public String defaultHome(HttpSession session, Model model, User user, LinkedList<Basket> basket
			, LinkedList<Wishlist> wishlist, LinkedList<InterfaceGood> compare, String searchField){
	
		LinkedList<ManufacturerSelected>  manufacturersFilter = (LinkedList<ManufacturerSelected>) session.getAttribute("manufacturersFilterGrO");
		if (manufacturersFilter==null){
			manufacturersFilter = createManufacturersFilter();
		}
		LinkedList<GearBoxType>  gearBoxTypeFilter = (LinkedList<GearBoxType>) session.getAttribute("gearBoxTypeFilterGrO");
		if (gearBoxTypeFilter==null){
			gearBoxTypeFilter = createGearBoxTypeFilter();
		}

		LinkedList<OilStuff>  oilStuffFilter = (LinkedList<OilStuff>) session.getAttribute("oilStuffFilterGrO");
		if (oilStuffFilter==null){
			oilStuffFilter = createOilStuffFilter();
		}
		
		HashMap<String,Boolean> viscosityFilter = (HashMap<String,Boolean>) session.getAttribute("viscosityFilterGrO");
		if (viscosityFilter==null){
			viscosityFilter = createViscosityFilter();
		}

		String currentPriceFilter = createCurrentPriceFilter();
		if (session.getAttribute("currentPriceFilterGrO")!=null){
			currentPriceFilter =(String) session.getAttribute("currentPriceFilterGrO");
		}else{
		}
		int elementsInList = Service.ELEMENTS_IN_LIST;
		if (session.getAttribute("elementsInListGrO")!=null){
			elementsInList = (Integer) session.getAttribute("elementsInListGrO");
		}
		
		elementsInList=(elementsInList==0?Service.ELEMENTS_IN_LIST:elementsInList);
		double currentMinPriceFilter=gearBoxOilDAO.minData("Price");
		double currentMaxPriceFilter=gearBoxOilDAO.maxData("Price");
		model.addAttribute("MinPrice", currentMinPriceFilter);
		model.addAttribute("MaxPrice", currentMaxPriceFilter);
		model.addAttribute("currentMinPriceFilter", currentMinPriceFilter);
		model.addAttribute("currentMaxPriceFilter", currentMaxPriceFilter);		

		double currentMinValueFilter=gearBoxOilDAO.minData("Value")*1000;
		double currentMaxValueFilter=gearBoxOilDAO.maxData("Value")*1000;
		model.addAttribute("MinValue", currentMinValueFilter);
		model.addAttribute("MaxValue", currentMaxValueFilter);
		model.addAttribute("currentMinValueFilter", currentMinValueFilter);
		model.addAttribute("currentMaxValueFilter", currentMaxValueFilter);		
		
		double currentMinJudgementFilter=gearBoxOilDAO.minData("Judgement");
		double currentMaxJudgementFilter=gearBoxOilDAO.maxData("Judgement");
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
		for (GearBoxType current:gearBoxTypeFilter){
			if (current.isSelected()){
				count++;
			}
		}
		
		int engineTyprSelections[]=new int[count];
		count=0;
		for (GearBoxType current:gearBoxTypeFilter){
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
	
		ArrayList<GearBoxOil> list=gearBoxOilDAO.getGearBoxOils(1,elementsInList,manufacturersFilter,gearBoxTypeFilter
				,oilStuffFilter,viscosityFilter,currentMinPriceFilter,currentMaxPriceFilter
				,currentMinValueFilter/1000,currentMaxValueFilter/1000
				,currentMinJudgementFilter,currentMaxJudgementFilter, searchField); 
		
		model.addAttribute("listGearBoxOils", list);
		int totalProduct=gearBoxOilDAO.getCountRows(1,elementsInList,manufacturersFilter,gearBoxTypeFilter
				,oilStuffFilter,viscosityFilter,currentMinPriceFilter,currentMaxPriceFilter
				,currentMinValueFilter/1000,currentMaxValueFilter/1000
				,currentMinJudgementFilter,currentMaxJudgementFilter, searchField);		
		
		

		int totalPages = (int)(totalProduct/elementsInList)+(totalProduct%elementsInList>0?1:0);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", 1);
		
		model.addAttribute("manufacturersFilter", manufacturersFilter);
		model.addAttribute("gearBoxTypeFilter", gearBoxTypeFilter);
		model.addAttribute("oilStuffFilter", oilStuffFilter);
		model.addAttribute("viscosityFilter", viscosityFilter);
		model.addAttribute("recommended", gearBoxOilDAO.getGearBoxOilsRecommended());
		model.addAttribute("currentPriceFilter", currentPriceFilter);
		
		model.addAttribute("paginationString_part1", ""+(1)+"-"+elementsInList);
		model.addAttribute("paginationString_part2", totalProduct);

		model.addAttribute("user", user);
		
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		session.setAttribute("user", user);
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		session.setAttribute("currentPriceFilterGrO", currentPriceFilter);
		session.setAttribute("manufacturersFilterGrO", manufacturersFilter);
		session.setAttribute("elementsInListGrO", elementsInList);
		
		return "GearBoxOilhome";
	}	
	
	@RequestMapping(value = {"/InsertUpdateGearBoxOil","/adminpanel/InsertUpdateGearBoxOil"}, method = {RequestMethod.POST, RequestMethod.GET})
	public String insertUpdateGearBoxOil(HttpServletRequest request,Locale locale, Model model
			,@RequestParam(value = "variant", defaultValue="", required=false) String variant
			,@RequestParam(value = "id", defaultValue="0", required=false) int id
			,@RequestParam(value = "id_Good" , defaultValue="0", required=false) int id_GearBoxOil
			,@RequestParam(value = "name_Good", defaultValue="", required=false) String name_GearBoxOil
			,@RequestParam(value = "Manufacturer", defaultValue="", required=false) String manufacturer
			,@RequestParam(value = "GearBoxType", defaultValue="", required=false) String fieldGearBoxType
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
		fieldGearBoxType=("Выберите тип двигателя".equals(fieldGearBoxType)?Service.EMPTY:fieldGearBoxType);
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
			//return Service.isAdminPanel(session,request)+"GearBoxOil";
			return "GearBoxOilhome";
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
		
		
		Service.workWithList(id, Service.GEARBOX_OIL_PREFIX, 0, false, variant, user, basket, wishlist, compare, brakingFluidDAO, motorOilDAO, gearBoxOilDAO
				, logDAO, clientDAO, manufacturerDAO, offerStatusDAO,  infoDAO, demandDAO, payDAO, wishlistDAO, session,0,0, 0, 1, new Customer());
		model=Service.createHeader(model, user, basket, wishlist,compare, infoDAO, wishlistDAO);		 //method
		
		session.setAttribute("basket", basket);
		session.setAttribute("wishlist", wishlist);
		session.setAttribute("compare", compare);
		
		String result="GearBoxOilhome";
//		try {
//			pageInfo=new String(pageInfo.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
		
		if ("Excel".equals(variant)){
			GearBoxOil good = gearBoxOilDAO.getGearBoxOil(id_GearBoxOil);
			
			WorkWithExcel.createGoodCard(good, session);
			
			String photo="";
			if (good.hasPhoto()){
				photo=good.getPhoto();
			}
			model.addAttribute("Photo", photo);
			model.addAttribute("photoBackUp", photo);
			
			model.addAttribute("errors", new ArrayList<String>());
			model.addAttribute("currentGearBoxOil", good);
			model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
			model.addAttribute("combobox_GearBoxType", gearBoxTypeDAO.getGearBoxTypes());
			model.addAttribute("combobox_OilStuff", oilStuffDAO.getOilStuffs());
			
			result= "GearBoxOilInsertUpdate";			
		
		}else if ("New".equals(variant)){
			model.addAttribute("pageInfo", "Введите новую номенклатуру: ");
			model.addAttribute("errors", new ArrayList<String>());
			model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
			model.addAttribute("combobox_GearBoxType", gearBoxTypeDAO.getGearBoxTypes());
			model.addAttribute("combobox_OilStuff", oilStuffDAO.getOilStuffs());
			
			result = "GearBoxOilInsertUpdate";
		}else{
			String fieldName="", fieldManufacturer="", fieldValue="", fieldPrice="", fieldPhoto="", fieldDescription="", fieldSpecification="", fieldJudgement="";
			//try {
				fieldName=name_GearBoxOil; //new String(name_BrakeFluid.getBytes("iso-8859-1"), "UTF-8");
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
			
			GearBoxOil gearBoxOil= new GearBoxOil(id_GearBoxOil,fieldName, fieldManufacturer, fieldOilStuff
					,fieldGearBoxType, fieldViscosity, new Double(fieldValue),new Double(fieldPrice), fieldPhoto, fieldDescription, fieldSpecification
					,new Double(fieldJudgement), new Double(discount), new Integer(inStock));
					
			model.addAttribute("pageInfo", pageInfo);
			if (("Refresh".equals(variant)) || (errors.size()>0)){
				if (formPhoto.getOriginalFilename().length()>0){
					gearBoxOil.setPhoto(Service.copyPhoto(WorkWithExcel.convertMultipartFile(formPhoto).getAbsolutePath(), request.getSession().getServletContext().getRealPath("/")));
				}
				String photo="";
				if (gearBoxOil.hasPhoto()){
					photo=gearBoxOil.getPhoto();
				}
				model.addAttribute("Photo", photo);
				model.addAttribute("photoBackUp", photo);
				
				model.addAttribute("errors", errors);
				model.addAttribute("currentGearBoxOil", gearBoxOil);
				model.addAttribute("combobox_Manufacturers", manufacturerDAO.getManufacturers());
				model.addAttribute("combobox_GearBoxType", gearBoxTypeDAO.getGearBoxTypes());
				model.addAttribute("combobox_OilStuff", oilStuffDAO.getOilStuffs());
				
				result= "GearBoxInsertUpdate";
				
			}else if(("Update".equals(variant)) || ("Save".equals(variant))){
				if (request.isUserInRole("ROLE_PRODUCT") || request.isUserInRole("ROLE_ADMIN")){
					Manufacturer currentManufacturer=(Manufacturer)gearBoxOil.getManufacturer();
					GearBoxType currentGearBoxType=(GearBoxType)gearBoxOil.getGearBoxType();
					OilStuff currentOilStuff=(OilStuff)gearBoxOil.getOilStuff();
					
					Country country=(Country)currentManufacturer.getCountry();
					if (country.getId()==0){
						country=countryDAO.createCountry(country.getName());
						currentManufacturer.setCountry(country);
					}
					if (currentManufacturer.getId()==0){
						currentManufacturer=manufacturerDAO.createManufacturer(currentManufacturer.getName(),country.getId());
						gearBoxOil.setManufacturer(currentManufacturer);
					}
					if (currentGearBoxType.getId()==0){
						currentGearBoxType=gearBoxTypeDAO.createGearBoxType(currentGearBoxType.getName());
						gearBoxOil.setGearBoxType(currentGearBoxType);
					}	
					if (currentOilStuff.getId()==0){
						currentOilStuff=oilStuffDAO.createOilStuff(currentOilStuff.getName());
						gearBoxOil.setOilStuff(currentOilStuff);
					}	
				
					if (gearBoxOil.getId()==0){
						GearBoxOil currentGearBoxOil = null;
						if (request.isUserInRole("ROLE_PRICE") || request.isUserInRole("ROLE_ADMIN")){
							currentGearBoxOil = gearBoxOilDAO.createGearBoxOil(gearBoxOil);
						}else{
							currentGearBoxOil = gearBoxOilDAO.createGearBoxOilWithoutPrice(gearBoxOil);
						}
						
						logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentGearBoxOil, "Создан товар: моторное масло"));
						if (currentGearBoxOil.getPrice()>0){
							Price currentPrice = priceDAO.createPrice(new Price(currentGearBoxOil.getId(),new GregorianCalendar().getTime()
									,gearBoxOil.getPrice(),currentGearBoxOil,user), Service.GEARBOX_OIL_PREFIX);
							logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentPrice
									, "Создана цена для "+currentGearBoxOil.getId()+". "+currentGearBoxOil.getName()));
						}
					}else{

						GearBoxOil currentGearBoxOil = gearBoxOilDAO.createGearBoxOilWithoutPrice(gearBoxOil);
						logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentGearBoxOil, "Изменён товар: моторное масло"));
					}
				}else if (request.isUserInRole("ROLE_PRICE")){
					GearBoxOil oldGearBoxOil = gearBoxOilDAO.getGearBoxOil(gearBoxOil.getId());
					gearBoxOilDAO.fillPrices(gearBoxOil);
					GearBoxOil currentGearBoxOil = gearBoxOilDAO.getGearBoxOil(gearBoxOil.getId());
					
					if (oldGearBoxOil.getPrice()!=currentGearBoxOil.getPrice()){
						Price currentPrice = priceDAO.createPrice(new Price(0,new GregorianCalendar().getTime()
								,gearBoxOil.getPrice(),currentGearBoxOil,user), Service.GEARBOX_OIL_PREFIX);
						logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), currentPrice
								, "Создана цена для "+currentGearBoxOil.getId()+". "+currentGearBoxOil.getName()));
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

	@ModelAttribute("viscosityFilterGrO")
	public HashMap<String,Boolean> createViscosityFilter(){
		HashMap<String,Boolean> map = new HashMap<String,Boolean>();
		for (String current: gearBoxOilDAO.getGearBoxOilViscosities()){
			map.put(current,false);
		}
		
		return map;
	}
	
	@ModelAttribute("gearBoxTypeFilterGrO")
	public LinkedList<GearBoxType> createGearBoxTypeFilter(){
		LinkedList<GearBoxType> list = new LinkedList<GearBoxType>(gearBoxTypeDAO.getGearBoxTypes());
		
		return list;
	}
	
	@ModelAttribute("oilStuffFilterGrO")
	public LinkedList<OilStuff> createOilStuffFilter(){
		LinkedList<OilStuff> list = new LinkedList<OilStuff>(oilStuffDAO.getOilStuffs());
		
		return list;
	}
	
	
	@ModelAttribute("manufacturersFilterGrO")
	public LinkedList<ManufacturerSelected> createManufacturersFilter(){
		LinkedList<ManufacturerSelected> listManufacturerSelected = new LinkedList<ManufacturerSelected>();
		for (Manufacturer currentManufacturer:manufacturerDAO.getManufacturers(Service.GEARBOX_OIL_PREFIX)){
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
		double minPrice=gearBoxOilDAO.minData(param);  //если текущая цена в фильтре не задана - возьмём максимум
		int i=new Double(minPrice).intValue();
		
		double maxPrice=gearBoxOilDAO.maxData(param);
		int j=new Double(maxPrice).intValue();
		if (j<maxPrice){
			j++;
		}
		
		return i+","+j;
	}
	
	@ModelAttribute("currentPriceFilterGrO")
	public String createCurrentPriceFilter(){
		return createFilterValue("price");
	}
	
	@ModelAttribute("currentValueFilterGrO")
	public String createCurrentValueFilter(){
		double minPrice=gearBoxOilDAO.minData("Value")*1000;  //если текущая цена в фильтре не задана - возьмём максимум
		int i=new Double(minPrice).intValue();
		double maxPrice=gearBoxOilDAO.maxData("Value")*1000;
		int j=new Double(maxPrice).intValue();
		if (j<maxPrice){
			j++;
		}
		return i+","+j;
	}
	
	@ModelAttribute("elementsInListGrO")  //количество элементов, выводимых одновременно в списке. Используется в педжинации
	public int createElementsInList(){
		return Service.ELEMENTS_IN_LIST;
	}		
}
