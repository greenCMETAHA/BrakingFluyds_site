<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <!-- Meta -->
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="keywords" content="MediaCenter, Template, eCommerce">
    <meta name="robots" content="all">
    <title>ЭфТех, ООО</title>
    <!-- Bootstrap Core CSS -->
    <link rel="stylesheet" href="resources/Ecommerce/assets/css/bootstrap.min.css">
    <!-- Customizable CSS -->
    <link rel="stylesheet" href="resources/Ecommerce/assets/css/main.css">
    <link rel="stylesheet" href="resources/Ecommerce/assets/css/navy.css">
    <link rel="stylesheet" href="resources/Ecommerce/assets/css/owl.carousel.css">
    <link rel="stylesheet" href="resources/Ecommerce/assets/css/owl.transitions.css">
    <link rel="stylesheet" href="resources/Ecommerce/assets/css/animate.min.css">
    <!-- Fonts -->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700,800' rel='stylesheet' type='text/css'>
    <!-- Icons/Glyphs -->
    <link rel="stylesheet" href="resources/Ecommerce/assets/css/font-awesome.min.css">
    <!-- Favicon -->
    <link rel="shortcut icon" href="resources/Ecommerce/assets/images/favicon.ico">
    <!-- HTML5 elements and media queries Support for IE8 : HTML5 shim and Respond.js -->
    <!--[if lt IE 9]>
    <script src="resources/Ecommerce/assets/js/html5shiv.js"></script>
    <script src="resources/Ecommerce/assets/js/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
    <div class="wrapper">
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
		                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                      				<li><a href="home?adminpanel=true">Конфигурирование</a></li>
                  				</sec:authorize>
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
      <header>
			<div class="container no-padding">
				<div class="col-xs-12 col-sm-12 col-md-3 logo-holder">
					<!-- ============================================================= LOGO ============================================================= -->
					<div class="logo">
						<a href="index">
							<img alt="logo" src="resources/Ecommerce/assets/images/logo_EfTech.png" width="233" height="54"/>
						</a>
					</div><!-- /.logo -->
					<!-- ============================================================= LOGO : END ============================================================= -->	
				</div><!-- /.logo-holder -->
		
				<div class="col-xs-12 col-sm-12 col-md-6 top-search-holder no-margin">
					<div class="contact-row">
					    <div class="phone inline">
					        <i class="fa fa-phone"></i> <c:out value="${requestScope.phone}"/>
					    </div>
					    <div class="contact inline">
					        <i class="fa fa-envelope"></i> <c:out value="${requestScope.email_part1}"/>@<span class="le-color"><c:out value="${requestScope.email_part2}" /></span>
					    </div>
					</div><!-- /.contact-row -->
					<!-- ============================================================= SEARCH AREA ============================================================= -->
					<div class="search-area">
					    <form action="home" method="GET">
					        <div class="control-group">
					            <input class="search-field" placeholder="Search for item" />
					
					            <ul class="categories-filter animate-dropdown">
					                <li class="dropdown">
					
					                    <a class="dropdown-toggle"  data-toggle="dropdown" href="index">Все категории</a>
					
					                    <ul class="dropdown-menu" role="menu" >
					                        <li role="presentation"><a role="menuitem" tabindex="-1" href="home">Тормозные жидкости</a></li>
					                    </ul>
					                </li>
					            </ul>
					
					            <a class="search-button" href="#" ></a>    
					
					        </div>
					    </form>
					</div><!-- /.search-area -->
					<!-- ============================================================= SEARCH AREA : END ============================================================= -->	
				</div><!-- /.top-search-holder -->
	
				<div class="col-xs-12 col-sm-12 col-md-3 top-cart-row no-margin">
					<div class="top-cart-row-container">
					    <div class="wishlist-compare-holder">
					        <div class="wishlist ">
					        	<sec:authorize access="!isAnonymous() and !hasRole('ROLE_ADMIN')">
					            	<a href="Wishlist"><i class="fa fa-heart"></i> Избранное <span class="value">(<c:out value="${requestScope.wishlist.size()}"/>)</span> </a>
					            </sec:authorize>
					        </div>
					        <div class="compare">
					            <a href="Comparison"><i class="fa fa-exchange"></i> Сравнить <span class="value">(<c:out value="${requestScope.compare.size()}"/>)</span> </a>
					        </div>
					    </div>
					
					    <!-- ============================================================= SHOPPING CART DROPDOWN ============================================================= -->
					    <div class="top-cart-holder dropdown animate-dropdown">
					        
					        <div class="basket">
					            
					            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
					                <div class="basket-item-count">
					                    <span class="count"><c:out value="${requestScope.basket.size()}"/></span>
					                    <img src="resources/Ecommerce/assets/images/icon-cart.png" alt="" />
					                </div>
					
					                <div class="total-price-basket"> 
					                    <span class="lbl">Ваша корзина:</span>
					                    <span class="total-price">
					                    	<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
					                        	<span class="sign">$</span><span class="value"><c:out value="${requestScope.totalBasket}"/></span>
					                        </sec:authorize>
					                    </span>
					                </div>
					            </a>
					
					            <ul class="dropdown-menu">
					            	<c:forEach var="currentBasket" items="${requestScope.basket}">
					            		<c:set  var="currentGood" value="${currentBasket.getGood()}" />
					            		<div class="basket-item">
					                        <div class="row">
					                            <div class="col-xs-4 col-sm-4 no-margin text-center">
					                                <div class="thumb">
					                                    <img height="73" width="73" alt="" src="resources/jpg/<c:out value="${currentGood.getPhoto()}"  />"/>
					                                </div>
					                            </div>
					                            <div class="col-xs-8 col-sm-8 no-margin">
					                                <div class="title">
					                                	<c:out value="${currentGood.getName()}"/>
					                                	<c:if test="${currentBasket.getQauntity()>1}">
					                                		(<c:out value="${currentBasket.getQauntity()}"/> шт.)
					                                	</c:if>
					                                </div>
					                                <div class="price">
					                                	<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
					                                		$<c:out value="${currentGood.getPrice()}"/>
					                                	</sec:authorize>		
					                                </div>
					                            </div>
					                        </div>
											<c:url value="Basket" var="deleteFromBasket">
												<c:param name="id" value="${currentGood.getId()}"/>
												<c:param name="goodPrefix" value="${currentGood.getGoodName()}"/>
												<c:param name="variant" value="deleteFromBasket"/>
											</c:url>
											<a class="close-btn" href="${deleteFromBasket}"  title="Удалить товар из корзины." ></a>
					                    </div>
					  				</c:forEach>
					
					                <li class="checkout">
					                    <div class="basket-item">
					                        <div class="row">
					                            <div class="col-xs-12 col-sm-6">
					                                <a href="Basket" class="le-button inverse">Корзина</a>
					                            </div>
					                            <!-- <div class="col-xs-12 col-sm-6">
					                                <a href="checkout.html" class="le-button">Checkout</a>
					                            </div>  -->
					                        </div>
					                    </div>
					                </li>
					
					            </ul>
					        </div><!-- /.basket -->
					    </div><!-- /.top-cart-holder -->
					</div><!-- /.top-cart-row-container -->
					<!-- ============================================================= SHOPPING CART DROPDOWN : END ============================================================= -->	
				</div><!-- /.top-cart-row -->
				
			</div><!-- /.container -->
		</header>

      <!-- ============================================================= HEADER : END ============================================================= -->		
      <section id="category-grid">
        <div class="container">
          <!-- ========================================= SIDEBAR ========================================= -->
          <div class="col-xs-12 col-sm-3 no-margin sidebar narrow">
            <!-- ========================================= PRODUCT FILTER ========================================= -->
            <form action="home" method="POST">
            <div class="widget">
              <h1>Фильтр по товарам</h1>
              <div class="body bordered">
              
                <div class="category-filter">
                  <h2>Производители:</h2>
                  <hr>
                  <c:set var="isSearch" value="${0}"/>
                  <ul>
                  	<c:forEach var="currentManufacturer" items="${requestScope.manufacturersFilter}">
                  		<li>
                  			<c:if test="${currentManufacturer.isSelected()}">
                  				<input checked="checked" type="checkbox" name="selections" value="${currentManufacturer.getId()}" class="le-checkbox"  />
                  				<label><c:out value="${currentManufacturer.getName()}"  /> </label>
                  				<c:set var="isSearch" value="${isSearch+1}"/>
                  			</c:if>
                  		</li>
                  	</c:forEach>
                  </ul>
                  	<c:if test="${isSearch==0}">
                  		По всем
                  	</c:if>
                  
                  <!-- ================================== TOP NAVIGATION ================================== -->
                  <c:set var="manufacturersFilterSize" value="${requestScope.manufacturersFilter.size()}"/>
                  <c:if test="${(manufacturersFilterSize-isSearch)>0}">
		            <div class="side-menu animate-dropdown" tabindex="-1" >
		              <nav class="yamm megamenu-horizontal" role="navigation" >
		                <ul class="nav">
		                  <li class="dropdown menu-item">
		                    <a href=""  class="dropdown-toggle" data-toggle="dropdown">Ещё <c:out value="${requestScope.manufacturersFilter.size()-isSearch}"/>  </a>
		                    <ul class="dropdown-menu mega-menu">
		                      <li class="yamm-content">
		                      	<div class="row">
		                            <div class="col-md-4">
		                                <ul class="list-unstyled">
		                                	<c:forEach var="currentManufacturer" items="${requestScope.manufacturersFilter}">
		                                   		<li>
													<c:if test="${!currentManufacturer.isSelected()}">
                										<input type="checkbox" name="selections" value="${currentManufacturer.getId()}" class="le-checkbox"  />
                										<label><c:out value="${currentManufacturer.getName()}"  /> </label>
						                   			</c:if>
												</li>
		                                   </c:forEach>
		                                </ul>
		                            </div>
		                        </div>
		                      </li>
		                    </ul>
		                  </li>
		                </ul>
		                <!-- /.nav -->
		              </nav>
		              <!-- /.megamenu-horizontal -->
		            </div>
		            </c:if>
		            <!-- /.side-menu -->
		            <!-- ================================== TOP NAVIGATION : END ================================== -->		
                  
                  
                  
                  
                </div>
                <!-- /.category-filter -->
                
                
                <div class="category-filter">
	                  <h2>Класс жидкости:</h2>
	                  <hr>
	                  <c:set var="isSearch" value="${0}"/>
	                  <ul>
	                  	<c:forEach var="currentFluidClass" items="${requestScope.fluidClassFilter}">
	                  		<li>
	                  			<c:if test="${currentFluidClass.isSelected()}">
	                  				<input checked="checked" type="checkbox" name="fluidClassselections" value="${currentFluidClass.getId()}" class="le-checkbox"  />
	                  				<label><c:out value="${currentFluidClass.getName()}"  /> 
	      							<c:set var="isSearch" value="${isSearch+1}"/> 
	                  			</c:if>
