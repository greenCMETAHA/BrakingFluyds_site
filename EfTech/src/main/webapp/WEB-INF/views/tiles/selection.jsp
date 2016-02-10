<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>


 <!--    <style type="text/css">
		.dropdown-menu:hover {
		  display: block;
		}
    </style>
 -->

          <!-- ========================================= SIDEBAR ========================================= -->
          <div class="col-xs-12 col-sm-3 no-margin sidebar narrow">
            <!-- ========================================= PRODUCT FILTER ========================================= -->
            <form action="home" method="POST">
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
                  <c:set var="manufacturersFilterSize" value="${requestScope.manufacturersFilter.size()}"/>
                  <c:if test="${(manufacturersFilterSize-isSearch)>0}">
		            <div class="side-menu animate-dropdown" tabindex="-1" >
		              <nav class="yamm megamenu-horizontal" role="navigation" >
		                <ul class="nav">
		                  <li class="dropdown menu-item">
		                    <a href=""  class="dropdown-toggle" data-toggle="dropdown">Ещё <c:out value="${requestScope.manufacturersFilter.size()-isSearch}"/>  </a>
		                    <ul class="greenCMmenu dropdown-menu  mega-menu">
		                      <li class="yamm-content">
		                      	<div class="row">
		                            <div class="col-md-4">
		                                <ul class="list-unstyled">
		                                	<c:forEach var="currentManufacturer" items="${requestScope.manufacturersFilter}">
		                                   		<li>
													<c:if test="${!currentManufacturer.isSelected()}">
                										<input type="checkbox" name="selections" value="${currentManufacturer.getId()}" class="le-checkbox"  />
                										<label><c:out value="${currentManufacturer.getName()}"  /> </label>
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
	                  <h2>Класс жидкости:</h2>
	                  <hr>
	                  <c:set var="isSearch" value="${0}"/>
	                  <ul>
	                  	<c:forEach var="currentFluidClass" items="${requestScope.fluidClassFilter}">
	                  		<li>
	                  			<c:if test="${currentFluidClass.isSelected()}">
	                  				<input checked="checked" type="checkbox" name="fluidClassselections" value="${currentFluidClass.getId()}" class="le-checkbox"  />
	                  				<label><c:out value="${currentFluidClass.getName()}"  /> 
	      							<c:set var="isSearch" value="${isSearch+1}"/> 
	                  			</c:if>
<%-- 	                  			<c:if test="${!currentFluidClass.isSelected()}"> --%>
<%-- 	                  				<input type="checkbox" name="fluidClassselections" value="${currentFluidClass.getId()}" class="le-checkbox"  /> --%>
<%-- 	                  			</c:if> --%>
	                  		</li>
	                  	</c:forEach>
	                  </ul>
	                  <c:if test="${isSearch==0}">
                  		По всем
                  	  </c:if>
                  	   <!-- ================================== TOP NAVIGATION ================================== -->
                  	   <c:set var="fluidClassFilterSize" value="${requestScope.fluidClassFilter.size()}"/>
                  	   <c:if test="${(fluidClassFilterSize-isSearch)>0}">
		            <div class="side-menu animate-dropdown" tabindex="-1" >
		              <nav class="yamm megamenu-horizontal" role="navigation" >
		                <ul class="nav">
		                  <li class="dropdown menu-item">
		                    <a href=""  class="dropdown-toggle" data-toggle="dropdown">Ещё <c:out value="${requestScope.fluidClassFilter.size()-isSearch}"/>  </a>
		                    <ul class="dropdown-menu mega-menu">
		                      <li class="yamm-content">
		                      	<div class="row">
		                            <div class="col-md-4">
		                                <ul class="list-unstyled">
		                                	<c:forEach var="currentfluidClassFilter" items="${requestScope.fluidClassFilter}">
		                                   		<li>
													<c:if test="${!currentfluidClassFilter.isSelected()}">
                										<input type="checkbox" name="fluidClassselections" value="${currentfluidClassFilter.getId()}" class="le-checkbox"  />
                										<label><c:out value="${currentfluidClassFilter.getName()}"  /> </label>
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
                
                 <div class="price-filter">
	                  <h2>Температура кипения (сухая)</h2>
	                  <hr>
	                  <div class="price-range-holder">
<%-- 	                  	<c:set  var="name" value="currentBoilingTemperatureDryFilter" /> --%>
						<input id="ex3" type="text" class="price-slider" value="" name="currentBoilingTemperatureDryFilter"
							data-slider-min="${requestScope.MinBoilingTemperatureDry}" data-slider-max="${requestScope.MaxBoilingTemperatureDry}" 
							data-slider-step="100" data-slider-value="[${requestScope.currentMinBoilingTemperatureDryFilter},${requestScope.currentMaxBoilingTemperatureDryFilter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinBoilingTemperatureDry}"/> до <c:out value="${requestScope.MaxBoilingTemperatureDry}"/> градусов
	                    </span>
	                  </div>
	                </div>
	                
	               <div class="price-filter">
	                  <h2>Температура кипения (влажная)</h2>
	                  <hr>
	                  <div class="price-range-holder">
