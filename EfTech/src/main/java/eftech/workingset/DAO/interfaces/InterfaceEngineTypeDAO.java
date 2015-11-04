package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.EngineType;

public interface InterfaceEngineTypeDAO {
	ArrayList<EngineType> getEngineTypes();
	ArrayList<EngineType> getEngineTypes(int num, int nextRows);
	EngineType getEngineType(int id);
	EngineType getEngineTypeByName(String country);
	int getCountRows();
	
	EngineType createEngineType(String name);
	EngineType createEngineType(EngineType engineType);
	void deleteEngineType(EngineType engineType);
	void deleteEngineType(int id);
}
