package eftech.vasil.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

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
import eftech.workingset.DAO.templates.ClientTemplate;
import eftech.workingset.DAO.templates.CountryTemplate;
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.LogTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
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

	
	@RequestMapping(value = "/action", method = RequestMethod.POST)
	public String compare(@ModelAttribute User user
			, @RequestParam("variant") String variant
			, @RequestParam("fileEcxel") MultipartFile fileEcxel
			, @RequestParam("variantDownload") byte variantDownload
			,HttpServletRequest request
			,Locale locale, Model model) {
		
		String result="home";
		
//		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		ArrayList<String> errors=new ArrayList<String>();
		
		if ("На главную".equals(variant)){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
		}else if (("Загрузить номернклатуру".equals(variant)) || (variantDownload==Service.VARIANT_PRODUCT)){
			variantDownload=Service.VARIANT_PRODUCT;
			String productFile=fileEcxel.getOriginalFilename().trim();
			if (!Service.isFileExist(productFile)){
				errors.add("Указанный Вами файл с номенклатурой не существует");
    		}else{
    			if (!productFile.contains(".xlsx")){
    				errors.add("Указанный Вами файл с номенклатурой не соответствует формату. Используйте Excel-файл с расширением *.xlsx");
    			}else{
    				ArrayList<BrakingFluid> listBrakingFluids = Service.importFromExcelProduct(Service.convertMultipartFile(fileEcxel), request.getSession().getServletContext().getRealPath("/"));
    				
    			    synchronized (listBrakingFluids){
    		        	for (BrakingFluid currentBF:listBrakingFluids){
    		        		if (!Service.isFileExist(currentBF.getPhoto())){
    		        			errors.add("Указанный Вами файл c изображением не существует");
    		        		}
    		        		currentBF.setPhoto(Service.copyPhoto(currentBF.getPhoto(), request.getSession().getServletContext().getRealPath("/")));
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
		}else if (("Загрузить цены".equals(variant)) || (variantDownload==Service.VARIANT_PRICES)){
			variantDownload=Service.VARIANT_PRICES;
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
		if (errors.size()>0){ 
			model.addAttribute("errors",errors);
			model.addAttribute("variantDownload",variantDownload);
			result="Download";
		}else{
			model.addAttribute("listBrakFluids",brakingFluidDAO.getBrakingFluids());
		}
		
		return "adminpanel/"+result;
	}

}
