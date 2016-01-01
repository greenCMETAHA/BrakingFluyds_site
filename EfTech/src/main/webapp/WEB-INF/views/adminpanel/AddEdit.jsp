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


   <div id="content"  class="box">
     <div class="headlines">
       <h2><span><c:out value="${requestScope.pageInfo}"  /></span></h2>
       <!-- <a href="#help" class="help"></a> --> 
     </div>
     <div class="box-content">
	     <form action="AddEdit" class="formBox" method="POST" >
	     	<input name="insert" type="hidden" value="${requestScope.variant}" >
			<c:set var="currentBrakFluid" value="${requestScope.currentBrakFluid}"/>
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
			<c:set var="current" value="${requestScope.current}"/>
			<input type="text" class="input" hidden value="${current.getId()}" name="id_current" id="input-three" />
			<input type="text" class="input" hidden value="${requestScope.variant}" name="variant" id="input-three" />
			
 			 <fieldset>
 			 <c:if test="${requestScope.variant=='country'}">
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Наименование:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getName()}" name="name_current" id="input-three" /></div>
		     	</div>
              </c:if>
              
              
              <c:if test="${requestScope.variant=='fluidClass'}">
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Наименование:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getName()}" name="name_current" id="input-three" /></div>
		     	</div>
              </c:if>

              <c:if test="${requestScope.variant=='oilStuff'}">
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Наименование:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getName()}" name="name_current" id="input-three" /></div>
		     	</div>
              </c:if>

              <c:if test="${requestScope.variant=='engineType'}">
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Наименование:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getName()}" name="name_current" id="input-three" /></div>
		     	</div>
              </c:if>
              
              
		      <c:if test="${requestScope.variant=='client'}">
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Наименование:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getName()}" name="name_current" id="input-three" /></div>
		     	</div>
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">E-mail:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getEmail()}" name="email" id="input-three" /></div>
		     	</div>
		     	<div class="clearfix">
		         <div class="lab"><label for="input-three">Адрес:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getAddress()}" name="address" id="input-three" /></div>
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
		     	
		     	
              
              <c:if test="${requestScope.variant=='log'}">
                <div class="clearfix">
		         <div class="lab"><label for="input-three">Пользователь:</label></div>
		     	 <div class="con"><input disabled type="text" class="input" value="${current.getUser().getName()}" name="login_current" id="input-three" />
		     	 				  <input type="text" hidden value="${current.getUser().getLogin()}" name="login"/>
		     	 </div>
		     	</div>
		     	<div class="clearfix">
		         <div class="lab"><label for="input-three">Дата и время:</label></div>
		     	 <div class="con"><input disabled type="text" class="input" value="${current.getTime()}" name="time" id="input-three" /></div>
		     	</div>
		     	<div class="clearfix">
		         <div class="lab"><label for="input-three">Вид объекта:</label></div>
		     	 <div class="con"><input disabled type="text" class="input" value="${current.getObjectName()}" name="object_name" id="input-three" /></div>
		     	</div>
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Объект:</label></div>
		     	 <div class="con"><input disabled type="text" class="input" 
		     	 	value="${current.getObject().getId()}.${current.getObject().getName()}" name="object" id="input-three" /></div>
		     	</div>
		     	<div class="clearfix">
		         <div class="lab"><label for="input-three">Дополнительная информация:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getInfo()}" name="info" id="input-three" /></div>
		     	</div>
              </c:if>
              
              
 			  <c:if test="${requestScope.variant=='user'}">
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Логин:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getLogin()}" name="login_current" id="input-three" />
		     	 				  <input type="text" hidden value="${current.getLogin()}" name="login"/>
		     	 </div>
		     	</div>
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Наименование:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getName()}" name="name_current" id="input-three" /></div>
		     	</div>
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">E-mail:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getEmail()}" name="email" id="input-three" /></div>
		     	</div>
              	<div class="clearfix">
		         <div class="lab"><label for="input-three">Пароль:</label></div>
		     	 <div class="con"><input type="text" class="input" value="${current.getPassword()}" name="password_current" id="input-three" /></div>
		     	</div>
		     	<div class="clearfix">
		     		<div class="lab"><label for="input-three">Список ролей:</label></div>
		     		<div class="container">
		     			<div class="box-content">
		     			<c:forEach var="punct" items="${requestScope.roles}">
		     				<c:set var="bFasRole" value="${false}"/>
		     				<c:forEach var="currentPunct" items="${requestScope.currentRoles}">				<!-- Это уже существующие в наличии роли -->
		     					<c:if test="${punct.getId()==currentPunct.getId()}">
		     						<c:set var="bFasRole" value="${true}"/>
		     					</c:if>
		     				</c:forEach>
		     				<c:if test="${bFasRole}">
		     					<input type="checkbox"  checked  name="selections" value="${punct.getId()}" /><c:out value="${punct.getName()}"/><br>
		     				</c:if>
		     				<c:if test="${!bFasRole}">
		     					<input type="checkbox"  name="selections" value="${punct.getId()}" /><c:out value="${punct.getName()}"/><br>
		     				</c:if>
		     			</c:forEach>
		     			</div>
		     		</div>
		     	</div>
              </c:if>
              
              
               
              <c:if test="${requestScope.variant=='manufacturer'}">
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
	       	 <input type="submit" name="task" value="Сохранить" class="button" >&nbsp;&nbsp;&nbsp;&nbsp;
	       	 <input type="submit" name="task" value="К списку" class="button" >
	       </div>
	   </form>
	   
   </div><!-- /box-content -->
   </div><!-- /box -->
</div>
</body>
</html>