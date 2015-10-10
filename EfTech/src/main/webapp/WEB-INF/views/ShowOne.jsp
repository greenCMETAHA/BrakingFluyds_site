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
		                        <li><a href="home">Список товаров</a></li>
		                        <li><a href="Basket">Корзина</a></li>
		                        <sec:authorize access="!isAnonymous() and !hasRole('ROLE_ADMIN')">
		                        	<li><a href="Wishlist">Избранное</a></li>
		                        </sec:authorize>		                        
		                        <li><a href="About">О нас</a></li>
		                        <sec:authorize access="hasAnyRole('ROLE_PRODUCT','ROLE_ADMIN')">
   		                        	<li><a href="Download">Загрузить товар из Excel</a></li>
   		                        </sec:authorize>
   		                        <sec:authorize access="hasAnyRole('ROLE_PRICE','ROLE_ADMIN')">
		                        	<li><a href="Download">Загрузить цены из Excel</a></li>
		                        </sec:authorize>
		                        <li><a href="Comparison">Сравнить товары</a></li>
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
					    <form action="home" method="POST">
					        <div class="control-group">
					            <input class="search-field" placeholder="Search for item" />
					
					            <ul class="categories-filter animate-dropdown">
					                <li class="dropdown">
					
					                    <a class="dropdown-toggle"  data-toggle="dropdown" href="home">Все категории</a>
					
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
					            		<c:set  var="currentBFluid" value="${currentBasket.getBrakingFluid()}" />
					            		<div class="basket-item">
					                        <div class="row">
					                            <div class="col-xs-4 col-sm-4 no-margin text-center">
					                                <div class="thumb">
					                                    <img height="73" width="73" alt="" src="resources/jpg/<c:out value="${currentBFluid.getPhoto()}"  />"/>
					                                </div>
					                            </div>
					                            <div class="col-xs-8 col-sm-8 no-margin">
					                                <div class="title">
					                                	<c:out value="${currentBFluid.getName()}"/>
					                                	<c:if test="${currentBasket.getQauntity()>1}">
					                                		(<c:out value="${currentBasket.getQauntity()}"/> шт.)
					                                	</c:if>
					                                </div>
					                                <div class="price">
					                                	<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
					                                		$<c:out value="${currentBFluid.getPrice()}"/>
					                                	</sec:authorize>		
					                                </div>
					                            </div>
					                        </div>
											<c:url value="Basket" var="deleteFromBasket">
												<c:param name="id" value="${currentBFluid.getId()}"/>
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
		
      <div id="single-product">
        <div class="container">
          <div class="no-margin col-xs-12 col-sm-6 col-md-5 gallery-holder">
            <div class="product-item-holder size-big single-product-gallery small-gallery">
              <div id="owl-single-product">
<!--                 <div class="single-product-gallery-item" id="slide1"> -->
                  <img class="img-responsive" alt="" src="resources/jpg/${requestScope.currentBrakFluid.getPhoto()}" />
<!--                 </div> -->
                <!-- /.single-product-gallery-item -->
              </div>
              <!-- /.single-product-slider -->
            </div>
            <!-- /.single-product-gallery -->
          </div>
          <!-- /.gallery-holder -->        
          <div class="no-margin col-xs-12 col-sm-7 body-holder">
            <div class="body">
              <div class="star-holder inline">
                <div class="star" data-score="${requestScope.currentBrakFluid.getJudgement()}"></div>
              </div>
              <div class="availability"><label>Доступно:</label><span class="available"> на складе</span></div>
              <div class="title"><c:out value="${requestScope.currentBrakFluid.getName()}"/></div>
              <div class="brand"><c:out value="${requestScope.currentBrakFluid.getManufacturer().getName()}"/></div>
              
              <div class="buttons-holder">
   		        <div class="wish-compare">
   		        	<sec:authorize access="!isAnonymous() and !hasRole('ROLE_ADMIN')">
		                <c:url value="home" var="UpdateBrakingFluid">
							<c:param name="id" value="${requestScope.currentBrakFluid.getId()}"/>
							<c:param name="variant" value="inWishlist"/>
						</c:url>
		          		<a class="btn-add-to-wishlist" href="${UpdateBrakingFluid}">В избранное</a>
	          		</sec:authorize>
		            <c:url value="home" var="UpdateBrakingFluid">
						<c:param name="id" value="${requestScope.currentBrakFluid.getId()}"/>
						<c:param name="variant" value="inCompare"/>
					</c:url>              
                	<a class="btn-add-to-compare" href="${UpdateBrakingFluid}">Сравнить</a>
              </div>
              <div class="excerpt">
                <p><c:out value="${requestScope.currentBrakFluid.getDescription()}"/></p>
              </div>
              <div class="prices">
              	<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
                	<div class="price-current">$<c:out value="${requestScope.currentBrakFluid.getPrice()}"/></div>
                </sec:authorize>
              </div>
              <div class="qnt-holder">
