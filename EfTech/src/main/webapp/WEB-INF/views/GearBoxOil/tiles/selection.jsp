<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

          <!-- ========================================= SIDEBAR ========================================= -->
          <div class="col-xs-12 col-sm-3 no-margin sidebar narrow">
            <!-- ========================================= PRODUCT FILTER ========================================= -->
            <form action="gearBoxOil" method="POST">
            	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
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
                  <c:set var="filterSize" value="${requestScope.manufacturersFilter.size()}"/>
                  <c:if test="${(filterSize-isSearch)>0}">
		            <div class="side-menu animate-dropdown" tabindex="-1" >
		              <nav class="yamm megamenu-horizontal" role="navigation" >
		                <ul class="nav">
		                  <li class="greenCMmenu dropdown menu-item">
		                    <a href=""  class="dropdown-toggle" data-toggle="dropdown">Ещё <c:out value="${requestScope.manufacturersFilter.size()-isSearch}"/>  </a>
		                    <ul class="mega-menu dropdown-menu  greenCMmenu">
		                      <li class="yamm-content">
		                      	<div class="row">
		                            <div class="col-md-4">
		                                <ul class="list-unstyled">
		                                	<c:forEach var="current" items="${requestScope.manufacturersFilter}">
		                                   		<li>
													<c:if test="${!current.isSelected()}">
                										<input type="checkbox" name="selections" value="${current.getId()}" class="le-checkbox"  />
                										<label><c:out value="${current.getName()}"  /> </label>
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
                  <h2>Тип КПП:</h2>
                  <hr>
                  <ul>
                  	<c:forEach var="current" items="${requestScope.gearBoxTypeFilter}">
                  		<li>
                  			<c:if test="${current.isSelected()}">
	               				<input checked="checked" type="checkbox" name="gearBoxTypeSelections" value="${current.getId()}" class="le-checkbox"  />
    	           				<label><c:out value="${current.getName()}"  /> </label>
    	           			</c:if>
                  			<c:if test="${!current.isSelected()}">
	               				<input type="checkbox" name="gearBoxTypeSelections" value="${current.getId()}" class="le-checkbox"  />
    	           				<label><c:out value="${current.getName()}"  /> </label>
    	           			</c:if>
                  		</li>
                  	</c:forEach>
                  </ul>
                </div>
                <!-- /.category-filter -->   
                
                <div class="category-filter">
                  <h2>Состав масла:</h2>
                  <hr>
                  <ul>
                  	<c:forEach var="current" items="${requestScope.oilStuffFilter}">
                  		<li>
                  			<c:if test="${current.isSelected()}">
                  				<input checked="checked" type="checkbox" name="oilStuffSelections" value="${current.getId()}" class="le-checkbox"  />
                  				<label><c:out value="${current.getName()}"  /> </label>
                  				<c:set var="isSearch" value="${isSearch+1}"/>
                  			</c:if>
							<c:if test="${!current.isSelected()}">
                  				<input type="checkbox" name="oilStuffSelections" value="${current.getId()}" class="le-checkbox"  />
                  				<label><c:out value="${current.getName()}"  /> </label>
                  				<c:set var="isSearch" value="${isSearch+1}"/>
                  			</c:if>                  			
                  		</li>
                  	</c:forEach>
                  </ul>
                </div>
                <!-- /.category-filter -->                
                             
                <div class="category-filter">
                  <h2>Вязкость:</h2>
                  <hr>
                  <c:set var="numValue" value="${0}"/>
                  <ul>
                  	<c:forEach var="current" items="${requestScope.viscosityFilter}">
                  		<li>
                  			<c:if test="${current.getValue()}">
                  				<input checked="checked" type="checkbox" name="viscositySelections" value="${numValue}" class="le-checkbox"  />
                  				<label><c:out value="${current.getKey()}"  /> </label>
                  			</c:if>
                  			<c:if test="${!current.getValue()}">
                  				<input type="checkbox" name="viscositySelections" value="${numValue}" class="le-checkbox"  />
                  				<label><c:out value="${current.getKey()}"  /> </label>
                  			</c:if>
                  		</li>
                  		<c:set var="numValue" value="${numValue+1}"/>
                  	</c:forEach>
                  </ul>
                </div>
                <!-- /.category-filter -->                    
                
<%--                <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_DISTR','ROLE_OFFERPRICE','ROLE_PRICE')"> --%>
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
<%--                  </sec:authorize> --%>
                
 	                <div class="price-filter">
	                  <h2>Объём</h2>
	                  <hr>
	                  <div class="price-range-holder">
						<input id="ex5" type="text" class="price-slider" value="" name="currentValueFilter" 
							data-slider-min="${requestScope.MinValue}" data-slider-max="${requestScope.MaxValue}" 
							data-slider-step="100" data-slider-value="[${requestScope.currentMinValueFilter},${requestScope.currentMaxValueFilter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinValue}"/> до <c:out value="${requestScope.MaxValue}"/> мл.
	                    </span>
	                  </div>
	                </div>
              </div>
              <!-- /.body -->
              <span class="filter-button">
                   	<button class="le-button" type="submit" name="sss">Отбор</button>	
               </span>
              
            </div>
            <!-- /.widget -->
            </form>
            <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')">
            	<form action="InsertUpdateGearBoxOil?${_csrf.parameterName}=${_csrf.token}" method="POST"  enctype="multipart/form-data">
            		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            		<input name="variant" type="hidden" value="New" >
		            <div class="widget">
		            	<div class="body bordered">
			            	<div class="buttons-holder">
			            	  <button class="le-button" type="submit" name="sss">Новый товар</button>	
<!-- 			                  <a class="le-button big" href="InsertUpdateGearBoxOil?variant=New" >Новый товар</a> -->
			                </div>
		            	</div>
		            </div>
	            </form>
            </sec:authorize>            
                        
            <!-- ========================================= PRODUCT FILTER : END ========================================= -->
            
          </div>
  
  		  <link href="resources/greenCMmenu.css" rel="stylesheet" type="text/css"/>