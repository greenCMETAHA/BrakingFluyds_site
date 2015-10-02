package eftech.vasil.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.LinkedList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import eftech.workingset.DAO.templates.BrakingFluidTemplate;
import eftech.workingset.DAO.templates.InfoTemplate;
import eftech.workingset.DAO.templates.UserTemplate;
import eftech.workingset.DAO.templates.WishlistTemplate;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.User;
import eftech.workingset.beans.Wishlist;

@Controller
@SessionAttributes({"user", "basket", "wishlist", "compare", "currentPriceFilter", "manufacturersFilter", "elementsInList"})
public class AutorisationController {
	@Autowired
	UserTemplate userDAO;
	
	@Autowired
	BrakingFluidTemplate brakingFluidDAO;
	
	@Autowired
	InfoTemplate infoDAO;
	
	@Autowired
	WishlistTemplate wishlistDAO;
	
	
	

//	@RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})	
//	public String homeAuthorisation(@ModelAttribute User user
//			, Locale locale, Model model) {
//		
//		String page="login";
//		return page;
//	}
//	
//	@RequestMapping(value = "/loginOk", method = {RequestMethod.POST, RequestMethod.GET})	
//	public String homeAuthorisationOk(@ModelAttribute User user, @AuthenticationPrincipal User userPrincipal
//			,HttpServletRequest request
//			, Locale locale, Model model) {
//		
//		model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
//		
//		return "home";
//	}
	

//	@RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})	
//	public String homeAuthorisation(@ModelAttribute User user
//			,@RequestParam("authorButton") String button
//			,HttpServletRequest request
//			, Locale locale, Model model) {
//		
//		String page="home";
//		System.out.println("0000");
//		//System.out.println(userPrincipal.toString());
//		try {
//			button=new String(button.getBytes("iso-8859-1"), "UTF-8");
//			if ((button.compareTo("На главную")==0) || (button.compareTo("Выйти")==0)){
//				user=new User();
//				model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
//				model.addAttribute("user", user);
//				System.out.println("1111");
//			}else{
//				page="Admin";
//				System.out.println("22222");
//			}
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return page;
//	}		
	
	@RequestMapping(value = "/autorization", method = RequestMethod.POST)	
	public String login(@ModelAttribute User user
			,@RequestParam("login") String login
			,@RequestParam("password") String password
			,@RequestParam("authorButton") String button, Locale locale, Model model) {
		String page="home";
		
		try {
			button=new String(button.getBytes("iso-8859-1"), "UTF-8");
			if (button.compareTo("Авторизация")==0){
				User currentUser=userDAO.getUser(login.trim(),password.trim());
				if (!currentUser.isEmpty()){
					user=currentUser;
					model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
				}else{
					page="Admin";
				}
			}else{
				model.addAttribute("listBrakFluids", brakingFluidDAO.getBrakingFluids());
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		model.addAttribute("user", user);
		
		return page;
	}			

}
