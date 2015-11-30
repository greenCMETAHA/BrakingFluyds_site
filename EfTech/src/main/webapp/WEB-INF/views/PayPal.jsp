<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

    <div id="single-product">
    
      <c:if test="${requestScope.variant=='Error'}">
		  <main id="faq" class="inner">
		        <div class="container">
		          <div class="row">
		            <div class="col-md-8 center-block">
		              <div class="info-404 text-center">
		                <h3 class="primary-color inner-bottom-xs"><c:out value="Невозможно произвести оплату"/></h3>
		                <p class="lead"><c:out value="${requestScope.errMessage}"/></p>
		                <div class="sub-form-row inner-top-xs inner-bottom-xs">
		                </div>
		              </div>
		            </div>
		          </div>
		        </div>
		        <div class="text-center">
        			<a href="index" class="btn-lg huge"><i class="fa fa-home"></i> Перейти на главную страницу</a>
      			</div>
		      </main>    
      </c:if>
	  <c:if test="${requestScope.variant=='Confirm'}">
		  <main id="faq" class="inner">
		        <div class="container">
		          <div class="row">
		            <div class="col-md-8 center-block">
		              <div class="info-404 text-center">
		                <h4 class="primary-color inner-bottom-xs"><c:out value="Оплата произведена"/></h4>
		                <p class="lead"><c:out value="Спасибо, что выбрали наш магазин"/></p>
		                <div class="sub-form-row inner-top-xs inner-bottom-xs">
		                </div>
		              </div>
		            </div>
		          </div>
		        </div>
		        <div class="text-center">
        			<a href="index" class="btn-lg huge"><i class="fa fa-home"></i> Перейти на главную страницу</a>
      			</div>
		      </main>    
      </c:if>	      	
    
      <c:if test="${requestScope.variant=='New'}">
        <div class="container">
        <h1 class="border">Введить данные для оплаты кредитной картой:</h1>
    	  <form action="PayPal" method="POST">
    	  	<input name="variant" type="hidden" value="${requestScope.variant}" >
    	  	
    	  	<h4 class="border">Данные владельца:</h4><br> 
            <div class="no-margin col-xs-12 col-sm-7 body-holder">
              <ul class="tabled-data">
                 <li>
                   <label>Фамилия:</label>
                   <div class="value"><input type="text" class="input" value="${requestScope.lastName}" name="lastName" />
					</div>
                 </li>
                 <li>
                   <label>Имя:</label>
                   <div class="value"><input type="text" class="input" value="${requestScope.firstName}" name="firstName" />
				   </div>
                 </li>
                 <li>
                   <label>Город:</label>
                   <div class="value"><input type="text" class="input" value="${requestScope.city}" name="city" />
				   </div>
                 </li>
                 <li>
                   <label>Адрес:</label>
                   <div class="value"><input type="text" class="input" value="${requestScope.address}" name="address" />
				   </div>
                 </li>
                 <li>
                   <label>Код страны:</label>
                   <div class="value"><input type="text" class="input" value="${requestScope.countryCode}" name="countryCode" />
				   </div>
                 </li>	                 
                 <li>
                   <label>Код штата:</label>
                   <div class="value"><input type="text" class="input" value="${requestScope.stateCode}" name="stateCode" />
				   </div>
                 </li>	                 
                 <li>
                   <label>Почтовый индекс:</label>
                   <div class="value"><input type="text" class="input" value="${requestScope.zip}" name="zip" />
				   </div>
                 </li>	                 
              </ul>
              <br><br><br>
            <h4 class="border">Данные кредитной карты:</h4><br>
              <ul class="tabled-data">
                 <li>
                   <label>Вид карты:</label>
                   <div class="value">
                   		<select size="1" name="card"  class="le-select">
 							<option >Выберите вид карты</option>
 							<option selected value="Visa">Visa</option>
 						</select>
			            <div class="col-xs-12 col-sm-6 no-margin">
			                <div class="payment-methods ">
			                    <ul>
			                        <li><img alt="" src="resources/Ecommerce/assets/images/payments/payment-visa.png"></li>
<!-- 			                        <li><img alt="" src="resources/Ecommerce/assets/images/payments/payment-master.png"></li> -->
<!-- 			                        <li><img alt="" src="resources/Ecommerce/assets/images/payments/payment-paypal.png"></li> -->
<!-- 			                        <li><img alt="" src="resources/Ecommerce/assets/images/payments/payment-skrill.png"></li> -->
			                    </ul>
			                </div><!-- /.payment-methods -->
			            </div>
				   </div>
                 </li>
                 <li>
                   <label>Номер карты:</label>
                   <div class="value"><input type="text" class="input" value="${requestScope.cardNumber}" name="cardNumber" />
				   </div>
                 </li>	   
                 <li>
                   <label>Дата карты:</label>
                   <div class="value">
                   		<input type="text" class="input" value="${requestScope.cardMonth}" name="cardMonth" />
                   		<input type="text" class="input" value="${requestScope.cardYear}" name="cardYear" />
					</div>
                 </li>
                 <div class="col-xs-12 col-sm-7 no-margin">
		            <div class="buttons-holder">
		               <button class="le-button" type="submit" name="task" value="PayPal">Оплатить</button>&nbsp&nbsp&nbsp
		               <button class="le-button" type="submit" name="task" value="Basket">К корзине</button>&nbsp&nbsp&nbsp
<!-- 		               <button class="le-button" type="submit" name="task" value="home">На главную</button> -->
		            </div>
			     </div>
                 
              </ul>
           </div>
			
		</form>
      	</div>
       </c:if>
    </div>