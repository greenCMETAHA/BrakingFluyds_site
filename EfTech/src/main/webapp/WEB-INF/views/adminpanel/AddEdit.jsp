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


   <div class="box">
     <div class="headlines">
       <h2><span><c:out value="${requestScope.pageInfo}"  /></span></h2>
       <!-- <a href="#help" class="help"></a> --> 
     </div>
     <div class="box-content">
	     <form action="AddEdit" class="formBox" method="POST" >
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
			<c:set />
			
			<c:set var="current" value="${requestScope.current}"/>
			<input type="text" class="input" hidden value="${current.getName()}" name="id_current" id="input-three" />
			<input type="text" class="input" hidden value="${requestScope.variant}" name="variant" id="input-three" />
			
 			 <fieldset>
 			  <c:if test="${requestScope.variant}='User'">
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Наименование:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getName()}" name="name_current" id="input-three" /></div>
		     	</div>
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">E-mail:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getEmail()}" name="email" id="input-three" /></div>
		     	</div>
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Логин:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getLogin()}" name="login" id="input-three" /></div>
		     	</div>
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Пароль:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getPassword)}" name="password" id="input-three" /></div>
		     	</div>
		     	<div class="clearfix">
		     		<div class="lab"><label for="input-three">Список ролей:</label></div>
		     		<div class="container">
		     			<c:forEach var="punct" begin="1" end="${requestScope.roles.size()}">
		     				<c:set var="bFasRole" value="${false}"/>
		     				<c:forEach var="currentPunct" begin="1" end="${requestScope.currentRoles.size()}">  <!-- Это уже существующие в наличии роли -->
		     					<c:if test="${punct=currentPunct}">
		     						<c:set var="bFasRole" value="${true}"/>
		     					</c:if>
		     				</c:forEach>
		     				<c:if test="${bFasRole}">
		     					<input type="checkbox"  checked  name="selections" value="${currentBFluid.getId()}" /><c:out value="${role.getName()}"/>
		     				</c:if>
		     				<c:if test="${!bFasRole}">
		     					<input type="checkbox"  name="selections" value="${currentBFluid.getId()}" /><c:out value="${role.getName()}"/>
		     				</c:if>
		     			</c:forEach>
		     		</div>
		     	</div>
              </c:if>
              
              
               
              <c:if test="${requestScope.variant}='Manufacturer'">
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Наименование:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getName()}" name="name_current" id="input-three" /></div>
		     	</div>
              	<div class="clearfix">
			         <div class="lab"><label for="input-three">Страна происхождения:</label></div>
			     	 <div class="con">
	                   	<select size="1" name="country"   class="le-select">
		                    <option >Выберите страну происхождения...</option>	
								<c:forEach var="punct" items="${requestScope.combobox_countris}">
								<c:if test="${current.getCountry().getName() != punct.getName()}">
									<option value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
								</c:if>
								<c:if test="${current.getCountry().getName() == punct.getName()}">
									<option selected value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
								</c:if>
							</c:forEach>
		                </select>
					</div>
				</div>
              </c:if>
		     </fieldset>
		     
           <div class="btn-submit"><!-- Submit form -->
	       	 <input type="submit" name="variant" value="Сохранить"class="button" >
	       	 <input type="submit"  name="variant" value="К списку" class="button" >
	       	 <input type="submit"  name="variant" value="На главную" class="button" >
	       </div>
	   </form>
	   
   </div><!-- /box-content -->
   </div>
   <!-- /box -->
</div>

</body>
</html>