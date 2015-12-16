package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.LinkedList;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.FluidClassSelected;
import eftech.workingset.beans.ManufacturerSelected;

public interface InterfaceBrakingFluidDAO {
	ArrayList<BrakingFluid> getBrakingFluids();  
	BrakingFluid getBrakingFluid(int id);
	BrakingFluid getBrakingFluidByName(String name);
	BrakingFluid createBrakingFluid(BrakingFluid brFluid);
	BrakingFluid createBrakingFluidWithoutPrice(BrakingFluid brFluid);
	BrakingFluid fillPrices(BrakingFluid brFluid);
	
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
			,double currentMinJudgementFilter,double currentMaxJudgementFilter, String searchField);
	ArrayList<BrakingFluid> getBrakingFluids(int currentPage, int elementsInList
			, LinkedList<ManufacturerSelected> manufacturersSelected, LinkedList<FluidClassSelected>fluidClassFilter
			,double minPrice, double maxPrice
			,double currentMinBoilingTemperatureDryFilter,double currentMaxBoilingTemperatureDryFilter
			,double currentMinBoilingTemperatureWetFilter,double currentMaxBoilingTemperatureWetFilter
			,double currentMinValueFilter,double currentMaxValueFilter
			,double currentMinViscosity40Filter,double currentMaxViscosity40Filter
			,double currentMinViscosity100Filter,double currentMaxViscosity100Filter
			,double currentMinJudgementFilter,double currentMaxJudgementFilter, String searchField);
	ArrayList<BrakingFluid> getBrakingFluidsRecommended();
}