<%-- 	                  			<c:if test="${!currentFluidClass.isSelected()}"> --%>
<%-- 	                  				<input type="checkbox" name="fluidClassselections" value="${currentFluidClass.getId()}" class="le-checkbox"  /> --%>
<%-- 	                  			</c:if> --%>
	                  		</li>
	                  	</c:forEach>
	                  </ul>
	                  <c:if test="${isSearch==0}">
                  		По всем
                  	  </c:if>
                  	   <!-- ================================== TOP NAVIGATION ================================== -->
                  	   <c:set var="fluidClassFilterSize" value="${requestScope.fluidClassFilter.size()}"/>
                  	   <c:if test="${(fluidClassFilterSize-isSearch)>0}">
		            <div class="side-menu animate-dropdown" tabindex="-1" >
		              <nav class="yamm megamenu-horizontal" role="navigation" >
		                <ul class="nav">
		                  <li class="dropdown menu-item">
		                    <a href=""  class="dropdown-toggle" data-toggle="dropdown">Ещё <c:out value="${requestScope.fluidClassFilter.size()-isSearch}"/>  </a>
		                    <ul class="dropdown-menu mega-menu">
		                      <li class="yamm-content">
		                      	<div class="row">
		                            <div class="col-md-4">
		                                <ul class="list-unstyled">
		                                	<c:forEach var="currentfluidClassFilter" items="${requestScope.fluidClassFilter}">
		                                   		<li>
													<c:if test="${!currentfluidClassFilter.isSelected()}">
                										<input type="checkbox" name="fluidClassselections" value="${currentfluidClassFilter.getId()}" class="le-checkbox"  />
                										<label><c:out value="${currentfluidClassFilter.getName()}"  /> </label>
						                   			</c:if>
												</li>
		                                   </c:forEach>
		                                </ul>
		                            </div>
		                            
		                        </div>
		                      </li>
		                    </ul>
		                  </li>
		                </ul>
		                <!-- /.nav -->
		              </nav>
		              <!-- /.megamenu-horizontal -->
		            </div>
		            </c:if>
		            <!-- /.side-menu -->
		            <!-- ================================== TOP NAVIGATION : END ================================== -->	
                  	  
                </div>                
                
                
               <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
					<div class="price-filter">
	                  <h2>Цена</h2>
	                  <hr>
	                  <div class="price-range-holder">
						<input id="ex2" type="text" class="price-slider" value="" name="currentPriceFilter"
							data-slider-min="${requestScope.MinPrice}" data-slider-max="${requestScope.MaxPrice}" 
							data-slider-step="100" data-slider-value="[${requestScope.currentMinPriceFilter},${requestScope.currentMaxPriceFilter}]"/>
	                    <span class="min-max">
	                    Цена: $<c:out value="${requestScope.MinPrice}"/> - $<c:out value="${requestScope.MaxPrice}"/>
	                    </span>
	                  </div>
	                </div>                
                <!-- /.price-filter -->
                 </sec:authorize>
                
                 <div class="price-filter">
	                  <h2>Температура кипения (сухая)</h2>
	                  <hr>
	                  <div class="price-range-holder">
