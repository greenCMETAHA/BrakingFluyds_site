package eftech.workingset.Services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import eftech.workingset.DAO.templates.BrakingFluidTemplate;
import eftech.workingset.DAO.templates.CountryTemplate;
import eftech.workingset.DAO.templates.EngineTypeTemplate;
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.LogTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.MotorOilTemplate;
import eftech.workingset.DAO.templates.OilStuffTemplate;
import eftech.workingset.DAO.templates.PriceTemplate;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Country;
import eftech.workingset.beans.EngineType;
import eftech.workingset.beans.FluidClass;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;
import eftech.workingset.beans.MotorOil;
import eftech.workingset.beans.OilStuff;
import eftech.workingset.beans.Price;
import eftech.workingset.beans.User;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

public class DownloadDataFromExcel {
	
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
		 //������ ��� .xlsx !!!!  .xls ���� ������������� ����� HSSF
		
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

                for (Cell cell : row) { //���������� �������� ������
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
            	//������ �������� ������.
            	
            	//System.out.println(currentBF.toString());
            	if (currentBF.getName().length()>0)
            		list.add(currentBF);
            }
        }
		
		return list;
	}
	
	public static ArrayList<MotorOil> importFromExcelMotorOils(File path, String GlobalPath){
		 //������ ��� .xlsx !!!!  .xls ���� ������������� ����� HSSF
		
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

               for (Cell cell : row) { //���������� �������� ������
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
           	//������ �������� ������.
           	
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
		
		ArrayList<BrakingFluid> list = new ArrayList<BrakingFluid>(); //���������� � ��� MotorOil: ������������ � ���� ��� ����������
		
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            for (Row row : sheet) {
            	BrakingFluid current = new BrakingFluid();

                for (Cell cell : row) { //���������� �������� ������
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
            	//������ �������� ������.
            	
            	//System.out.println(currentBF.toString());
            	if (current.getName().length()>0)	//��������� ������, � ������� �� ��������� ������������
            		list.add(current);
            }
        }
 
		return list;
	}
		
	
	public static ArrayList<String> downloadExcel(String variant,User user, String good, MultipartFile fileExcel
			,CountryTemplate countryDAO, ManufacturerTemplate manufacturerDAO, FluidClassTemplate fluidClassDAO, BrakingFluidTemplate brakingFluidDAO,
			OilStuffTemplate oilStuffDAO, EngineTypeTemplate engineTypeDAO, MotorOilTemplate motorOilDAO, LogTemplate logDAO, PriceTemplate priceDAO
			, HttpSession session) {
		ArrayList<String> errors=new ArrayList<String>();
		
		 if ("Product".equals(variant)){
			String productFile=fileExcel.getOriginalFilename().trim();
//			if (!Service.isFileExist(productFile)){
//				errors.add("��������� ���� ���� � ������������� �� ����������");
//    		}else{
    			if (!productFile.contains(".xlsx")){
    				errors.add("��������� ���� ���� � ������������� �� ������������� �������. ����������� Excel-���� � ����������� *.xlsx");
    			}else{
    				if ("MotorOils".equals(good)){
    					ArrayList<MotorOil> list = importFromExcelMotorOils(convertMultipartFile(fileExcel)
    							,session.getServletContext().getRealPath("/"));
    				
	    			    synchronized (list){
	    		        	for (MotorOil current:list){
	    		        		if (!Service.isFileExist(current.getPhoto())){
	    		        			errors.add("��������� ���� ���� c ������������ �� ����������");
	    		        		}
	    		        		current.setPhoto(Service.copyPhoto(current.getPhoto(), session.getServletContext().getRealPath("/")));
	    		        		Manufacturer currentManufacturer=(Manufacturer)current.getManufacturer();	    		        		
	    		    			Country country=(Country)currentManufacturer.getCountry();
	    		    			if (country.getId()==0){
	    		    				country=countryDAO.createCountry(country.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),country, "��������� �� Excel"));
	    		    				currentManufacturer.setCountry(country);
	    		    			}
	    		    			OilStuff currentOilStuff=(OilStuff)current.getOilStuff();
	    		    			if (currentOilStuff.getId()==0){
	    		    				currentOilStuff=oilStuffDAO.createOilStuff(currentOilStuff.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentOilStuff, "��������� �� Excel"));
	    		    			}
	    		    			current.setOilStuff(currentOilStuff);
	    		    			EngineType currentEngineType=(EngineType)current.getEngineType();
	    		    			if (currentEngineType.getId()==0){
	    		    				currentEngineType=engineTypeDAO.createEngineType(currentEngineType.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentEngineType, "��������� �� Excel"));
	    		    			}
	    		    			current.setEngineType(currentEngineType);
	    		    			if (currentManufacturer.getId()==0){
	    		    				currentManufacturer=manufacturerDAO.createManufacturer(currentManufacturer.getName(),country.getId());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentManufacturer, "��������� �� Excel"));
	    		    				current.setManufacturer(currentManufacturer);
	    		    			}
	    		    			current.setManufacturer(currentManufacturer);
	    		        		current = motorOilDAO.createMotorOil(current); //breakingFluidDAO		
	    		        		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),current, "��������� �� Excel"));
			    			}
			    		}
	    			}else if ("BrakingFluids".equals(good)){
    					ArrayList<BrakingFluid> list = importFromExcelBrakingFluids(convertMultipartFile(fileExcel)
    							,session.getServletContext().getRealPath("/"));
    				
	    			    synchronized (list){
	    		        	for (BrakingFluid currentBF:list){
	    		        		if (!Service.isFileExist(currentBF.getPhoto())){
	    		        			errors.add("��������� ���� ���� c ������������ �� ����������");
	    		        		}
	    		        		currentBF.setPhoto(Service.copyPhoto(currentBF.getPhoto(), session.getServletContext().getRealPath("/")));
	    		        		Manufacturer currentManufacturer=(Manufacturer)currentBF.getManufacturer();
	    		    			FluidClass currentFluidClass=(FluidClass)currentBF.getFluidClass();
	    		    			Country country=(Country)currentManufacturer.getCountry();
	    		    			if (country.getId()==0){
	    		    				country=countryDAO.createCountry(country.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),country, "��������� �� Excel"));
	    		    				currentManufacturer.setCountry(country);
	    		    			}
	    		    			if (currentManufacturer.getId()==0){
	    		    				currentManufacturer=manufacturerDAO.createManufacturer(currentManufacturer.getName(),country.getId());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentManufacturer, "��������� �� Excel"));
	    		    				currentBF.setManufacturer(currentManufacturer);
	    		    			}
	    		    			if (currentFluidClass.getId()==0){
	    		    				currentFluidClass=fluidClassDAO.createFluidClass(currentFluidClass.getName());
	    		    				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),currentFluidClass, "��������� �� Excel"));
	    		    				currentBF.setFluidClass(currentFluidClass);
	    		    			}		    			
	    		        		BrakingFluid value = brakingFluidDAO.createBrakingFluid(currentBF); //breakingFluidDAO		
	    		        		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(),value, "��������� �� Excel"));
			    			}
			    		}
	    			}
	        	}
			//}
		}else if ("Price".equals(variant)){
			String priceFile=fileExcel.getOriginalFilename().trim();
//			if (!Service.isFileExist(priceFile)){
//				errors.add("��������� ���� ���� � ������ �� ����������");
//    		}else{
    			if (!priceFile.contains(".xlsx")){
    				errors.add("��������� ���� ���� � ������ �� ������������� �������. ����������� Excel-���� � ����������� *.xlsx");
    			}else{
					ArrayList<BrakingFluid> list = importFromExcelPrices(convertMultipartFile(fileExcel));   
		 	        synchronized (list){
		 	        	for (BrakingFluid current:list){
		 	        		Price price = new Price();
		 	        		if ("MotorOils".equals(good)){
		 	        			price.setGood(motorOilDAO.getMotorOilByName(current.getName()));
		 	        		}else if ("BrakingFluids".equals(good)){
		 	        			price.setGood(brakingFluidDAO.getBrakingFluidByName(current.getName()));
		 	        		}
		 	        		price.setPrice(current.getPrice());  //��� ����� ����������� ����. ����� � ������� � ���, ��� ����. 
		 	        		price.setTime(new GregorianCalendar().getTime());
		 	        		price.setUser(user);
		 	        		if (price.getGood().getId()>0){  //������ ��� ������������ ������������. ��������� ����������
		 	        			if (price.getPrice()!=price.getGood().getPrice()){
				 	        		if ("MotorOils".equals(good)){					//� ������� � ���, ��� ����
			 	        				price.getGood().setPrice(price.getPrice());										//������� ���� � �������� ������...
			 	        				MotorOil value = motorOilDAO.fillPrices((MotorOil)price.getGood());
					 	        		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), value, "��������� �� Excel, ��������� ���"));

					 	        		price=priceDAO.createPrice(price, Service.MOTOR_OIL_PREFIX);					//...� � ������� ��������� ����
						        		log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), price, "��������� �� Excel, ��������� ���"));
				 	        		}else if ("BrakingFluids".equals(good)){
				 	        			BrakingFluid value = brakingFluidDAO.fillPrices((BrakingFluid)price.getGood());  //������� ���� � �������� ������...
						        		value = brakingFluidDAO.fillPrices(current);
					        			Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), value, "��������� �� Excel, ��������� ���"));

					        			price=priceDAO.createPrice(price, Service.BRAKING_FLUID_PREFIX);				//...� � ������� ��������� ����
					        			log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), price, "��������� �� Excel, ��������� ���"));
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
	
	
}
