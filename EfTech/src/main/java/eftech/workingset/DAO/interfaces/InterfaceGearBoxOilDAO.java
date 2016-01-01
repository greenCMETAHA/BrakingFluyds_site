package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import eftech.workingset.beans.GearBoxType;
import eftech.workingset.beans.ManufacturerSelected;
import eftech.workingset.beans.GearBoxOil;
import eftech.workingset.beans.OilStuff;

public interface InterfaceGearBoxOilDAO {
	ArrayList<GearBoxOil> getGearBoxOils();  
	GearBoxOil getGearBoxOil(int id);
	GearBoxOil getGearBoxOilByName(String name);
	GearBoxOil createGearBoxOil(GearBoxOil motorOil);
	GearBoxOil createGearBoxOilWithoutPrice(GearBoxOil motorOil);
	GearBoxOil fillPrices(GearBoxOil motorOil);
	ArrayList<String> getGearBoxOilViscosities();
	
	double minData(String param);
	double maxData(String param);
	
	int getCountRows(int currentPage, int elementsInList
			, LinkedList<ManufacturerSelected> manufacturersSelected, LinkedList<GearBoxType>gearBoxTypeFilter, LinkedList<OilStuff>oilStuffFilter
			,HashMap<String,Boolean> viscosityFilter, double minPrice, double maxPrice
			,double currentMinValueFilter,double currentMaxValueFilter
			,double currentMinJudgementFilter,double currentMaxJudgementFilter, String searchField);
	ArrayList<GearBoxOil> getGearBoxOils(int currentPage, int elementsInList
			, LinkedList<ManufacturerSelected> manufacturersSelected,LinkedList<GearBoxType>gearBoxTypeFilter, LinkedList<OilStuff>oilStuffFilter
			,HashMap<String,Boolean> viscosityFilter, double minPrice, double maxPrice
			,double currentMinValueFilter,double currentMaxValueFilter
			,double currentMinJudgementFilter,double currentMaxJudgementFilter, String searchField);
	ArrayList<GearBoxOil> getGearBoxOilsRecommended();
}
