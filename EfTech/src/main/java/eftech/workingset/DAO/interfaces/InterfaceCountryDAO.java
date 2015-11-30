package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Country;

public interface InterfaceCountryDAO {
	ArrayList<Country> getCountries();
	ArrayList<Country> getCountries(int num, int nextRows);
	Country getCountry(int id);
	Country getCountryByName(String country);
	int getCountRows();
	
	Country createCountry(String name);
	Country createCountry(Country country);
	void deleteCountry(Country country);
	void deleteCountry(int id);
	
}
