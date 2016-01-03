package eftech.workingset.beans;

import java.io.File;

import eftech.workingset.Services.Service;
import eftech.workingset.beans.intefaces.InterfaceEngineType;
import eftech.workingset.beans.intefaces.InterfaceGearBoxOil;
import eftech.workingset.beans.intefaces.InterfaceGearBoxType;
import eftech.workingset.beans.intefaces.InterfaceManufacturer;
import eftech.workingset.beans.intefaces.InterfaceOilStuff;

public class GearBoxOil implements InterfaceGearBoxOil{
	
	private int id;
	private String manufacturerCode;
	private String name;
	private InterfaceManufacturer manufacturer;
	private double price; 
	private OilStuff oilStuff;
	private GearBoxType gearBoxType;
	private String viscosity;
	private double value;
	private String photo;
	private String description;
	private String specification;
	private double judgement;
	private double discount;
	private int inStock;
		
	public GearBoxOil() {
			super();
			name="";
			photo="";
			description="";
			specification="";
			viscosity="";
			
			gearBoxType = new GearBoxType();
			gearBoxType.setName("<empty>");
			
			oilStuff = new OilStuff();
			oilStuff.setName("<empty>");
			
		}

	public GearBoxOil(int id, String name, InterfaceManufacturer manufacturer, double price, InterfaceOilStuff oilStuff
				, InterfaceGearBoxType gearBoxType, String viscosity, double value, String photo, String description
				, String specification,	double judgement, double discount, int inStock) {
			super();
			this.id = id;
			this.name = name;
			this.manufacturer = manufacturer;
			this.price = price;
			this.oilStuff = (OilStuff) oilStuff;
			this.gearBoxType = (GearBoxType) gearBoxType;
			this.viscosity = viscosity;
			this.value = value;
			this.photo = createPhotoPath(photo);
			this.description = description;
			this.specification = specification;
			this.judgement = judgement;
			this.discount=discount;
			this.inStock=inStock;
		}
		
		public GearBoxOil(int id, String name, String manufacturer, String oilStuff, String gearBoxType ,String viscosity, double value
				,double price, String photo, String description, String specification, double judgement, double discount, int inStock) {
			super();
			this.id = id;
			this.name = name;

			Manufacturer currentManufacturer=new Manufacturer();
			currentManufacturer.setName(manufacturer);
			this.manufacturer = currentManufacturer;
			
			OilStuff currentOilStuff=new OilStuff();
			currentOilStuff.setName(oilStuff);
			this.oilStuff = currentOilStuff;

			GearBoxType currentGearBoxType=new GearBoxType();
			currentGearBoxType.setName(gearBoxType);
			this.gearBoxType = currentGearBoxType;
			
			this.value = value;
			this.price = price;		
			this.photo = createPhotoPath(photo);
			this.description = description;
			this.viscosity = viscosity;
			this.specification = specification;
			this.judgement = judgement;
			this.discount=discount;
			this.inStock=inStock;
		}	

		/**
		 * @return the oilStuff
		 */
		public OilStuff getOilStuff() {
			return oilStuff;
		}

		/**
		 * @param oilStuff the oilStuff to set
		 */
		public void setOilStuff(OilStuff oilStuff) {
			this.oilStuff = oilStuff;
		}

		/**
		 * @return the engineType
		 */
		public GearBoxType getGearBoxType() {
			return gearBoxType;
		}

		/**
		 * @param engineType the engineType to set
		 */
		public void setGearBoxType(GearBoxType gearBoxType) {
			this.gearBoxType = gearBoxType;
		}

		/**
		 * @return the viscosity
		 */
		public String getViscosity() {
			return viscosity;
		}

		/**
		 * @param viscosity the viscosity to set
		 */
		public void setViscosity(String viscosity) {
			this.viscosity = viscosity;
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

		public String getManufacturerCode() {
			return manufacturerCode;
		}

		public void setManufacturerCode(String manufacturerCode) {
			this.manufacturerCode = manufacturerCode;
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

//		/**
//		 * @return the price
//		 */
//		public InterfacePrice getPrice() {
//			return price;
//		}
	//
//		/**
//		 * @param price the price to set
//		 */
//		public void setPrice(InterfacePrice price) {
//			this.price = price;
//		}

		/**
		 * @return the price
		 */
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
			return "MotorOil [id=" + id + ", name=" + name + ", value=" + value + "]";
		}

		@Override
		public String getGoodName() {
			return Service.GEARBOX_OIL_PREFIX;
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
