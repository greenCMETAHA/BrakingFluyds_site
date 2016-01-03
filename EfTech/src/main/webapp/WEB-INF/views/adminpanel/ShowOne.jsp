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
    <link href="resources/Adminpanel/css/default.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="resources/Adminpanel/css/gray.css" rel="stylesheet" type="text/css" media="screen" /> <!-- color skin: blue / red / green / dark -->
  
  </head>
<body>

	<c:set var="currentUser" value="${requestScope.user}"></c:set>
	<c:set var="currentBrakFluid" value="${requestScope.currentBrakFluid}"></c:set>
<!-- /box -->
   <div class="box">
     <div class="headlines">
       <h2><span>Данные о товаре:</span></h2>
       <!-- <a href="#help" class="help"></a> --> 
     </div>
     <div class="box-content">
     <form class="formBox" method="GET" action="makeDemand?id=${currentBrakFluid.getId()}">
     	<input type="hidden" name="id" value="${currentBrakFluid.getId()}">
     	
       <fieldset>
		<div class="clearfix">
         <div class="lab"><label for="input-three">Наименование:</label></div>
     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getName()}" name="" id="input-three" /></div>
     	</div>
		<div class="clearfix">
         <div class="lab"><label for="input-three">Наименование/код по производителю:</label></div>
     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getManufacturerCode()}" name="" id="input-three" /></div>
     	</div>
		<div class="clearfix">
         <div class="lab"><label for="input-three">Производитель:</label></div>
     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getManufacturer().getName()}" name="" id="input-three" /></div>
     	</div>
    	
		<div class="clearfix">
         <div class="lab"><label for="input-three">Класс жидкости:</label></div>
     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getFluidClass().getName()}" name="" id="input-three" /></div>
     	</div>
		<div class="clearfix">
         <div class="lab"><label for="input-three">Температура кипения (сух.):</label><p></div>
     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getBoilingTemperatureDry()}" name="" id="input-three" /></div>
     	</div>
		<div class="clearfix">
         <div class="lab"><label for="input-three">Температура кипения (вл.):</label></div>
     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getBoilingTemperatureWet()}" name="" id="input-three" /></div>
     	</div>
		<div class="clearfix">
         <div class="lab"><label for="input-three">Объём:</label></div>
     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getValue()}" name="" id="input-three" /></div>
     	</div>
     	
     	<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_OFFER+PRICE','ROLE_DISTR')">
			<div class="clearfix">
	         <div class="lab"><label for="input-three">Price:</label></div>
	     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getPrice()}" name="" id="input-three" /></div>
	     	</div> 
        </sec:authorize>
		<div class="clearfix">
         <div class="lab"><label for="input-three">Изображение:</label></div>
     	 <div >
     	 		<c:choose>
				<c:when test="${currentBrakFluid.hasPhoto()}">
					<img src="resources/jpg/<c:out value="${currentBrakFluid.getPhoto()}"  />" alt="${currentBrakFluid.getName()}">
				</c:when>
				</c:choose>
		 </div>
	    </div>
	    <div class="clearfix">
	         <div class="lab"><label for="textarea-one">Описание:</label></div>
	       <div class="con"><textarea readonly="readonly" cols="" rows="7" class="textarea" id="textarea-one"><c:out value="${currentBrakFluid.getDescription()}"  /></textarea></div>
	     </div>
		<div class="clearfix">
         <div class="lab"><label for="input-three">Вязкость (при -40):</label></div>
     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getViscosity40()}" name="" id="input-three" /></div>
     	</div>
     	<div class="clearfix">
         <div class="lab"><label for="input-three">Вязкость (при 100):</label></div>
     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getViscosity100()}" name="" id="input-three" /></div>
     	</div>
	    <div class="clearfix">
	         <div class="lab"><label for="textarea-one">Спецификация:</label></div>
	       <div class="con"><textarea readonly="readonly" cols="" rows="7" class="textarea" id="textarea-one"><c:out value="${currentBrakFluid.getSpecification()}"  /></textarea></div>
	     </div>
		<div class="clearfix">
         <div class="lab"><label for="input-three">Оценка покупателей:</label></div>
     	 <div class="con"><input readonly type="text" class="input" value="${currentBrakFluid.getJudgement()}" name="" id="input-three" /></div>
     	</div>


       <div class="btn-submit"><!-- Submit form -->
       	 <input type="submit" name="variant" value="Заявка"class="button" >
       	 <input type="submit"  name="variant" value="На главную" class="button" >
       </div>
     </fieldset>    
   </form>
   </div><!-- /box-content -->
   </div>
   <!-- /box -->

</body>
</html>