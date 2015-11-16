package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Client;

public interface InterfaceClientDAO {
	ArrayList<Client> getClients();
	ArrayList<Client> getClients(int num, int nextRows);
	Client getClient(int id);
	int getCountRows();
	
	Client createClient(Client client);
	void deleteClient(Client client);
	void deleteClient(int id);

}
