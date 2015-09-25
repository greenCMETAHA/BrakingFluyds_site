<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name='robots' content='all, follow' />
    <meta name="description" content="" />
    <meta name="keywords" content="" />
    <title>ЭфТех ООО</title>   
    <link href="resources/css/default.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="resources/css/gray.css" rel="stylesheet" type="text/css" media="screen" /> <!-- color skin: blue / red / green / dark -->
    <link href="resources/css/datePicker.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="resources/css/wysiwyg.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="resources/css/fancybox-1.3.1.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="resources/css/visualize.css" rel="stylesheet" type="text/css" media="screen" />
    
    <script type="text/javascript" src="resources/js/jquery-1.4.2.min.js"></script>   
    <script type="text/javascript" src="resources/js/jquery.dimensions.min.js"></script>
    
    <!-- // Tabs // -->
    <script type="text/javascript" src="resources/js/ui.core.js"></script>
    <script type="text/javascript" src="resources/js/jquery.ui.tabs.min.js"></script>

    <!-- // Table drag and drop rows // -->
    <script type="text/javascript" src="resources/js/tablednd.js"></script>

    <!-- // Date Picker // -->
    <script type="text/javascript" src="resources/js/date.js"></script>
    <!--[if IE]><script type="text/javascript" src="resources/js/jquery.bgiframe.js"></script><![endif]-->
    <script type="text/javascript" src="resources/js/jquery.datePicker.js"></script>

    <!-- // Wysiwyg // -->
    <script type="text/javascript" src="resources/js/jquery.wysiwyg.js"></script>

    <!-- // Graphs // -->
    <script type="text/javascript" src="resources/js/excanvas.js"></script>
    <script type="text/javascript" src="resources/js/jquery.visualize.js"></script>

    <!-- // Fancybox // -->
  	<script type="text/javascript" src="resources/js/jquery.fancybox-1.3.1.js"></script>

    <!-- // File upload // --> 
    <script type="text/javascript" src="resources/js/jquery.filestyle.js"></script>
    
    <script type="text/javascript" src="resources/js/init.js"></script>
	
