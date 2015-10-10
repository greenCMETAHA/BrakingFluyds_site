package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import eftech.workingset.beans.Basket;
import eftech.workingset.beans.Demand;
import eftech.workingset.beans.Offer;
import eftech.workingset.beans.OfferStatus;
import eftech.workingset.beans.User;

public interface InterfaceDemandDAO {
	int getCountRows(Date begin, Date end);
	ArrayList<Demand> getDemandsLast(int numPage, int quantity);
	ArrayList<Demand> getDemand(String offer_id);
	Demand getDemandById(int id);
	Demand getDemandByDemandId(String demand_id, int braking_fluid_id);
	
	ArrayList<Demand> createDemand(String offer_id, LinkedList<Basket> basket, User user);

}
