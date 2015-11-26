package eftech.workingset.beans.intefaces.base;

public interface InterfaceGood {
	
	int getId();
	void setId(int id);
	
	String getName();
	void setName(String name);
	
	String getPhoto();
	double getValue();
	
	double getPrice();
	void setPrice(double price);

	double getDiscount();
	double getPriceWithDiscount();
	int getInStock();
	
	
	String getDescription();
	String getSpecification();
	
	String getGoodName();
	void setPhoto(String string);
}
