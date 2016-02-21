package eftech.workingset.Services;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.WriteAbortedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFBorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import eftech.vasil.greenCM.ExcelController;
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
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.EngineType;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.GearBoxOil;
import eftech.workingset.beans.GearBoxType;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.MotorOil;
import eftech.workingset.beans.OilStuff;
import eftech.workingset.beans.Price;
import eftech.workingset.beans.User;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public class WorkWithExcel {
	
	public static File convertMultipartFile(MultipartFile file){
		File result = new File(file.getOriginalFilename());
	    try {
	    	result.createNewFile();
		    FileOutputStream fos = new FileOutputStream(result); 
		    fos.write(file.getBytes());
		    fos.close(); 			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
	    return result;
	}
	
	public static ArrayList<BrakingFluid> importFromExcelBrakingFluids(File path, String GlobalPath){
		 //Только для .xlsx !!!!  .xls надо обрабаотывать через HSSF
		
        Workbook wb=null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<BrakingFluid> list = new ArrayList<BrakingFluid>();
		
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            for (Row row : sheet) {
            	BrakingFluid currentBF = new  BrakingFluid();
            	Manufacturer manufacturer = new Manufacturer();
            	Country country = new Country();
            	FluidClass fluidClass = new FluidClass();

                for (Cell cell : row) { //перебираем значения строки
                	switch (cell.getColumnIndex()) {
					case 0:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentBF.setName(cell.getStringCellValue());
						}
						break;
					}
					case 1:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							manufacturer.setName(cell.getStringCellValue());
						}
						currentBF.setManufacturer(manufacturer);
						break;
					}
					case 2:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							fluidClass.setName(cell.getStringCellValue());
						}
						currentBF.setFluidClass(fluidClass);
						break;
					}
					case 3:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentBF.setBoilingTemperatureDry(cell.getNumericCellValue());
						}
						break;
					}
					case 4:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentBF.setBoilingTemperatureWet(cell.getNumericCellValue());
						}
						break;
					}
					case 5:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentBF.setValue(cell.getNumericCellValue());
						}
						break;
					}
					case 6:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentBF.setPhoto(cell.getStringCellValue());
						}						
						break;
					}
					case 7:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentBF.setDescription(cell.getStringCellValue());
						}						
						break;
					}
					case 8:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentBF.setViscosity40(cell.getNumericCellValue());
						}						
						break;
					}
					case 9:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentBF.setViscosity100(cell.getNumericCellValue());
						}						
						break;
					}
					case 10:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentBF.setSpecification(cell.getStringCellValue());
						}						
						break;
					}
					case 11:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentBF.setJudgement(cell.getNumericCellValue());
						}							
						break;
					}
					case 12:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							country.setName(cell.getStringCellValue());
						}						
						manufacturer.setCountry(country);
						break;
					}
					default:
						break;
					}                	
                }
            	//теперь собираем список.
            	
            	//System.out.println(currentBF.toString());
            	if (currentBF.getName().length()>0)
            		list.add(currentBF);
            }
        }
		
		return list;
	}
	
	public static ArrayList<MotorOil> importFromExcelMotorOils(File path, String GlobalPath){
		 //Только для .xlsx !!!!  .xls надо обрабаотывать через HSSF
		
       Workbook wb=null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<MotorOil> list = new ArrayList<MotorOil>();
		
       for (int i = 0; i < wb.getNumberOfSheets(); i++) {
           Sheet sheet = wb.getSheetAt(i);
           for (Row row : sheet) {
        	   MotorOil currentOil = new  MotorOil();
           	   Manufacturer manufacturer = new Manufacturer();
           	   OilStuff oilStuff = new OilStuff();
           	   Country country = new Country();
           	   EngineType engineType = new EngineType();

               for (Cell cell : row) { //перебираем значения строки
               	switch (cell.getColumnIndex()) {
					case 0:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentOil.setName(cell.getStringCellValue());
						}
						break;
					}
					case 1:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							oilStuff.setName(cell.getStringCellValue());
						}
						currentOil.setOilStuff(oilStuff);
						break;
					}
					case 2:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							engineType.setName(cell.getStringCellValue());
						}
						currentOil.setEngineType(engineType);
						break;
					}
					case 3:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentOil.setViscosity(cell.getStringCellValue());
						}
						break;
					}
					case 4:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentOil.setValue(cell.getNumericCellValue());
						}
						break;
					}
					case 5:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentOil.setPhoto(cell.getStringCellValue());
						}
						break;
					}
					case 6:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){ 
							manufacturer.setName(cell.getStringCellValue());
						}
						currentOil.setManufacturer(manufacturer);
						break;
					}
					case 7:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentOil.setDescription(cell.getStringCellValue());
						}						
						break;
					}
					case 8:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentOil.setSpecification(cell.getStringCellValue());
						}						
						break;
					}
					case 9:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							country.setName(cell.getStringCellValue());
						}
						manufacturer.setCountry(country);
						break;
					}
					case 10:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentOil.setJudgement(cell.getNumericCellValue());
						}							
						break;
					}
					default:
						break;
					}                	
               }
           	//теперь собираем список.
           	
           	//System.out.println(currentBF.toString());
           	if (currentOil.getName().length()>0)
           		list.add(currentOil);
           }
       }
		
		return list;
	}	
	
	public static ArrayList<GearBoxOil> importFromExcelGearBoxOils(File path, String GlobalPath){
		 //Только для .xlsx !!!!  .xls надо обрабаотывать через HSSF
		
      Workbook wb=null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<GearBoxOil> list = new ArrayList<GearBoxOil>();
		
      for (int i = 0; i < wb.getNumberOfSheets(); i++) {
          Sheet sheet = wb.getSheetAt(i);
          for (Row row : sheet) {
        	  GearBoxOil currentOil = new  GearBoxOil();
          	  Manufacturer manufacturer = new Manufacturer();
          	  OilStuff oilStuff = new OilStuff();
          	  Country country = new Country();
          	  GearBoxType gearBoxType = new GearBoxType();

              for (Cell cell : row) { //перебираем значения строки
              	switch (cell.getColumnIndex()) {
					case 0:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentOil.setName(cell.getStringCellValue());
						}
						break;
					}
					case 1:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							oilStuff.setName(cell.getStringCellValue());
						}
						currentOil.setOilStuff(oilStuff);
						break;
					}
					case 2:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							gearBoxType.setName(cell.getStringCellValue());
						}
						currentOil.setGearBoxType(gearBoxType);
						break;
					}
					case 3:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentOil.setViscosity(cell.getStringCellValue());
						}
						break;
					}
					case 4:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentOil.setValue(cell.getNumericCellValue());
						}
						break;
					}
					case 5:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentOil.setPhoto(cell.getStringCellValue());
						}
						break;
					}
					case 6:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){ 
							manufacturer.setName(cell.getStringCellValue());
						}
						currentOil.setManufacturer(manufacturer);
						break;
					}
					case 7:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentOil.setDescription(cell.getStringCellValue());
						}						
						break;
					}
					case 8:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							currentOil.setSpecification(cell.getStringCellValue());
						}						
						break;
					}
					case 9:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							country.setName(cell.getStringCellValue());
						}
						manufacturer.setCountry(country);
						break;
					}
					case 10:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentOil.setJudgement(cell.getNumericCellValue());
						}							
						break;
					}
					default:
						break;
					}                	
              }
          	//теперь собираем список.
          	
          	//System.out.println(currentBF.toString());
          	if (currentOil.getName().length()>0)
          		list.add(currentOil);
          }
      }
		
		return list;
	}			

	public static ArrayList<BrakingFluid> importFromExcelPrices(File path){
		
        Workbook wb=null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<BrakingFluid> list = new ArrayList<BrakingFluid>(); //используем и для MotorOil: наименование и цена там одинаковые
		
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            for (Row row : sheet) {
            	BrakingFluid current = new BrakingFluid();

                for (Cell cell : row) { //перебираем значения строки
                	switch (cell.getColumnIndex()) {
					case 0:{
						if (cell.getCellType()==cell.CELL_TYPE_STRING){
							current.setName(cell.getStringCellValue());
						}
						break;
					}
					case 1:{
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							current.setPrice(cell.getNumericCellValue());
						}						
						break;
					}
					default:
						break;
					}                	
                }
            	//теперь собираем список.
            	
            	//System.out.println(currentBF.toString());
            	if (current.getName().length()>0)	//пропустим строки, в которых не заполнено наименование
            		list.add(current);
            }
        }
 
		return list;
	}
		
	
	public static ArrayList<String> downloadExcel(String variant,User user, String good, MultipartFile fileExcel
			,CountryTemplate countryDAO, ManufacturerTemplate manufacturerDAO, FluidClassTemplate fluidClassDAO
			, BrakingFluidTemplate brakingFluidDAO, OilStuffTemplate oilStuffDAO, EngineTypeTemplate engineTypeDAO
			, MotorOilTemplate motorOilDAO, GearBoxTypeTemplate gearBoxTypeDAO, GearBoxOilTemplate gearBoxOilDAO
			, LogTemplate logDAO, PriceTemplate priceDAO, HttpSession session) {
		ArrayList<String> errors=new ArrayList<String>();
		
		 if ("Product".equals(variant)){
			String productFile=fileExcel.getOriginalFilename().trim();
//			if (!Service.isFileExist(productFile)){
//				errors.add("Указанный Вами файл с номенклатурой не существует");
//    		}else{
    			if (!productFile.contains(".xlsx")){
    				errors.add("Указанный Вами файл с номенклатурой не соответствует формату. Используйте Excel-файл с расширением *.xlsx");
    			}else{
    				if ("GearBoxOils".equals(good)){
    					ArrayList<GearBoxOil> list = importFromExcelGearBoxOils(convertMultipartFile(fileExcel)
    							,session.getServletContext().getRealPath("/"));
    				
	    			    synchronized (list){
	    		        	for (GearBoxOil current:list){
	    		        		if (!Service.isFileExist(current.getPhoto())){
	    		        			errors.add("Указанный Вами файл c изображением не существует");
	    		        		}
	    		        		current.setPhoto(Service.copyPhoto(current.getPhoto(), session.getServletContext().getRealPath("/")));
	    		        		Manufacturer currentManufacturer=(Manufacturer)current.getManufacturer();	
	    		        		if (currentManufacturer==null){
	    		        			currentManufacturer=new Manufacturer();
	    		        		}
	    		    			Country country=(Country)currentManufacturer.getCountry();
	    		    			if (country.getId()==0){
	    		    				country=countryDAO.createCountry(country.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),country, "Загрузили из Excel"));
	    		    				currentManufacturer.setCountry(country);
	    		    			}
	    		    			OilStuff currentOilStuff=(OilStuff)current.getOilStuff();
	    		    			if (currentOilStuff.getId()==0){
	    		    				currentOilStuff=oilStuffDAO.createOilStuff(currentOilStuff.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentOilStuff, "Загрузили из Excel"));
	    		    			}
	    		    			current.setOilStuff(currentOilStuff);
	    		    			GearBoxType currentGearBoxType=(GearBoxType)current.getGearBoxType();
	    		    			if (currentGearBoxType.getId()==0){
	    		    				currentGearBoxType=gearBoxTypeDAO.createGearBoxType(currentGearBoxType.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentGearBoxType, "Загрузили из Excel"));
	    		    			}
	    		    			current.setGearBoxType(currentGearBoxType);
	    		    			if (currentManufacturer.getId()==0){
	    		    				currentManufacturer=manufacturerDAO.createManufacturer(currentManufacturer.getName(),country.getId());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentManufacturer, "Загрузили из Excel"));
	    		    				current.setManufacturer(currentManufacturer);
	    		    			}
	    		    			current.setManufacturer(currentManufacturer);
	    		        		current = gearBoxOilDAO.createGearBoxOil(current); 		
	    		        		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),current, "Загрузили из Excel"));
			    			}
			    		}
	    			}else if ("MotorOils".equals(good)){
    					ArrayList<MotorOil> list = importFromExcelMotorOils(convertMultipartFile(fileExcel)
    							,session.getServletContext().getRealPath("/"));
    				
	    			    synchronized (list){
	    		        	for (MotorOil current:list){
	    		        		if (!Service.isFileExist(current.getPhoto())){
	    		        			errors.add("Указанный Вами файл c изображением не существует");
	    		        		}
	    		        		current.setPhoto(Service.copyPhoto(current.getPhoto(), session.getServletContext().getRealPath("/")));
	    		        		Manufacturer currentManufacturer=(Manufacturer)current.getManufacturer();	
	    		        		if (currentManufacturer==null){
	    		        			currentManufacturer=new Manufacturer();
	    		        		}
	    		    			Country country=(Country)currentManufacturer.getCountry();
	    		    			if (country.getId()==0){
	    		    				country=countryDAO.createCountry(country.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),country, "Загрузили из Excel"));
	    		    				currentManufacturer.setCountry(country);
	    		    			}
	    		    			OilStuff currentOilStuff=(OilStuff)current.getOilStuff();
	    		    			if (currentOilStuff.getId()==0){
	    		    				currentOilStuff=oilStuffDAO.createOilStuff(currentOilStuff.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentOilStuff, "Загрузили из Excel"));
	    		    			}
	    		    			current.setOilStuff(currentOilStuff);
	    		    			EngineType currentEngineType=(EngineType)current.getEngineType();
	    		    			if (currentEngineType.getId()==0){
	    		    				currentEngineType=engineTypeDAO.createEngineType(currentEngineType.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentEngineType, "Загрузили из Excel"));
	    		    			}
	    		    			current.setEngineType(currentEngineType);
	    		    			if (currentManufacturer.getId()==0){
	    		    				currentManufacturer=manufacturerDAO.createManufacturer(currentManufacturer.getName(),country.getId());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentManufacturer, "Загрузили из Excel"));
	    		    				current.setManufacturer(currentManufacturer);
	    		    			}
	    		    			current.setManufacturer(currentManufacturer);
	    		        		current = motorOilDAO.createMotorOil(current); //breakingFluidDAO		
	    		        		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),current, "Загрузили из Excel"));
			    			}
			    		}
	    			}else if ("BrakingFluids".equals(good)){
    					ArrayList<BrakingFluid> list = importFromExcelBrakingFluids(convertMultipartFile(fileExcel)
    							,session.getServletContext().getRealPath("/"));
    				
	    			    synchronized (list){
	    		        	for (BrakingFluid currentBF:list){
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
			//}
		}else if ("Price".equals(variant)){
			String priceFile=fileExcel.getOriginalFilename().trim();
//			if (!Service.isFileExist(priceFile)){
//				errors.add("Указанный Вами файл с ценами не существует");
//    		}else{
    			if (!priceFile.contains(".xlsx")){
    				errors.add("Указанный Вами файл с ценами не соответствует формату. Используйте Excel-файл с расширением *.xlsx");
    			}else{
					ArrayList<BrakingFluid> list = importFromExcelPrices(convertMultipartFile(fileExcel));   
		 	        synchronized (list){
		 	        	for (BrakingFluid current:list){
		 	        		Price price = new Price();
		 	        		if ("MotorOils".equals(good)){
		 	        			price.setGood(motorOilDAO.getMotorOilByName(current.getName()));
		 	        		}else if ("BrakingFluids".equals(good)){
		 	        			price.setGood(brakingFluidDAO.getBrakingFluidByName(current.getName()));
		 	        		}else if ("GearBoxOils".equals(good)){
		 	        			price.setGood(gearBoxOilDAO.getGearBoxOilByName(current.getName()));
		 	        		}
		 	        		price.setPrice(current.getPrice());  //это новая загруженная цена. Далее её сравним с той, что была. 
		 	        		price.setTime(new GregorianCalendar().getTime());
		 	        		price.setUser(user);
		 	        		if (price.getGood().getId()>0){  //только для существующей номенклатуры. Остальную игнорируем
		 	        			if (price.getPrice()!=price.getGood().getPrice()){
				 	        		if ("MotorOils".equals(good)){					//её сравним с той, что была
			 	        				price.getGood().setPrice(price.getPrice());										//запишем цены в карточку товара...
			 	        				MotorOil value = motorOilDAO.fillPrices((MotorOil)price.getGood());
					 	        		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), value, "Загрузили из Excel, изменение цен"));

					 	        		price=priceDAO.createPrice(price, Service.MOTOR_OIL_PREFIX);					//...и в историю изменения цены
						        		log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), price, "Загрузили из Excel, изменение цен"));
				 	        		}else if ("GearBoxOils".equals(good)){					//её сравним с той, что была
			 	        				price.getGood().setPrice(price.getPrice());										//запишем цены в карточку товара...
			 	        				GearBoxOil value = gearBoxOilDAO.fillPrices((GearBoxOil)price.getGood());
					 	        		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), value, "Загрузили из Excel, изменение цен"));

					 	        		price=priceDAO.createPrice(price, Service.GEARBOX_OIL_PREFIX);					//...и в историю изменения цены
						        		log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), price, "Загрузили из Excel, изменение цен"));
				 	        		}else if ("BrakingFluids".equals(good)){
				 	        			BrakingFluid value = brakingFluidDAO.fillPrices((BrakingFluid)price.getGood());  //запишем цены в карточку товара...
						        		value = brakingFluidDAO.fillPrices(current);
					        			Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), value, "Загрузили из Excel, изменение цен"));

					        			price=priceDAO.createPrice(price, Service.BRAKING_FLUID_PREFIX);				//...и в историю изменения цены
					        			log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), price, "Загрузили из Excel, изменение цен"));
					 	        	}
		 	        			}
		 	        		}
    					}
					}			
    			}
    		//}
		}
		
		return errors;
	}

	public static void listManufacturersExcel(String variant, ManufacturerTemplate manufacturerDAO, LogTemplate logDAO, HttpSession session
			, HttpServletRequest request, HttpServletResponse response) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Производители");
		HSSFCellStyle style = workbook.createCellStyle();
		 
		Row row = sheet.createRow(0);                                       //шапка
		style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		
		style.setBorderBottom(HSSFBorderFormatting.BORDER_MEDIUM);
		Cell cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Производитель");
		sheet.setColumnWidth(0, 10000);
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("Полное название производителя");
		sheet.setColumnWidth(1, 20000);
		
		style = workbook.createCellStyle();
		style.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		int numrow=1; 
		ArrayList<Manufacturer> listManufacturers = manufacturerDAO.getManufacturers();   //данные
		for (Manufacturer manufacturer:listManufacturers){
			row = sheet.createRow(numrow++);
			
			cell = row.createCell(0);                               
			cell.setCellStyle(style);
			cell.setCellValue((String)manufacturer.getName());
			cell = row.createCell(1);
			cell.setCellStyle(style);
			cell.setCellValue((String)manufacturer.getFullName());			
		}
		
		for (int i=0; i<2;i++){															 //2 пустые строки внизу		
			row = sheet.createRow(numrow++);
			
			for (int j=0; j<2;j++){
				cell = row.createCell(j);                               
				cell.setCellStyle(style);
				cell.setCellValue((String)"");
			}
		}
		
	    try {
	    	if (Desktop.isDesktopSupported()) {
	    		File f = new File(session.getServletContext().getRealPath("/")+"Manufacturers.xls");
		    	workbook.write(new FileOutputStream(f));
		    	workbook.close();
		        
		    	Desktop desktop = Desktop.getDesktop();
		    	desktop.edit(f);
		    	f.delete();
	    	}
	    	
	    	
	    } catch (IOException e) {
		   e.printStackTrace();
	    }
		//----------------------------------------------------------
