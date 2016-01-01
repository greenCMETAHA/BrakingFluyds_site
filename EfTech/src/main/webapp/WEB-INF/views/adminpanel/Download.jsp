<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
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
 <div id="content">
	<c:set var="currentUser" value="${requestScope.user}"></c:set>
<!-- /box -->
   <div class="box">
     <div class="headlines">
       <h2><span>Загрузка Excel-файла:</span></h2>
       <!-- <a href="#help" class="help"></a> --> 
     </div>
     <div class="box-content">
	     <form action="action" class="formBox" method="POST"  enctype="multipart/form-data">
	     	<input name="variantDownload" type="hidden" value="${requestScope.variantDownload}" >
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

 			 <fieldset>
		     	<c:if test="${requestScope.variantDownload==1}">
					<div class="lab"><label for="file">Загрузить номенклатуру:</label></div>
	            </c:if>
	            <c:if test="${requestScope.variantDownload==2}">
	            <div class="lab"><label for="file">Загрузить цены:</label></div>
	            </c:if>
	            <br> <br>
				<div>
					<input type="radio"  name="good" value="BrakingFluids">Тормозные жидкости<br>
					<input type="radio"  name="good" value="MotorOils" >Моторные масла<br>
					<input checked type="radio"  name="good" value="GearBoxOils" >Трансмиссионные масла<br>
				</div>
	            <br><br>
	            <div class="con"><input type="file"  name="fileExcel" class="upload-file" id="file" value=""/>  
		     </fieldset>
		     
           <div class="btn-submit"><!-- Submit form -->
	       	 <input type="submit" name="variant" value="Загрузка"class="button" >
	       	 <input type="submit"  name="variant" value="На главную" class="button" >
	       </div>
		         
	   </form>
   </div><!-- /box-content -->
   </div>
   <!-- /box -->
</div>
</body>
</html>