package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Manufacturer;

public interface InterfaceManufacturerDAO {
	ArrayList<Manufacturer> getManufacturers();
	ArrayList<Manufacturer> getManufacturers(int num, int nextRows);
	Manufacturer getManufacturer(int id);
	Manufacturer getManufacturerByName(String name);
	int getCountRows();
	
	Manufacturer createManufacturer(String name);
	Manufacturer createManufacturer(String name, int countryId);
	Manufacturer createManufacturer(Manufacturer manufacturer); 
	void deleteManufacturer(Manufacturer manufacturer);
	void deleteManufacturer(int id);

}
