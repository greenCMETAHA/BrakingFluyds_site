package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.FluidClass;

public interface InterfaceFluidClassDAO {
	ArrayList<FluidClass> getFluidClassis();
	ArrayList<FluidClass> getFluidClassis(int num, int nextRows);
	FluidClass getFluidClass(int id);
	FluidClass getFluidClassByName(String name);
	int getCountRows();
	
	FluidClass createFluidClass(String name);
	void deleteFluidClass(FluidClass fluidClass);
	void deleteFluidClass(int id);
}
