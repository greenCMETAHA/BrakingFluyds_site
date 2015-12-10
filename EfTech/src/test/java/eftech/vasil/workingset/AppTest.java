package eftech.vasil.workingset;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import eftech.workingset.DAO.templates.CountryTemplate;
import eftech.workingset.beans.BrakingFluid;
import eftech.workingset.beans.Country;

public class AppTest {
	
	@Mock
	CountryTemplate countryDAO=mock(CountryTemplate.class);
	
	Country country = null;
	   
	@Before
	public void before(){
	    // create mock
		BrakingFluid test = Mockito.mock(BrakingFluid.class);
		//countryDAO = Mockito.mock(CountryTemplate.class);
	    // define return value for method getUniqueId()
	    country = new Country(21,"<empty>");
	    when(countryDAO.getCountry(21)).thenReturn(country);
	    
	    
	    
	}
	
	@Test
    public void testSimpleInt() {
		System.out.println(countryDAO.getCountry(21).getName());


	    // use mock in test....
	    assertEquals(countryDAO.getCountry(21).getName(), "<empty>");
    }
}
