<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
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
 <c:set var="currentUser" value="${requestScope.user}"></c:set> 

 <div id="main"> 
<!--      #header  -->
          <div id="header">  
<!--     #logo   -->
      <div id="logo"> 
         <h1><font size="6" color="white" face="Arial">ЭфТех ООО</font></h1> 
     </div> 
<!--        #user    -->
 	     	<c:choose> 
		<c:when test="${currentUser.isEmpty()}"> 
			    <form action="login" method="POST">                      
	     				<div id="user">
						<input type="submit" name="authorButton" value="Авторизируйтесь"  class="button"/>
					</div>
				</form>
 			</c:when> 
			
			<c:when test="${!currentUser.isEmpty()}"> 

			    <form action="/controller/j_spring_security_logout" method="POST">                      
	     				<div id="user">
						<font color="white">Здравствуйте, <c:out value="${currentUser.getName()}"  />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    </font>
						<input type="submit" name="authorButton" value="Выйти"  class="button">
					</div>
				</form>
					  <br/>
 			</c:when> 
 			</c:choose> 
     </div> 
 
	<form action="selection" method="POST"> 
	    
	<style> 
 			input[type="submit"]{ 
 				border:0; 
				padding:0; 
 				background:none; 
 				cursor:pointer; 
 				text-decoration:blink; 
 				color: #404040; font-weight: bold; font-size: 1.2em; 
 				margin-left: -15px; 
 			} 
 			input[type="submit"]:hover{ 
 				border:0; 
 				padding:0; 
 				background:none; 
 				cursor:pointer; 
 				text-decoration:blink; 
 				color: #000000; font-weight: bold; font-size: 1.2em; 
 				margin-left: -15px; 
 			} 
 		</style>  
 	    <div id="sidebar"> 
 	    	<input name="variant" type="hidden" value="0" > 
<!--	        mainmenu -->
	        
 	        <ul id="floatMenu" class="mainmenu"> 
 	          <li class="first" ><input type="submit" name="button" value="Сравнить"></li>	
	     	  <sec:authorize access="isAnonymous() or hasAnyRole('ROLE_ADMIN,ROLE_OFFER,ROLE_OFFERPRICE,ROLE_PRICE')"> 
		          <!-- <li class="first"><input type="submit" name="button" value="Сравнить"></li> 
		           --> 
 		          <li><input type="submit" name="button" value="В корзину"></li>
 		      </sec:authorize>
 	          <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')">	 
	          		<li><input type="submit" name="button" value="Загрузить номенклатуру"></li>
	          </sec:authorize> 
	          <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRICE')">
 	          		<li><input type="submit" name="button" value="Загрузить цены"></li>
			  </sec:authorize>
			  <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR')">	 	          		 
 	          		<li><input type="submit" name="button" value="Коммерческое приложение"></li>
 	          </sec:authorize>	
 	          </ul>	

 	    </div> 
    

	<div id="content" class="box">	
	
   <!-- box --> 
     <div class="box">
     
       <div class="headlines">
         <h2><span>Тормозные жидкости:</span></h2>
           <a href="#" class="show-filter">show filter</a>
       </div>	
      <table class="tab tab-drag">
            <tr class="top nodrop nodrag">
              <th class="dragHandle">&nbsp;</th>
              <th class="checkbox"><input type="checkbox" name="selections" value="" class="check-all" /></th>
			  <th>Наименование</th>
			  <th>Производитель</th>
			  <th>Класс жидкости</th>
			  <th>Температура кипения (сух.)</th>
			  <th>Температура кипения (вл.)</th>
			  <th>Объём</th>
			  <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_OFFERPRICE','ROLE_PRICE','ROLE_DISTR')">
			  		<th>Цена</th>
			  </sec:authorize>		
			  <th>Изображение</th>
			  <th>Описание</th>
			  <th>Вязкость (-40)</th>
			  <th>Вязкость (100)</th>
			  <th>Спецификация</th>
			  <th>Оценка покупателей</th>
            </tr>
		 	<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
		      <tr>
          	    <td class="dragHandle">&nbsp;</td>
			 	<td class="checkbox"><input type="checkbox" name="selections" value="${currentBFluid.getId()}" /></td>
		 		<td align="left">
					<form action="update" method="GET">
						<c:url value="update" var="UpdateBrakingFluid">
							<c:param name="id" value="${currentBFluid.getId()}"/>
							<c:param name="variant" value="update"/>
						</c:url>
						<a href="${UpdateBrakingFluid}"  title="Редактировать тормозную жидкость."> <c:out value="${currentBFluid.getName()}"  /> </a>
					</form>
				</td>
		 		<td align="center"><c:out value="${currentBFluid.getManufacturer().getName()}"/></td>
		 		<td align="center"><c:out value="${currentBFluid.getFluidClass().getName()}"/></td>
	 			<td align="center"><c:out value="${currentBFluid.getBoilingTemperatureDry()}"  /></td>
				<td align="center"><c:out value="${currentBFluid.getBoilingTemperatureWet()}"  /></td>
				<td align="center"><c:out value="${currentBFluid.getValue()}"  /></td>
				<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_OFFERPRICE','ROLE_PRICE','ROLE_DISTR')">   
					<td align="right"><c:out value="${currentBFluid.getPrice()}"  /></td>
				</sec:authorize>   
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
       </div>
     </form>
   </div>
	
	</div>
	  <div id="footer">
	      <p>© Васильченко, 2015 | <a href="#main">Top</a></p>
	    </div>
	</div>
</body>
</html>
