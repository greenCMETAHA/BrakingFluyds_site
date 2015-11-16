package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import eftech.workingset.beans.Basket;
import eftech.workingset.beans.Offer;
import eftech.workingset.beans.OfferStatus;
import eftech.workingset.beans.User;

public interface InterfaceOfferDAO {
	int getCountRows(Date begin, Date end);
	ArrayList<Offer> getOffersLast(int numPage, int quantity);
	ArrayList<Offer> getOffersIn(Date begin, Date end, int numPage, int quantity);
	ArrayList<Offer> getOffer(String offer_id);
	Offer getOfferById(int id);
	Offer getOfferByOfferId(String offer_id, int good_id, String goodPrefix);
	
	ArrayList<Offer> createOffer(String offer_id, LinkedList<Basket> basket, User user);
}
