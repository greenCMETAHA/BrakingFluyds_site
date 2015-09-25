package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Role;

public interface InterfaceRoleDAO {
	ArrayList<Role> getRoles();
	Role getRole(int id);

}
