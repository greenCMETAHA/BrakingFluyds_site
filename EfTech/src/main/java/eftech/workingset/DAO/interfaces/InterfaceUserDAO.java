package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.User;

public interface InterfaceUserDAO {
	
	User getUser(String login, String password);
	User getUser(int id);
	User getUser(String login);
	ArrayList<User> getUsers();
}
