package eftech.workingset.Services;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import eftech.workingset.DAO.templates.BrakingFluidTemplate;
import eftech.workingset.DAO.templates.ClientTemplate;
import eftech.workingset.DAO.templates.CountryTemplate;
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.LogTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.UserTemplate;
import eftech.workingset.beans.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.*;

public class Service {
	public static String PATH_TO_JPG="resources\\jpg\\";
	public static byte VARIANT_PRODUCT = 1;
	public static byte VARIANT_PRICES = 2;
	public static String EMPTY="<emptry>";
	public static String PHONE="phone";  //for table "information" , bean infoDAO
	public static String WEBSITE="website";
	public static String EMAIL="email";
	public static int ELEMENTS_IN_LIST = 4;  //��������� ���������� ��� ���������� //24 - ������. �� � �������� �� ������ 10 �������
	public static int LOG_ELEMENTS_IN_LIST = 50;  //��������� ���������� ��� ���������� //24 - ������. �� � �������� �� ������ 10 �������
	public static int ELEMENTS_IN_RECOMMENDED = 7; //���������� ������������ � "�������������"
	public static int ID_CUSTOMER = 7; //default customer
	public static int ID_EXECUTER = 6; //default executer
	
	@Autowired
	static BrakingFluidTemplate brakingFluidDAO;

	@Autowired
	UserTemplate userDAO;
	
	@Autowired
	static	ManufacturerTemplate manufacturerDAO;

	@Autowired
	static FluidClassTemplate fluidClassDAO;
	
	@Autowired
	ClientTemplate clientDAO;

	@Autowired
	static CountryTemplate countryDAO;
		
	
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
	
	public static ArrayList<BrakingFluid> importFromExcelProduct(File path, String GlobalPath){
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
		
		ArrayList<BrakingFluid> listBrakingFluids = new ArrayList<BrakingFluid>();
		
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
            		listBrakingFluids.add(currentBF);
            }
        }
//        try {
//			wb.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
    
		
		return listBrakingFluids;
	}
	
	public static boolean isFileExist(String file){
		File oldFile=new File(file);
		
		return oldFile.exists();	
		
	}

	public static synchronized String copyPhoto(String oldPhoto, String globalPath){
		String result="";
		
		if ((!"http".equals(oldPhoto.substring(0,4))) & (!":\\".equals(oldPhoto.substring(1,2)))){ //������, �� ��� ����� �� �������
			result=oldPhoto;
		}
		
		File oldFile=new File(oldPhoto);
		File newFile=new File(globalPath+PATH_TO_JPG+oldFile.getName());
		if (newFile.exists()){
			result=newFile.getName();
		}else{
			try {
				newFile.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			InputStream is = null;
		    OutputStream os = null;
		    try {
		        is = new FileInputStream(oldFile);
		        os = new FileOutputStream(newFile);
		        byte[] buffer = new byte[1024];
		        int length;
		        while ((length = is.read(buffer)) > 0) {
		            os.write(buffer, 0, length);
		        }
		        result=newFile.getName();
		    } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
		        try {
					is.close();
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
		    }
		}

		return result;
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
		
		ArrayList<BrakingFluid> listBrakingFluids = new ArrayList<BrakingFluid>();
		
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
						if (cell.getCellType()==cell.CELL_TYPE_NUMERIC){
							currentBF.setPrice(cell.getNumericCellValue());
						}						
						break;
					}
					default:
						break;
					}                	
                }
            	//������ �������� ������.
            	
            	//System.out.println(currentBF.toString());
            	if (currentBF.getName().length()>0)	//��������� ������, � ������� �� ��������� ������������
            		listBrakingFluids.add(currentBF);
            }
        }
