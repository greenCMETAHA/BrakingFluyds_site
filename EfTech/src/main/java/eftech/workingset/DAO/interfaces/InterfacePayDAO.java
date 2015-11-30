package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.Date;
import eftech.workingset.beans.Pay;

public interface InterfacePayDAO {
	int getCountRows(Date begin, Date end);
	ArrayList<Pay> getPaysLast(int numPage, int quantity);
	ArrayList<Pay> getPaysIn(Date begin, Date end, int numPage, int quantity);
	ArrayList<Pay> getPay(String demand_id);
	Pay getPayById(int id);
	ArrayList<Pay> getPayByDemandId(String demand_id);
	ArrayList<Pay> getPaysByNumDocOnleMarketing(String numDoc, int marketing_id);
	ArrayList<Pay> getPrepayByContracter(int client_id, int manufacturer_id);
	ArrayList<Pay> getPaysByClient(int client_id, int manufacturer_id);
	ArrayList<Pay> getPaysByNumDoc(String numDoc);
	
	Pay createPay(Pay pay);	
	void deletePay(int id);
	Pay changeSumm(int id, double value);
	void clearPaysDemand_id(String doc_id);
	void deletePaysWithNumDoc(String doc_id);
	ArrayList<Pay> getPaysForReport(Date begin, Date end, int marketing_id, int mainFirm_id);
	

}
