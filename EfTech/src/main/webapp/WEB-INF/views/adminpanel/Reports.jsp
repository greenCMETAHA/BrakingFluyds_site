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
    <link href="resources/Adminpanel/css/datePicker.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="resources/Adminpanel/css/wysiwyg.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="resources/Adminpanel/css/fancybox-1.3.1.css" rel="stylesheet" type="text/css" media="screen" />
    <link href="resources/Adminpanel/css/visualize.css" rel="stylesheet" type="text/css" media="screen" />
    
    <script type="text/javascript" src="resources/Adminpanel/js/jquery-1.4.2.min.js"></script>   
    <script type="text/javascript" src="resources/Adminpanel/js/jquery.dimensions.min.js"></script>
    
    <!-- // Tabs // -->
    <script type="text/javascript" src="resources/Adminpanel/js/ui.core.js"></script>
    <script type="text/javascript" src="resources/Adminpanel/js/jquery.ui.tabs.min.js"></script>

    <!-- // Table drag and drop rows // -->
    <script type="text/javascript" src="resources/Adminpanel/js/tablednd.js"></script>

    <!-- // Date Picker // -->
    <script type="text/javascript" src="resources/Adminpanel/js/date.js"></script>
    <!--[if IE]><script type="text/javascript" src="resources/Adminpanel/js/jquery.bgiframe.js"></script><![endif]-->
    <script type="text/javascript" src="resources/Adminpanel/js/jquery.datePicker.js"></script>

    <!-- // Wysiwyg // -->
    <script type="text/javascript" src="resources/Adminpanel/js/jquery.wysiwyg.js"></script>

    <!-- // Graphs // -->
    <script type="text/javascript" src="resources/Adminpanel/js/excanvas.js"></script>
    <script type="text/javascript" src="resources/Adminpanel/js/jquery.visualize.js"></script>

    <!-- // Fancybox // -->
  	<script type="text/javascript" src="resources/Adminpanel/js/jquery.fancybox-1.3.1.js"></script>

    <!-- // File upload // --> 
    <script type="text/javascript" src="resources/Adminpanel/js/jquery.filestyle.js"></script>
    
    <script type="text/javascript" src="resources/Adminpanel/js/init.js"></script>
	
</head>
<body>
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
	<form action="menu" method="POST"> 
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
    	<input type="text" name="variant" value="${requestScope.variant}">
    	<input name="result" type="hidden" value="0" > 
			<!--	        mainmenu -->
	        <ul id="floatMenu" class="mainmenu"> 
	          <li class="first" ><input type="submit" name="task" value="На главную"></li>
			  <li><input type="submit" name="task" value="Загрузить номенклатуру"></li>
	          <li><input type="submit" name="task" value="Загрузить цены"></li>
	          <li><input type="submit" name="task" value="Отчеты"></li>
	          <li><input type="submit" name="task" value="Создать новый"></li>
	          <li><a href="#">Редактировать</a>
		            <ul class="submenu">
		              <li><input type="submit" name="task" value="Производители"></a></li>          
		              <li><input type="submit" name="task" value="Классы жидкости"></li>
		              <li><input type="submit" name="task" value="Страны"></li>
		              <li><input type="submit" name="task" value="Клиенты"></li>
		              <li><input type="submit" name="task" value="Пользователи"></li>
		              <li><input type="submit" name="task" value="Состав масел"></li>
		              <li><input type="submit" name="task" value="Тип двигателя"></li>
		              <li><input type="submit" name="task" value="Тип КПП"></li>
		              <li><input type="submit" name="task" value="Логирование"></li>
		            </ul>
		          </li>
          	  </li>
            </ul>	
    </div> 
    </form>
    <div id="content">
		<c:set var="currentUser" value="${requestScope.user}"></c:set>
	<!-- /box -->
	   <div class="box">
	     <div class="headlines">
	       <h2><span>Отчеты</span></h2>
	     </div>
	     <div class="box-content">
	       	<a href="reports?variant=ManufacturerList" >Список поставщиков</a><br>
		   	<a href="reports?variant=Price" >Прайс-лист</a><br>
		   	<p>
	     
		    <form action="action" class="formBox" method="POST">
	           <div class="btn-submit"><!-- Submit form -->
		       	 <input type="submit"  name="variant" value="На главную" class="button" >
		       </div>
		    </form>
	   </div><!-- /box-content -->
	   </div>
	   <!-- /box -->
	</div> 


</div>
</body>
</html>