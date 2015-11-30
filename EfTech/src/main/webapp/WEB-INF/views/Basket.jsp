<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

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
	                <img class="lazy" height="73" width="" alt="${currentGood.getName()}" src="resources/jpg/${currentGood.getPhoto()}" />
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
							<c:param name="goodPrefix" value="${currentGood.getGoodName()}"/>
							<c:param name="variant" value="deleteQuantityFromBasket"/>
							<c:param name="quantity" value="${-1}"/>
						  </c:url>
	                      <a class="minus" href="${deleteFromBasket}"></a>
	                      
	                      <input name="quantity" readonly="readonly" type="text" value="${currentBasket.getQauntity()}" />   <!-- отображаем количество -->

	                	  <c:url value="Basket" var="deleteFromBasket">
							<c:param name="id" value="${currentGood.getId()}"/>
							<c:param name="goodPrefix" value="${currentGood.getGoodName()}"/>
							<c:param name="variant" value="deleteQuantityFromBasket"/>
							<c:param name="quantity" value="${1}"/>
						  </c:url>
						  <a class="plus" href="${deleteFromBasket}"></a>
	                  </div>
	              
	                
	                  </div>
	              </div>
	          	  <div class="col-xs-12 col-sm-2 no-margin">
	                <div class="price">
                  		$<c:out value="${currentGood.getPriceWithDiscount()*currentBasket.getQauntity()}" />
	                </div>
	                <c:url value="Basket" var="deleteFromBasket">
						<c:param name="id" value="${currentGood.getId()}"/>
						<c:param name="goodPrefix" value="${currentGood.getGoodName()}"/>
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
		          <form action="PayPal" method="POST"  target="_top">
		            <div class="widget cart-summary">
		              <h1 class="border">В Корзине:</h1>
		              <div class="body">
		                <ul class="tabled-data no-border inverse-bold">
		                  <li>
		                    <label>Итого:</label>
		                    <div class="value pull-right">
		 	                 	$<c:out value="${requestScope.totalBasket}" />
		                    </div>
		                  </li>
						<li>
		                    <label>Оплата:</label>
		                    <div class="value pull-right">
								<input  checked type="radio"  name="card" value="PayPal" >PayPal<br>
									<input type="radio"  name="card" value="Visa">Кредитная карта<br>
									<input type="radio"  name="card" value="Nalik">Наличные<br><br>	
								</div>
		                  </li>		                  
		                  
		                  <li>
		                    <label>Доставка:</label>
		                    <div class="value pull-right">
		                    	<select class="select" size="1" name="shipping" id="input-two">
									<option value="0">Самовывоз</option>
									<option selected="selected" value="1">Доставка</option>
				                </select>
		                    </div>
		                  </li>
		                </ul>
		                <ul id="total-price" class="tabled-data inverse-bold no-border">
		                  <li>
		                    <label>Всего:</label>
		                    <div class="value pull-right">
		                    	$<c:out value="${requestScope.totalBasket}" />
			                </div>
		                  </li>
		                 </ul>
<%-- 		                 <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')"> --%>
<!-- 			                 <ul class="tabled-data no-border inverse-bold "> -->
<!-- 				                  <li> -->
<!-- 				                    <label>Введите сумму оплаты:</label> -->
<!-- 				                    <div class="value"> -->
<%-- 					                    <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')"> --%>
<%-- 					                    	$<input type="text" class="input" name="paySumm" value="${requestScope.totalBasket}" /> --%>
<%-- 					                    </sec:authorize> --%>
<!-- 					                </div> -->
<!-- 				                  </li> -->
<!-- 				                  <li> -->
<!-- 				                    <label>Выберите клиента:</label> -->
				                    
<!-- 				                    <div class="value pull-left"> -->
<!-- 										<select class="select" size="1" name="client_id" id="input-one"> -->
<!-- 						                    <option disabled>Выберите получателя</option>	 -->
<%-- 											<c:forEach var="punct" items="${requestScope.listClients}"> --%>
<%-- 												<option value="${punct.getId()}"><c:out value="${punct.getName()}" /></option> --%>
<%-- 											</c:forEach> --%>
<!-- 						                </select> -->
<!-- 									</div> -->
<!-- 									<br> <br> -->
<!-- 				                  </li> -->
<!-- 			                </ul> -->

			                
<%-- 		                </sec:authorize> --%>
		                <div class="buttons-holder" align="left">
								<!-- Paypal Button and Instant Payment Notification (IPN) Integration with Java
								  http://codeoftheday.blogspot.com/2013/07/paypal-button-and-instant-payment_6.html	-->
								
									
									<input type="image" src="https://www.paypalobjects.com/en_US/GB/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal ? The safer, easier way to pay online.">
									<img alt="" border="0" src="https://www.paypalobjects.com/en_GB/i/scr/pixel.gif" width="1" height="1">
     		                
<!-- 		                	<button class="le-button" type="submit" name="variant" value="checkout">Оплатить</button> -->
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
 