<!--                 <div class="le-quantity"> -->
<!--                   <form> -->
<!--                     <a class="minus" href="#reduce"></a> -->
<!--                     <input name="quantity" readonly="readonly" type="text" value="1" /> -->
<!--                     <a class="plus" href="#add"></a> -->
<!--                   </form> -->
<!--                 </div> -->
                <c:url value="home" var="UpdateBrakingFluid">
					<c:param name="id" value="${requestScope.currentBrakFluid.getId()}"/>
					<c:param name="variant" value="inBasket"/>
				</c:url>      
                <a id="addto-cart" href="${UpdateBrakingFluid}" class="le-button huge">В Корзину</a>
                <sec:authorize access="!hasAnyRole('ROLE_PRODUCT','ROLE_PRICE','ROLE_DISTR')">
	                <c:url value="ShowOne" var="UpdateBrakingFluid">
						<c:param name="id" value="${requestScope.currentBrakFluid.getId()}"/>
						<c:param name="variant" value="Demand"/>
					</c:url>      
	                <a id="addto-cart" href="${UpdateBrakingFluid}" class="le-button huge">Заявка</a>
                </sec:authorize>
              </div>
              <!-- /.qnt-holder -->
            </div>
            <!-- /.body -->
          </div>
          <!-- /.body-holder -->
        </div>
        <!-- /.container -->
      </div>
      <!-- /.single-product -->
      <!-- ========================================= SINGLE PRODUCT TAB ========================================= -->
      <section id="single-product-tab">
        <div class="container">
          <div class="tab-holder">
            <ul class="nav nav-tabs simple" >
              <li class="active"><a href="#description" data-toggle="tab">Описание</a></li>
              <li><a href="#additional-info" data-toggle="tab">Дополнительная информация</a></li>
              <li><a href="#reviews" data-toggle="tab">Отзывы (<c:out value="${requestScope.reviews.size()}"/>)</a></li>
            </ul>
            <!-- /.nav-tabs -->
            <div class="tab-content">
              <div class="tab-pane active" id="description">
                <p><c:out value="${requestScope.currentBrakFluid.getDescription()}"/></p>
                <div class="meta-row">
                  <div class="inline">
                    <label>SKU:</label>
                    <span>не назначено</span>
                  </div>
                  <!-- /.inline -->
                  <span class="seperator">/</span>
                  <div class="inline">
                    <label>Разделы:</label>
                    <span><a href="home">Тормозные жидкости</a>,</span>
                  </div>
                  <!-- /.inline -->
                  <span class="seperator">/</span>
                  <div class="inline">
                    <label>тэги:</label>
                    <span><a href="home">Тормозные жидкости</a>,</span>
                  </div>
                  <!-- /.inline -->
                </div>
                <!-- /.meta-row -->
              </div>
              
              <!-- /.tab-pane #description -->
              <div class="tab-pane" id="additional-info">
                <ul class="tabled-data">
                  <li>
                    <label>Класс жидкости:</label>
                    <div class="value"><c:out value="${requestScope.currentBrakFluid.getFluidClass().getName()}"/></div>
                  </li>
                  <li>
                    <label>Температура кипения (сух.):</label>
                    <div class="value"><c:out value="${requestScope.currentBrakFluid.getBoilingTemperatureDry()}"/></div>
                  </li>
                  <li>
                    <label>Температура кипения (вл.):</label>
                    <div class="value"><c:out value="${requestScope.currentBrakFluid.getBoilingTemperatureWet()}"/></div>
                  </li>
                  <li>
                    <label>Объём:</label>
                    <div class="value"><c:out value="${requestScope.currentBrakFluid.getValue()}"/></div>
                  </li>
                  <li>
                    <label>Вязкость (при -40):</label>
                    <div class="value"><c:out value="${requestScope.currentBrakFluid.getViscosity40()}"/></div>
                  </li>
                  <li>
                    <label>Вязкость (при 100):</label>
                    <div class="value"><c:out value="${requestScope.currentBrakFluid.getViscosity100()}"/></div>
                  </li>
                  <li>
                    <label>Спецификация:</label>
                    <div class="value"><c:out value="${requestScope.currentBrakFluid.getSpecification()}"/></div>
                  </li>
                </ul>
                
                <!-- /.tabled-data -->
                <div class="meta-row">
                  <div class="inline">
                    <label>SKU:</label>
                    <span>не назначено</span>
                  </div>
                  <!-- /.inline -->
                  <span class="seperator">/</span>
                  <div class="inline">
                    <label>категории:</label>
                    <span><a href="home">Тормозные жидкости</a>,</span>
                  </div>
                  <!-- /.inline -->
                  <span class="seperator">/</span>
                  <div class="inline">
                    <label>тэги:</label>
                    <span><a href="home">Тормозные жидкости</a>,</span>
                  </div>
                  <!-- /.inline -->
                </div>
                <!-- /.meta-row -->
              </div>
              
              <!-- /.tab-pane #additional-info -->
              <div class="tab-pane" id="reviews">
                <div class="comments">
                   <c:forEach var="review" items="${requestScope.reviews}">
	                  <div class="comment-item">
	                    <div class="row no-margin">
	                      <div class="col-lg-1 col-xs-12 col-sm-2 no-margin">
	                        <div class="avatar">
	                          <img alt="avatar" src="resources/Ecommerce/assets/images/default-avatar.jpg">
	                        </div><!-- 	                        /.avatar -->
	                      </div><!-- 	                      /.col -->
	                      <div class="col-xs-12 col-lg-11 col-sm-10 no-margin">
	                        <div class="comment-body">
	                          <div class="meta-info">
	                            <div class="author inline">
	                              <a href="${review.getEmail()}" class="bold"><c:out value="${review.getName()}"/></a>
	                            </div>
	                            <div class="star-holder inline">
	                              <div class="star" data-score="${review.getJudgement()}"></div>
	                            </div>
	                            <div class="date inline pull-right">
	                              ---
	                            </div>
	                          </div><!-- 	                          /.meta-info -->
	                          <p class="comment-text">
	                            <c:out value="${review.getReview()}"/>
	                          </p><!-- 	                          /.comment-text -->
	                        </div><!-- 	                        /.comment-body -->
	                      </div><!-- 	                      /.col -->
	                    </div><!-- 	                    /.row -->
	                  </div><!-- 	                  /.comment-item -->
                  </c:forEach>
   
                </div>
                <!-- /.comments -->
                <div class="add-review row">
                  <div class="col-sm-8 col-xs-12">
                    <div class="new-review-form">
                      <h2>Добавьте отзыв:</h2>
                      <c:set  var="name" value="user" />
  					  <c:set var="currentUser" value="${sessionScope[name]}"></c:set>
  					  
                      <form action="ShowOne" id="contact-form" class="contact-form" method="post" >
                        <input type="hidden"  name="id" value="${requestScope.currentBrakFluid.getId()}"> 
                        <div class="row field-row">
                          <div class="col-xs-12 col-sm-6">
                            <label>Имя*</label>
                            <input class="le-input" name="userLogin" value="${currentUser.getLogin()}">
                          </div>
                          <div class="col-xs-12 col-sm-6">
                            <label>e-mail*</label>
                            <input class="le-input" name="userEmail" value="${currentUser.getEmail()}">
                          </div>
                        </div>
                        <!-- /.field-row -->
                        <div class="field-row star-row">
                          <label>Ваша оценка:</label>
                          <div class="star-holder">
                            <div class="star big" data-score="0" ></div>
                          </div>
                        </div>
                        <!-- /.field-row -->
                        <div class="field-row">
                          <label>Ваш отзыв:</label>
                          <textarea rows="8" class="le-input" name="userReviw"></textarea>
                        </div>
                        <!-- /.field-row -->
                        <div class="buttons-holder">
                          <button type="submit" class="le-button huge">Сохранить</button>
                        </div>
                        <!-- /.buttons-holder -->
                      </form>
                      <!-- /.contact-form -->
                    </div>
                    <!-- /.new-review-form -->
                  </div>
                  <!-- /.col -->
                </div>
                <!-- /.add-review -->
              </div>
              <!-- /.tab-pane #reviews -->
            </div>
            <!-- /.tab-content -->
          </div>
          <!-- /.tab-holder -->
        </div>
        <!-- /.container -->
      </section>
      <!-- /#single-product-tab -->
      <!-- ========================================= SINGLE PRODUCT TAB : END ========================================= -->

	<!-- ============================================================= FOOTER ============================================================= -->
      <footer id="footer" class="color-bg">
         <!-- /.link-list-row -->
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

