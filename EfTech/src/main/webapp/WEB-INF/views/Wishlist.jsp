<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

     <div class="main-content" id="main-content">
        <div class="row">
          <div class="col-lg-10 center-block page-wishlist style-cart-page inner-bottom-xs">
            <div class="inner-xs">
              <div class="page-header">
                <h2 class="page-title">Избранное</h2>
              </div>
            </div>
            <!-- /.section-page-title -->
            <div class="items-holder">
              <div class="container-fluid wishlist_table">
              
                <c:set var="name" value="wishlist" />
                <c:forEach var="currentWishlist" items="${sessionScope[name]}">
                    <c:set var="currentGood" value="${currentWishlist.getGood()}" />
					<div class="row cart-item cart_item" id="yith-wcwl-row-${currentGood.getId()}">
                  		<div class="col-xs-12 col-sm-1 no-margin">
                  			<c:url value="Wishlist" var="UpdateGood">
								<c:param name="id" value="${currentGood.getId()}"/>
								<c:param name="goodPrefix" value="${currentGood.getGoodName()}"/>
								<c:param name="variant" value="deleteFromWishlist"/>
							</c:url>	
                    		<a title="Удалить из избранного" class="remove_from_wishlist remove-item" href="${UpdateGood}">×</a>
                  		</div>		
						<div class="col-xs-12 col-sm-1 no-margin">
							<c:if test="${currentGood.getGoodName()=='BrF'}"> 
	                    		<a href="ShowOne?id=${currentGood.getId()}">
    		                		<img width="" height="73" alt="${currentGood.getName()}" class="attachment-shop_thumbnail wp-post-image"
            		        			 src="resources/jpg/${currentGood.getPhoto()}">
                    			</a>
                    		</c:if>	
							<c:if test="${currentGood.getGoodName()=='Oil'}"> 
	                    		<a href="ShowOneMotorOil?id=${currentGood.getId()}">
    		                		<img width="" height="73" alt="${currentGood.getName()}" class="attachment-shop_thumbnail wp-post-image"
            		        			 src="resources/jpg/${currentGood.getPhoto()}">
                    			</a>
                    		</c:if>	
							<c:if test="${currentGood.getGoodName()=='GrO'}"> 
	                    		<a href="ShowOneGearBoxOil?id=${currentGood.getId()}">
    		                		<img width="" height="73" alt="${currentGood.getName()}" class="attachment-shop_thumbnail wp-post-image"
            		        			 src="resources/jpg/${currentGood.getPhoto()}">
                    			</a>
                    		</c:if>	
                  		</div>
						<div class="col-xs-12 col-sm-4 no-margin">
		                    <div class="title">
		                    	<c:if test="${currentGood.getGoodName()=='BrF'}">
		                      		<a href="ShowOne?id=${currentGood.getId()}">${currentGood.getName()}</a>
		                      	</c:if>
		                      	<c:if test="${currentGood.getGoodName()=='Oil'}">
		                      		<a href="ShowOneMotorOil?id=${currentGood.getId()}">${currentGood.getName()}</a>
		                      	</c:if>
		                      	<c:if test="${currentGood.getGoodName()=='GrO'}">
		                      		<a href="ShowOneGearBoxOil?id=${currentGood.getId()}">${currentGood.getName()}</a>
		                      	</c:if>
		                    </div>
		                    <!-- /.title --> 
		                    <div>
		                      <span class="label label-success wishlist-in-stock">В наличии</span>
		                    </div>
		                 </div>
		                 <div class="col-xs-12 col-sm-3 no-margin">
		                 	<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
			                    <div class="price">
			                      <span class="amount">$<c:out value="${currentGood.getPrice()}" /></span>							
			                    </div>
			                </sec:authorize>
		                  </div>
						  <div class="col-xs-12 col-sm-3 no-margin">
		                    <div class="text-right">
		                      <div class="add-cart-button">
			                    <c:url value="Wishlist" var="UpdateGood">
									<c:param name="id" value="${currentGood.getId()}"/>
									<c:param name="goodPrefix" value="${currentGood.getGoodName()}"/>
									<c:param name="variant" value="inBasket"/>
								</c:url>
		                        <a class="le-button add_to_cart_button product_type_simple" href="${UpdateGood}">В корзину</a>
		                      </div>
		                    </div>
		                  </div>
		              </div> <!-- /.cart-item -->
				</c:forEach>
              </div>
              <!-- /.wishlist-table -->
            </div>
            <!-- /.items-holder -->
          </div>
          <!-- .large-->
        </div>
        <!-- .row-->	
      </div>