package eftech.workingset.DAO.interfaces;

import java.util.ArrayList;

import eftech.workingset.beans.Log;
import eftech.workingset.beans.Price;

public interface InterfacePriceDAO {
	int getCountRows(int id);  //��� ������������ ��������
	ArrayList<Price> getPrices();
	ArrayList<Price> getPrices(int id); //��� ������������ ��������
	Price getPriceById(int id);

	Price createPrice(Price price);
}
