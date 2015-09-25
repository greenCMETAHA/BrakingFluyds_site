package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Client;

public interface InterfaceClientDAO {
	ArrayList<Client> getClients();
	Client getClient(int id);

}
