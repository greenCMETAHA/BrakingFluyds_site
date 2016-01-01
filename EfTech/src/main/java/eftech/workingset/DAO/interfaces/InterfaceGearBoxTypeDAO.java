package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import eftech.workingset.beans.GearBoxType;

public interface InterfaceGearBoxTypeDAO {
	ArrayList<GearBoxType> getGearBoxTypes();
	ArrayList<GearBoxType> getGearBoxTypes(int num, int nextRows);
	GearBoxType getGearBoxType(int id);
	GearBoxType getGearBoxTypeByName(String country);
	int getCountRows();
	
	GearBoxType createGearBoxType(String name);
	GearBoxType createGearBoxType(GearBoxType country);
	void deleteGearBoxType(GearBoxType oilStuff);
	void deleteGearBoxType(int id);
}
