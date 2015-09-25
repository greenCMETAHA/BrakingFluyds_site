package eftech.vasil.controller;

import java.io.File;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import eftech.workingset.DAO.templates.BrakingFluidTemplate;
import eftech.workingset.DAO.templates.ClientTemplate;
import eftech.workingset.DAO.templates.FluidClassTemplate;
import eftech.workingset.DAO.templates.ManufacturerTemplate;
import eftech.workingset.DAO.templates.UserTemplate;
import eftech.workingset.Services.Service;
import eftech.workingset.beans.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase{
	@Autowired
	BrakingFluidTemplate brakingFluidDAO;

	@Autowired
	UserTemplate userDAO;
	
	@Autowired
	ManufacturerTemplate manufacturerDAO;

	@Autowired
	FluidClassTemplate fluidClassDAO;
	
	@Autowired
	ClientTemplate clientDAO;	
	
	public AppTest( String testName ){
	        super( testName );
	}
	 
	public static TestSuite suite(){
		    return new TestSuite( AppTest.class );
	}
	   
	public void testApp() {
	   	boolean resultOfTest=true;
	   	
	   	System.out.println("Тестирование началось");
	   	   	
	   	
	   	try{
	   		
	   		System.out.println(userDAO.getUsers());
		   	
		   	System.out.println(fluidClassDAO.getFluidClassis());
		   	FluidClass fc=fluidClassDAO.getFluidClassByName("DOT4");
		   	System.out.println(fc.getName());

		   	
	   		/*
	   		  BrakingFluid brF=brakingFluidDAO.getBrakingFluid(2); //C ID=1 нету в тестовой БД
	   		if (brF.getName().length()==0){
	   			System.out.println("BrakingFluid brF=db.getBrakingFluid(2);");
	   			resultOfTest=false;
	   		}
	   		brF.setPhoto("d:\\grimpi_Christmas.jpg");
	   		brakingFluidDAO.createBrakingFluid(brF);
	   		
	   		
	   		boolean result = Service.importFromExcelProduct(new File("d://Product.xlsx"),"C:\Users\Asus\workspace_test_forWork\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\EfTech\");
	   		if (!result){
	   			resultOfTest=false;
	   		}
	   		result =Service.importFromExcelPrices(new File("d://prices.xlsx"));
	   		if (!result){
	   			resultOfTest=false;
	   		}
	   				
	   		
	   		

	   		User user=db.getUser("admin", "444");
	   		if (user.getName().length()>0){
	   			System.out.println("User user=db.getUser(admin, 444);");
	   			resultOfTest=false;
	   		}
	   		user=db.getUser("admin", "111");
	   		if (user.getName().length()==0){
	   			System.out.println("User user=db.getUser(admin, 111);");
	   			resultOfTest=false;
	   		}
	   		
	   		ArrayList<Role> r=db.getRoles();
	   		if (r.size()==0){
	   			System.out.println("ArrayString<Role> r=db.getRoles();");
	   			resultOfTest=false;
	   		}
	   		Role role=db.getRole(1);
	   		if (role.getName().length()==0){
	   			System.out.println("Role role=db.getRole(1);");
	   			resultOfTest=false;
	   		}
	   		ArrayList<Manufacturer> manArray=db.getManufacturers();
	   		if (manArray.size()==0){
	   			System.out.println("ArrayList<Manufacturer> manArray=db.getManufacturers();");
	   			resultOfTest=false;
	   		}
	   		Manufacturer manufacturer=db.getManufacturer(1);
	   		if (manufacturer.getName().length()==0){
	   			System.out.println("Manufacturer manufacturer=db.getManufacturer(1);");
	   			resultOfTest=false;
	   		}	   		
	   		manufacturer=db.getManufacturerByName("Toyota");
	   		if (manufacturer.getName().length()==0){
	   			System.out.println("Manufacturer manufacturer=db.getManufacturer(Toyota);");
	   			resultOfTest=false;
	   		}	
	   		
	   		ArrayList<FluidClass> fcArray=db.getFluidClassis();
	   		if (fcArray.size()==0){
	   			System.out.println("ArrayList<FluidClass> fcArray=db.getFluidClassis()");
	   			resultOfTest=false;
	   		}
	   		FluidClass fc=db.getFluidClass(1);
	   		if (fc.getName().length()==0){
	   			System.out.println("FluidClass fc=db.getFluidClass(1);");
	   			resultOfTest=false;
	   		}	   		
	   		fc=db.getFluidClassByName("DOT4");
	   		if (fc.getName().length()==0){
	   			System.out.println("fc=db.getFluidClassByName(DOT4);");
	   			resultOfTest=false;
	   		}	   			   		
	   		
	   		
	   		ArrayList<Country> countryArray=db.getCountries();
	   		if (countryArray.size()==0){
	   			System.out.println("ArrayList<FluidClass> fcArray=db.getFluidClassis()");
	   			resultOfTest=false;
	   		}
	   		Country country=db.getCountry(1);
	   		if (country.getName().length()==0){
	   			System.out.println("Country country=db.getCountry(1);");
	   			resultOfTest=false;
	   		}	   		
	   		country=db.getCountryByName("Германия");
	   		if (manufacturer.getName().length()==0){
	   			System.out.println("country=db.getCountryByName(Германия);");
	   			resultOfTest=false;
	   		}	   			   		
	   		
	   		ArrayList<Client> clientArray=db.getClients();
	   		if (clientArray.size()==0){
	   			System.out.println("ArrayList<Client> clientArray=db.getClients();");
	   			resultOfTest=false;
	   		}
	   		Client client=db.getClient(1);
	   		if (client.getName().length()==0){
	   			System.out.println("Client client=db.getClient(1);");
	   			resultOfTest=false;
	   		}	  
	   		
	   		
	   		ArrayList<BrakingFluid> bkArray=db.getBrakingFluids();
	   		if (bkArray.size()==0){
	   			System.out.println("ArrayList<BrakingFluid> bkArray=db.getBrakingFluids();");
	   			resultOfTest=false;
	   		}
	   		BrakingFluid brF=db.getBrakingFluid(2); //C ID=1 нету в тестовой БД
	   		if (brF.getName().length()==0){
	   			System.out.println("BrakingFluid brF=db.getBrakingFluid(2);");
	   			resultOfTest=false;
	   		}	   		
	   		brF=db.getBrakingFluidByName("Товар1");
	   		if (brF.getName().length()==0){
	   			System.out.println("brF=db.getBrakingFluidByName(Товар1);");
	   			resultOfTest=false;
	   		}
	   		
	   		
	   		
	   		manufacturer=db.createManufacturer("Procter&Gamble");
	   		if (manufacturer.getName().length()==0){
	   			System.out.println("manufacturer=db.CreateManufacturer(Procter&Gamble);");
	   			resultOfTest=false;
	   		}		   		
	   		manufacturer=db.createManufacturer("Apple","Canada");
	   		if (manufacturer.getName().length()==0){
	   			System.out.println("manufacturer=db.CreateManufacturer(Apple,Canada);");
	   			resultOfTest=false;
	   		}		   		
	   		fc=db.createFluidClass("LADA");
	   		if (fc.getName().length()==0){
	   			System.out.println("fc=db.CreateFluidClass(LADA);");
	   			resultOfTest=false;
	   		}			   		
	   		country=db.createCountry("Zimbabve");
	   		if (country.getName().length()==0){
	   			System.out.println("country=db.createCountry(Zimbabve);");
	   			resultOfTest=false;
	   		}
	   		brF.setName("Unknown ware");
	   		BrakingFluid brF2=db.createBrakingFluid(brF);
	   		if (country.getName().length()==0){
	   			System.out.println("brF.setName(Unknown ware);");
	   			resultOfTest=false;
	   		}			   		
	   		brF.setName("Товар1");
	   		brF2=db.createBrakingFluid(brF);
	   		if (country.getName().length()==0){
	   			System.out.println("brF.setName(Unknown ware);");
	   			resultOfTest=false;
	   		}			   		
	   		*/
	   	
	   	}catch(Exception e){
	   		resultOfTest=false;
	   		e.printStackTrace();
	   	}
	   	
	   	
   		System.out.println("Тестирование закончено. "+(resultOfTest?"Положительно":"      Завалено"));

   		assertTrue( resultOfTest );
	}	   
}
