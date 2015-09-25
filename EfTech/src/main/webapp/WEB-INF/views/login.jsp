<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
1111
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name='robots' content='all, follow' />
    <meta name="description" content="" />
    <meta name="keywords" content="" />
    <title>ЭфТех ООО</title>   
  <link href="resources/css/login.css" rel="stylesheet" type="text/css" />
    <link href="resources/css/login-gray.css" rel="stylesheet" type="text/css" />  <!-- color skin: blue / red / green / dark -->

</head>
<body>
 <div id="main">
    <div id="content">
      <div id="login">
            <div id="logo">     </div>
  	     	<form name='f' action='/controller/login' method='POST' id="form-login" class="formBox">
	          <fieldset>
	            <div class="form-col">
	                <label for="username" class="lab">Имя пользователя: <span class="warning"></span></label>
	                <input type="text" name="username" class="input" id="username" />
	            </div>
	            <p>
	            <div class="form-col form-col-right">
	                <label for="password" class="lab">Пароль: <span class="warning"></span></label>
	                <input type="password" name="password" class="input" id="password" />
	            </div>
	            <div class="form-col form-col-check">
            		  <label><input type="checkbox" name="remeber_me" class="checkbox" />Запомнить меня</label> 
            	</div>
	            <div class="form-col form-col-right"> 
	              <input type="submit" name="authorButton" value="Авторизация" class="submit" />
	              <input type="submit" name="authorButton" value="На главную" class="submit" />
	            </div> 
	          </fieldset>
	        </form>        
		</div>
	  </div>
	</div>
 </div>
	
</body>
</html>