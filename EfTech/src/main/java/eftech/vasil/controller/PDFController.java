package eftech.vasil.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.UserTemplate;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.User;

@Controller
@SessionAttributes({"user", "basket"})
public class PDFController {
	@Autowired
	BrakingFluidTemplate brakingFluidDAO;
	
	@Autowired
	ClientTemplate clientDAO;

//	
//	@RequestMapping(value = "/makeDemand", method = {RequestMethod.POST,RequestMethod.GET})
//	public String makeDemand(@ModelAttribute User user
//			,@RequestParam(value="backPage", defaultValue="home", required=false) String backPage
//			,@RequestParam(value="id", defaultValue="0", required=false) int id //может прийти со страницы номенклатуры, а может - из корзины.
//			,HttpServletRequest request
//			, Locale locale, Model model) {
//			
//			
//
//		model.addAttribute("id", id);
//		
//		return backPage;
//	}
//	
//	
//
//	
//	@RequestMapping(value = "/BussinessOffer", method = {RequestMethod.POST,RequestMethod.GET})
//	public String bussinessOffer(
//			 @RequestParam(value = "variant", defaultValue="Show", required = false) String variant
//			, @RequestParam(value = "client", required = false) int clientId
//			,HttpServletRequest request
//			,Locale locale, Model model) {
//
//		String result="Basket";
//		
//		HttpSession session=request.getSession();
//		LinkedList<BrakingFluid> listBrakingFluid = (LinkedList<BrakingFluid>) session.getAttribute("basket");
//		User user = (User) session.getAttribute("user");
//		
//
//		return result;
//	}
	
}
