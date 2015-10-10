package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Log;
import eftech.workingset.beans.Price;

public interface InterfacePriceDAO {
	int getCountRows(int id);  //для определенной жидкости
	ArrayList<Price> getPrices();
	ArrayList<Price> getPrices(int id); //для определенной жидкости
	Price getPriceById(int id);

	Price createPrice(Price price);
}
