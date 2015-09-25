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
	<form action="selection" method="POST" class="formBox">
        <!-- box -->
        <div class="box">
          <div class="headlines">
            <h2><span>Сравниваем тормозные жидкости:</span></h2>
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
  	   	  <tr>
			<th width="300" bgcolor="#eeeeee" valign="top" >Наименование
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<th align="center" class="checkbox" valign="middle">
					<c:out value="${currentBFluid.getName()}"  /><br>
					<input type="checkbox" name="selections" checked="checked" value="${currentBFluid.getId()}"/>
				</th>	
			</c:forEach>
		  <tr>
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Производитель
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getManufacturer().getName()}"  /><p>
				</td>	
			</c:forEach>
		</tr>
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Класс жидкости
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getFluidClass().getName()}"  />
				</td>	
			</c:forEach>
		</tr>
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Температура кипения (сух.)
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getBoilingTemperatureDry()}"  />
				</td>	
			</c:forEach>
		</tr>	
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Температура кипения (вл.)
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getBoilingTemperatureWet()}"  />
				</td>	
			</c:forEach>
		</tr>		
		
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Объём
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getValue()}"  />
				</td>	
			</c:forEach>
		</tr>		
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Цена
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getPrice()}"  />
				</td>	
			</c:forEach>
		</tr>				
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Изображение
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:choose>
					<c:when test="${currentBFluid.hasPhoto()}">
						<img width="50" height="120" src="resources/jpg/<c:out value="${currentBFluid.getPhoto()}"  />" alt="${currentBFluid.getName()}">
					</c:when>
					</c:choose>
				</td>	
			</c:forEach>
		</tr>				
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Описание
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getDescription()}"  />
				</td>	
			</c:forEach>
		</tr>				
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Вязкость (-40)
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getViscosity40()}"  />
				</td>	
			</c:forEach>
		</tr>				
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Вязкость (100)
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getViscosity100()}"  />
				</td>	
			</c:forEach>
		</tr>				
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Спецификация
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getSpecification()}"  />
				</td>	
			</c:forEach>
		</tr>
		<tr>
			<th width="300" bgcolor="#eeeeee" valign="top">Оценка покупателей
			</th>
			<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
				<td align="center" valign="middle">
					<c:out value="${currentBFluid.getJudgement()}"  />
				</td>	
			</c:forEach>
		</tr>		          
	  </table>          
	  <p>
  	  <div class="btn-submit"><!-- Submit form -->
       	 <input type="submit" name="button" value="Сравнить"class="button" >
       	 <input type="submit"  name="button" value="На главную" class="button" >
      </div>	
   </form>
</body>
</html>