package eftech.vasil.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class testController {

	 
	 @Mock
	 private HomeController controller;
	 
	 @Test
	 public void testMappings() throws Exception {
		 
		 MockHttpServletRequest request = new MockHttpServletRequest();
//		 MockHttpServletResponse response = new MockHttpServletResponse();
//		 AnnotationMethodHandlerAdapter adapter = new AnnotationMethodHandlerAdapter();
//		 
//	  request.setRequestURI("/employee/list");
//	  request.setMethod("GET");
//	  request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING, true);
//	 
//	  adapter.handle(request, response, controller);
//	 
//	  Mockito.verify(controller).createBasket();
		 xmlConfigSetup("classpath:mvc-test.xml").build()
	 }
}