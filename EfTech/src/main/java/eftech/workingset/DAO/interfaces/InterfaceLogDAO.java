package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Log;
import eftech.workingset.beans.Manufacturer;

public interface InterfaceLogDAO {
	ArrayList<Log> getLog();
	ArrayList<Log> getLog(int num, int nextRows);
	Log getLogById(int id);
	int getCountRows();
	
	Log createLog(Log log);
	void deleteLog(Log log);
	void deleteLog(int id);
	
}
