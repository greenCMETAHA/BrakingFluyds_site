<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

      <div id="single-product">
        <div class="container">
    	  <form action="InsertUpdate" method="POST"  enctype="multipart/form-data">
    	  	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    	    <c:set var="currentBrakFluid" value="${requestScope.currentBrakFluid}"></c:set>
			<input name="id_BrakeFluid" type="hidden" value="${currentBrakFluid.getId()}" >
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
			                  <div class="star" data-score="${currentBrakFluid.getJudgement()}" id="Judgement" ></div>
			                </div>
		                </div>
	                 </li>
	                 <li>
	                   <label>Наименование:</label>
	                   <div class="value">
	                   		<input <c:out value="${readOnly}"/> type="text" class="input" value="${currentBrakFluid.getName()}" name="name_BrakeFluid" />
						</div>
	                 </li>
	           	  	 <li>
	                   <label>Описание:</label>
	                   <div class="value">
	                   		<textarea name="Description"   <c:out value="${readOnly}"/>  cols="75" rows="7" class="textarea" ><c:out value="${currentBrakFluid.getDescription()}"  /></textarea>
					   </div>
	                 </li>
	                 <li>
	                   <label>Цена:</label>
	                   <div class="value">
	                   		<sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRICE')">
	                   			<input type="text" class="input" value="${currentBrakFluid.getPrice()}" name="Price" />
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
              <li><a href="#character" data-toggle="tab">Характеристики</a></li>
              <li><a href="#additional-info" data-toggle="tab">Дополнительная информация</a></li>
              <li><a href="#image" data-toggle="tab">Изображение</a></li>
            </ul>
            <!-- /.nav-tabs -->
            <div class="tab-content">
	            <div class="tab-pane active" id="description">
		            <ul class="tabled-data">
	                  <li>
	                    <label>Производитель:</label>
	                    <div class="value">
	                    	<select size="1" name="Manufacturer"  <c:out value="${disabledValue}"/> class="le-select">
			                    <option >Выберите получателя</option>	
									<c:forEach var="punct" items="${requestScope.combobox_Manufacturers}">
									<c:if test="${currentBrakFluid.getManufacturer().getName() != punct.getName()}">
										<option value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
									</c:if>
									<c:if test="${currentBrakFluid.getManufacturer().getName() == punct.getName()}">
										<option selected value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
									</c:if>
								</c:forEach>
			                </select>
						</div>
	                  </li>
	                  <li>
	                    <label>Класс жидкости:</label>
	                    <div class="value">
	                    	<select size="1" name="FluidClass"  <c:out value="${disabledValue}"/>  class="le-select">
			                    <option >Выберите класс жидкости</option>	
									<c:forEach var="punct" items="${requestScope.combobox_FluidClasses}">
									<c:if test="${currentBrakFluid.getFluidClass().getName() != punct.getName()}">
										<option value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
									</c:if>
									<c:if test="${currentBrakFluid.getFluidClass().getName() == punct.getName()}">
										<option selected value="${punct.getName()}"><c:out value="${punct.getName()}"  /></option>
									</c:if>
								</c:forEach>
			                </select>
						</div>
	                  </li>
	            	  <li>
	                    <label>Объём:</label>
	                    <div class="value">
	                    	<input  <c:out value="${readOnly}"/> type="text" value="${currentBrakFluid.getValue()}" name="Value" />
						</div>
	                  </li>
	         		</ul>
	              </div>
	              
	              <div class="tab-pane" id="character">
		            <ul class="tabled-data">
	                  <li>
	                    <label>Температура кипения (сух.):</label>
	                    <div class="value">
	                    	<input <c:out value="${readOnly}"/> type="text" value="${currentBrakFluid.getBoilingTemperatureDry()}" name="BoilingTemperatureDry" />
						</div>
	                  </li>
	                  <li>
	                    <label>Температура кипения (вл.):</label>
	                    <div class="value">
	                    	<input <c:out value="${readOnly}"/> type="text" value="${currentBrakFluid.getBoilingTemperatureWet()}" name="BoilingTemperatureWet" />
						</div>
	                  </li>
	            	  <li>
	                    <label>Вязкость (при -40):</label>
	                    <div class="value">
	                    	<input <c:out value="${readOnly}"/> type="text" value="${currentBrakFluid.getViscosity40()}" name="Viscosity40" />
						</div>
	                  </li>
	                  <li>
	                    <label>Вязкость (при 100):</label>
	                    <div class="value">
	                    	<input <c:out value="${readOnly}"/> type="text" value="${currentBrakFluid.getViscosity100()}" name="Viscosity100" />
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
			                	<div><textarea name="Specification"  readonly="${readOnly}" cols="90" rows="7" class="textarea" ><c:out value="${currentBrakFluid.getSpecification()}"  /></textarea></div>
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
		                	<c:if test="${currentBrakFluid.hasPhoto()}">
						  		<img src="resources/jpg/<c:out value="${currentBrakFluid.getPhoto()}"  />" alt="${currentBrakFluid.getName()}">
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
                  </ul>
                
                <!-- /.tabled-data -->
                <div class="meta-row">
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