</head>
<body>

   <div class="box">
     <div class="headlines">
       <h2><span><c:out value="${requestScope.pageInfo}"  /></span></h2>
       <!-- <a href="#help" class="help"></a> --> 
     </div>
     <div class="box-content">
	     <form action="insert" class="formBox" method="POST"  enctype="multipart/form-data">
	     	<input name="insert" type="hidden" value="${requestScope.variant}" >
			<c:set var="currentBrakFluid" value="${requestScope.currentBrakFluid}"></c:set>
			<input name="id_BrakeFluid" type="hidden" value="${currentBrakFluid.getId()}" >
	     	<c:choose>
			<c:when test="${requestScope.errors.size()>0}">
	        	  <!-- Error form message -->            
		         <div class="form-message error">
		           <p>При загрузке произошли ошибки:</p>
		           <ul>
		           	<c:forEach var="currentError" items="${requestScope.errors}">
		             <li><c:out value="${currentError}"></c:out></li>
		            </c:forEach> 
		           </ul>
		         </div>
			</c:when>
			</c:choose>
			
			<c:set var="readOnly" value=""></c:set>
			<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_OFFER+PRICE','ROLE_PRICE')">
				<c:set var="readOnly" value="readonly"></c:set>
			</sec:authorize>
 			 <fieldset>
				<div class="clearfix">
		         <div class="lab"><label for="input-three">Наименование:</label></div>
		     	 <div class="con"><input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getName()}" name="name_BrakeFluid" id="input-three" /></div>
		     	</div>
				<div class="clearfix">
		         <div class="lab"><label for="input-three">Производитель:</label></div>
		     	 <div class="con"><input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getManufacturer().getName()}" name="Manufacturer" id="input-three" /></div>
		     	</div>
		    	
				<div class="clearfix">
		         <div class="lab"><label for="input-three">Класс жидкости:</label></div>
		     	 <div class="con"><input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getFluidClass().getName()}" name="FluidClass" id="input-three" /></div>
		     	</div>
				<div class="clearfix">
		         <div class="lab"><label for="input-three">Температура кипения (сух.):</label><p></div>
		     	 <div class="con"><input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getBoilingTemperatureDry()}" name="BoilingTemperatureDry" id="input-three" /></div>
		     	</div>
				<div class="clearfix">
		         <div class="lab"><label for="input-three">Температура кипения (вл.):</label></div>
		     	 <div class="con"><input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getBoilingTemperatureWet()}" name="BoilingTemperatureWet" id="input-three" /></div>
		     	</div>
				<div class="clearfix">
		         <div class="lab"><label for="input-three">Объём:</label></div>
		     	 <div class="con"><input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getValue()}" name="Value" id="input-three" /></div>
		     	</div>
		     	<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRICE')">
					<div class="clearfix">
			         <div class="lab"><label for="input-three">Цена:</label></div>
			     	 <div class="con"><input type="text" class="input" value="${currentBrakFluid.getPrice()}" name="Price" id="input-three" /></div>
			     	</div>
			    </sec:authorize>
		     	<sec:authorize access="!hasAnyRole('ROLE_ADMIN','ROLE_PRICE')">
			     	 <input type="hidden" value="${currentBrakFluid.getPrice()}" name="Price" />
			    </sec:authorize>			    
				<div class="clearfix">
		         <div class="lab"><label for="input-three">Изображение:</label></div>
		     	 <div >
		     	 		<c:choose>
						<c:when test="${currentBrakFluid.hasPhoto()}">
							<img src="resources/jpg/<c:out value="${currentBrakFluid.getPhoto()}"  />" alt="${currentBrakFluid.getName()}">
						</c:when>
						</c:choose><br>
				 </div>
			    </div>
			    <input name="photoBackUp" type="hidden" value="${requestScope.photoBackUp}" >
			    <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')">
				    <div class="clearfix file">
			            <div class="con"><input type="file" name="Photo" class="upload-file" id="file" />
			              <div class="clearfix">
			              	<input type="submit" name="button" value="Обновить"class="button" >
			              </div> 	
	                	</div>
	                </div>
                </sec:authorize>
				<sec:authorize access=" not hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')">
			     	  <div class="clearfix file">
			            <div class="con">
			            	<input  type="file"  name="Photo" class="upload-file" id="file"/>
	                	</div>
	                </div>
			    </sec:authorize>	                
			    <div class="clearfix">
			         <div class="lab"><label for="textarea-one">Описание:</label></div>
			       <div class="con"><textarea name="Description"  readonly="${readOnly}" cols="" rows="7" class="textarea" id="textarea-one"><c:out value="${currentBrakFluid.getDescription()}"  /></textarea></div>
			     </div>
				<div class="clearfix">
		         <div class="lab"><label for="input-three">Вязкость (при -40):</label></div>
		     	 <div class="con"><input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getViscosity40()}" name="Viscosity40" id="input-three" /></div>
		     	</div>
		     	<div class="clearfix">
		         <div class="lab"><label for="input-three">Вязкость (при 100):</label></div>
		     	 <div class="con"><input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getViscosity100()}" name="Viscosity100" id="input-three" /></div>
		     	</div>
			    <div class="clearfix">
			         <div class="lab"><label for="textarea-one">Спецификация:</label></div>
			       <div class="con"><textarea name="Specification" readonly="${readOnly}"  cols="" rows="7" class="textarea" id="textarea-one"><c:out value="${currentBrakFluid.getSpecification()}"  /></textarea></div>
			     </div>
				<div class="clearfix">
		         <div class="lab"><label for="input-three">Оценка покупателей:</label></div>
		     	 <div class="con"><input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getJudgement()}" name="Judgement" id="input-three" /></div>
		     	</div>			 
		     </fieldset>
		     
           <div class="btn-submit"><!-- Submit form -->
	       	 <input type="submit" name="button" value="Сохранить"class="button" >
	       	 <input type="submit"  name="button" value="На главную" class="button" >
	       </div>
		         
	   </form>
   </div><!-- /box-content -->
   </div>
   <!-- /box -->
</div>

</body>
</html>