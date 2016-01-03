<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

     <div id="single-product">
        <div class="container">
    	  <form action="InsertUpdateMotorOil" method="POST"  enctype="multipart/form-data">
    	    <c:set var="currentMotorOil" value="${requestScope.currentMotorOil}"></c:set>
			<input name="id_MotorOil" type="hidden" value="${currentMotorOil.getId()}" >
			<input name="pageInfo" type="hidden" value="${requestScope.pageInfo}" >
			<c:set var="readOnly" value=""></c:set>
			<c:set var="disabledValue" value=""></c:set>
			<sec:authorize access="!hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')">
				<c:set var="readOnly" value="readonly"></c:set>
				<c:set var="disabledValue" value="disabled"></c:set>
			</sec:authorize>
			
			<h1 class="border"><c:out value="${requestScope.pageInfo}"/></h1>
           
			
 	
            <div class="no-margin col-xs-12 col-sm-7 body-holder">
              <ul class="tabled-data">
	            	<li>
	                   <label></label>
	                   <div class="value">
			                <div class="star-holder inline">
			                  <div class="star" data-score="${currentMotorOil.getJudgement()}" id="Judgement" ></div>
			                </div>
		                </div>
	                 </li>
	                 <li>
	                   <label>Наименование:</label>
	                   <div class="value">
	                   		<input <c:out value="${readOnly}"/> type="text" class="input" value="${currentMotorOil.getName()}" name="name_MotorOil" />
						</div>
	                 </li>
	                 <li>
	                   <label>Наименование/код по производителю:</label>
	                   <div class="value">
	                   		<input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getManufacturerCode()}" name="name_BrakeFluid" />
						</div>
	                 </li>
	           	  	 <li>
	                   <label>Описание:</label>
	                   <div class="value">
	                   		<textarea name="Description"   <c:out value="${readOnly}"/>  
	                   			cols="75" rows="7" class="textarea" ><c:out value="${currentMotorOil.getDescription().trim()}"  /></textarea>
					   </div>
	                 </li>
	                 <li>
	                   <label>Цена:</label>
	                   <div class="value">
	                   		<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRICE')">
	                   			<input type="text" class="input" value="${currentMotorOil.getPrice()}" name="Price" />
	                   		</sec:authorize>
	                   		<input name="photoBackUp" type="hidden" value="${requestScope.photoBackUp}" >
					   </div>
	                 </li>
	                 <li>
	                   <label>Скидка:</label>
	                   <div class="value">
	                   		<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRICE')">
	                   			<input type="text" class="input" value="${currentMotorOil.getDiscount()}" name="Discount" />
	                   		</sec:authorize>
					   </div>
	                 </li>
	                 <li>
	                   <label>Количество на складе:</label>
	                   <div class="value">
	                   		<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRICE')">
	                   			<input type="text" class="input" value="${currentMotorOil.getInStock()}" name="InStock" />
	                   		</sec:authorize>
					   </div>
	                 </li>
	                 
	        	 </ul>
	           </div>
	            <div class="col-xs-12 col-sm-3 no-margin">
		         <div class="buttons-holder">
		             <button class="le-button" type="submit" name="variant" value="Save">Сохранить</button>
		         </div>
		     </div>
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
              <li><a href="#image" data-toggle="tab">Изображение</a></li>
              <sec:authorize access="hasRole('ROLE_PRICE')">
              	<li><a href="#prices" data-toggle="tab">История цены (<c:out value="${requestScope.prices.size()}"/>)</a></li>
              </sec:authorize>
            </ul>
            <!-- /.nav-tabs -->
            <div class="tab-content">
	            <div class="tab-pane active" id="description">
		            <ul class="tabled-data">
	                  <li>
	                    <label>Производитель:</label>
	                    <div class="value">
	                    	<select size="1" name="Manufacturer"  <c:out value="${disabledValue}"/> class="le-select">
			                    <option >Выберите производителя</option>	
									<c:forEach var="punct" items="${requestScope.combobox_Manufacturers}">
									<c:if test="${currentMotorOil.getManufacturer().getName() != punct.getName()}">
										<option value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
									</c:if>
									<c:if test="${currentMotorOil.getManufacturer().getName() == punct.getName()}">
										<option selected value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
									</c:if>
								</c:forEach>
			                </select>
						</div>
	                  </li>
	                  <li>
	                    <label>Тип двигателя:</label>
	                    <div class="value">
	                    	<select size="1" name="EngineType"  <c:out value="${disabledValue}"/> class="le-select">
			                    <option >Выберите тип двигателя</option>	
									<c:forEach var="punct" items="${requestScope.combobox_EngineType}">
									<c:if test="${currentMotorOil.getEngineType().getName() != punct.getName()}">
										<option value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
									</c:if>
									<c:if test="${currentMotorOil.getEngineType().getName() == punct.getName()}">
										<option selected value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
									</c:if>
								</c:forEach>
			                </select>
						</div>
	                  </li>
	                   <li>
	                    <label>Тип масла:</label>
	                    <div class="value">
	                    	<select size="1" name="OilStuff"  <c:out value="${disabledValue}"/> class="le-select">
			                    <option >Выберите тип масла</option>	
									<c:forEach var="punct" items="${requestScope.combobox_OilStuff}">
									<c:if test="${currentMotorOil.getOilStuff().getName() != punct.getName()}">
										<option value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
									</c:if>
									<c:if test="${currentMotorOil.getOilStuff().getName() == punct.getName()}">
										<option selected value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
									</c:if>
								</c:forEach>
			                </select>
						</div>
	                  </li>
	                  <li>
	                    <label>Вязкость:</label>
	                    <div class="value">
	                    	<input  <c:out value="${readOnly}"/> type="text" value="${currentMotorOil.getViscosity()}" name="Viscosity" />
						</div>
	                  </li>
	            	  <li>
	                    <label>Объём:</label>
	                    <div class="value">
	                    	<input  <c:out value="${readOnly}"/> type="text" value="${currentMotorOil.getValue()}" name="Value" />
						</div>
	                  </li>
	         		</ul>
	              </div>
	              
	              <div class="tab-pane" id="additional-info">
		            <ul class="tabled-data">
	                  <li>
	                    <label>Спецификация:</label>
	                    <div class="value">	
	                    	<div class="excerpt">
	                    		<sec:authorize access="!hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')">
				                	<div><textarea name="Specification" readonly="${readOnly}"
			                		 	cols="90" rows="7" class="textarea" ><c:out value="${currentMotorOil.getSpecification().trim()}"  /></textarea>
			                		</div>
				                </sec:authorize>
	                    		<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')">
				                	<div><textarea name="Specification"	
				                		cols="90" rows="7" class="textarea" ><c:out value="${currentMotorOil.getSpecification().trim()}"  /></textarea>
				                	</div>
				                </sec:authorize>
            			  	</div>
            			</div>
	                  </li>
	         		</ul>
	              </div>
	              <div class="tab-pane" id="image">
	                <ul class="tabled-data">
	                  <li>
		                <label></label>
		                <div class="value">	
		                	<c:if test="${currentMotorOil.hasPhoto()}">
						  		<img src="resources/jpg/<c:out value="${currentMotorOil.getPhoto()}"  />" alt="${currentMotorOil.getName()}">
					    	</c:if><p>
					    </div>
					   </li>
  	                   <li>
	                    <label>Изменить изображение:</label>
	                    <div class="value">	
				            <div>
				            	<input  type="file"  name="Photo" class="upload-file" id="file"/><p>
				            	    <button class="le-button" type="submit" name="variant" value="Refresh">Обновить</button>
		                	</div>
		                	
		                </div>
	                  </li>
	         		</ul>
	              </div>
	              <!-- /.tab-pane #description												 --> 
	              <div class="tab-pane" id="prices">
	                <!-- ========================================= CONTENT ========================================= -->
			        <div class="col-xs-20 col-md-12 items-holder no-margin">
			         	<ul class="tabled-data">
			            <c:forEach var="price" items="${requestScope.prices}">
				           	<li> <div class="row no-margin">
				              <div class="col-xs-12 col-sm-5 no-margin">
				                <c:out value="${price.getTime()}"/>
				              </div>
				              <div class="col-xs-12 col-sm-3 ">
				                <c:out value="${price.getUser().getName()}"/>
				              </div>
				              <div class="col-xs-12 col-sm-4 ">
				                <c:out value="${price.getPrice()}"/>
				              </div>
				            </div>
				            </li>
				       	</c:forEach>
				       	</ul>
			        </div>
			        <!-- ========================================= CONTENT : END ========================================= -->
	             </div>   
                <!-- /.meta-row -->
              </div>
              
              <!-- /.tab-pane #reviews -->
            </div>
            <!-- /.tab-content -->
          </div>
        </div>
        <!-- /.container -->
      </section>
      <!-- /#single-product-tab -->
    
      <!-- ========================================= SINGLE PRODUCT TAB : END ========================================= -->

		</form>
      	</div>
      </div>
