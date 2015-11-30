package eftech.workingset.beans;

import java.io.File;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceBrakingFluid;
import eftech.workingset.beans.intefaces.InterfaceFluidClass;
import eftech.workingset.beans.intefaces.InterfaceManufacturer;

public class BrakingFluid implements InterfaceBrakingFluid {
	private int id;
	private String name;
	private InterfaceManufacturer manufacturer;
	private double price; //private InterfacePrice price;
	private InterfaceFluidClass fluidClass;
	private double boilingTemperatureDry;
	private double boilingTemperatureWet;
	private double value;
	private String photo;
	private String description;
	private double viscosity40;
	private double viscosity100;
	private String specification;
	private double judgement;
	private double discount;
	private int inStock;
	
	public BrakingFluid() {
		super();
		name="";
		photo="";
		description="";
		specification="";
	}

	public BrakingFluid(int id, String name, InterfaceManufacturer manufacturer, double price, //InterfacePrice price,
			InterfaceFluidClass fluidClass, double boilingTemperatureDry, double boilingTemperatureWet, double value,
			String photo, String description, double viscosity40, double viscosity100, String specification,
			double judgement, double discount, int inStock) {
		super();
		this.id = id;
		this.name = name;
		this.manufacturer = manufacturer;
		this.price = price;
		this.fluidClass = fluidClass;
		this.boilingTemperatureDry = boilingTemperatureDry;
		this.boilingTemperatureWet = boilingTemperatureWet;
		this.value = value;
		this.photo = createPhotoPath(photo);
		this.description = description;
		this.viscosity40 = viscosity40;
		this.viscosity100 = viscosity100;
		this.specification = specification;
		this.judgement = judgement;
		this.discount=discount;
		this.inStock=inStock;
	}
	public BrakingFluid(int id, String name, String manufacturer, String fluidClass, //InterfacePrice price,
			double boilingTemperatureDry, double boilingTemperatureWet, double value, double price,
			String photo, String description, double viscosity40, double viscosity100, String specification,
			double judgement, double discount, int inStock) {
		super();
		this.id = id;
		this.name = name;

		Manufacturer currentManufacturer=new Manufacturer();
		currentManufacturer.setName(manufacturer);
		this.manufacturer = currentManufacturer;
		
		FluidClass currentFluidClass=new FluidClass();
		currentFluidClass.setName(fluidClass);
		this.fluidClass = currentFluidClass;
		
		this.boilingTemperatureDry = boilingTemperatureDry;
		this.boilingTemperatureWet = boilingTemperatureWet;
		this.value = value;
		this.price = price;		
		this.photo = createPhotoPath(photo);
		this.description = description;
		this.viscosity40 = viscosity40;
		this.viscosity100 = viscosity100;
		this.specification = specification;
		this.judgement = judgement;
		this.discount=discount;
		this.inStock=inStock;
	}	

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public int getInStock() {
		return inStock;
	}

	public void setInStock(int inStock) {
		this.inStock = inStock;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the manufacturer
	 */
	public InterfaceManufacturer getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(InterfaceManufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

//	/**
//	 * @return the price
//	 */
//	public InterfacePrice getPrice() {
//		return price;
//	}
//
//	/**
//	 * @param price the price to set
//	 */
//	public void setPrice(InterfacePrice price) {
//		this.price = price;
//	}

	/**
	 * @return the price
	 */
	@Override
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}	
	
	/**
	 * @return the fluidClass
	 */
	public InterfaceFluidClass getFluidClass() {
		return fluidClass;
	}

	/**
	 * @param fluidClass the fluidClass to set
	 */
	public void setFluidClass(InterfaceFluidClass fluidClass) {
		this.fluidClass = fluidClass;
	}

	/**
	 * @return the boilingTemperatureDry
	 */
	public double getBoilingTemperatureDry() {
		return boilingTemperatureDry;
	}

	/**
	 * @param boilingTemperatureDry the boilingTemperatureDry to set
	 */
	public void setBoilingTemperatureDry(double boilingTemperatureDry) {
		this.boilingTemperatureDry = boilingTemperatureDry;
	}

	/**
	 * @return the boilingTemperatureWet
	 */
	public double getBoilingTemperatureWet() {
		return boilingTemperatureWet;
	}

	/**
	 * @param boilingTemperatureWet the boilingTemperatureWet to set
	 */
	public void setBoilingTemperatureWet(double boilingTemperatureWet) {
		this.boilingTemperatureWet = boilingTemperatureWet;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the photo
	 */
	public String getPhoto() {
		String result="";
		if (new File(photo).exists()){
			result=photo;
		}
			
		return photo;
		
	}

	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(String photo) {
		this.photo = createPhotoPath(photo);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the viscosity40
	 */
	public double getViscosity40() {
		return viscosity40;
	}

	/**
	 * @param viscosity40 the viscosity40 to set
	 */
	public void setViscosity40(double viscosity40) {
		this.viscosity40 = viscosity40;
	}

	/**
	 * @return the viscosity100
	 */
	public double getViscosity100() {
		return viscosity100;
	}

	/**
	 * @param viscosity100 the viscosity100 to set
	 */
	public void setViscosity100(double viscosity100) {
		this.viscosity100 = viscosity100;
	}

	/**
	 * @return the specification
	 */
	public String getSpecification() {
		return specification;
	}

	/**
	 * @param specification the specification to set
	 */
	public void setSpecification(String specification) {
		this.specification = specification;
	}

	/**
	 * @return the judgement
	 */
	public double getJudgement() {
		return judgement;
	}

	/**
	 * @param judgement the judgement to set
	 */
	public void setJudgement(double judgement) {
		this.judgement = judgement;
	}
	
	private String createPhotoPath(String photo){
		String result="";

		result=photo;
		
		return result;
	}
	
	public boolean hasPhoto(){
		boolean result;
	
		result=(getPhoto().length()==0?false:true);
		
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BrakingFluid [id=" + id + ", name=" + name + ", value=" + value + "]";
	}

	@Override
	public String getGoodName() {
		return Service.BRAKING_FLUID_PREFIX;
	}

	@Override
	public double getPriceWithDiscount() {
		double result=getPrice();
		
		if (discount>0){
			result = result-(result/100*discount);
			result = Math.round(result*100)/100;
		}

		return result;
	}


}