//	    try {
//   		 File f = new File(session.getServletContext().getRealPath("/")+"Manufacturers.xls");
//   		 workbook.write(new FileOutputStream(f));
//		    workbook.close();
//            response.setContentType("application/vnd.ms-excel");
//	    // experiment with either inline or attachment. IE settings can override this behavior.
//            response.setHeader("Content-disposition", "inline;filename="+"Manufacturers.xls");
//            // response.setHeader("Content-disposition", "attachment;filename=inline.xls");
//            f.delete();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//		
		
		String file = request.getParameter( "xls" ) ;
	    response.setContentType( "application/vnd.ms-excel" );
	    response.setHeader( "Content-Disposition", "inline; filename=\"Manufacturers.xls\"");
	    try {
			OutputStream out = response.getOutputStream();
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	    
	 
	}
	
	public static void PriceExcel(String goodPrefix, BrakingFluidTemplate brakingFluidDAO, MotorOilTemplate motorOilDAO
			, GearBoxOilTemplate gearBoxOilDAO, LogTemplate logDAO, HttpSession session) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Прайс-лист");
		sheet.createRow(0);
		
		if ((goodPrefix.equals(Service.BRAKING_FLUID_PREFIX)) || (goodPrefix.length()==0)){ 
			createPriceBrakingFluidExcel("Тормозные жидкости", workbook, sheet, brakingFluidDAO);
		}
		if ((goodPrefix.equals(Service.MOTOR_OIL_PREFIX)) || (goodPrefix.length()==0)){
			createPriceBrakingFluidExcel("Моторные масла", workbook, sheet, motorOilDAO);
		}
		if ((goodPrefix.equals(Service.GEARBOX_OIL_PREFIX)) || (goodPrefix.length()==0)){
			createPriceGearBoxOilExcel("Трансмиссионные масла", workbook, sheet, gearBoxOilDAO);	
		}
		
	    try {
	    	if (Desktop.isDesktopSupported()) {
		    	File f = new File(session.getServletContext().getRealPath("/")+"Price.xls");
		    	workbook.write(new FileOutputStream(f));
		    	workbook.close();
		        
		    	Desktop desktop = Desktop.getDesktop();
		    	desktop.edit(f);
		    	f.delete();
	    	}
	    } catch (IOException e) {
		   e.printStackTrace();
	    }
	}
	
	private static int getDiscountRow(String priceName, HSSFWorkbook workbook, HSSFSheet sheet){
		int result=sheet.getLastRowNum();
		Row row = sheet.createRow(result+1);									//строка со скидкой
		
		Cell cell = row.createCell(0);  
		HSSFFont hSSFFontBlack = workbook.createFont();
		hSSFFontBlack.setFontName(HSSFFont.FONT_ARIAL);
		hSSFFontBlack.setFontHeightInPoints((short) 12);
		hSSFFontBlack.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		hSSFFontBlack.setColor(HSSFColor.BLACK.index);
        
        HSSFCellStyle styleBlack = workbook.createCellStyle();
        styleBlack.setAlignment(CellStyle.ALIGN_LEFT);
        styleBlack.setFont(hSSFFontBlack);
        
        cell.setCellStyle(styleBlack);
        cell.setCellValue(priceName);
		
		cell = row.createCell(4);  
		HSSFFont hSSFFont = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 12);
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        hSSFFont.setColor(HSSFColor.BLUE.index);
        
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(hSSFFont);
        
        cell.setCellStyle(style);
        cell.setCellValue("Ваша скидка, %");
        
        cell = row.createCell(5);
        cell.setCellStyle(style);
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue((double)0);
        result=sheet.getLastRowNum()+1;
        
        row = sheet.createRow(result+1);									//пустая строка
        
        return result;
	}
	
	private static void createHeader(HSSFWorkbook workbook, HSSFSheet sheet){
		int numRow=sheet.getLastRowNum()+1;

		Row row = sheet.createRow(numRow++);									//Шапка
		row.setHeight((short)700);
        
		HSSFFont hSSFFont  = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 10);
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		style.setWrapText(true);
        
        style.setFont(hSSFFont);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue("Производитель/Бренд");
        sheet.setColumnWidth(0, 10000);

        cell = row.createCell(1);
        cell.setCellStyle(style);
        cell.setCellValue("Номер/код по производителю");
        sheet.setColumnWidth(1, 12000);
        
        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("Наличие");
        sheet.setColumnWidth(2, 4000);
        
        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("Кратн.");
        sheet.setColumnWidth(3, 4000);
        
        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("Цена, руб.");
        sheet.setColumnWidth(4, 4000);
        
     	HSSFFont hSSFFontBlue  = workbook.createFont();
		hSSFFontBlue.setFontName(HSSFFont.FONT_ARIAL);
		hSSFFontBlue.setFontHeightInPoints((short) 10);
		hSSFFontBlue.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		hSSFFontBlue.setColor(HSSFColor.BLUE.index);
		
        HSSFCellStyle styleBlue = workbook.createCellStyle();
        styleBlue.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        styleBlue.setAlignment(CellStyle.ALIGN_CENTER);
        styleBlue.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
        styleBlue.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
        styleBlue.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
        styleBlue.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
        styleBlue.setFont(hSSFFontBlue);
        styleBlue.setWrapText(true);
        
        cell = row.createCell(5);
        cell.setCellStyle(styleBlue);
        cell.setCellValue("Цена со скидкой, руб.");
        sheet.setColumnWidth(5, 4000);
        
        cell = row.createCell(6);
        cell.setCellStyle(style);
        cell.setCellValue("Описание");
        sheet.setColumnWidth(6, 20000);
	}
	
	private static void createTableData(HSSFWorkbook workbook, HSSFSheet sheet, int discountRow, ArrayList<InterfaceGood> listGoods){
		int numRow=sheet.getLastRowNum()+1;
		
		HSSFFont hSSFFont  = workbook.createFont();
        hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
        hSSFFont.setFontHeightInPoints((short) 10);
        hSSFFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
  	
	    HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(hSSFFont);
		style.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
        style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);

	    HSSFCellStyle styleRight = workbook.createCellStyle();
	    styleRight.setFont(hSSFFont);
	    styleRight.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
	    styleRight.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
	    styleRight.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
	    styleRight.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
	    styleRight.setVerticalAlignment(CellStyle.VERTICAL_TOP);
	    styleRight.setAlignment(CellStyle.ALIGN_RIGHT);
	    styleRight.setWrapText(true);
		
		HSSFFont hSSFFontBlue  = workbook.createFont();
		hSSFFontBlue.setFontName(HSSFFont.FONT_ARIAL);
		hSSFFontBlue.setFontHeightInPoints((short) 10);
		hSSFFontBlue.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		hSSFFontBlue.setColor(HSSFColor.BLUE.index);
  	
	    HSSFCellStyle styleBlue = workbook.createCellStyle();
	    styleBlue.setFont(hSSFFontBlue);
	    styleBlue.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
	    styleBlue.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
	    styleBlue.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
	    styleBlue.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);		
	    styleBlue.setVerticalAlignment(CellStyle.VERTICAL_TOP);
	    styleBlue.setAlignment(CellStyle.ALIGN_RIGHT);
	    styleBlue.setWrapText(true);
        
		
		for(InterfaceGood good:listGoods){
			Row row = sheet.createRow(numRow++);

			Cell cell = row.createCell(0);                               
			cell.setCellStyle(style);
			cell.setCellValue((String)((Manufacturer)good.getManufacturer()).getName());

			cell = row.createCell(1);                               
			cell.setCellStyle(style);
			cell.setCellValue((String)good.getManufacturerCode());
			
			cell = row.createCell(2); 
			cell.setCellStyle(styleRight);
			int inStock=good.getInStock();
			if (inStock>10){
				cell.setCellValue((String)"10>");
			}else {
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue((int)inStock);
			}
			
			cell = row.createCell(3);
			cell.setCellStyle(styleRight);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue((int)1);						
			
			cell = row.createCell(4);
			cell.setCellStyle(styleRight);
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue((double)good.getPriceWithDiscount());	
			
			cell = row.createCell(5);
			cell.setCellStyle(styleBlue);
			cell.setCellValue((double)0); //good.getPriceWithDiscount()
			String strFormula= "E"+(numRow)+"-(E"+(numRow)+"/100*F"+discountRow+")";
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(strFormula);

			cell = row.createCell(6);
			style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
			cell.setCellStyle(style);
			cell.setCellValue((String)good.getDescription());	
		}
		sheet.createRow(numRow++);
	}

	private static void createPriceGearBoxOilExcel(String priceName, HSSFWorkbook workbook, HSSFSheet sheet, GearBoxOilTemplate gearBoxOilDAO) {
		int  discountRow = getDiscountRow(priceName, workbook, sheet);
		createHeader(workbook,sheet);
		createTableData(workbook,sheet, discountRow, new ArrayList(gearBoxOilDAO.getGearBoxOils()));
	}

	private static void createPriceBrakingFluidExcel(String priceName, HSSFWorkbook workbook, HSSFSheet sheet, MotorOilTemplate motorOilDAO) {
		int  discountRow = getDiscountRow(priceName, workbook, sheet);
		createHeader(workbook,sheet);
		createTableData(workbook,sheet, discountRow,  new ArrayList(motorOilDAO.getMotorOils()));		
	}

	private static void createPriceBrakingFluidExcel(String priceName, HSSFWorkbook workbook, HSSFSheet sheet, BrakingFluidTemplate brakingFluidDAO) {
		int  discountRow = getDiscountRow(priceName, workbook, sheet);
		createHeader(workbook,sheet);
		createTableData(workbook,sheet, discountRow,  new ArrayList(brakingFluidDAO.getBrakingFluids()));		
	}
	
	
	public static void createGoodCard(BrakingFluid good, HttpSession session){
		String sheetName=((Manufacturer)good.getManufacturer()).getName()+" "+good.getManufacturerCode();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);
		
		int numRow=0; 	//так будет потом легче редактировать, не надо думать о нумерации строк
		
		Row row = sheet.createRow(numRow++);                                       //шапка
		HSSFCellStyle styleYellow = workbook.createCellStyle();
		styleYellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		styleYellow.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleYellow.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setWrapText(true);
		styleYellow.setVerticalAlignment(CellStyle.VERTICAL_TOP);


		Cell cell = row.createCell(0);                               
		cell.setCellStyle(styleYellow);
		cell.setCellValue("Производитель");
		sheet.setColumnWidth(0, 10000);
		
		cell = row.createCell(1);
		cell.setCellStyle(styleYellow);
		cell.setCellValue(((Manufacturer)good.getManufacturer()).getName());
		sheet.setColumnWidth(1, 20000);
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(styleYellow);
		cell.setCellValue("Номер/код по производителю");
		
		cell = row.createCell(1);
		cell.setCellStyle(styleYellow);
		cell.setCellValue(good.getManufacturerCode());

		HSSFCellStyle style = workbook.createCellStyle();		
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		style.setWrapText(true);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);

		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Класс жидкости");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(((FluidClass)good.getFluidClass()).getName());
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Температура кипения \"сухой\" жидкости, °С");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		if (good.getBoilingTemperatureDry()!=0){
			cell.setCellValue((double)good.getBoilingTemperatureDry());
		}else{
			cell.setCellValue("no");
		}
				
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Температура кипения \"увлажненной\" жидкости, °С");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		if (good.getBoilingTemperatureWet()!=0){
			cell.setCellValue((double)good.getBoilingTemperatureWet());
		}else{
			cell.setCellValue("no");
		}
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Вязкость при -40°С, мм²/с");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		if (good.getViscosity40()!=0){
			cell.setCellValue((double)good.getViscosity40());
		}else{
			cell.setCellValue("no");
		}		

		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Вязкость при 100°С, мм²/с");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		if (good.getViscosity100()!=0){
			cell.setCellValue((double)good.getViscosity100());
		}else{
			cell.setCellValue("no");
		}		
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Объём, л");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		if (good.getValue()!=0){
			cell.setCellValue((double)good.getValue());
		}else{
			cell.setCellValue("no");
		}				
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Описание");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(good.getDescription());
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Photo 1");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("");	
			
		try {
				
		
			InputStream inputStream;
			inputStream = new FileInputStream(session.getServletContext().getRealPath("/")+"resources\\jpg\\"+good.getPhoto());
		   //Get the contents of an InputStream as a byte[].
		    byte[] bytes = IOUtils.toByteArray(inputStream);
		   //Adds a picture to the workbook
		    int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		   //close the input stream
		    inputStream.close();

		   //Returns an object that handles instantiating concrete classes
		    CreationHelper helper = workbook.getCreationHelper();

		   //Creates the top-level drawing patriarch.
		    Drawing drawing = sheet.createDrawingPatriarch();

		   //Create an anchor that is attached to the worksheet
		    ClientAnchor anchor = helper.createClientAnchor();
		   //set top-left corner for the image
		    anchor.setCol1(1);
		    anchor.setRow1(sheet.getLastRowNum());

		   //Creates a picture
		    Picture pict = drawing.createPicture(anchor, pictureIdx);

		    short rowHeight = (short) (pict.getImageDimension().getHeight()*20);
		    row.setHeight(rowHeight);			   
		   //Reset the image to the original size
		    pict.resize();
	
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Photo 2");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("no");				
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Photo 3");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("np");			
		
		
	    try {
	    	if (Desktop.isDesktopSupported()) {
		    	File f = new File(session.getServletContext().getRealPath("/")+sheetName+".xls");
		    	workbook.write(new FileOutputStream(f));
		    	workbook.close();
		        
		    	Desktop desktop = Desktop.getDesktop();
		    	desktop.edit(f);
		    	f.delete();
	    	}
	    } catch (IOException e) {
		   e.printStackTrace();
	    }		
		
	}
	
	public static void createGoodCard(MotorOil good, HttpSession session){
		String sheetName=((Manufacturer)good.getManufacturer()).getName()+" "+good.getManufacturerCode();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);
		
		int numRow=0; 	//так будет потом легче редактировать, не надо думать о нумерации строк
		
		Row row = sheet.createRow(numRow++);                                       //шапка
		HSSFCellStyle styleYellow = workbook.createCellStyle();
		styleYellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		styleYellow.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleYellow.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setWrapText(true);
		styleYellow.setVerticalAlignment(CellStyle.VERTICAL_TOP);


		Cell cell = row.createCell(0);                               
		cell.setCellStyle(styleYellow);
		cell.setCellValue("Производитель");
		sheet.setColumnWidth(0, 10000);
		
		cell = row.createCell(1);
		cell.setCellStyle(styleYellow);
		cell.setCellValue(((Manufacturer)good.getManufacturer()).getName());
		sheet.setColumnWidth(1, 20000);
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(styleYellow);
		cell.setCellValue("Номер/код по производителю");
		
		cell = row.createCell(1);
		cell.setCellStyle(styleYellow);
		cell.setCellValue(good.getManufacturerCode());

		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		style.setWrapText(true);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);


		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Тип двигателя");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(((EngineType)good.getEngineType()).getName());
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Состав масла");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(((OilStuff)good.getOilStuff()).getName());
				
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Вязкость, мм²/с");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue((String)good.getViscosity());

		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Объём, л");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		if (good.getValue()!=0){
			cell.setCellValue((double)good.getValue());
		}else{
			cell.setCellValue("no");
		}				
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Описание");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(good.getDescription());
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Photo 1");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("");	
			
		try {
		
			InputStream inputStream;
			inputStream = new FileInputStream(session.getServletContext().getRealPath("/")+"resources\\jpg\\"+good.getPhoto());
		   //Get the contents of an InputStream as a byte[].
		    byte[] bytes = IOUtils.toByteArray(inputStream);
		   //Adds a picture to the workbook
		    int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		   //close the input stream
		    inputStream.close();

		   //Returns an object that handles instantiating concrete classes
		    CreationHelper helper = workbook.getCreationHelper();

		   //Creates the top-level drawing patriarch.
		    Drawing drawing = sheet.createDrawingPatriarch();

		   //Create an anchor that is attached to the worksheet
		    ClientAnchor anchor = helper.createClientAnchor();
		   //set top-left corner for the image
		    anchor.setCol1(1);
		    anchor.setRow1(sheet.getLastRowNum());

		   //Creates a picture
		    Picture pict = drawing.createPicture(anchor, pictureIdx);

		    short rowHeight = (short) (pict.getImageDimension().getHeight()*20);
		    row.setHeight(rowHeight);			   
		   //Reset the image to the original size
		    pict.resize();
	
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Photo 2");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("no");				
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Photo 3");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("np");			
		
	    try {
	    	if (Desktop.isDesktopSupported()) {
		    	File f = new File(session.getServletContext().getRealPath("/")+sheetName+".xls");
		    	workbook.write(new FileOutputStream(f));
		    	workbook.close();
		        
		    	Desktop desktop = Desktop.getDesktop();
		    	desktop.edit(f);
		    	f.delete();
	    	}
	    } catch (IOException e) {
		   e.printStackTrace();
	    }				
	}

	public static void createGoodCard(GearBoxOil good, HttpSession session){
		String sheetName=((Manufacturer)good.getManufacturer()).getName()+" "+good.getManufacturerCode();
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(sheetName);
		
		int numRow=0; 	//так будет потом легче редактировать, не надо думать о нумерации строк
		
		Row row = sheet.createRow(numRow++);                                       //шапка
		HSSFCellStyle styleYellow = workbook.createCellStyle();
		styleYellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		styleYellow.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styleYellow.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		styleYellow.setWrapText(true);
		styleYellow.setVerticalAlignment(CellStyle.VERTICAL_TOP);


		Cell cell = row.createCell(0);                               
		cell.setCellStyle(styleYellow);
		cell.setCellValue("Производитель");
		sheet.setColumnWidth(0, 10000);
		
		cell = row.createCell(1);
		cell.setCellStyle(styleYellow);
		cell.setCellValue(((Manufacturer)good.getManufacturer()).getName());
		sheet.setColumnWidth(1, 20000);
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(styleYellow);
		cell.setCellValue("Номер/код по производителю");
		
		cell = row.createCell(1);
		cell.setCellStyle(styleYellow);
		cell.setCellValue(good.getManufacturerCode());

		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setBorderTop(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderLeft(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderRight(HSSFBorderFormatting.BORDER_THIN);
		style.setBorderBottom(HSSFBorderFormatting.BORDER_THIN);
		style.setWrapText(true);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);


		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Тип коробки передач");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(((GearBoxType)good.getGearBoxType()).getName());
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Состав масла");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(((OilStuff)good.getOilStuff()).getName());		
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Вязкость при -40°С, мм²/с");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(good.getViscosity());
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Объём, л");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		if (good.getValue()!=0){
			cell.setCellValue((double)good.getValue());
		}else{
			cell.setCellValue("no");
		}				
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Описание");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue(good.getDescription());
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Photo 1");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("");	
			
		try {
			InputStream inputStream;
			inputStream = new FileInputStream(session.getServletContext().getRealPath("/")+"resources\\jpg\\"+good.getPhoto());
		   //Get the contents of an InputStream as a byte[].
		    byte[] bytes = IOUtils.toByteArray(inputStream);
		   //Adds a picture to the workbook
		    int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
		   //close the input stream
		    inputStream.close();

		   //Returns an object that handles instantiating concrete classes
		    CreationHelper helper = workbook.getCreationHelper();

		   //Creates the top-level drawing patriarch.
		    Drawing drawing = sheet.createDrawingPatriarch();

		   //Create an anchor that is attached to the worksheet
		    ClientAnchor anchor = helper.createClientAnchor();
		   //set top-left corner for the image
		    anchor.setCol1(1);
		    anchor.setRow1(sheet.getLastRowNum());

		   //Creates a picture
		    Picture pict = drawing.createPicture(anchor, pictureIdx);
		    

		    short rowHeight = (short) (pict.getImageDimension().getHeight()*20);
		    row.setHeight(rowHeight);			   
		   //Reset the image to the original size
		    pict.resize();
	
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Photo 2");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("no");				
		
		row = sheet.createRow(numRow++);  
		cell = row.createCell(0);                               
		cell.setCellStyle(style);
		cell.setCellValue("Photo 3");
		
		cell = row.createCell(1);
		cell.setCellStyle(style);
		cell.setCellValue("np");			
		
	    try {
	    	if (Desktop.isDesktopSupported()) {
		    	File f = new File(session.getServletContext().getRealPath("/")+sheetName+".xls");
		    	workbook.write(new FileOutputStream(f));
		    	workbook.close();
		        
		    	Desktop desktop = Desktop.getDesktop();
		    	desktop.edit(f);
		    	f.delete();
	    	}
	    } catch (IOException e) {
		   e.printStackTrace();
	    }				
	}	
	
}
