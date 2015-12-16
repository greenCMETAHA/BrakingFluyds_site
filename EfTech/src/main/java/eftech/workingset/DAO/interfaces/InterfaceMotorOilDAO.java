package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import eftech.workingset.beans.EngineType;
import eftech.workingset.beans.ManufacturerSelected;
import eftech.workingset.beans.MotorOil;
import eftech.workingset.beans.OilStuff;

public interface InterfaceMotorOilDAO {
	ArrayList<MotorOil> getMotorOils();  
	MotorOil getMotorOil(int id);
	MotorOil getMotorOilByName(String name);
	MotorOil createMotorOil(MotorOil motorOil);
	MotorOil createMotorOilWithoutPrice(MotorOil motorOil);
	MotorOil fillPrices(MotorOil motorOil);
	ArrayList<String> getMotorOilViscosities();
	
	double minData(String param);
	double maxData(String param);
	
	int getCountRows(int currentPage, int elementsInList
			, LinkedList<ManufacturerSelected> manufacturersSelected, LinkedList<EngineType>engineTypeFilter, LinkedList<OilStuff>oilStuffFilter
			,HashMap<String,Boolean> viscosityFilter, double minPrice, double maxPrice
			,double currentMinValueFilter,double currentMaxValueFilter
			,double currentMinJudgementFilter,double currentMaxJudgementFilter, String searchField);
	ArrayList<MotorOil> getMotorOils(int currentPage, int elementsInList
			, LinkedList<ManufacturerSelected> manufacturersSelected,LinkedList<EngineType>engineTypeFilter, LinkedList<OilStuff>oilStuffFilter
			,HashMap<String,Boolean> viscosityFilter, double minPrice, double maxPrice
			,double currentMinValueFilter,double currentMaxValueFilter
			,double currentMinJudgementFilter,double currentMaxJudgementFilter, String searchField);
	ArrayList<MotorOil> getMotorOilsRecommended();
}
