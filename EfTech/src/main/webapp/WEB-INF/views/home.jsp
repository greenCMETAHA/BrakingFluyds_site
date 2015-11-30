<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

      <section id="category-grid">
        <div class="container">
          <!-- ========================================= SIDEBAR ========================================= -->
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
			                      		<sec:authorize access="!hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
			                      			$<c:out value="${currentBFluid.getPriceWithDiscount()}"/>
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
                      <li class="grid-list-button-item"><a data-toggle="tab" href="#grid-view"><i class="fa fa-th-large"></i> Мозаика</a></li>
                      <li class="grid-list-button-item active"><a data-toggle="tab" href="#list-view"><i class="fa fa-th-list"></i> Список</a></li>
                    </ul>
                  </div>
                </div>
                <!-- /.control-bar -->
                <div class="tab-content">
                  <div id="grid-view" class="products-grid fade tab-pane">
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
                              <c:if test="${currentBFluid.getDiscount()>0 }">
                              		<div class="label-discount green">-<c:out value="${currentBFluid.getDiscount()}"/>% скидки</div>
                              </c:if>
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
	                              	<sec:authorize access="!hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
			                      		$<c:out value="${currentBFluid.getPriceWithDiscount()}"/>
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
                          	Товары <span><c:out value="${requestScope.paginationString_part1}"  /> </span> из <span> <c:out value="${requestScope.paginationString_part2}"  /> </span> найденных
                           
                          </div>
                        </div>
                      </div>
                      <!-- /.row -->
                    </div>
                    <!-- /.pagination-holder -->
                  </div>
                  <!-- /.products-grid #grid-view -->
                  <div id="list-view" class="products-grid fade tab-pane in active ">
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
	                              <c:if test="${currentBFluid.getDiscount()>0}">
                              		<div class="label-discount green">-<c:out value="${currentBFluid.getDiscount()}"/>% скидки</div>
                              	  </c:if>
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
		                      		<sec:authorize access="!hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')">
		                      			$<c:out value="${currentBFluid.getPriceWithDiscount()}"/>
		                      		</sec:authorize>
	                              </div>
	                              	
	                              <div class="availability"><label>В наличии:</label>
		                              <c:if test="${currentBFluid.getInStock()>0}">
		                              		<span class="available"> на складе</span>
		                              </c:if>
		                              <c:if test="${currentBFluid.getInStock()==0}">
		                              		<span class="not-available"> нет в наличии</span>
		                              </c:if>
	                              </div>
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
      <!-- /#category-grid -->