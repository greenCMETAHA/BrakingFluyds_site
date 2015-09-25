package eftech.vasil.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.FluidClass;
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
	
	@RequestMapping(value = "/action", method = RequestMethod.POST)
	public String compare(@ModelAttribute User user
			, @RequestParam("button") String formButton
			, @RequestParam("pathFluidsPrices") MultipartFile pathFluidsPrices
			, @RequestParam("variant") byte variant
			,HttpServletRequest request
			,Locale locale, Model model) {
		
		String result="home";
		
		String button="";
		try {
			button=new String(formButton.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> errors=new ArrayList<String>();
		
		if ("На главную".equals(button)){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
		}else if (("Загрузить номернклатуру".equals(button)) || (variant==Service.VARIANT_PRODUCT)){
			variant=Service.VARIANT_PRODUCT;
			String productFile=pathFluidsPrices.getOriginalFilename().trim();
			if (!Service.isFileExist(productFile)){
				errors.add("Указанный Вами файл с номенклатурой не существует");
    		}else{
    			if (!productFile.contains(".xlsx")){
    				errors.add("Указанный Вами файл с номенклатурой не соответствует формату. Используйте Excel-файл с расширением *.xlsx");
    			}else{
    				ArrayList<BrakingFluid> listBrakingFluids = Service.importFromExcelProduct(Service.convertMultipartFile(pathFluidsPrices), request.getSession().getServletContext().getRealPath("/"));
    				
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
		}else if (("Загрузить цены".equals(button)) || (variant==Service.VARIANT_PRICES)){
			variant=Service.VARIANT_PRICES;
			String priceFile=pathFluidsPrices.getOriginalFilename().trim();
			if (!Service.isFileExist(priceFile)){
				errors.add("Указанный Вами файл с ценами не существует");
    		}else{
    			if (!priceFile.contains(".xlsx")){
    				errors.add("Указанный Вами файл с ценами не соответствует формату. Используйте Excel-файл с расширением *.xlsx");
    			}else{
					ArrayList<BrakingFluid> listBrakingFluids = Service.importFromExcelPrices(Service.convertMultipartFile(pathFluidsPrices));
		 	        synchronized (listBrakingFluids){
			         	for (BrakingFluid currentBF:listBrakingFluids){
			        		BrakingFluid value = brakingFluidDAO.fillPrices(currentBF);
			        	}
					}			
    			}
    		}
		}
		if (errors.size()>0){ 
			model.addAttribute("errors",errors);
			model.addAttribute("variant",variant);
			result="Download";
		}
		
		return result;
	}

}