//        try {
//			wb.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
 
		return listBrakingFluids;
	}
	
	
	private static PdfPTable createTableOffer(LinkedList<Basket> basket,BaseFont times, String globalPath, User user) throws DocumentException, MalformedURLException, IOException {
        PdfPTable table = null;
        float[] columnWidths			 = {60, 30, 20, 15, 15, 15, 15, 15, 40, 30, 15, 15, 30};
        float[] columnWidthsWithoutPrice = {60, 30, 20, 15, 15, 15, 15, 40, 30, 15, 15, 30};
        //table.setWidths ((user.canChangePrice()?columnWidths:columnWidthsWithoutPrice));
        if (user.canChangePrice()){
        	table = new PdfPTable(13);
        	table.setWidths(columnWidths);
        }else{
        	table = new PdfPTable(12);
        	table.setWidths(columnWidthsWithoutPrice);
        }
        
        PdfPCell cell=new PdfPCell(new Phrase("������������",new Font(times,8)));
        cell.setRotation(90);
        table.addCell(cell);
        cell.setPhrase(new Phrase("�������������",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("����� ��������",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("����������� ������� (�����)",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("����������� ������� (�������)",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("�����",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("����������",new Font(times,8)));
        table.addCell(cell);
        if (user.canChangePrice()){
        	cell.setPhrase(new Phrase("����",new Font(times,8)));
        	table.addCell(cell);
        }
        cell.setPhrase(new Phrase("��������",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("�����������",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("�������� (��� -40)",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("�������� (��� 100)",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("������������",new Font(times,8)));
        table.addCell(cell);
        
        for (Basket currentBasket:basket) {
        	BrakingFluid currentBR=currentBasket.getBrakingFluid();
            table.addCell(new Phrase(currentBR.getName(),new Font(times,8)));
            table.addCell(new Phrase(((Manufacturer)currentBR.getManufacturer()).getName(),new Font(times,8)));
            table.addCell(new Phrase(((FluidClass)currentBR.getFluidClass()).getName(),new Font(times,8)));
            table.addCell(new Phrase(""+currentBR.getBoilingTemperatureDry(),new Font(times,8)));
            table.addCell(new Phrase(""+currentBR.getBoilingTemperatureWet(),new Font(times,8)));
            table.addCell(new Phrase(""+currentBR.getValue(),new Font(times,8)));
            table.addCell(new Phrase(""+currentBasket.getQauntity(),new Font(times,8)));
            if (user.canChangePrice()){
            	table.addCell(new Phrase(""+currentBR.getPrice(),new Font(times,8)));
            }
            table.addCell(new Phrase(currentBR.getDescription(),new Font(times,8)));
            
            if (currentBR.hasPhoto()){
            	Image image = Image.getInstance(globalPath+PATH_TO_JPG+currentBR.getPhoto());
	            image.setAlignment(Image.MIDDLE);
	            image.scaleToFit(30, 30);
	            table.addCell(image);
	            
            }else{
            	table.addCell("");
            }
            
            table.addCell(new Phrase(""+currentBR.getViscosity40(),new Font(times,8)));
            table.addCell(new Phrase(""+currentBR.getViscosity100(),new Font(times,8)));
            table.addCell(new Phrase(currentBR.getSpecification(),new Font(times,8)));

        }
       return table;
    }
	
	public static File createPDF_BussinessOffer(LinkedList<Basket> basket, String globalPath, User user) throws DocumentException, IOException, MalformedURLException{
		File result=null;

		String filename="bussinessOffer.pdf";
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        BaseFont times = BaseFont.createFont("c:/windows/fonts/times.ttf","cp1251",BaseFont.EMBEDDED);
        		
        document.add(new Paragraph("������ ����.",new Font(times,14)));
        document.add(new Paragraph("���� �������� ������ �� ���������� ��� ��������� ��������� ��������:",new Font(times,14)));
        document.add(new Paragraph(" "));
        document.add(createTableOffer(basket,times, globalPath, user));
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("� ���������           _______________________            _______________",new Font(times,14)));
        		
        // step 5
        document.close();
        
        result=new File(filename);

        return result;
	}

	public static boolean createPDF_Demand(LinkedList<Basket> basket, String globalPath,User user) throws DocumentException, IOException, MalformedURLException{
		boolean result=false;

		String filename="bussinessDemand.pdf";
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        BaseFont times = BaseFont.createFont("c:/windows/fonts/times.ttf","cp1251",BaseFont.EMBEDDED);
        		
        document.add(new Paragraph("������ ����.",new Font(times,14)));
        document.add(new Paragraph("����� ������������ ��� ��������� ���� ���������:",new Font(times,14)));
        document.add(new Paragraph(" "));
        document.add(createTableOffer(basket,times, globalPath, user));
        document.add(new Paragraph(" "));
       
        document.add(new Paragraph("� ���������           _______________________            _______________",new Font(times,14)));
        		
        // step 5
        document.close();
        
    	if (Desktop.isDesktopSupported()) {
			Desktop.getDesktop().open(new File(filename));
		} else {
			System.out.println("Awt Desktop is not supported!");
		}
		
		return result;
	}	
	
	
	public static User getUser(Principal userPrincipal, LogTemplate logDAO, UserTemplate userDAO){
		User result=new User();
		if (userPrincipal!=null){
			result=userDAO.getUser(userPrincipal.getName());		//������ ��� ����� �������� �������� ��� ������������, � �� �����
		}
		
		return result;
	}
	
	public static String createAdminEdit(Model model, String variant, int currentPage
			, ManufacturerTemplate manufacturerDAO, FluidClassTemplate fluidClassDAO, CountryTemplate countryDAO
			,ClientTemplate clientDAO, UserTemplate userDAO,LogTemplate logDAO, LinkedList<String> errors){
		
		String result="ShowList";
		int totalRows=0;
		int totalPages=0;
		int elementsInList=Service.LOG_ELEMENTS_IN_LIST;
		model.addAttribute("tablehead", variant);
		if ((variant.compareTo("�������������")==0) || (variant.compareTo("manufacturer")==0)){
			totalRows=manufacturerDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",manufacturerDAO.getManufacturers(currentPage, elementsInList));
			model.addAttribute("variant", "manufacturer");
			
		}else if ((variant.compareTo("������ ��������")==0) || (variant.compareTo("fluidClass")==0)){
			totalRows=fluidClassDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",fluidClassDAO.getFluidClassis(currentPage, elementsInList));
			model.addAttribute("variant", "fluidClass");

		}else if ((variant.compareTo("������")==0) || (variant.compareTo("country")==0)){
			totalRows=countryDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",countryDAO.getCountries(currentPage, elementsInList));
			model.addAttribute("variant", "country");
			
		}else if ((variant.compareTo("�������")==0) || (variant.compareTo("client")==0)){
			totalRows=clientDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",clientDAO.getClients(currentPage, elementsInList));
			model.addAttribute("variant", "client");
			
		}else if ((variant.compareTo("������������")==0) || (variant.compareTo("user")==0)){
			totalRows=userDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",userDAO.getUsers(currentPage, elementsInList));
			model.addAttribute("variant", "user");
			
		}else if ((variant.compareTo("�����������")==0) || (variant.compareTo("log")==0)){
			totalRows=logDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",logDAO.getLog(currentPage, elementsInList));
			model.addAttribute("variant", "log");
			model.addAttribute("tablehead", "��� ���������");
		}
		if (totalPages<currentPage){
			currentPage=totalPages;
		}
		
		model.addAttribute("errors", errors);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("id", -1);		
		
		return result;
		
	
	}
	
	public static String getFormattedDate(Date date){
		String result="";
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);

		result=gc.get(gc.YEAR)+"-"; 
		result=result+((gc.get(gc.MONTH)+1)<10?"0":"")+(gc.get(gc.MONTH)+1)+"-";
		result=result+(gc.get(gc.DAY_OF_MONTH)<10?"0":"")+gc.get(gc.DAY_OF_MONTH);
		
		return  result;
	}
	
	

	public static boolean isDate(Date begin, Date end){
		boolean result=false;
		
		if (begin.getTime()<end.getTime()){
			result=true;
		}
		
		return result;
	}
	
}
