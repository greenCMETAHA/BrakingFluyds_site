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
		                <li><a href="">В начало</a></li>
		                 <!-- <li><a href="contact.html">Contact</a></li>  -->
		                <li class="dropdown">
		                    <a class="dropdown-toggle" data-toggle="dropdown" href="#pages">Страницы</a>
		                    <ul class="dropdown-menu" role="menu">
		                        <li><a href="">В начало</a></li>
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
<c:set var="name" value="basket" />		
<section id="cart-page">
  <div class="container">
        
        <!-- ========================================= CONTENT ========================================= -->
        <div class="col-xs-12 col-md-9 items-holder no-margin">
            <c:forEach var="currentBasket" items="${sessionScope[name]}">
				<c:set  var="currentGood" value="${currentBasket.getGood()}" />
	            <div class="row no-margin cart-item">
	              <div class="col-xs-12 col-sm-2 no-margin">
	                <a href="ShowOne?id=${currentGood.getId()}" class="thumb-holder">
	                <img class="lazy" height="73" width="73" alt="${currentGood.getName()}" src="resources/jpg/${currentGood.getPhoto()}" />
	                </a>
	              </div>
	              <div class="col-xs-12 col-sm-5 ">
	                <div class="title">
	                  <a href="ShowOne?id=${currentGood.getId()}"><c:out value="${currentGood.getName()}" /></a>
	                </div>
	                <div class="brand"><c:out value="${currentGood.getManufacturer().getName()}"/></div>
	              </div>
	          	  <div class="col-xs-12 col-sm-3 no-margin">
	          	  <div class="quantity">
	                
	                  <div class="le-quantity">
	          	  	      <c:url value="Basket" var="deleteFromBasket">
							<c:param name="id" value="${currentGood.getId()}"/>
							<c:param name="variant" value="deleteQuantityFromBasket"/>
							<c:param name="quantity" value="${-1}"/>
						  </c:url>
	                      <a class="minus" href="${deleteFromBasket}"></a>
	                      
	                      <input name="quantity" readonly="readonly" type="text" value="${currentBasket.getQauntity()}" />   <!-- отображаем количество -->

	                	  <c:url value="Basket" var="deleteFromBasket">
							<c:param name="id" value="${currentGood.getId()}"/>
							<c:param name="variant" value="deleteQuantityFromBasket"/>
							<c:param name="quantity" value="${1}"/>
						  </c:url>
						  <a class="plus" href="${deleteFromBasket}"></a>
	                  </div>
	              
	                
	                  </div>
	              </div>
	          	  <div class="col-xs-12 col-sm-2 no-margin">
	                <div class="price">
	                	<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
	                  		$<c:out value="${currentGood.getPrice()*currentBasket.getQauntity()}" />
	                  	</sec:authorize>
	                </div>
	                <c:url value="Basket" var="deleteFromBasket">
						<c:param name="id" value="${currentGood.getId()}"/>
						<c:param name="variant" value="deleteFromBasket"/>
					</c:url>
	                <a class="close-btn" href="${deleteFromBasket}"></a>
	              </div>
	            </div>
	            <!-- /.cart-item -->
          	</c:forEach>
        </div>
        <!-- ========================================= CONTENT : END ========================================= -->

        <!-- ========================================= SIDEBAR ========================================= -->

        <div class="col-xs-12 col-md-3 no-margin sidebar ">
          <c:if test="${sessionScope[name].size()!=0}">
					<!-- Paypal Button and Instant Payment Notification (IPN) Integration with Java
					  http://codeoftheday.blogspot.com/2013/07/paypal-button-and-instant-payment_6.html	-->
					
					<form action="PayPal" method="post" target="_top">
						<input checked type="radio"  name="card" value="PayPal" >PayPal<br>
						<input type="radio"  name="card" value="Visa">Кредитная карта<br><br>
						<input type="image" src="https://www.paypalobjects.com/en_US/GB/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal ? The safer, easier way to pay online.">
						<img alt="" border="0" src="https://www.paypalobjects.com/en_GB/i/scr/pixel.gif" width="1" height="1">
					</form>		  			 		
          
		          <form action="home" method="POST">
		            <div class="widget cart-summary">
		              <h1 class="border">В Корзине:</h1>
		              <div class="body">
		                <ul class="tabled-data no-border inverse-bold">
		                  <li>
		                    <label>Итого:</label>
		                    <div class="value pull-right">
			                   	<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
			 	                 	$<c:out value="${requestScope.totalBasket}" />
			                    </sec:authorize>
		                    </div>
		                  </li>
		                  <li>
		                    <label>Доставка:</label>
		                    <div class="value pull-right">Самовывоз</div>
		                  </li>
		                </ul>
		                <ul id="total-price" class="tabled-data inverse-bold no-border">
		                  <li>
		                    <label>Всего:</label>
		                    <div class="value pull-right">
			                    <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
			                    	$<c:out value="${requestScope.totalBasket}" />
			                    </sec:authorize>
			                </div>
		                  </li>
		                 </ul>
		                 <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
			                 <ul class="tabled-data no-border inverse-bold ">
				                  <li>
				                    <label>Введите сумму оплаты:</label>
				                    <div class="value">
					                    <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
					                    	$<input type="text" class="input" name="paySumm" value="${requestScope.totalBasket}" />
					                    </sec:authorize>
					                </div>
				                  </li>
				                  <li>
				                    <label>Выберите клиента:</label>
				                    
				                    <div class="value pull-left">
										<select class="select" size="1" name="client_id" id="input-one">
						                    <option disabled>Выберите получателя</option>	
											<c:forEach var="punct" items="${requestScope.listClients}">
												<option value="${punct.getId()}"><c:out value="${punct.getName()}" /></option>
											</c:forEach>
						                </select>
									</div>
									<br> <br>
				                  </li>
			                </ul>
			                
		                </sec:authorize>
		                <div class="buttons-holder">
		                	<button class="le-button" type="submit" name="variant" value="checkout">Оплатить</button>
		                  <a class="simple-link block" href="home" >Продолжить</a>
		                </div>
		              </div>
		            </div>
		             
		       		</form>
       		</c:if>
          </div>
          <c:if test="${sessionScope[name].size()!=0}">
          <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR')">
	          <h1 class="border">Коммерческое предложение</h1>
	          <div class="row">
	  			 <div class="items-holder">
					<div class="container-fluid wishlist_table">
						<div class="row cart-item cart_item" id="yith-wcwl-row-1">
							<div class="col-xs-12 col-sm-4 no-margin">Распечатать коммерческое предложение:
							</div>
							<div class="col-xs-12 col-sm-2 no-margin">
							</div>
							<div class="col-xs-12 col-sm-3 no-margin">
								<div class="text-right">
									<div class="add-cart-button">
										<a class="le-button add_to_cart_button product_type_simple" href="Basket?variant=Show">Распечатать</a>
										<a class="le-button add_to_cart_button product_type_simple" href="InsertUpdateDoc?variant=Offer&task=New">Сохранить</a>
									</div>							
								</div>
							</div>
		              	</div>
	               	 </div>
	              </div>
	            </div>
 
	            <div class="row">
	  			 <div class="items-holder">
					<div class="container-fluid wishlist_table">
						<div class="col-xs-12 col-sm-4 no-margin">Отправить по электронной почте:
						</div>
						
						<form action="Basket" method="POST">
							<div class="col-xs-12 col-sm-5 no-margin">
								<select class="select" size="1" name="client" id="input-one">
				                    <option disabled>Выберите получателя</option>	
									<c:forEach var="punct" items="${requestScope.listClients}">
										<option value="${punct.getId()}"><c:out value="${punct.getName()}" />(<c:out value="${punct.getEmail()}" />)</option>
									</c:forEach>
				                </select>
							</div>
							<div class="col-xs-12 col-sm-3 no-margin">
			                    <div class="buttons-holder">
		                            <button class="le-button" type="submit" name="variant" value="Send">Отправить</button>
		                        </div>
	                        </div>
	                    </form>
	               	 </div>
	              </div>
	            </div> 
	            </sec:authorize>
	            <sec:authorize access="(!hasAnyRole('ROLE_PRODUCT','ROLE_PRICE')) or (hasAnyRole('ROLE_ADMIN'))">
				<div class="row">
	  			 <div class="items-holder">
					<div class="container-fluid wishlist_table">
						<div class="row cart-item cart_item" id="yith-wcwl-row-1">
							<div class="col-xs-12 col-sm-4 no-margin">Создать заявку:
							</div>
							<div class="col-xs-12 col-sm-2 no-margin">
							</div>
							<div class="col-xs-12 col-sm-3 no-margin">
								<div class="text-right">
									<div class="add-cart-button">
										<a class="le-button add_to_cart_button product_type_simple" href="Basket?variant=Demand">Заявка</a>
										<a class="le-button add_to_cart_button product_type_simple" href="InsertUpdateDoc?variant=Demand&task=New">Сохранить</a>
									</div>							
								</div>
							</div>
		              	</div>
	               	 </div>
	              </div>
	            </div>
	            </sec:authorize>
	       </c:if>	            
		</div>
	</section>
	
	    
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
