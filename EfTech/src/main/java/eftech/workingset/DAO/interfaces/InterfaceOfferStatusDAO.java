package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.OfferStatus;

public interface InterfaceOfferStatusDAO {
	OfferStatus getOfferStatus(int id);
	ArrayList<OfferStatus> getStatuses();
}
