package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Role;
import eftech.workingset.beans.User;

public interface InterfaceUserDAO {
	
	User getUser(String login, String password);
	User getUser(int id);
	User getUser(String login);
	int getCountRows();
	
	ArrayList<User> getUsers();
	ArrayList<User> getUsers(int num, int nextRows);
	User createUserWithRole(User user, Role roles);
	void updateUsers(User user);
	void deleteUser(User user);
	void deleteUser(User user, Role role);
	void deleteUser(String login);
	void deleteUser(int id);
	void deleteUser(String login, int role_id);
}