<%-- 	                  	<c:set  var="name" value="currentBoilingTemperatureDryFilter" /> --%>
						<input id="ex3" type="text" class="price-slider" value="" name="currentBoilingTemperatureDryFilter"
							data-slider-min="${requestScope.MinBoilingTemperatureDry}" data-slider-max="${requestScope.MaxBoilingTemperatureDry}" 
							data-slider-step="100" data-slider-value="[${requestScope.currentMinBoilingTemperatureDryFilter},${requestScope.currentMaxBoilingTemperatureDryFilter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinBoilingTemperatureDry}"/> до <c:out value="${requestScope.MaxBoilingTemperatureDry}"/> градусов
	                    </span>
	                  </div>
	                </div>
	                
	               <div class="price-filter">
	                  <h2>Температура кипения (влажная)</h2>
	                  <hr>
	                  <div class="price-range-holder">
<%-- 	                  	<c:set  var="name" value="currentBoilingTemperatureWetFilter" /> --%>
						<input id="ex4" type="text" class="price-slider" value="" name="currentBoilingTemperatureWetFilter"
							data-slider-min="${requestScope.MinBoilingTemperatureWet}" data-slider-max="${requestScope.MaxBoilingTemperatureWet}" 
							data-slider-step="100" data-slider-value="[${requestScope.currentMinBoilingTemperatureWetFilter},${requestScope.currentMaxBoilingTemperatureWetFilter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinBoilingTemperatureWet}"/> до <c:out value="${requestScope.MaxBoilingTemperatureWet}"/> градусов
	                    </span>
	                  </div>
	                </div>
	                
	                <div class="price-filter">
	                  <h2>Объём</h2>
	                  <hr>
	                  <div class="price-range-holder">
