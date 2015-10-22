package eftech.workingset.Services;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import eftech.workingset.DAO.templates.DemandTemplate;
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.InfoTemplate;
import eftech.workingset.DAO.templates.LogTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.OfferStatusTemplate;
import eftech.workingset.DAO.templates.PayTemplate;
import eftech.workingset.DAO.templates.UserTemplate;
import eftech.workingset.beans.*;
import eftech.workingset.beans.intefaces.InterfaceClient;

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
	public static String ADMIN_EMAIL="adminEmail";
	public static String EMAIL="email";
	public static int ELEMENTS_IN_LIST = 4;  //начальное количество для педжинации //24 - основа. Но в тестовой БД только 10 записей
	public static int LOG_ELEMENTS_IN_LIST = 50;  //начальное количество для педжинации //24 - основа. Но в тестовой БД только 10 записей
	public static int ELEMENTS_IN_RECOMMENDED = 7; //количество номенклатуры в "Рекомендуемом"
	public static int ID_CUSTOMER = 7; //default customer
	public static int ID_EXECUTER = 6; //default executer
	
	public static String MARKETING_FIRM = "marketingFirm";  //эта фирма осуществляет продажу и забирает себе 5% от стоимости товара
	public static String MAIN_FIRM = "mainFirm";  //эта фирма предоставляет товар (условно: оптовый склад)
	public static int ID_EMPTY_CLIENT = 8;  //эта фирма предоставляет товар (условно: оптовый склад)
	
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
		
		ArrayList<BrakingFluid> listBrakingFluids = new ArrayList<BrakingFluid>();
		
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
		
		if ((!"http".equals(oldPhoto.substring(0,4))) & (!":\\".equals(oldPhoto.substring(1,2)))){ //значит, он уже лежит на серваке
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

                for (Cell cell : row) { //перебираем значения строки
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
            	//теперь собираем список.
            	
            	//System.out.println(currentBF.toString());
            	if (currentBF.getName().length()>0)	//пропустим строки, в которых не заполнено наименование
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
        
        PdfPCell cell=new PdfPCell(new Phrase("Наименование",new Font(times,8)));
        cell.setRotation(90);
        table.addCell(cell);
        cell.setPhrase(new Phrase("Производитель",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Класс жидкости",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Температура кипения (сухая)",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Температура кипения (влажная)",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Объём",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Количество",new Font(times,8)));
        table.addCell(cell);
        if (user.canChangePrice()){
        	cell.setPhrase(new Phrase("Цена",new Font(times,8)));
        	table.addCell(cell);
        }
        cell.setPhrase(new Phrase("Описание",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Изображение",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Вязкость (при -40)",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Вязкость (при 100)",new Font(times,8)));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Спецификация",new Font(times,8)));
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
        		
        document.add(new Paragraph("Добрый день.",new Font(times,14)));
        document.add(new Paragraph("Наша компания хотела бы предложить вам следующие тормозные жидкости:",new Font(times,14)));
        document.add(new Paragraph(" "));
        document.add(createTableOffer(basket,times, globalPath, user));
        document.add(new Paragraph(" "));
        
        document.add(new Paragraph("С уважением           _______________________            _______________",new Font(times,14)));
        		
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
        		
        document.add(new Paragraph("Добрый день.",new Font(times,14)));
        document.add(new Paragraph("Прошу предоставить мне указанную ниже продукцию:",new Font(times,14)));
        document.add(new Paragraph(" "));
        document.add(createTableOffer(basket,times, globalPath, user));
        document.add(new Paragraph(" "));
       
        document.add(new Paragraph("С уважением           _______________________            _______________",new Font(times,14)));
        		
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
			result=userDAO.getUser(userPrincipal.getName());		//сделал так чтобы выцепить реальное имя пользователя, а не логин
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
		if ((variant.compareTo("Производители")==0) || (variant.compareTo("manufacturer")==0)){
			totalRows=manufacturerDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",manufacturerDAO.getManufacturers(currentPage, elementsInList));
			model.addAttribute("variant", "manufacturer");
			
		}else if ((variant.compareTo("Классы жидкости")==0) || (variant.compareTo("fluidClass")==0)){
			totalRows=fluidClassDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",fluidClassDAO.getFluidClassis(currentPage, elementsInList));
			model.addAttribute("variant", "fluidClass");

		}else if ((variant.compareTo("Страны")==0) || (variant.compareTo("country")==0)){
			totalRows=countryDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",countryDAO.getCountries(currentPage, elementsInList));
			model.addAttribute("variant", "country");
			
		}else if ((variant.compareTo("Клиенты")==0) || (variant.compareTo("client")==0)){
			totalRows=clientDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",clientDAO.getClients(currentPage, elementsInList));
			model.addAttribute("variant", "client");
			
		}else if ((variant.compareTo("Пользователи")==0) || (variant.compareTo("user")==0)){
			totalRows=userDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",userDAO.getUsers(currentPage, elementsInList));
			model.addAttribute("variant", "user");
			
		}else if ((variant.compareTo("Логирование")==0) || (variant.compareTo("log")==0)){
			totalRows=logDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",logDAO.getLog(currentPage, elementsInList));
			model.addAttribute("variant", "log");
			model.addAttribute("tablehead", "Лог изменений");
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
	
	public static String isAdminPanel(HttpSession session, HttpServletRequest request){
		String result="";
		
		if (session.getAttribute("adminpanel")!=null){
			if ((Boolean) session.getAttribute("adminpanel")){
				result=(request.isUserInRole("ROLE_ADMIN")?"adminpanel/":"");
			}
			
		}
		
		return result;
	}
	
	public static void sendTheErrorToAdmin(String exceptionMessage, String adminEmail) {

		//отошлем по электронной почте
		if (!adminEmail.isEmpty()){
		 	Properties props = new Properties();			//ssl для яндекса
		 	props.put("mail.smtp.host", "smtp.yandex.ru");
		 	props.put("mail.smtp.socketFactory.port", "465");
		 	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		 	props.put("mail.smtp.auth", "true");
		 	props.put("mail.smtp.port", "465");

		 	Session session = Session.getDefaultInstance(props, new Authenticator() {
		            protected PasswordAuthentication getPasswordAuthentication() {
		            	return new PasswordAuthentication("locomotions2@yandex.ru", "1z2x3c4v5b"); 
		            }
		        });
	 
	        try {
	            Message message = new MimeMessage(session);
	            //от кого
	            message.setFrom(new InternetAddress("locomotions2@yandex.ru","Васильченко"));
	            //кому
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adminEmail));
	            //Заголовок письма
	            message.setSubject("Ошибки в работе системы 'Тормозные жидкости'");
	            //Содержимое
	            // Now set the actual message
	            message.setText(exceptionMessage);  
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
	//------------------------------------------------
	public static ArrayList<Demand> getDemandsWithoutPay(Client client, DemandTemplate demandDAO, PayTemplate payDAO){
		ArrayList<Demand> result = new ArrayList<Demand>();
		
		ArrayList<Demand> demands=demandDAO.getDemandsByClient(client.getId());
		ArrayList<Pay> pays=payDAO.getPaysByClient(client.getId());
		for (Demand demand:demands){
			double summ=demand.getPrice()*demand.getQuantity();
			for (Pay pay:pays){
				if (pay.getDemand_id().equals(demand.getDemand_id())){
					summ-=pay.getSumm();
					pays.remove(pay);
					if (summ<=0){
						break;
					}
				}
			}
			if (summ<=0){
				demands.remove(demand);
			}else{
				demand.setQuantity(0);
				demand.setPrice(summ); //вместо цены в price сохраним сумму, которую ещё должен оплатить клиент по заказу
				result.add(demand);
			}
		}
		
		return result;
	}
	
	private static void createEntry(double summ, String demand_id, Date currentTime, Client client, User user, ManufacturerTemplate manufacturerDAO, InfoTemplate infoDAO, PayTemplate payDAO, LogTemplate logDAO){
		String timeDoc=Service.getFormattedDate(currentTime)
				+":"+currentTime.getHours()+":"+currentTime.getMinutes()+":"+currentTime.getSeconds();
		// Теперь - внесение денег на счет посредника
		Pay pay=new Pay(0,currentTime, user, "pay_"+timeDoc,demand_id
				,manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)))
				,false, summ, client);
		pay=payDAO.createPay(pay);
		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), pay, "Оплатили посреднику"));
		summ=summ/100*95;
		pay=new Pay(0,currentTime, user, "pay_"+timeDoc,demand_id
				,manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)))
				,false, -summ, client);
		pay=payDAO.createPay(pay);
		log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), pay, "Посредник оплатил за товар поставщику"));
		pay=new Pay(0,currentTime, user, "pay_"+timeDoc,demand_id
				,manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MAIN_FIRM)))
				,false, summ, client);
		pay=payDAO.createPay(pay);
		log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), pay, "Поставщик получил деньги за товар"));
	}
	
	
	public static void createDemandAndPay(User user, LinkedList<Basket> basket, ClientTemplate clientDAO, ManufacturerTemplate manufacturerDAO
			, OfferStatusTemplate offerStatusDAO, InfoTemplate infoDAO, DemandTemplate demandDAO, PayTemplate payDAO
			, double paySumm, int client_id, LogTemplate logDAO){
		Client client=clientDAO.getClient((client_id==0?Service.ID_EMPTY_CLIENT:client_id));
		double totalSumm=0;
		for (Basket position:basket){
			totalSumm+=position.getQauntity()*position.getBrakingFluid().getPrice();
		}
		if (paySumm>=totalSumm){
			double summ=Math.min(paySumm,totalSumm);
			// сначала создадим заявку - основание для внесения денег
			
		GregorianCalendar currentTime=new GregorianCalendar(); 
		String timeDoc=Service.getFormattedDate(currentTime.getTime())
				+":"+currentTime.getTime().getHours()+":"+currentTime.getTime().getMinutes()+":"+currentTime.getTime().getSeconds();
		String demand_id="Demand_"+timeDoc;
		synchronized (demand_id) {
			ArrayList<Demand> demand = demandDAO.createDemand(demand_id, basket, user, offerStatusDAO.getOfferStatus(1), user, client);
			Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), demand, "Создали заявку (товар уже оплачен)"));
			createEntry(summ, demand_id, currentTime.getTime(), client, user, manufacturerDAO, infoDAO, payDAO, logDAO);
		}
			summ=paySumm-Math.min(paySumm,totalSumm); //если внесли большую сумму - спишем старые долги по клиенту
			if (summ>0){
				ArrayList<Demand> list=getDemandsWithoutPay(client, demandDAO, payDAO); //получили список заказов, не оплаченных, или оплаченных не полностью, отсортированный по дате (FIFO)
				for (Demand demand:list){
					double entrySumm=Math.min(summ, demand.getPrice());
					if (entrySumm>0){
						createEntry(entrySumm, demand.getDemand_id(), currentTime.getTime(), client, user, manufacturerDAO, infoDAO, payDAO, logDAO);
						summ-=entrySumm;
					}
					if (summ<=0){
						break;
					}
					
				}
			}
			if (summ>0){ //если ещё что-то осталось - сделаем предоплату
				Pay pay=new Pay(0,currentTime.getTime(), user, "pay_"+timeDoc,""
						,manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)))
						,false, summ, client);  //поставщику ничего платить не будем, бо рано ещё. Это сделаем после формирования заказа.
				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), pay, "Посредник получил предоплату"));
			}
		}
	}
}
