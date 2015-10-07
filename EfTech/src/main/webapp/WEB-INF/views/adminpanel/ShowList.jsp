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
	    	<input name="result" type="hidden" value="0" > 
				<!--	        mainmenu -->
		        <ul id="floatMenu" class="mainmenu"> 
		          <li class="first" ><input type="submit" name="variant" value="Сравнить"></li>	
		          <li><input type="submit" name="variant" value="В корзину"></li>
         		  <li><input type="submit" name="variant" value="Загрузить номенклатуру"></li>
		          <li><input type="submit" name="variant" value="Загрузить цены"></li>
			  	  <li><input type="submit" name="variant" value="Коммерческое приложение"></li>
		          <li><input type="submit" name="variant" value="Редактировать">
			            <ul class="submenu">
			              <li><input type="submit" name="variant" value="Производители"></a></li>          
			              <li><input type="submit" name="variant" value="Классы жидкости"></li>
			              <li><input type="submit" name="variant" value="Страны"></li>
			              <li><input type="submit" name="variant" value="Клиенты"></li>
			              <li><input type="submit" name="variant" value="Пользователи"></li>
			              <li><input type="submit" name="variant" value="Логирование"></li>
			            </ul>
			          </li>
	          	  </li>
	            </ul>	
	    </div> 
	    </form>
	    
	 <form action="ShowList" method="GET">
		<div id="content" class="box">	
   		<!-- box --> 
	     <div class="box">
     
     
       <div class="headlines">
         <h2><span><c:out value="${requestScope.tablehead}"/> </span></h2>
       </div>	
      <table class="tab tab-drag">
            <tr class="top nodrop nodrag">
              <th class="dragHandle">&nbsp;</th>
              <th class="checkbox"><input type="checkbox" name="selections" value="" class="check-all" /></th>
              <c:if test="${requestScope.variant}='User'">
              	<th>Наименование</th>
              	<th>E-mail</th>
              	<th>Логин</th>
              	<th>Пароль</th>
              </c:if> 
              <c:if test="${requestScope.variant}='Manufacturer'">
              	<th>Наименование</th>
              	<th>Страна</th>
              </c:if> 
              <c:if test="${requestScope.variant}='Country'">
              	<th>Наименование</th>
              </c:if> 
              <c:if test="${requestScope.variant}='FluidClass'">
              	<th>Наименование</th>
              </c:if> 
              <c:if test="${requestScope.variant}='Client'">
              	<th>Наименование</th>
              	<th>E-mail</th>
              	<th>Адрес</th>
              	<th>Страна</th>
              </c:if> 
              <c:if test="${requestScope.variant}='Log'">
              	<th>Пользователь</th>
              	<th>Время</th>
              	<th>Вид объекта</th>
              	<th>Объект</th>
              	<th>Дополнительная информация</th>
              </c:if>
              <th class="action">Действие</th> 
            </tr>
            
		 	<c:forEach var="current" items="${requestScope.list}">
	 	 	  <c:url value="ShowList" var="UpdateCurrent">
				<c:param name="variant" value="${requestScope.variant}"/>
				<c:param name="task" value="Edit"/>
				<c:param name="id" value="${current.getId()}"/>
			  </c:url>
		      <tr>
          	    <td class="dragHandle">&nbsp;</td>
			 	<td class="checkbox"><input type="checkbox" name="selections" value="${current.getId()}" /></td>
	              <c:if test="${requestScope.variant}='User'">
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getName()}"  /></a>
					</td>
					<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getEmail()}"  /></a>
					</td>
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getLogin()}"  /></a>
					</td>
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getLogin()}"  /></a>
					</td>
	              </c:if> 
	              <c:if test="${requestScope.variant}='Manufacturer'">
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getName()}"  /></a>
					</td>
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getCountry().getName()}"  /></a>
					</td>
	              </c:if> 
	              <c:if test="${requestScope.variant}='Country'">
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getName()}"  /></a>
					</td>
	              </c:if> 
	              <c:if test="${requestScope.variant}='FluidClass'">
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getName()}"  /></a>
					</td>
	              </c:if> 
	              <c:if test="${requestScope.variant}='Client'">
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getName()}"  /></a>
					</td>
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getEmail()}"  /></a>
					</td>
					<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getAddress()}"  /></a>
					</td>
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getCountry().getName()}"  /></a>
					</td>
	              </c:if> 
	              <c:if test="${requestScope.variant}='Log'">
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getUser().getName()}"  /></a>
					</td>
	              	<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getDate()}"  /></a>
					</td>
					<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getObjectName()}"  /></a>
					</td>
					<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getObject.getName()}"  /></a>
					</td>
					<td>
						<a href="${UpdateCurrent}"  title="Редактировать элемент"> <c:out value="${current.getObject.getInfo()}"  /></a>
					</td>
                  </c:if> 
			 	  <td class="action">
				 	<c:url value="ShowList" var="DeleteCurrent">
						<c:param name="variant" value="${requestScope.variant}"/>
						<c:param name="task" value="Delete"/>
						<c:param name="id" value="${current.getId()}"/>
			  		</c:url>
	                <a href="${DeleteCurrent}" class="ico ico-delete">Delete</a>
	                <a href="${UpdateCurrent}" class="ico ico-edit">Edit</a>
	              </td>
			  </tr>		 	
		 	</c:forEach>
		 </table>
		 
		 <c:if test="${requestScope.totalPages}>1">
		 	<c:set var="currentPage" value="${requestScope.currentPage}"/> 
			 <!-- /pagination -->
	          <div class="pagination">
	            <ul>
	            	<c:forEach var="page" begin="1" end="${requestScope.totalPages}">
	            	  <c:if test="${currentPage}>1">
		              	<li class="graphic first"><a href="ShowList?variant=${requestScope.variant}&page=1"></a></li>
		              	<li class="graphic prev"><a href="ShowList?variant=${requestScope.variant}&page=${currentPage-1}"></a></li>
		              </c:if>
		              <c:if test="${page}=${currentPage}">
		              	<li class="active"><a href="ShowList?variant=${requestScope.variant}&page=${page}"><c:out value="${page}"/></a></li>
		              </c:if>
		              <c:if test="${page}!=${currentPage}">
		              	<li><a href="ShowList?variant=${requestScope.variant}&page=${page}"><c:out value="${page}"/></a></li>
		              </c:if>
		              <c:if test="${currentPage}<${requestScope.totalPages}">	
		              	<li class="graphic next"><a href="ShowList?variant=${requestScope.variant}&page=${currentPage+1}"></a></li>
		              	<li class="graphic last"><a href="ShowList?variant=${requestScope.variant}&page=${requestScope.totalPages}"></a></li>
		              </c:if>
	              </c:forEach>
	            </ul>
	            <p>Страница <c:out value="${currentPage}"/> из <c:out value="${requestScope.totalPages}"/></p>
             </div>
          </c:if>
          <!-- /pagination --> 
       </div>
       </div>	    
    </form>
   	<form action="ShowList" method="POST">
   	    <div class="btn-submit"><!-- Submit form -->
       	 <input type="submit" name="task" value="Создать новый элемент" class="button" >
       	 <input type="submit"  name="task" value="На главную" class="button" >
       </div>
   	</form>

    										

	   
   </div><!-- /box-content -->

</body>
</html>