<%-- 	                  	<c:set  var="name" value="currentBoilingTemperatureWetFilter" /> --%>
						<input id="ex4" type="text" class="price-slider" value="" name="currentBoilingTemperatureWetFilter"
							data-slider-min="${requestScope.MinBoilingTemperatureWet}" data-slider-max="${requestScope.MaxBoilingTemperatureWet}" 
							data-slider-step="100" data-slider-value="[${requestScope.currentMinBoilingTemperatureWetFilter},${requestScope.currentMaxBoilingTemperatureWetFilter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinBoilingTemperatureWet}"/> до <c:out value="${requestScope.MaxBoilingTemperatureWet}"/> градусов
	                    </span>
	                  </div>
	                </div>
	                
	                <div class="price-filter">
	                  <h2>Объём</h2>
	                  <hr>
	                  <div class="price-range-holder">
<%-- 	                  	<c:set  var="name" value="currentValueFilter" /> --%>
						<input id="ex5" type="text" class="price-slider" value="" name="currentValueFilter" 
							data-slider-min="${requestScope.MinValue}" data-slider-max="${requestScope.MaxValue}" 
							data-slider-step="100" data-slider-value="[${requestScope.currentMinValueFilter},${requestScope.currentMaxValueFilter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinValue}"/> до <c:out value="${requestScope.MaxValue}"/> мл.
	                    </span>
	                  </div>
	                </div>
                
                 <div class="price-filter">
	                  <h2>Вязкость (при -40 градусах)</h2>
	                  <hr>
	                  <div class="price-range-holder">
<%-- 	                  	<c:set  var="name" value="currentViscosity40Filter" /> --%>
						<input id="ex6" type="text" class="price-slider" value="" name="currentViscosity40Filter"
							data-slider-min="${requestScope.MinViscosity40}" data-slider-max="${requestScope.MaxViscosity40}" 
							data-slider-step="50" data-slider-value="[${requestScope.currentMinViscosity40Filter},${requestScope.currentMaxViscosity40Filter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinViscosity40}"/> до <c:out value="${requestScope.MaxViscosity40}"/>
	                    </span>
	                  </div>
	                </div>
	                
	                <div class="price-filter">
	                  <h2>Вязкость (при 100 градусах)</h2>
	                  <hr>
	                  <div class="price-range-holder">
	                  	<c:set  var="name" value="currentViscosity100Filter" />
						<input id="ex7" type="text" class="price-slider" value="" name="currentViscosity100Filter"
							data-slider-min="${requestScope.MinViscosity100}" data-slider-max="${requestScope.MaxViscosity100}" 
							data-slider-step="50" data-slider-value="[${requestScope.currentMinViscosity100Filter},${requestScope.currentMaxViscosity100Filter}]"/>
	                    <span class="min-max">
	                    От <c:out value="${requestScope.MinViscosity100}"/> до <c:out value="${requestScope.MaxViscosity100}"/>
	                    </span>
	                  </div>
	                </div>
	                
	                
<!-- 	                 <div class="price-filter"> -->
<!-- 	                  <h2>Оценка пользователей</h2> -->
<!-- 	                  <hr> -->
<%-- 	                  <c:out value="${requestScope.currentJudgementFilter}"></c:out> --%>
<!-- 	                  <div class="price-range-holder"> -->
<%-- 							<c:set  var="name" value="currentJudgementFilter" /> --%>
<!-- 						<input id="ex8" type="text" class="price-slider" value="" name="currentJudgementFilter" -->
<%-- 							data-slider-min="${requestScope.MinJudgement}" data-slider-max="${requestScope.MaxJudgement}"  --%>
<%-- 							data-slider-step="1" data-slider-value="[${requestScope.currentMinJudgementFilter},${requestScope.currentMaxJudgementFilter}]"/> --%>
<!-- 	                    <span class="min-max"> -->
<%-- 	                    От <c:out value="${requestScope.MinJudgement}"/> до <c:out value="${requestScope.MaxJudgement}"/> --%>
<!-- 	                    </span> -->
<!-- 	                  </div> -->
<!-- 	                </div> -->
                
                
              </div>
              <!-- /.body -->
              <span class="filter-button">
                   	<button class="le-button" type="submit" name="sss">Отбор</button>	
               </span>
              
            </div>
            <!-- /.widget -->
            </form>
            <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')">
	           <form action="InsertUpdate?${_csrf.parameterName}=${_csrf.token}" method="POST"  enctype="multipart/form-data">
	           		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            		<input name="variant" type="hidden" value="New" >
		            <div class="widget">
		            	<div class="body bordered">
			            	<div class="buttons-holder">
			            	  <button class="le-button" type="submit" name="sss">Новый товар</button>	
<!-- 			                  <a class="le-button big" href="InsertUpdateMotorOil?variant=New" >Новый товар</a> -->
			                </div>
		            	</div>
		            </div>
	            </form>
            </sec:authorize>            
                        
            <!-- ========================================= PRODUCT FILTER : END ========================================= -->
            
          </div>
          <!-- ========================================= CONTENT ========================================= -->
          <link href="resources/greenCMmenu.css" rel="stylesheet" type="text/css"/>