<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

    <div class="wrapper">
         <div id="top-banner-and-menu">
        <div class="container">
          <div class="col-xs-12 col-sm-4 col-md-3 sidemenu-holder">
            <!-- ================================== TOP NAVIGATION ================================== -->
            <div class="side-menu animate-dropdown">
              <div class="head"><i class="fa fa-list"></i> Все категории</div>
              <nav class="yamm megamenu-horizontal" role="navigation">
                <ul class="nav">
                  <li class="dropdown menu-item">
                    <a href="" class="dropdown-toggle" data-toggle="dropdown">Страницы</a>
                    <ul class="dropdown-menu mega-menu">
                      <li class="yamm-content">
                      	<div class="row">
                            <div class="col-md-4">
                                <ul class="list-unstyled">
                                   <li><a href="login">Авторизация</a></li>
                                   <li><a href="About">О нас</a></li>
                                </ul>
                            </div>
                            <div class="col-md-4">
                                <ul class="list-unstyled">
	 		                        <li><a href="home?good=BrakinFluids">Список тормозных жидкостей</a></li>
	 		                        <li><a href="motorOil">Список моторных масел</a></li>
			                        <li><a href="Basket">Корзина</a></li>
			                        <sec:authorize access="!isAnonymous() and !hasRole('ROLE_ADMIN')">
			                        	<li><a href="Wishlist">Избранное</a></li>
			                        </sec:authorize>		                        
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
				                        <li><a href="listDoc?variant=Pay">Список оплат</a></li>
				                    </sec:authorize>
			                        <sec:authorize access="hasRole('ROLE_DELIVERY')">
			                        	<li><a href="listDoc?variant=Demand">Доставка</a></li>
			                        </sec:authorize>
			                        <sec:authorize access="hasRole('ROLE_MANAGER_SALE')">
				                        <li><a href="listDoc?variant=Demand">Список заявок</a></li>
				                    </sec:authorize>
				                    <sec:authorize access="hasRole('ROLE_DISTR')">
				                        <li><a href="listDoc?variant=Demand">Список заявок</a></li>
				                        <li><a href="listDoc?variant=Offer">Список ком. предложений</a></li>
				                        <li><a href="listDoc?variant=Pay">Список оплат</a></li>
				                    </sec:authorize>
				                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                      					<li><a href="home?adminpanel=true">Конфигурирование</a></li>
                  					</sec:authorize>
                                </ul>
                            </div>
							<div class="col-md-4">
                                <ul class="list-unstyled">
	 		                        <li><a href="report?variant=Pay">Отчет по платежам</a></li>
                                </ul>
                            </div>                            
                        </div>
                      </li>
                    </ul>
                  </li>
                  <!-- /.menu-item -->
                  <li><a href="home?good=BrakinFluids">Тормозные жидкости</a></li>
	 		      <li><a href="motorOil">Моторные масла</a></li>
                  <sec:authorize access="hasRole('ROLE_DELIVERY')">
                  	<li><a href="listDoc?variant=Demand">Доставка</a></li>
                  </sec:authorize>
                  <sec:authorize access="hasRole('ROLE_MANAGER_SALE')">
                      <li><a href="listDoc?variant=Demand">Список заявок</a></li>
                  </sec:authorize>
                  <sec:authorize access="hasRole('ROLE_DISTR')">
                      <li><a href="listDoc?variant=Demand">Список заявок</a></li>
                      <li><a href="listDoc?variant=Offer">Список ком. предложений</a></li>
                      <li><a href="listDoc?variant=Pay">Список оплат</a></li>
                  </sec:authorize>
                   <sec:authorize access="hasRole('ROLE_ADMIN')">
                      <li><a href="home?adminpanel=true">Конфигурирование</a></li>
                  </sec:authorize>
                </ul>
                <!-- /.nav -->
              </nav>
              <!-- /.megamenu-horizontal -->
            </div>
            <!-- /.side-menu -->
            <!-- ================================== TOP NAVIGATION : END ================================== -->		
          </div>
          <!-- /.sidemenu-holder -->
          <div class="col-xs-12 col-sm-8 col-md-9 homebanner-holder">
            <!-- ========================================== SECTION – HERO ========================================= -->
            <div id="hero">
              <div id="owl-main" class="owl-carousel owl-inner-nav owl-ui-sm">
                <div class="item" style="background-image: url(resources/Ecommerce/assets/images/sliders/slider01.jpg);">
							<div class="container-fluid">
								<div class="caption vertical-center text-left">
									<div class="big-text fadeInDown-1">
										Покупайте тормозные жидкости<span class="big"><span class="sign"></span>только у нас</span>
									</div>
				
									<div class="excerpt fadeInDown-2">
										мы предлагаем лучше цены
									</div>
									<div class="small fadeInDown-2">
										и хорошее качество
									</div>
								</div><!-- /.caption -->
							</div><!-- /.container-fluid -->
						</div><!-- /.item -->
				
						<div class="item" style="background-image: url(resources/Ecommerce/assets/images/sliders/slider03.jpg);">
							<div class="container-fluid">
								<div class="caption vertical-center text-left">
									<div class="big-text fadeInDown-1">
										Лучшие товары<span class="big"><span class="sign"></span></span>только у нас
									</div>
				
									<div class="excerpt fadeInDown-2">
										Сайт разработан <br>в компании ЭфТех<br>
									</div>
									<div class="small fadeInDown-2">
										Разработчик: 
									</div>
									<div class="button-holder fadeInDown-3">
										<a href="Http://locomotions.ru" class="big le-button ">Васильченко Глеб</a>
									</div>
								</div><!-- /.caption -->
							</div><!-- /.container-fluid -->
						</div><!-- /.item -->
              <!-- /.owl-carousel -->
            </div>
            <!-- ========================================= SECTION – HERO : END ========================================= -->			
          </div>
          <!-- /.homebanner-holder -->
        </div>
        <!-- /.container -->
      </div>
      <!-- /#top-banner-and-menu -->
  
      </div>
   </div>
 

