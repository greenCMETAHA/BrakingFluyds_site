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
    <link href="resources/Adminpanel/css/fancybox-1.3.1.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="resources/Adminpanel/css/visualize.css" rel="stylesheet" type="text/css" media="screen" />
    
    <script type="text/javascript" src="resources/Adminpanel/js/jquery-1.4.2.min.js"></script>   
    <script type="text/javascript" src="resources/Adminpanel/js/jquery.dimensions.min.js"></script>
    
    <!-- // Tabs // -->
    <script type="text/javascript" src="resources/Adminpanel/js/ui.core.js"></script>
    <script type="text/javascript" src="resources/Adminpanel/js/jquery.ui.tabs.min.js"></script>

    <!-- // Table drag and drop rows // -->
    <script type="text/javascript" src="resources/Adminpanel/js/tablednd.js"></script>
    <script type="text/javascript" src="resources/Adminpanel/js/init.js"></script>
	
</head>
<body>
	<form action="home" method="POST" class="formBox">
    
        <!-- box -->
        <div class="box">
          <div class="headlines">
            <h2><span>Корзина:</span></h2>
            <!--   <a href="#" class="show-filter">show filter</a> -->
          </div>
          <!-- filter -->
          <!-- <div class="filter">
            <input type="text" value="column one" title="column one" class="input" />
            <input type="text" value="column two" title="column two" class="input" />
            <input type="text" value="column three" title="column three" class="input" />
            <input type="submit" value="Use filter" class="submit" />
          </div>  -->
          <!-- /filter -->

          <!-- table -->
          <table class="tab tab-drag">
            <tr class="top nodrop nodrag">
              <th class="dragHandle">&nbsp;</th>
              <th class="checkbox"></th><!-- <input type="checkbox" name="" value="" class="check-all" /> -->
			  <th>Наименование</th>
			  <th>Производитель</th>
			  <th>Класс жидкости</th>
			  <th>Температура кипения (сух.)</th>
			  <th>Температура кипения (вл.)</th>
			  <th>Объём</th>
			  <th>Цена</th>
			  <th>Изображение</th>
			  <th>Описание</th>
			  <th>Вязкость (-40)</th>
			  <th>Вязкость (100)</th>
			  <th>Спецификация</th>
			  <th>Оценка покупателей</th>
            </tr>
            <c:set var="name" value="basket" />
		 	<c:forEach var="currentBFluid" items="${sessionScope[name]}">
		      <tr>
          	    <td class="dragHandle">&nbsp;</td>
			 	<td class="checkbox"><input type="checkbox" name="selections" checked="checked" value="${currentBFluid.getId()}" /></td>
		 		<td align="left"><c:out value="${currentBFluid.getName()}"/></td>
		 		<td align="center"><c:out value="${currentBFluid.getManufacturer().getName()}"/></td>
		 		<td align="center"><c:out value="${currentBFluid.getFluidClass().getName()}"/></td>
	 			<td align="center"><c:out value="${currentBFluid.getBoilingTemperatureDry()}"  /></td>
				<td align="center"><c:out value="${currentBFluid.getBoilingTemperatureWet()}"  /></td>
				<td align="center"><c:out value="${currentBFluid.getValue()}"  /></td>   
				<td align="right"><c:out value="${currentBFluid.getPrice()}"  /></td>   
				<td align="center">
					<c:choose>
					<c:when test="${currentBFluid.hasPhoto()}">
						<img width="50" height="120" src="resources/jpg/<c:out value="${currentBFluid.getPhoto()}"  />" alt="${currentBFluid.getName()}">
					</c:when>
					</c:choose>
				</td>   
				<td align="left"><c:out value="${currentBFluid.getDescription()}"  /></td>   
				<td align="center"><c:out value="${currentBFluid.getViscosity40()}"  /></td>   
				<td align="center"><c:out value="${currentBFluid.getViscosity100()}"  /></td>   
				<td align="left"><c:out value="${currentBFluid.getSpecification()}"  /></td>   
				<td align="center"><c:out value="${currentBFluid.getJudgement()}"  /></td>
			  </tr>		 	
		 	</c:forEach>
		 </table>
         <!-- /table -->
	  </div>
      <div class="btn-submit"><!-- Submit form -->
       	 <input type="submit" name="variant" value="Заявка" class="button" >
       	 <input type="submit"  name="variant" value="На главную" class="button" >
      </div>
	</form>
</body>
</html>