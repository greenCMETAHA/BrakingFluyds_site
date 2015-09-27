<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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

	    <title>MediaCenter - Responsive eCommerce Template</title>

	    <!-- Bootstrap Core CSS -->
	    <link rel="stylesheet" href="resources/Ecommerce/assets/css/bootstrap.min.css">
	    
	    <!-- Customizable CSS -->
	    <link rel="stylesheet" href="resources/Ecommerce/assets/css/main.css">
	    <link rel="stylesheet" href="resources/Ecommerce/assets/css/navy.css">
	    <link rel="stylesheet" href="resources/Ecommerce/assets/css/owl.carousel.css">
		<link rel="stylesheet" href="resources/Ecommerce/assets/css/owl.transitions.css">
		<link rel="stylesheet" href="resources/Ecommerce/assets/css/animate.min.css">

<!-- 		<link rel="stylesheet" href="resources/Ecommerce/assets/css/config.css">  Demo Purpose Only. Should be removed in production -->
<!-- 		<link href="resources/Ecommerce/assets/css/navy.css" rel="alternate stylesheet" title="Navy color"> -->

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
                <li><a href="index.html">В начало</a></li>
                 <!-- <li><a href="contact.html">Contact</a></li>  -->
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#pages">Страницы</a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="index">В начало</a></li>
                        <li><a href="home">Список товаров</a></li>
                        <li><a href="Basketl">Корзина</a></li>
                        <li><a href="About">О нас</a></li>
                        <li><a href="Download">Загрузить данные из Excel</a></li>
                        <li><a href="Comparison">Сравнить товары</a></li>
                        <li><a href="BossinessOffer">Bussiness offer</a></li>
                        <li><a href="login">Авторизация</a></li>
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
                <li><a href="login">Авторизация</a></li>
            </ul>
        </div><!-- /.col -->
    </div><!-- /.container -->
</nav><!-- /.top-bar -->
<!-- ============================================================= TOP NAVIGATION : END ============================================================= -->		<!-- ============================================================= HEADER ============================================================= -->
<header>
	<div class="container no-padding">
		
		<div class="col-xs-12 col-sm-12 col-md-3 logo-holder">
			<!-- ============================================================= LOGO ============================================================= -->
<div class="logo">
	<a href="index">
		<img alt="logo" src="resources/Ecommerce/assets/images/logo.png" width="233" height="54"/>
	</a>
</div><!-- /.logo -->
<!-- ============================================================= LOGO : END ============================================================= -->		</div><!-- /.logo-holder -->

		<div class="col-xs-12 col-sm-12 col-md-6 top-search-holder no-margin">
			<div class="contact-row">
    <div class="phone inline">
        <i class="fa fa-phone"></i> (+800) 123 456 7890
    </div>
    <div class="contact inline">
        <i class="fa fa-envelope"></i> contact@<span class="le-color">oursupport.com</span>
    </div>
</div><!-- /.contact-row -->
<!-- ============================================================= SEARCH AREA ============================================================= -->
<div class="search-area">
    <form>
        <div class="control-group">
            <input class="search-field" placeholder="Search for item" />

            <ul class="categories-filter animate-dropdown">
                <li class="dropdown">

                    <a class="dropdown-toggle"  data-toggle="dropdown" href="category-grid.html">all categories</a>

                    <ul class="dropdown-menu" role="menu" >
                        <li role="presentation"><a role="menuitem" tabindex="-1" href="category-grid.html">laptops</a></li>
                        <li role="presentation"><a role="menuitem" tabindex="-1" href="category-grid.html">tv & audio</a></li>
                        <li role="presentation"><a role="menuitem" tabindex="-1" href="category-grid.html">gadgets</a></li>
                        <li role="presentation"><a role="menuitem" tabindex="-1" href="category-grid.html">cameras</a></li>

                    </ul>
                </li>
            </ul>

            <a class="search-button" href="#" ></a>    

        </div>
    </form>
</div><!-- /.search-area -->
<!-- ============================================================= SEARCH AREA : END ============================================================= -->		</div><!-- /.top-search-holder -->

		<div class="col-xs-12 col-sm-12 col-md-3 top-cart-row no-margin">
			<div class="top-cart-row-container">
    <div class="wishlist-compare-holder">
        <div class="wishlist ">
            <a href="#"><i class="fa fa-heart"></i> wishlist <span class="value">(21)</span> </a>
        </div>
        <div class="compare">
            <a href="#"><i class="fa fa-exchange"></i> compare <span class="value">(2)</span> </a>
        </div>
    </div>

    <!-- ============================================================= SHOPPING CART DROPDOWN ============================================================= -->
    <div class="top-cart-holder dropdown animate-dropdown">
        
        <div class="basket">
            
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                <div class="basket-item-count">
                    <span class="count">3</span>
                    <img src="assets/images/icon-cart.png" alt="" />
                </div>

                <div class="total-price-basket"> 
                    <span class="lbl">your cart:</span>
                    <span class="total-price">
                        <span class="sign">$</span><span class="value">3219,00</span>
                    </span>
                </div>
            </a>

            <ul class="dropdown-menu">
                <li>
                    <div class="basket-item">
                        <div class="row">
                            <div class="col-xs-4 col-sm-4 no-margin text-center">
                                <div class="thumb">
                                    <img alt="" src="assets/images/products/product-small-01.jpg" />
                                </div>
                            </div>
                            <div class="col-xs-8 col-sm-8 no-margin">
                                <div class="title">Blueberry</div>
                                <div class="price">$270.00</div>
                            </div>
                        </div>
                        <a class="close-btn" href="#"></a>
                    </div>
                </li>

                <li>
                    <div class="basket-item">
                        <div class="row">
                            <div class="col-xs-4 col-sm-4 no-margin text-center">
                                <div class="thumb">
                                    <img alt="" src="assets/images/products/product-small-01.jpg" />
                                </div>
                            </div>
                            <div class="col-xs-8 col-sm-8 no-margin">
                                <div class="title">Blueberry</div>
                                <div class="price">$270.00</div>
                            </div>
                        </div>
                        <a class="close-btn" href="#"></a>
                    </div>
                </li>

                <li>
                    <div class="basket-item">
                        <div class="row">
                            <div class="col-xs-4 col-sm-4 no-margin text-center">
                                <div class="thumb">
                                    <img alt="" src="assets/images/products/product-small-01.jpg" />
                                </div>
                            </div>
                            <div class="col-xs-8 col-sm-8 no-margin">
                                <div class="title">Blueberry</div>
                                <div class="price">$270.00</div>
                            </div>
                        </div>
                        <a class="close-btn" href="#"></a>
                    </div>
                </li>


                <li class="checkout">
                    <div class="basket-item">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <a href="cart.html" class="le-button inverse">View cart</a>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <a href="checkout.html" class="le-button">Checkout</a>
                            </div>
                        </div>
                    </div>
                </li>

            </ul>
        </div><!-- /.basket -->
    </div><!-- /.top-cart-holder -->
</div><!-- /.top-cart-row-container -->
<!-- ============================================================= SHOPPING CART DROPDOWN : END ============================================================= -->		</div><!-- /.top-cart-row -->

	</div><!-- /.container -->
</header>



</body>
</html>