<%-- 	                  	<c:set  var="name" value="currentValueFilter" /> --%>
						<input id="ex5" type="text" class="price-slider" value="" name="currentValueFilter" 
							data-slider-min="${requestScope.MinValue}" data-slider-max="${requestScope.MaxValue}" 
							data-slider-step="100" data-slider-value="[${requestScope.currentMinValueFilter},${requestScope.currentMaxValueFilter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinValue}"/> до <c:out value="${requestScope.MaxValue}"/> мл.
	                    </span>
	                  </div>
	                </div>
                
                 <div class="price-filter">
	                  <h2>Вязкость (при -40 градусах)</h2>
	                  <hr>
	                  <div class="price-range-holder">
<%-- 	                  	<c:set  var="name" value="currentViscosity40Filter" /> --%>
						<input id="ex6" type="text" class="price-slider" value="" name="currentViscosity40Filter"
							data-slider-min="${requestScope.MinViscosity40}" data-slider-max="${requestScope.MaxViscosity40}" 
							data-slider-step="50" data-slider-value="[${requestScope.currentMinViscosity40Filter},${requestScope.currentMaxViscosity40Filter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinViscosity40}"/> до <c:out value="${requestScope.MaxViscosity40}"/>
	                    </span>
	                  </div>
	                </div>
	                
	                <div class="price-filter">
	                  <h2>Вязкость (при 100 градусах)</h2>
	                  <hr>
	                  <div class="price-range-holder">
	                  	<c:set  var="name" value="currentViscosity100Filter" />
						<input id="ex7" type="text" class="price-slider" value="" name="currentViscosity100Filter"
							data-slider-min="${requestScope.MinViscosity100}" data-slider-max="${requestScope.MaxViscosity100}" 
							data-slider-step="50" data-slider-value="[${requestScope.currentMinViscosity100Filter},${requestScope.currentMaxViscosity100Filter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinViscosity100}"/> до <c:out value="${requestScope.MaxViscosity100}"/>
	                    </span>
	                  </div>
	                </div>
	                
	                
