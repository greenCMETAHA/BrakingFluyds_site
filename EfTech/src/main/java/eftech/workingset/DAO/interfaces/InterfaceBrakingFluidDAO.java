package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import eftech.workingset.beans.BrakingFluid;

public interface InterfaceBrakingFluidDAO {
	ArrayList<BrakingFluid> getBrakingFluids();  
	BrakingFluid getBrakingFluid(int id);
	BrakingFluid getBrakingFluidByName(String name);
	BrakingFluid createBrakingFluid(BrakingFluid brFluid);
	BrakingFluid fillPrices(BrakingFluid brFluid);
}
