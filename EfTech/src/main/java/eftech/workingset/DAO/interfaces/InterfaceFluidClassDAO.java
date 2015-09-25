package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.FluidClass;

public interface InterfaceFluidClassDAO {
	ArrayList<FluidClass> getFluidClassis();
	FluidClass getFluidClass(int id);
	FluidClass getFluidClassByName(String name);
	FluidClass createFluidClass(String name);

}
