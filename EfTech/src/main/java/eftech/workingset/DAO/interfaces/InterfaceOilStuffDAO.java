package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import eftech.workingset.beans.OilStuff;

public interface InterfaceOilStuffDAO {
	ArrayList<OilStuff> getOilStuffs();
	ArrayList<OilStuff> getOilStuffs(int num, int nextRows);
	OilStuff getOilStuff(int id);
	OilStuff getOilStuffByName(String country);
	int getCountRows();
	
	OilStuff createOilStuff(String name);
	OilStuff createOilStuff(OilStuff country);
	void deleteOilStuff(OilStuff oilStuff);
	void deleteOilStuff(int id);
}