<!-- 	                 <div class="price-filter"> -->
<!-- 	                  <h2>Оценка пользователей</h2> -->
<!-- 	                  <hr> -->
<%-- 	                  <c:out value="${requestScope.currentJudgementFilter}"></c:out> --%>
<!-- 	                  <div class="price-range-holder"> -->
<%-- 							<c:set  var="name" value="currentJudgementFilter" /> --%>
<!-- 						<input id="ex8" type="text" class="price-slider" value="" name="currentJudgementFilter" -->
<%-- 							data-slider-min="${requestScope.MinJudgement}" data-slider-max="${requestScope.MaxJudgement}"  --%>
<%-- 							data-slider-step="1" data-slider-value="[${requestScope.currentMinJudgementFilter},${requestScope.currentMaxJudgementFilter}]"/> --%>
<!-- 	                    <span class="min-max"> -->
<%-- 	                    От <c:out value="${requestScope.MinJudgement}"/> до <c:out value="${requestScope.MaxJudgement}"/> --%>
<!-- 	                    </span> -->
<!-- 	                  </div> -->
<!-- 	                </div> -->
                
                
              </div>
              <!-- /.body -->
              <span class="filter-button">
                   	<button class="le-button" type="submit" name="sss">Отбор</button>	
               </span>
              
            </div>
            <!-- /.widget -->
            </form>
            <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')">
	           <form action="InsertUpdateMotorOil" method="POST"  enctype="multipart/form-data">
            		<input name="variant" type="hidden" value="New" >
		            <div class="widget">
		            	<div class="body bordered">
			            	<div class="buttons-holder">
			            	  <button class="le-button" type="submit" name="sss">Новый товар</button>	
