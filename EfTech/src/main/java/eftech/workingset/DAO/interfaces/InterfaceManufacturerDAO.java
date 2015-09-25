package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Manufacturer;

public interface InterfaceManufacturerDAO {
	ArrayList<Manufacturer> getManufacturers();
	Manufacturer getManufacturer(int id);
	Manufacturer getManufacturerByName(String name);
	Manufacturer createManufacturer(String name);
	Manufacturer createManufacturer(String name, int countryId);

}
