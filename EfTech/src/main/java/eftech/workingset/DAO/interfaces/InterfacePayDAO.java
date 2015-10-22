package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import eftech.workingset.beans.Basket;
import eftech.workingset.beans.Pay;
import eftech.workingset.beans.User;

public interface InterfacePayDAO {
	int getCountRows(Date begin, Date end);
	ArrayList<Pay> getPaysLast(int numPage, int quantity);
	ArrayList<Pay> getPaysIn(Date begin, Date end, int numPage, int quantity);
	ArrayList<Pay> getPay(String demand_id);
	Pay getPayById(int id);
	ArrayList<Pay> getPayByDemandId(String demand_id);
	ArrayList<Pay> getPrepayByContracter(int client_id);
	ArrayList<Pay> getPaysByClient(int client_id);
	
	Pay createPay(Pay pay);	

}
