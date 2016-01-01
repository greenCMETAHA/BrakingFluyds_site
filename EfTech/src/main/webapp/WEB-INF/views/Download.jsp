<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

       <h1 class="border">Импорт данных из Excel:</h1>
       
<%--        <c:out value="${requestScope.variant}"/>:<c:out value="${requestScope.task}"/> --%>
       
       
       <c:if test="${requestScope.task=='Product'}">
	       <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRODUCT')">
	         <div class="row">
		         
	  			 <div class="items-holder">
					<div class="container-fluid wishlist_table">
						<div class="col-xs-12 col-sm-4 no-margin">Загрузить номерклатуру:
						</div>
						<form action="Download" method="POST" enctype="multipart/form-data">
<%-- 							<input name="variant" type="hidden" value="${requestScope.variant}" > --%>
							<div class="col-xs-12 col-sm-4 no-margin">
								<input type="radio"  name="good" value="BrakingFluids">Тормозные жидкости<br><br>
								<input type="radio"  name="good" value="MotorOils" >Моторные масла<br>
								<input checked type="radio"  name="good" value="GearBoxOils">Трансмиссионные масла<br><br>
							</div>
							<div class="col-xs-12 col-sm-4 no-margin">
								<input type="file"  name="fileExcel" class="upload-file" id="file" value=""/>
							</div>
							<div class="col-xs-12 col-sm-3 no-margin">
			                    <div class="buttons-holder">
		                            <button class="le-button" type="submit" name="variant" value="Product">Загрузить</button>
		                        </div>
	                        </div>
	                    </form>
	               	 </div>
	              </div>
	            </div> 
	       </sec:authorize>
       </c:if>
            <br>
       <c:if test="${requestScope.task=='Price'}">
	       <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_PRICE')">
	            <div class="row">
	  			 <div class="items-holder">
					<div class="container-fluid wishlist_table">
						<div class="col-xs-12 col-sm-4 no-margin">Загрузить цены:
						</div>
						
						<form action="Download" method="POST" enctype="multipart/form-data">
<%-- 							<input name="variant" type="hidden" value="${requestScope.variant}" > --%>
							<div class="col-xs-12 col-sm-4 no-margin">
								<input type="radio"  name="good" value="MotorOils" >Моторные масла<br>
								<input type="radio"  name="good" value="BrakingFluids">Тормозные жидкости<br><br>
								<input checked type="radio"  name="good" value="GearBoxOils">Трансмиссионные масла<br><br>
							</div>
							<div class="col-xs-12 col-sm-4 no-margin">
								<input type="file"  name="fileExcel" class="upload-file" id="file" value=""/>  
							</div>
							<div class="col-xs-12 col-sm-3 no-margin">
			                    <div class="buttons-holder">
		                            <button class="le-button" type="submit" name="variant" value="Price">Загрузить</button>
		                        </div>
	                        </div>
	                    </form>
	               	 </div>
	              </div>
	            </div> 
		  </sec:authorize>	
  	  </c:if>
