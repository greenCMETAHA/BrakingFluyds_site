package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Price;

public interface InterfacePriceDAO {
	int getCountRows(int id, String goodPrefix);  //для определенной жидкости
	ArrayList<Price> getPrices(String goodPrefix);
	ArrayList<Price> getPrices(int id, String goodPrefix); //для определенной жидкости
	Price getPriceById(int id, String goodPrefix);

	Price createPrice(Price price, String goodPrefix);
}
