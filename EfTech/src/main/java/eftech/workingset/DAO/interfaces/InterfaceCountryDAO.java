package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Country;

public interface InterfaceCountryDAO {
	ArrayList<Country> getCountries();
	Country getCountry(int id);
	Country getCountryByName(String country);
	Country createCountry(String name);

}
