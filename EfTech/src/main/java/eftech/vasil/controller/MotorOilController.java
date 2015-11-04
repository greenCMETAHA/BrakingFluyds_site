package eftech.vasil.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"user", "adminpanel", "basket", "wishlist", "compare", "manufacturersFilter", "fluidClassFilter", "elementsInList"
	, "currentPriceFilter" , "currentBoilingTemperatureDryFilter" , "currentBoilingTemperatureWetFilter" , "currentValueFilter" 
	, "currentViscosity40Filter" , "currentViscosity100Filter", "currentJudgementFilter"
	,"dateBeginFilterOffer", "dateEndFilterOffer", "dateBeginFilterDemand", "dateEndFilterDemand", "Payment_Amount", "Payment_Option" })
public class MotorOilController {

}
