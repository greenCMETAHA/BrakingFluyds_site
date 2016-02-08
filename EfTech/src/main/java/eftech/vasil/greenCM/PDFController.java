package eftech.vasil.greenCM;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.itextpdf.text.DocumentException;

import eftech.workingset.DAO.templates.BrakingFluidTemplate;
import eftech.workingset.DAO.templates.ClientTemplate;
import eftech.workingset.DAO.templates.LogTemplate;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.Basket;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Log;
import eftech.workingset.beans.User;

@Controller
@SessionAttributes({"user", "Basket"})
public class PDFController {
	@Autowired
	BrakingFluidTemplate brakingFluidDAO;
	
	@Autowired
	ClientTemplate clientDAO;

	@Autowired
	LogTemplate logDAO;
	

	@RequestMapping(value = "/makeDemand", method = RequestMethod.GET)
	public String home(@ModelAttribute User user
			,@RequestParam("variant") String formButton
			,@RequestParam("id") int id
			,HttpServletRequest request, Locale locale, Model model) {
			
//		try {
//			formButton=new String(formButton.getBytes("iso-8859-1"), "UTF-8");
			if (formButton.compareTo("Заявка")==0){
				LinkedList<Basket> basket = new LinkedList<Basket>();
				basket.add(new Basket(brakingFluidDAO.getBrakingFluid(id)));
				try {
					Service.createPDF_Demand(basket, request.getSession().getServletContext().getRealPath("/"), user);
					logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создана заявка"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
		
		model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
		
		return "adminpanel/home";
	}
	
	@RequestMapping(value = "/BussinessOffer", method = RequestMethod.POST)
	public String bussinessOffer(@ModelAttribute User user
			, @ModelAttribute LinkedList<Basket> basket
			, @RequestParam(value = "selections", required=false ) int[] selections
			, @RequestParam(value = "variant") String variant
			, @RequestParam(value = "client") int clientId
			,HttpServletRequest request,Locale locale, Model model) {
//		try {
//			variant=new String(variant.getBytes("iso-8859-1"), "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String result="home";
		
		if (("Назад к списку номенклатуры".equals(variant)) || ("На главную".equals(variant))){
			model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
		}else{		
			basket.clear();
			if (selections!=null){
				for (int i=0;i<selections.length; i++){
					basket.add(new Basket(brakingFluidDAO.getBrakingFluid(selections[i])));
				}
			}
			if (("Распечатать коммерческое предложение".equals(variant)) || ("Печать".equals(variant))){
				File pdfFile=null;
				try {
					pdfFile=Service.createPDF_BussinessOffer(basket, request.getSession().getServletContext().getRealPath("/"), user);
					logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создано бизнес-предложение"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (pdfFile!=null){
					if (Desktop.isDesktopSupported()) {
						try {
							Desktop.getDesktop().open(pdfFile);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						System.out.println("Awt Desktop is not supported!");
					}
				}
				
				model.addAttribute("listBrakFluids", basket);
				model.addAttribute("listClients", clientDAO.getClients());
				result="BussinessOffer";
			}		
			if ("Отослать".equals(variant)){
				File pdfFile=null;
				try {
					pdfFile=Service.createPDF_BussinessOffer(basket, request.getSession().getServletContext().getRealPath("/"), user);
					logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime(), null, "Создано бизнес-предложение"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//отошлем по электронной почте
				if (pdfFile!=null){
					if (clientId>0){
						Client currentClient=(Client)clientDAO.getClient(clientId);
						if (currentClient.getEmail().length()>0){
						 	Properties props = new Properties();			//ssl для яндекса
						 	props.put("mail.smtp.host", "smtp.yandex.ru");
						 	props.put("mail.smtp.socketFactory.port", "465");
						 	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
						 	props.put("mail.smtp.auth", "true");
						 	props.put("mail.smtp.port", "465");
						 	
						 	
						 	
//						 	props.put("mail.smtp.auth", "true");   
//						 	props.put("mail.transport.protocol", "smtp");
//
////						 	props.put("mail.smtp.host", "smtp.beget.ru");
////						 	props.put("mail.smtp.port", "25");
//						 	
//						 	
//						 	props.put("mail.smtp.host", "smtp.mail.ru");
//					 	props.put("mail.smtp.port", "465");
////					 	props.put("mail.user", "test@locomotions.ru");
////						 	props.put("mail.password" , "12345678qa");					 	
//					        
////						 		props.put("mail.smtp.auth", "true");
//						        props.put("mail.smtp.starttls.enable", "true");
////						        props.put("mail.smtp.host", "smtp.gmail.com");
////						        props.put("mail.smtp.port", "587");
//				        
//					        
//					     //   Session session = Session.getInstance(props,null);
						        Session session = Session.getInstance(props, new Authenticator() {
						            protected PasswordAuthentication getPasswordAuthentication() {
						            	return new PasswordAuthentication("locomotions2@yandex.ru", "1z2x3c4v5b");
						            }
						        });
					 
					        try {
					            Message message = new MimeMessage(session);
					            //от кого
					            message.setFrom(new InternetAddress("locomotions2@yandex.ru","Васильченко"));
					            //кому
					            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(currentClient.getEmail()));
					            //Заголовок письма
					            message.setSubject("Коммерческое предложение");
					            //Содержимое
					            
					            // Create the message part
					            BodyPart messageBodyPart = new MimeBodyPart();

					            // Now set the actual message
					            messageBodyPart.setText("Бизнес предложение (тестовое задание) для "+currentClient.getName());

					            // Create a multipar message
					            Multipart multipart = new MimeMultipart();

					            // Set text message part
					            multipart.addBodyPart(messageBodyPart);

					            // Part two is attachment
					            
					          //now write the PDF content to the output stream
					            messageBodyPart = new MimeBodyPart();
					            DataSource source = new FileDataSource(pdfFile);
					            messageBodyPart.setDataHandler(new DataHandler(source));
					            messageBodyPart.setFileName(pdfFile.getName());
					            multipart.addBodyPart(messageBodyPart);
					            
					            message.setContent(messageBodyPart.getParent());

					            // Send the complete message parts
					            // Session mailSession = Session.getDefaultInstance(props, null);
					            //Transport = mailSession.getTransport();
					            //Отправляем сообщение
					            Transport.send(message);
					            logDAO.createLog(new Log(0, user, new GregorianCalendar().getTime()
					            			, null, "Создано бизнес-предложение, отправлено на e-mail: "+currentClient.getEmail()));
					        } catch (MessagingException e) {
					            throw new RuntimeException(e);
					        } catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
						}
				        
					}
				}
				
				model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
			}
		}
		return "adminpanel/"+result;
	}
	
}