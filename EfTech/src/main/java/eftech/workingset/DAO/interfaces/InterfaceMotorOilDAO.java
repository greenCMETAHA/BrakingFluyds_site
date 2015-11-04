package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.LinkedList;

import eftech.workingset.beans.ManufacturerSelected;
import eftech.workingset.beans.MotorOil;

public interface InterfaceMotorOilDAO {
	ArrayList<MotorOil> getMotorOils();  
	MotorOil getMotorOil(int id);
	MotorOil getMotorOilByName(String name);
	MotorOil createMotorOil(MotorOil motorOil);
	MotorOil createMotorOilWithoutPrice(MotorOil motorOil);
	MotorOil fillPrices(MotorOil motorOil);
	
	double minData(String param);
	double maxData(String param);
	int getCountRows(int currentPage, int elementsInList
			, LinkedList<ManufacturerSelected> manufacturersSelected, LinkedList<FluidClassSelected>fluidClassFilter
			,double minPrice, double maxPrice
			,double currentMinBoilingTemperatureDryFilter,double currentMaxBoilingTemperatureDryFilter
			,double currentMinBoilingTemperatureWetFilter,double currentMaxBoilingTemperatureWetFilter
			,double currentMinValueFilter,double currentMaxValueFilter
			,double currentMinViscosity40Filter,double currentMaxViscosity40Filter
			,double currentMinViscosity100Filter,double currentMaxViscosity100Filter
			,double currentMinJudgementFilter,double currentMaxJudgementFilter);
	ArrayList<MotorOil> getMotorOils(int currentPage, int elementsInList
			, LinkedList<ManufacturerSelected> manufacturersSelected, LinkedList<FluidClassSelected>fluidClassFilter
			,double minPrice, double maxPrice
			,double currentMinBoilingTemperatureDryFilter,double currentMaxBoilingTemperatureDryFilter
			,double currentMinBoilingTemperatureWetFilter,double currentMaxBoilingTemperatureWetFilter
			,double currentMinValueFilter,double currentMaxValueFilter
			,double currentMinViscosity40Filter,double currentMaxViscosity40Filter
			,double currentMinViscosity100Filter,double currentMaxViscosity100Filter
			,double currentMinJudgementFilter,double currentMaxJudgementFilter);
	ArrayList<MotorOil> getMotorOilsRecommended();
}
