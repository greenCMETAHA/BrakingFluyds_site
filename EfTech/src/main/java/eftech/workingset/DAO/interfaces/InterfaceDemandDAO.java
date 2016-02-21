package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import eftech.workingset.beans.Basket;
import eftech.workingset.beans.Client;
import eftech.workingset.beans.Customer;
import eftech.workingset.beans.Demand;
import eftech.workingset.beans.OfferStatus;
import eftech.workingset.beans.User;

public interface InterfaceDemandDAO {
	int getCountRows(Date begin, Date end);
	ArrayList<Demand> getDemandsLast(int numPage, int quantity);
	ArrayList<Demand> getDemandsIn(Date begin, Date end, int numPage, int quantity, int executer_id);
	ArrayList<Demand> getDemandsByClient(int client_id);
	ArrayList<Demand> getDemand(String offer_id);
	Demand getDemandById(int id);
	Demand getDemandByDemandId(String demand_id, int good_id, String goodPrefix);
	
	
	ArrayList<Demand> createDemand(String demand_id, LinkedList<Basket> basket, User user, OfferStatus status, User executor
			, Client Client, int paid, int shipping, Customer customer);
	ArrayList<Demand> changeStatus(String demand_id, OfferStatus status);
	ArrayList<Demand> changeExecuter(String demand_id, User executer);
}
