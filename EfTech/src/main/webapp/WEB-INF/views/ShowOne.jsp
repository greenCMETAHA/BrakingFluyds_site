<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

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
              <div class="availability"><label>Доступно:</label> 
                <c:if test="${current.getInStock()>0}">
                		<span class="available"> на складе</span>
                </c:if>
                <c:if test="${current.getInStock()==0}">
                		<span class="not-available"> нет в наличии</span>
                </c:if>
              </div>
              
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
              		<div class="price-current">$<c:out value="${requestScope.currentBrakFluid.getPriceWithDiscount()}"/></div>
                	<div class="price-prev">$<c:out value="${requestScope.currentBrakFluid.getPrice()}"/></div>
                </sec:authorize>
           		<sec:authorize access="!hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
           			<div class="price-current">$<c:out value="${requestScope.currentBrakFluid.getPriceWithDiscount()}"/></div>
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
              <sec:authorize access="hasRole('ROLE_PRICE')">
              	<li><a href="#prices" data-toggle="tab">История цены (<c:out value="${requestScope.prices.size()}"/>)</a></li>
              </sec:authorize>
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
              
              <!-- /.tab-pane #description													!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! --> 
              <div class="tab-pane" id="prices">
                <!-- ========================================= CONTENT ========================================= -->
		        <div >
		            <c:forEach var="price" items="${requestScope.prices}">
			            <div class="row no-margin">
			              <div class="col-xs-12 col-sm-4 no-margin">
			                <c:out value="${price.showDate()}"/>
			              </div>
			              <div class="col-xs-12 col-sm-4 ">
			                <c:out value="${price.getUser().getName()}"/>
			              </div>
			              <div class="col-xs-12 col-sm-2 ">
			                <c:out value="${price.getPrice()}"/>
			              </div>
			            </div>
			       	</c:forEach>
		        </div>
		        <!-- ========================================= CONTENT : END ========================================= -->
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
