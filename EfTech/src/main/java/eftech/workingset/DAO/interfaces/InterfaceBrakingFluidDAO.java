package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;

import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.ManufacturerSelected;

public interface InterfaceBrakingFluidDAO {
	ArrayList<BrakingFluid> getBrakingFluids();  
	BrakingFluid getBrakingFluid(int id);
	BrakingFluid getBrakingFluidByName(String name);
	BrakingFluid createBrakingFluid(BrakingFluid brFluid);
	BrakingFluid fillPrices(BrakingFluid brFluid);
	
	double minPrice();
	double maxPrice();
	int getCountRows(int currentPage, int elementsInList, double minPrice, double maxPrice, LinkedList<ManufacturerSelected> manufacturersSelected);
	ArrayList<BrakingFluid> getBrakingFluids(int currentPage, int elementsInList, double minPrice, double maxPrice,  LinkedList<ManufacturerSelected> manufacturersSelected);
	ArrayList<BrakingFluid> getBrakingFluidsRecommended();
}
