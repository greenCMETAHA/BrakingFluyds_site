package eftech.vasil.controller;

import java.util.ArrayList;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

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
import eftech.workingset.DAO.templates.CountryTemplate;
import eftech.workingset.DAO.templates.EngineTypeTemplate;
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.GearBoxOilTemplate;
import eftech.workingset.DAO.templates.GearBoxTypeTemplate;
import eftech.workingset.DAO.templates.LogTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.MotorOilTemplate;
import eftech.workingset.DAO.templates.OilStuffTemplate;
import eftech.workingset.DAO.templates.PriceTemplate;
import eftech.workingset.Services.WorkWithExcel;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.User;

@Controller
@SessionAttributes({"user",  "Basket"})
public class ExcelController {
	
	@Autowired
	BrakingFluidTemplate brakingFluidDAO;
	
	@Autowired
	ManufacturerTemplate manufacturerDAO;

	@Autowired
	FluidClassTemplate fluidClassDAO;

	@Autowired
	CountryTemplate countryDAO;	
	
	@Autowired
	LogTemplate logDAO;
	
	@Autowired
	OilStuffTemplate oilStuffDAO;

	@Autowired
	EngineTypeTemplate engineTypeDAO;

	@Autowired
	MotorOilTemplate motorOilDAO;

	@Autowired
	PriceTemplate priceDAO;
	
	@Autowired
	GearBoxTypeTemplate gearBoxTypeDAO;
	
	@Autowired
	GearBoxOilTemplate gearBoxOilDAO;		
	
	
	@RequestMapping(value = "/action", method = { RequestMethod.POST, RequestMethod.GET})
	public String compare(@ModelAttribute User user
			, @RequestParam(value="variant", defaultValue="", required=false) String variant
			, @RequestParam(value="good", defaultValue="", required=false) String good
			, @RequestParam(value="fileExcel", required=false) MultipartFile fileExcel
			, @RequestParam(value="variantDownload", defaultValue="", required=false) byte variantDownload
			,HttpServletRequest request
			,Locale locale, Model model) {
		
		String result="home";

		if ("На главную".equals(variant)){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
		}else if ("Загрузка".equals(variant)){ 
			if (("Загрузить номернклатуру".equals(variant)) || (variantDownload==Service.VARIANT_PRODUCT)){
				variant="Product";
			}else if (("Загрузить цены".equals(variant)) || (variantDownload==Service.VARIANT_PRICES)){
				variant="Price";
			}
		
			ArrayList<String> errors=new ArrayList<String>();
			if (variant!=""){
				errors=WorkWithExcel.downloadExcel(variant,user, good, fileExcel, countryDAO, manufacturerDAO, fluidClassDAO, brakingFluidDAO,
						oilStuffDAO, engineTypeDAO, motorOilDAO, gearBoxTypeDAO, gearBoxOilDAO, logDAO, priceDAO, request.getSession());
			}
			
			model.addAttribute("errors", errors);		
			
		}
		
		return "adminpanel/"+result;
	}
	
	@RequestMapping(value = "/reports", method = { RequestMethod.POST, RequestMethod.GET})
	public String reports(@ModelAttribute User user
			, @RequestParam(value="variant", defaultValue="", required=false) String variant
			, @RequestParam(value="goodPrefix", defaultValue="", required=false) String goodPrefix
			,HttpServletRequest request
			,Locale locale, Model model) {
		
		String result="home";

		if ("На главную".equals(variant)){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
		}else { 
			if (("Список поставщиков".equals(variant))  || ("ManufacturerList".equals(variant))){
				result="Reports";				
			}else if (("Прайс".equals(variant)) || ("Price".equals(variant))) {
				result="Reports";				
			}
		
			ArrayList<String> errors=new ArrayList<String>();
			if (("Список поставщиков".equals(variant))  || ("ManufacturerList".equals(variant))){
				WorkWithExcel.listManufacturersExcel(variant,manufacturerDAO, logDAO,request.getSession());
			} else if (("Прайс".equals(variant)) || ("Price".equals(variant))) {
				WorkWithExcel.PriceExcel(goodPrefix,brakingFluidDAO, motorOilDAO, gearBoxOilDAO, logDAO, request.getSession());
			} 
			
			model.addAttribute("errors", errors);		
		}
		
		return "adminpanel/"+result;
	}	

}