<!-- 			                  <a class="le-button big" href="InsertUpdateMotorOil?variant=New" >Новый товар</a> -->
			                </div>
		            	</div>
		            </div>
	            </form>
            </sec:authorize>            
                        
            <!-- ========================================= PRODUCT FILTER : END ========================================= -->
            
          </div>
          <!-- ========================================= CONTENT ========================================= -->
          <div class="col-xs-12 col-sm-9 no-margin wide sidebar">
            <section id="recommended-products" class="carousel-holder hover small">
              <div class="title-nav">
                <h2 class="inverse">Рекомендуемые товары:</h2>
                <div class="nav-holder">
                  <a href="#prev" data-target="#owl-recommended-products" class="slider-prev btn-prev fa fa-angle-left"></a>
                  <a href="#next" data-target="#owl-recommended-products" class="slider-next btn-next fa fa-angle-right"></a>
                </div>
              </div>
              <!-- /.title-nav -->
              <div id="owl-recommended-products" class="owl-carousel product-grid-holder">
                  <c:forEach var="currentBFluid" items="${requestScope.recommendedBrakFluids}">
                     <div class="no-margin carousel-item product-item-holder hover size-medium">
                  	   
		                   <div class="product-item">
		                    
			                    <div class="image">
			                      <img height="240" width="140" alt="${currentBFluid.getName()}" src="resources/jpg/<c:out value="${currentBFluid.getPhoto()}"  />" 
			                      									data-echo="resources/jpg/<c:out value="${currentBFluid.getPhoto()}"  />"  />
			                    </div>
			                    <div class="body">
			                      <div class="title">
			                        <a href="ShowOne?id=${currentBFluid.getId()}"><c:out value="${currentBFluid.getName()}"  /></a>
			                      </div>
			                      <div class="brand"><c:out value="${currentBFluid.getManufacturer().getName()}"/>
			                      </div>
			                    </div>
			                    <div class="prices">
			                      	<div class="price-current text-right">
			                      		<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
			                      			$<c:out value="${currentBFluid.getPrice()}"/>
			                      		</sec:authorize>
			                      	</div>
			                    </div>
			                    <div class="hover-area">
			                      <div class="add-cart-button">
				                    <c:url value="home" var="UpdateBrakingFluid">
										<c:param name="id" value="${currentBFluid.getId()}"/>
										<c:param name="variant" value="inBasket"/>
										<c:param name="currentPage" value="${requestScope.currentPage}"/>
									</c:url>
			                        <a href="${UpdateBrakingFluid}" class="le-button">В корзину</a>
			                      </div>
			                      <div class="wish-compare">
			                      	<sec:authorize access="!isAnonymous() and !hasRole('ROLE_ADMIN')">
				                        <c:url value="home" var="UpdateBrakingFluid">
											<c:param name="id" value="${currentBFluid.getId()}"/>
											<c:param name="variant" value="inWishlist"/>
											<c:param name="currentPage" value="${requestScope.currentPage}"/>
										</c:url>
										
					                    <a class="btn-add-to-wishlist" href="${UpdateBrakingFluid}">В избранное</a>
				                    </sec:authorize>
			                        <c:url value="home" var="UpdateBrakingFluid">
										<c:param name="id" value="${currentBFluid.getId()}"/>
										<c:param name="variant" value="inCompare"/>
										<c:param name="currentPage" value="${requestScope.currentPage}"/>
									</c:url>
			                        <a class="btn-add-to-compare" href="${UpdateBrakingFluid}">Сравнить</a>
			                      </div>
		                    	</div>
		                    </div>
		                  </div>
		               </form>
                  </c:forEach>
               </div>
              <!-- /#recommended-products-carousel .owl-carousel -->
            </section>
            <!-- /.carousel-holder -->            
            <section id="gaming">
              <div class="grid-list-products">
                <h2 class="section-title">Отобранные товары:</h2>
                <div class="control-bar">
                   <div id="item-count" class="le-select">
                    <select name="elementsInList">
                      <option value="4">4 на странице</option>
                      <option value="6">6 на странице</option>
                      <option value="10">10 на странице</option>
                      <option value="24">24 на странице</option>
                      <option value="32">32 на странице</option>
                    </select>
                  </div>
                  <div class="grid-list-buttons">
                    <ul>
                      <li class="grid-list-button-item active"><a data-toggle="tab" href="#grid-view"><i class="fa fa-th-large"></i> Мозаика</a></li>
                      <li class="grid-list-button-item active"><a data-toggle="tab" href="#list-view"><i class="fa fa-th-list"></i> Список</a></li>
                    </ul>
                  </div>
                </div>
                <!-- /.control-bar -->
                <div class="tab-content">
                  <div id="grid-view" class="products-grid fade tab-pane in active">
                    <div class="product-grid-holder">
                      <div class="row no-margin">
						<c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
                  	 	  <div class="col-xs-12 col-sm-4 no-margin product-item-holder hover">
                          <div class="product-item">
                            <div class="image">
                              <img height="240" width="140" alt="${currentBFluid.getName()}" src="resources/jpg/<c:out value="${currentBFluid.getPhoto()}"  />" 
			                      									data-echo="resources/jpg/<c:out value="${currentBFluid.getPhoto()}"  />"  />
                            </div>
                            <div class="body">
                              <div class="title">
                                <a href="ShowOne?id=${currentBFluid.getId()}"><c:out value="${currentBFluid.getName()}"  /></a>
                              </div>
                              <div class="brand"><c:out value="${currentBFluid.getManufacturer().getName()}"/></div>
                            </div>
                            <div class="prices">
                              <div class="price-current pull-right">
									<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">		                              
	                              		$<c:out value="${currentBFluid.getPrice()}"/>
	                              	</sec:authorize>
   	                          </div>
                            </div>
                            <div class="hover-area">
                              <div class="add-cart-button">
                                <c:url value="home" var="UpdateBrakingFluid">
									<c:param name="id" value="${currentBFluid.getId()}"/>
									<c:param name="variant" value="inBasket"/>
									<c:param name="currentPage" value="${requestScope.currentPage}"/>
								</c:url>
			                    <a href="${UpdateBrakingFluid}" class="le-button">В корзину</a>
                              </div>
                              <div class="wish-compare">
                              	   <sec:authorize access="!isAnonymous() and !hasRole('ROLE_ADMIN')">
				                        <c:url value="home" var="UpdateBrakingFluid">
											<c:param name="id" value="${currentBFluid.getId()}"/>
											<c:param name="variant" value="inWishlist"/>
											<c:param name="currentPage" value="${requestScope.currentPage}"/>
										</c:url>
										
				                        <a class="btn-add-to-wishlist" href="${UpdateBrakingFluid}">В избранное</a>
				                   </sec:authorize>
		                        <c:url value="home" var="UpdateBrakingFluid">
									<c:param name="id" value="${currentBFluid.getId()}"/>
									<c:param name="variant" value="inCompare"/>
									<c:param name="currentPage" value="${requestScope.currentPage}"/>
								</c:url>
		                        <a class="btn-add-to-compare" href="${UpdateBrakingFluid}">Сравнить</a>
			                        
			                  </div>
                            </div>
                          </div>
                          <!-- /.product-item -->
                        </div>
                        <!-- /.product-item-holder -->
                        </c:forEach>
                      </div>
                      <!-- /.row -->
                    </div>
                    <!-- /.product-grid-holder -->
                    <div class="pagination-holder">
                      <div class="row">
                        <div class="col-xs-12 col-sm-6 text-left">
                          <ul class="pagination ">
                          	<c:forEach var="page" begin="1" end="${requestScope.totalPages}">
                          		<c:url value="home" var="UpdateBrakingFluid">
									<c:param name="variant" value="home"/>
									<c:param name="currentPage" value="${page}"/>
								</c:url>
                       	    	<c:set var="current" value=""/>
 		                  			<c:if test="${(page+1)==requestScope.currentPage}">
        	          				<c:set var="current" value="class='current'"/>
            	      			</c:if>
                          	    <li <c:out value="${current}"  />  ><a  href="${UpdateBrakingFluid}"><c:out value="${page}"/></a></li>
                          	</c:forEach>
                          	<c:if test="${requestScope.totalPages>requestScope.currentPage}">
	                          	<c:url value="home" var="UpdateBrakingFluid">
									<c:param name="variant" value="home"/>
									<c:param name="currentPage" value="${requestScope.currentPage+1}"/>
								</c:url>
								<li><a href="${UpdateBrakingFluid}">Следующая</a></li>
							</c:if>
                          </ul>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                          <div class="result-counter">
                          	Товары <span><c:out value="${requestScope.paginationString_part1}"  /> </span> из <span> <c:out value="${requestScope.paginationString_part2}"  /> <span> найденных
                           
                          </div>
                        </div>
                      </div>
                      <!-- /.row -->
                    </div>
                    <!-- /.pagination-holder -->
                  </div>
                  <!-- /.products-grid #grid-view -->
                  <div id="list-view" class="products-grid fade tab-pane ">
                    <div class="products-list">
                        
                        
						 <c:forEach var="currentBFluid" items="${requestScope.listBrakFluids}">
						    <div class="product-item product-item-holder">
                        		<div class="row">
						 
	                  	 	 <div class="no-margin col-xs-12 col-sm-4 image-holder">
	                            <div class="image">
	                              <img height="240" width="140" alt="${currentBFluid.getName()}" src="resources/jpg/<c:out value="${currentBFluid.getPhoto()}"  />" 
			                      									data-echo="resources/jpg/<c:out value="${currentBFluid.getPhoto()}"  />"  />
	                            </div>
	                         </div> 
	                         <!-- /.image-holder -->
	                          <div class="no-margin col-xs-12 col-sm-5 body-holder">
	                            <div class="body">
	                              <div class="title">
	                                <a href="ShowOne?id=${currentBFluid.getId()}"><c:out value="${currentBFluid.getName()}"  /></a>
	                              </div>
	                              <div class="brand"><c:out value="${currentBFluid.getManufacturer().getName()}"/></div>
	                              <div class="excerpt">
	                                <p><c:out value="${currentBFluid.getDescription()}"/></p>
	                              </div>
	                              <div class="addto-compare">
	                               <c:url value="home" var="UpdateBrakingFluid">
										<c:param name="id" value="${currentBFluid.getId()}"/>
										<c:param name="variant" value="inCompare"/>
										<c:param name="currentPage" value="${requestScope.currentPage}"/>
									</c:url>
			                        <a class="btn-add-to-compare" href="${UpdateBrakingFluid}">Сравнить</a>
	                              </div>
	                            </div>
	                          </div>
	                          <!-- /.body-holder --> 
	                           <div class="no-margin col-xs-12 col-sm-3 price-area">
	                            <div class="right-clmn">
	                              <div class="price-current">
	                              	<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
	                              		$<c:out value="${currentBFluid.getPrice()}"/>
	                              	</sec:authorize>
	                              </div>
	                              	
	                              <div class="availability"><label>В наличии:</label><span class="available"> на складе</span></div>
	                              <div class="add-cart-button">
	                                <c:url value="home" var="UpdateBrakingFluid">
										<c:param name="id" value="${currentBFluid.getId()}"/>
										<c:param name="variant" value="inBasket"/>
										<c:param name="currentPage" value="${requestScope.currentPage}"/>
									</c:url>
				                    <a href="${UpdateBrakingFluid}" class="le-button">В корзину</a>
		                            <div class="wish-compare">
			                            <sec:authorize access="!isAnonymous()  and !hasRole('ROLE_ADMIN')">
					                        <c:url value="home" var="UpdateBrakingFluid">
												<c:param name="id" value="${currentBFluid.getId()}"/>
												<c:param name="variant" value="inWishlist"/>
												<c:param name="currentPage" value="${requestScope.currentPage}"/>
											</c:url>
				                        <a class="btn-add-to-wishlist" href="${UpdateBrakingFluid}">В избранное</a>
				                        </sec:authorize>
			                  		</div>
			                  		
	                            </div>
	                          </div>
	                          <!-- /.price-area -->
	                          </div>
	                          <!-- /.product-item -->
		                        <!-- /.product-item-holder -->
	                          </div> 
                        </div>
                        <!-- /.row -->
	                        </c:forEach>                        
                        
                      </div>
                    <!-- /.pagination-holder -->
                    <div class="pagination-holder">
                      <div class="row">
                        <div class="col-xs-12 col-sm-6 text-left">
                          <ul class="pagination ">
                          	<c:forEach var="page" begin="1" end="${requestScope.totalPages}">
                          		<c:url value="home" var="UpdateBrakingFluid">
									<c:param name="variant" value="home"/>
									<c:param name="currentPage" value="${page}"/>
								</c:url>
                       	    	<c:set var="current" value=""/>
 		                  			<c:if test="${(page+1)==requestScope.currentPage}">
        	          				<c:set var="current" value="class='current'"/>
            	      			</c:if>
                          	    <li <c:out value="${current}"  />  ><a  href="${UpdateBrakingFluid}"><c:out value="${page}"/></a></li>
                          	</c:forEach>
                          	<c:if test="${requestScope.totalPages>requestScope.currentPage}">
	                          	<c:url value="home" var="UpdateBrakingFluid">
									<c:param name="variant" value="home"/>
									<c:param name="currentPage" value="${requestScope.currentPage+1}"/>
								</c:url>
								<li><a href="${UpdateBrakingFluid}">Следующая</a></li>
							</c:if>
                          </ul>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                          <div class="result-counter">
                          	Товары <span><c:out value="${requestScope.paginationString_part1}"  /> </span> из <span> <c:out value="${requestScope.paginationString_part2}"  /> <span> найденных
                          </div>
                        </div>
                      </div>
                      <!-- /.row -->
                    </div>
                  </div>
                  <!-- /.products-grid #list-view -->
                </div>
                <!-- /.tab-content -->
              </div>
              <!-- /.grid-list-products -->
            </section>
            <!-- /#gaming -->            
          </div>
          <!-- /.col -->
          <!-- ========================================= CONTENT : END ========================================= -->    
        </div>
        <!-- /.container -->
      </section>
      <!-- /#category-grid -->		<!-- ============================================================= FOOTER ============================================================= -->
      <footer id="footer" class="color-bg">
         <div class="copyright-bar">
          <div class="container">
            <div class="col-xs-12 col-sm-6 no-margin">
              <div class="copyright">
                &copy; <a href="index">Media Center</a> - all rights reserved
              </div>
              <!-- /.copyright -->
            </div>
            <div class="col-xs-12 col-sm-6 no-margin">
              <div class="payment-methods ">
                <ul>
                  <li><img alt="" src="resources/Ecommerce/assets/images/payments/payment-visa.png"></li>
                  <li><img alt="" src="resources/Ecommerce/assets/images/payments/payment-master.png"></li>
                  <li><img alt="" src="resources/Ecommerce/assets/images/payments/payment-paypal.png"></li>
                  <li><img alt="" src="resources/Ecommerce/assets/images/payments/payment-skrill.png"></li>
                </ul>
              </div>
              <!-- /.payment-methods -->
            </div>
          </div>
          <!-- /.container -->
        </div>
        <!-- /.copyright-bar -->
      </footer>
      <!-- /#footer -->
      <!-- ============================================================= FOOTER : END ============================================================= -->	
    </div>
    <!-- /.wrapper -->
    <!-- JavaScripts placed at the end of the document so the pages load faster -->
    <script src="resources/Ecommerce/assets/js/jquery-1.10.2.min.js"></script>
    <script src="resources/Ecommerce/assets/js/jquery-migrate-1.2.1.js"></script>
    <script src="resources/Ecommerce/assets/js/bootstrap.min.js"></script>
    <script src="http://maps.google.com/maps/api/js?sensor=false&amp;language=en"></script>
    <script src="resources/Ecommerce/assets/js/gmap3.min.js"></script>
    <script src="resources/Ecommerce/assets/js/bootstrap-hover-dropdown.min.js"></script>
    <script src="resources/Ecommerce/assets/js/owl.carousel.min.js"></script>
    <script src="resources/Ecommerce/assets/js/css_browser_selector.min.js"></script>
    <script src="resources/Ecommerce/assets/js/echo.min.js"></script>
    <script src="resources/Ecommerce/assets/js/jquery.easing-1.3.min.js"></script>
    <script src="resources/Ecommerce/assets/js/bootstrap-slider.min.js"></script>
    <script src="resources/Ecommerce/assets/js/jquery.raty.min.js"></script>
    <script src="resources/Ecommerce/assets/js/jquery.prettyPhoto.min.js"></script>
    <script src="resources/Ecommerce/assets/js/jquery.customSelect.min.js"></script>
    <script src="resources/Ecommerce/assets/js/wow.min.js"></script>
    <script src="resources/Ecommerce/assets/js/scripts.js"></script>
    <!-- For demo purposes вЂ“ can be removed on production -->
    <script src="resources/Ecommerce/switchstylesheet/switchstylesheet.js"></script>
    <script>
      $(document).ready(function(){ 
      	$(".changecolor").switchstylesheet( { seperator:"color"} );
      	$('.show-theme-options').click(function(){
      		$(this).parent().toggleClass('open');
      		return false;
      	});
      });
      
      $(window).bind("load", function() {
         $('.show-theme-options').delay(2000).trigger('click');
      });
    </script>
    <!-- For demo purposes вЂ“ can be removed on production : End -->
    <script src="http://w.sharethis.com/button/buttons.js"></script>
    
  </body>
</html>

