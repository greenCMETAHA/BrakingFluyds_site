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
import java.text.DecimalFormat;
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
import eftech.workingset.DAO.templates.EngineTypeTemplate;
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.InfoTemplate;
import eftech.workingset.DAO.templates.LogTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.OfferStatusTemplate;
import eftech.workingset.DAO.templates.OfferTemplate;
import eftech.workingset.DAO.templates.OilStuffTemplate;
import eftech.workingset.DAO.templates.PayTemplate;
import eftech.workingset.DAO.templates.UserTemplate;
import eftech.workingset.beans.*;
import eftech.workingset.beans.intefaces.InterfaceClient;
import eftech.workingset.beans.intefaces.base.InterfaceGood;

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
	public static String EMPTY="<empty>";
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
	public static String BRAKING_FLUID_PREFIX = "BrF";
	public static String MOTOR_OIL_PREFIX = "Oil";

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
        	BrakingFluid currentBR=(BrakingFluid) currentBasket.getGood();
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
			,ClientTemplate clientDAO, UserTemplate userDAO,LogTemplate logDAO, OilStuffTemplate oilStuffDAO, EngineTypeTemplate engineTypeDAO
			, LinkedList<String> errors){
		
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

		}else if ((variant.compareTo("Состав масел")==0) || (variant.compareTo("oilStuff")==0)){
			totalRows=oilStuffDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",oilStuffDAO.getOilStuffs(currentPage, elementsInList));
			model.addAttribute("variant", "oilStuff");

		}else if ((variant.compareTo("Тип двигателя")==0) || (variant.compareTo("engineType")==0)){
			totalRows=engineTypeDAO.getCountRows();
			totalPages = (int)(totalRows/elementsInList)+(totalRows%elementsInList>0?1:0);
			model.addAttribute("list",engineTypeDAO.getEngineTypes(currentPage, elementsInList));
			model.addAttribute("variant", "engineType");

			
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
	
	//определим, какие заказы ещё не были оплачены. Их мы будем закрывать остаточными суммами
	public static ArrayList<Demand> getDemandsWithoutPay(Client client, InfoTemplate infoDAO, DemandTemplate demandDAO, PayTemplate payDAO){
		ArrayList<Demand> result = new ArrayList<Demand>();
		
		ArrayList<Demand> demands=demandDAO.getDemandsByClient(client.getId()); 
		ArrayList<Pay> pays=payDAO.getPaysByClient(client.getId(),new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)));
		for (Demand demand:demands){
			double summ=demand.getPrice()*demand.getQuantity();
			for (Pay pay:pays){
				if (pay.getSumm()>0.0001){
					if (pay.getDemand_id().equals(demand.getDemand_id())){
						double value=Math.min(summ, pay.getSumm());
						summ-=value;
						value=pay.getSumm()-value;
						pay.setSumm(value);
						if (summ<=0){
							break;
						}
					}
				}
			}
			if (summ>0){
				demand.setQuantity(0);
				demand.setPrice(summ); //вместо цены в price сохраним сумму, которую ещё должен оплатить клиент по заказу
				result.add(demand);
			}
		}
		
		return result;
	}
	
	private static void createEntry(double summ, boolean storno, String numDoc, String demand_id, Date currentTime, Client client, User user
			, ManufacturerTemplate manufacturerDAO, InfoTemplate infoDAO, PayTemplate payDAO, LogTemplate logDAO){
		String timeDoc=Service.getFormattedDate(currentTime)
				+":"+currentTime.getHours()+":"+currentTime.getMinutes()+":"+currentTime.getSeconds();
		// Теперь - внесение денег на счет посредника
		numDoc=(numDoc==null?"pay_"+timeDoc:numDoc);
		Pay pay=new Pay(0,currentTime, user, numDoc,demand_id
				,manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)))
				,storno, summ, client);
		pay=payDAO.createPay(pay);
		Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), pay, "Оплатили посреднику"));
		summ=summ/100*95;
		pay=new Pay(0,currentTime, user, numDoc,demand_id
				,manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)))
				,storno, -summ, client);
		pay=payDAO.createPay(pay);
		log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), pay, "Посредник оплатил за товар поставщику"));
		pay=new Pay(0,currentTime, user, numDoc,demand_id
				,manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MAIN_FIRM)))
				,storno, summ, client);
		pay=payDAO.createPay(pay);
		log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), pay, "Поставщик получил деньги за товар"));
	}
	
	
	public static void createDemandAndPay(User user, LinkedList<Basket> basket, ClientTemplate clientDAO, ManufacturerTemplate manufacturerDAO
			, OfferStatusTemplate offerStatusDAO, InfoTemplate infoDAO, DemandTemplate demandDAO, PayTemplate payDAO
			, double paySumm, int client_id, LogTemplate logDAO){
		Client client=clientDAO.getClient((client_id==0?Service.ID_EMPTY_CLIENT:client_id));
		boolean storno=false;
		if (paySumm<0){
			paySumm=-paySumm;
			storno=true;
		}
			
		double totalSumm=0;
		for (Basket position:basket){
			totalSumm+=position.getQauntity()*position.getGood().getPrice();
		}
		double summ=Math.min(paySumm,totalSumm); 			//вносимая сумма может быть не равна сумме к оплате: как от недостатка денег - так и из-за наличия не отгруженных старых платежей 
			
		GregorianCalendar currentTime=new GregorianCalendar();  // сначала создадим заявку - основание для внесения денег 
		String timeDoc=Service.getFormattedDate(currentTime.getTime())
				+":"+currentTime.getTime().getHours()+":"+currentTime.getTime().getMinutes()+":"+currentTime.getTime().getSeconds();
		String demand_id="Demand_"+timeDoc;
		synchronized (demand_id) {
			ArrayList<Demand> listDemand = demandDAO.createDemand(demand_id, basket, user, offerStatusDAO.getOfferStatus(1), user, client);
			for (Demand demand:listDemand){
				Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), demand, "Создали заявку (товар уже оплачен)"));
			}
			createEntry(summ, storno, null,demand_id, currentTime.getTime(), client, user, manufacturerDAO, infoDAO, payDAO, logDAO);
		}
		if (summ<totalSumm){ //сумма была меньше. Попытаемся списать старые предоплаты
			summ=totalSumm-summ;
			ArrayList<Pay> list=payDAO.getPrepayByContracter(client_id, new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)));
			for (Pay pay:list){
				double entrySumm=Math.min(summ, pay.getSumm());
				if (entrySumm>0){
					createEntry(entrySumm, storno, pay.getNumDoc(), demand_id, pay.getTime(), (Client)pay.getClient(), pay.getUser(), manufacturerDAO, infoDAO, payDAO, logDAO);
					summ-=entrySumm;
				}
				double value=pay.getSumm()-entrySumm;
				if (value<0.0001){
					payDAO.deletePay(pay.getId());
				}else{
					payDAO.changeSumm(pay.getId(),value);
				}
				if (summ<=0.0001){
					break;
				}
			}
		}
		
		summ=paySumm-Math.min(paySumm,totalSumm); //если внесли большую сумму - спишем старые долги по клиенту
		if (summ>0){
			ArrayList<Demand> list=getDemandsWithoutPay(client, infoDAO, demandDAO, payDAO); //получили список заказов, не оплаченных, или оплаченных не полностью, отсортированный по дате (FIFO)
			for (Demand demand:list){
				double entrySumm=Math.min(summ, demand.getPrice());
				if (entrySumm>0.0001){
					createEntry(entrySumm, storno, null, demand.getDemand_id(), currentTime.getTime(), client, user, manufacturerDAO, infoDAO, payDAO, logDAO);
					summ-=entrySumm;
				}
				if (summ<=0.0001){
					break;
				}
				
			}
		}
		if (summ>0){ //если ещё что-то осталось - сделаем предоплату
			Pay pay=new Pay(0,currentTime.getTime(), user, "pay_"+timeDoc,""
					,manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)))
					,storno, summ, client);  //поставщику ничего платить не будем, бо рано ещё. Это сделаем после формирования заказа.
			pay=payDAO.createPay(pay);
			Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), pay, "Посредник получил предоплату"));
		}

	}
	
	public static ArrayList<Pay> getlistPays(Date dateBeginFilter, Date dateEndFilter, int firstElement, int elementsInList, User user, PayTemplate payDAO){
		ArrayList<Pay> result=new ArrayList<Pay>();
		
		ArrayList<Pay> listDoc=null;
		if ((dateBeginFilter.getYear()==0) & (dateEndFilter.getYear()==0)){
			listDoc=payDAO.getPaysLast(1, elementsInList);
		}else{
			listDoc=payDAO.getPaysIn(dateBeginFilter, dateEndFilter,firstElement, elementsInList);
		}
		
		for (Pay doc:listDoc){
			boolean bFind=false;
			for (Pay row:result){
				if (row.getNumDoc().equals(doc.getNumDoc())){
					row.setSumm(row.getSumm()+doc.getSumm());
					bFind=true;
				}
			}
			
			if (!bFind){
				result.add(new Pay(0, doc.getTime(), user, doc.getNumDoc(), "", doc.getManufacturer()
						,doc.isStorno(), doc.getSumm(), doc.getClient()));
			}
		}
		
		return result;
	}

	public static void spreadDemand(String doc_id, InfoTemplate infoDAO, LogTemplate logDAO, PayTemplate payDAO
			, DemandTemplate demandDAO, ManufacturerTemplate manufacturerDAO) {
	
		ArrayList<Demand> demands=demandDAO.getDemand(doc_id);
		double totalSumm=0;
		int client_id = 0;
		for(Demand position:demands){
			totalSumm+=position.getQuantity()*position.getPrice();
			client_id=((Client)position.getClient()).getId();
		}

		ArrayList<Pay> list=payDAO.getPrepayByContracter(client_id, new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)));
		for (Pay pay:list){
			double entrySumm=Math.min(totalSumm, pay.getSumm());
			if (entrySumm>0){
				createEntry(entrySumm, false, pay.getNumDoc(), doc_id, pay.getTime(), (Client)pay.getClient(), pay.getUser(), manufacturerDAO, infoDAO, payDAO, logDAO);
				totalSumm-=entrySumm;
			}
			double value=pay.getSumm()-entrySumm;
			if (value<0.0001){
				payDAO.deletePay(pay.getId());
			}else{
				payDAO.changeSumm(pay.getId(),value);
			}
			if (totalSumm<=0.0001){
				break;
			}
		}
	
		
	}

	public static void createPays(String doc_id, Date time, Client client, double doc_summ, User user, InfoTemplate infoDAO,
			LogTemplate logDAO, PayTemplate payDAO, DemandTemplate demandDAO, ManufacturerTemplate manufacturerDAO) {
		
		boolean storno=false;
		if (doc_summ<0){
			doc_summ=-doc_summ;
			storno=true;
		}
		
		if (doc_summ>0){
			ArrayList<Demand> list=getDemandsWithoutPay(client, infoDAO, demandDAO, payDAO); //получили список заказов, не оплаченных, или оплаченных не полностью, отсортированный по дате (FIFO)
			for (Demand demand:list){
				double entrySumm=Math.min(doc_summ, demand.getPrice());
				if (entrySumm>0.0001){
					createEntry(entrySumm, storno, doc_id, demand.getDemand_id(), time, client, user, manufacturerDAO, infoDAO, payDAO, logDAO);
					doc_summ-=entrySumm;
				}
				if (doc_summ<=0.0001){
					break;
				}
				
			}
		}
		if (doc_summ>0){ //если ещё что-то осталось - сделаем предоплату
			Pay pay=new Pay(0,time, user, doc_id,""
					,manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)))
					,storno, doc_summ, client);  //поставщику ничего платить не будем, бо рано ещё. Это сделаем после формирования заказа.
			pay=payDAO.createPay(pay);
			Log log=logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), pay, "Посредник получил предоплату"));
		}
	}

	//-----------------------------------------------------------------------------------------
	
	public static int showDocInListDoc(String variant, String task, Date dateBeginFilter, Date dateEndFilter,
			int currentPage, int elementsInList, User user, HttpServletRequest request, Model model, LinkedList<Basket>  basket,
			DemandTemplate demandDAO, OfferTemplate offerDAO, PayTemplate payDAO, 
			ManufacturerTemplate manufacturerDAO, InfoTemplate infoDAO, ClientTemplate clientDAO) {
		int totalDoc=0;
		
		if ("Demand".equals(variant)){
			ArrayList<Demand> listDoc=null;
//			if (Service.isDate(dateBeginFilter,dateEndFilter)){
//				listDoc=demandDAO.getDemandsLast(currentPage, elementsInList);
//			}else{
				listDoc=demandDAO.getDemandsIn(dateBeginFilter, dateEndFilter,currentPage, elementsInList
						, (request.isUserInRole("ROLE_MANAGER_SALE")?0:user.getId()));
//			}
			
			ArrayList<DocRow> table = new ArrayList<DocRow>(); 
			for (Demand doc:listDoc){
				boolean bFind=false;
				for (DocRow row:table){
					if (row.getNumDoc().equals(doc.getDemand_id())){
						row.setQuantity(row.getQuantity()+doc.getQuantity());
						row.setSumm(row.getSumm()+(doc.getQuantity()*doc.getPrice()));
						row.setStatus(doc.getStatus());
						bFind=true;
					}
				}
				if (!bFind){
					table.add(new DocRow(doc.getDemand_id(),doc.getTime(),doc.getBrakingFluid(),doc.getQuantity(),doc.getQuantity()*doc.getPrice(), null, doc.getExecuter(), false)); //--!!!!
				}
			}
			for (DocRow docRow:table){
				ArrayList<Pay> pays= payDAO.getPayByDemandId(docRow.getNumDoc());
				double summByDemand_id=0.0;
				for (Pay docPay:pays){
					summByDemand_id+=docPay.getSumm();
				}
				if (docRow.getSumm()==summByDemand_id){
					docRow.setPaid(true);
				}
				
			}
			totalDoc = demandDAO.getCountRows(dateBeginFilter, dateEndFilter);
			model.addAttribute("listDoc", table);
			
		}else if ("Offer".equals(variant)){
			ArrayList<Offer> listDoc=null;
//			if ((dateBeginFilter.getYear()==0) & (dateEndFilter.getYear()==0)){
//				listDoc=offerDAO.getOffersLast(currentPage, elementsInList);
//			}else{
				listDoc=offerDAO.getOffersIn(dateBeginFilter, dateEndFilter,currentPage, elementsInList);
			//}
			
			ArrayList<DocRow> table = new ArrayList<DocRow>(); 
			for (Offer doc:listDoc){
				boolean bFind=false;
				for (DocRow row:table){
					if (row.getNumDoc().equals(doc.getOffer_id())){
						row.setQuantity(row.getQuantity()+doc.getQuantity());
						row.setSumm(row.getSumm()+(doc.getQuantity()*doc.getPrice()));
						bFind=true;
					}
				}
				if (!bFind){
					table.add(new DocRow(doc.getOffer_id(),doc.getTime(),doc.getBrakingFluid(), doc.getQuantity(),doc.getQuantity()*doc.getPrice(), null,null, false)); //--!!!!
				}
			}
			totalDoc = offerDAO.getCountRows(dateBeginFilter, dateEndFilter);
			model.addAttribute("listDoc", table);
		}else if ("Pay".equals(variant)){
			if ("Создать".equals(task)){
				GregorianCalendar currentTime = new GregorianCalendar();
				model.addAttribute("pageInfo", "Создать новую оплату");
				model.addAttribute("id", 0);
				model.addAttribute("time", Service.getFormattedDate(currentTime.getTime()));
				model.addAttribute("doc_id", variant+"_"+Service.getFormattedDate(currentTime.getTime())
						+":"+currentTime.getTime().getHours()+":"+currentTime.getTime().getMinutes()+":"+currentTime.getTime().getSeconds());
				model.addAttribute("currentStatus", 1);
				model.addAttribute("user_login", user.getLogin());
				model.addAttribute("user_name", user.getName());
				model.addAttribute("listClients", clientDAO.getClients());
				model.addAttribute("currentClient", Service.ID_EMPTY_CLIENT);
				model.addAttribute("currentManufacturer", manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MARKETING_FIRM))));
				model.addAttribute("listDoc", basket);
				model.addAttribute("task", "New");
				model.addAttribute("doc_summ", 0);
				model.addAttribute("listDemands", new ArrayList<Pay>()); //список заявок, на которые распределилась сумма + предоплаты
			}else{
				ArrayList<Pay> listDoc=Service.getlistPays(dateBeginFilter, dateEndFilter, 1, elementsInList, user, payDAO);
				model.addAttribute("listDoc", listDoc);
				totalDoc=listDoc.size();
			}
		}
		return totalDoc;
	}

	public static void showDocInInsertUpdateDoc(String variant, String task, String doc_id, Double doc_summ, GregorianCalendar currentTime
			, User user, HttpServletRequest request,Model model, DemandTemplate demandDAO, OfferTemplate offerDAO, PayTemplate payDAO
			, ManufacturerTemplate manufacturerDAO, InfoTemplate infoDAO, ClientTemplate clientDAO
			, UserTemplate userDAO, OfferStatusTemplate offerStatusDAO) {
		if ("Demand".equals(variant)){
			model.addAttribute("pageInfo", "Редактировать заявку");
			ArrayList<Demand> listDoc=demandDAO.getDemand(doc_id);
			model.addAttribute("listDoc", listDoc);
			model.addAttribute("currentStatus",  (listDoc.size()>0?((OfferStatus)listDoc.get(0).getStatus()).getId():offerStatusDAO.getOfferStatus(1)));
			model.addAttribute("executer_id", (listDoc.size()>0?listDoc.get(0).getExecuter().getId():Service.ID_EXECUTER));
			model.addAttribute("userDoc", userDAO.getUser((listDoc.size()>0?listDoc.get(0).getUser().getId():Service.ID_EXECUTER)));
			model.addAttribute("executer_name", userDAO.getUser((listDoc.size()>0?listDoc.get(0).getExecuter().getId():Service.ID_EXECUTER)).getName());
			Client currentClient=clientDAO.getClient((listDoc.size()>0?((Client)listDoc.get(0).getClient()).getId():Service.ID_EMPTY_CLIENT));
			model.addAttribute("client", currentClient);
		}else if ("Offer".equals(variant)){
			model.addAttribute("pageInfo", "Редактировать коммерческое предложение");
			model.addAttribute("listDoc", offerDAO.getOffer(doc_id));
		}else if ("Pay".equals(variant)){
			model.addAttribute("pageInfo", "Редактировать оплату");
			ArrayList<Pay> listDoc=payDAO.getPaysByNumDoc(doc_id);
			model.addAttribute("time",  (listDoc.size()>0?listDoc.get(0).getTime():currentTime.getTime()));
			Client currentClient=clientDAO.getClient((listDoc.size()>0?((Client)listDoc.get(0).getClient()).getId():Service.ID_EMPTY_CLIENT));
			model.addAttribute("currentManufacturer", manufacturerDAO.getManufacturer(new Integer(infoDAO.getInfo(Service.MARKETING_FIRM))));
			model.addAttribute("user_login", (listDoc.size()>0?((User)listDoc.get(0).getUser()).getLogin():user.getLogin()));
			model.addAttribute("user_name", (listDoc.size()>0?((User)listDoc.get(0).getUser()).getName():user.getName()));
			ArrayList<Pay> listPays =  new ArrayList<Pay>();
			if (listDoc.size()>0){
				double summTotal=0;
				listPays=payDAO.getPaysByNumDocOnleMarketing(doc_id,new Integer(infoDAO.getInfo(Service.MARKETING_FIRM)));
				for (Pay pay:listPays){
					summTotal+=pay.getSumm();
				}
				model.addAttribute("summ", summTotal);
			}else{
				model.addAttribute("summ", doc_summ);
			}
			model.addAttribute("listDemands", listPays); //список заявок, на которые распределилась сумма + предоплаты
			model.addAttribute("client", currentClient);
			model.addAttribute("currentClient", currentClient.getId());
		}
	
	
	}
	
	public static double countBasket(LinkedList<Basket> basket){
		double totalBasket=0;	
		for (Basket current:basket){
			InterfaceGood brFluid= current.getGood();
			totalBasket+=(brFluid.getPrice()*current.getQauntity());
		}

		return totalBasket;
	}
	
	public static String strCountBasket(LinkedList<Basket> basket){
		String result=null;
		
		double totalBasket=countBasket(basket);
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		result = decimalFormat.format(totalBasket).replace(",", ".");

		return result;
	}
}
