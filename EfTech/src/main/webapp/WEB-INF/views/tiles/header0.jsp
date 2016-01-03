<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

      <!-- ============================================================= TOP NAVIGATION ============================================================= -->
      <nav class="top-bar animate-dropdown">
		    <div class="container">
		        <div class="col-xs-12 col-sm-6 no-margin">
		            <ul>
		                <li><a href="index">В начало</a></li>
		                 <!-- <li><a href="contact.html">Contact</a></li>  -->
		                <li class="dropdown">
		                    <a class="dropdown-toggle" data-toggle="dropdown" href="#pages">Страницы</a>
		                    <ul class="dropdown-menu" role="menu">
		                        <li><a href="index">В начало</a></li>
		                        <li><a href="home?good=BrakinFluids">Список тормозных жидкостей</a></li>
	 		                    <li><a href="motorOil">Список моторных масел</a></li>
	 		                    <li><a href="gearBoxOil">Список трансмиссионных масел</a></li>
		                        <li><a href="Basket">Корзина</a></li>
		                        <sec:authorize access="!isAnonymous() and !hasRole('ROLE_ADMIN')">
		                        	<li><a href="Wishlist">Избранное</a></li>
		                        </sec:authorize>		                        
		                        <li><a href="About">О нас</a></li>
		                        <sec:authorize access="hasAnyRole('ROLE_PRODUCT','ROLE_ADMIN')">
   		                        	<li><a href="Download?variant=download&task=Product">Загрузить товар из Excel</a></li>
   		                        </sec:authorize>
   		                        <sec:authorize access="hasAnyRole('ROLE_PRICE','ROLE_ADMIN')">
		                        	<li><a href="Download?variant=download&task=Price">Загрузить цены из Excel</a></li>
		                        </sec:authorize>
		                        <li><a href="Comparison">Сравнить товары</a></li>
		                        <sec:authorize access="!isAnonymous()"> 
			                        <li><a href="listDoc?variant=Demand">Список заявок</a></li>
			                        <li><a href="listDoc?variant=Offer">Список ком. предложений</a></li>
			                    </sec:authorize>
		                        <sec:authorize access="hasRole('ROLE_DELIVERY')">
		                        	<li><a href="listDoc?variant=Demand">Доставка</a></li>
		                        </sec:authorize>
		                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                      				<li><a href="home?adminpanel=true">Конфигурирование</a></li>
                  				</sec:authorize>
		                        
		                        <li>
		                        	<c:set  var="name" value="user" />
    								<c:set var="currentUser" value="${sessionScope[name]}"></c:set>
		                        	
									<c:if test="${currentUser.isEmpty()}"> 
										<a href="login">Авторизируйтесь</a>
						 			</c:if> 
									<c:if test="${!currentUser.isEmpty()}"> 
										<a href="j_spring_security_logout">Выйти</a>
						 			</c:if> 
		                        </li>
		                     </ul>
		                </li>
		            </ul>
		        </div><!-- /.col -->
		
		        <div class="col-xs-12 col-sm-6 no-margin">
		            <ul class="right">
		                <li class="dropdown">
		                    <a class="dropdown-toggle"  data-toggle="dropdown" href="#change-language">English</a>
		                    <ul class="dropdown-menu" role="menu" >
		                        <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Русский</a></li>
		                    </ul>
		                </li>
		                <li class="dropdown">
		                    <a class="dropdown-toggle"  data-toggle="dropdown" href="#change-currency">Доллар (US)</a>
		                    <ul class="dropdown-menu" role="menu">
		                        <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Евро (EU)</a></li>
		                        <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Белорусские рубли (BYR)</a></li>
		                    </ul>
		                </li>
		                <li>
                        	<c:set  var="name" value="user" />
  							<c:set var="currentUser" value="${sessionScope[name]}"></c:set>
                        	
							<c:if test="${currentUser.isEmpty()}"> 
								<a href="login">Авторизируйтесь</a>
				 			</c:if> 
							
							<c:if test="${!currentUser.isEmpty()}"> 
								Здравствуйте, <c:out value="${currentUser.getName()}"  />&nbsp;&nbsp;&nbsp;
								<a href="j_spring_security_logout">Выйти</a>
				 			</c:if> 
                        </li>
		            </ul>
		        </div><!-- /.col -->
		    </div><!-- /.container -->
		</nav><!-- /.top-bar -->
      <!-- /.top-bar -->
      <!-- ============================================================= TOP NAVIGATION : END ============================================================= -->		<!-- ============================================================= HEADER ============================================================= -->
