<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

     <div id="single-product">
        <div class="container">
    	  <form action="InsertUpdateDoc" method="POST">
    	  	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
			<input name="pageInfo" type="hidden" value="${requestScope.pageInfo}" >
			<input name="variant" type="hidden" value="${requestScope.variant}" >
						
			<h1 class="border"><c:out value="${requestScope.pageInfo}"/></h1>
           
            <div class="no-margin col-xs-12 col-sm-7 body-holder">
              <ul class="tabled-data">
	                 <li>
	                   <label>Номер документа:</label>
	                   <div class="value">
	                   	 <c:if test="${requestScope.task=='New'}">
	                   		<input type="text" class="input" value="${requestScope.doc_id}" name="doc_id" />
	                   	 </c:if>
	                   	 <c:if test="${requestScope.task!='New'}">
	                   	 	<c:out value="${requestScope.doc_id}"/>
	                   	 	<input type="text" hidden class="input" value="${requestScope.doc_id}" name="doc_id" />
	                   	 </c:if>
						</div>
	                 </li>
	                 <li>
	                   <label>Дата документа:</label>
	                   <div class="value">
	                     <c:if test="${requestScope.task=='New'}">
	                   		<input type="date" class="input" value="${requestScope.time}" name="time" />
	                   	 </c:if>
	                     <c:if test="${requestScope.task!='New'}">
	                   	 	<c:out value="${requestScope.time.toLocaleString()}"/>
	                   	 	<input type="text" hidden class="input" value="${requestScope.timeString}" name="time" />
	                   	 </c:if>
	                   		
						</div>
	                 </li>
	                 <c:if test="${requestScope.variant=='Pay'}">
	                 	<li>
		                   <label>Заказчик:</label>
		                   <div class="value">
		                   		<c:if test="${requestScope.task=='New'}">
									<select size="1" name="client_id"  class="le-select">
					                    <option >Выберите покупателя</option>	
											<c:forEach var="punct" items="${requestScope.listClients}">
											<c:if test="${requestScope.currentClient != punct.getId()}">
												<option value="${punct.getId()}"><c:out value="${punct.getName()}"  /></option>
											</c:if>
											<c:if test="${requestScope.currentClient == punct.getId()}">
												<option selected value="${punct.getId()}"><c:out value="${punct.getName()}"  /></option>
											</c:if>
										</c:forEach>
					                </select>
		                   		</c:if>
		                   		<c:if test="${requestScope.task!='New'}">
		                   			<input name="client_id" type="hidden" value="${requestScope.currentClient}" >
		                   			<c:out value="${requestScope.client.getName()}"/><br>
		                   		</c:if>
							</div>
		                 </li>
		                 <li>
		                   <label>Получатель:</label>
		                   <div class="value">
	                   			<c:out value="${requestScope.currentManufacturer.getName()}"/><br>
							</div>
		                 </li>
	                 	 <li>
		                   <label>Сумма:</label>
		                   <div class="value">
	                   			<input type="text" class="input" value="${requestScope.summ}" name="doc_summ" />
							</div>
		                 </li>
	                 </c:if>
	                 <c:if test="${requestScope.variant=='Demand'}">
	                  	<sec:authorize access="hasAnyRole('ROLE_DELIVERY','ROLE_MANAGER_SALE')">
	                 	<li>
		                   <label>Заказчик:</label>
		                   <div class="value">
			                   	<c:out value="${requestScope.userDoc.getName()}"/><br>
			                   	<c:out value="${requestScope.userDoc.getEmail()}"/><br>
							</div>
		                 </li>
		                </sec:authorize>
		                
		                 <li>
		                   <label>Покупатель:</label>
		                   <div class="value">
		                   		<c:if test="${requestScope.task=='New'}">
									<select size="1" name="client_id"  class="le-select">
					                    <option >Выберите покупателя</option>	
											<c:forEach var="punct" items="${requestScope.listClients}">
											<c:if test="${requestScope.currentClient != punct.getId()}">
												<option value="${punct.getId()}"><c:out value="${punct.getName()}"  /></option>
											</c:if>
											<c:if test="${requestScope.currentStatus == punct.getId()}">
												<option selected value="${punct.getId()}"><c:out value="${punct.getName()}"  /></option>
											</c:if>
										</c:forEach>
					                </select>
		                   		</c:if>
		                   		<c:if test="${requestScope.task!='New'}">
		                   			<input name="client_id" type="hidden" value="${requestScope.currentClient}" >
		                   			<c:out value="${requestScope.client.getName()}"/><br>
		                   		</c:if>
							</div>
		                 </li>
	                 
		           	  	 <li>
		                   <label>Статус:</label>
		                    <div class="value">
		                    	<select size="1" name="status_id"  class="le-select">
				                    <option >Выберите статус</option>	
										<c:forEach var="punct" items="${requestScope.statuslist}">
										<c:if test="${requestScope.currentStatus != punct.getId()}">
											<option value="${punct.getId()}"><c:out value="${punct.getName()}"  /></option>
										</c:if>
										<c:if test="${requestScope.currentStatus == punct.getId()}">
											<option selected value="${punct.getId()}"><c:out value="${punct.getName()}"  /></option>
										</c:if>
									</c:forEach>
				                </select>
							</div>
		                 </li>
		                 
		                 <li>
		                   <label>Исполнитель:</label>
		                   <div class="value">
		                   	   <c:out value="${requestScope.user_name}"/>
		                   	   <input name="executer_id" type="hidden" value="${requestScope.executer_id}" >
			                </div>
		                 </li>
	                 	 <li>
		                   <label>Оплачено:</label>
		                   <div class="value">
		                   		<c:if test="${requestScope.paid>0}">
		                   			<input type="checkbox" checked name=""paid""/>
		                   		</c:if>
		                   		<c:if test="${requestScope.paid==0}">
		                   			<input type="checkbox" name="paid"/>
		                   		</c:if>

							</div>
		                 </li>
	                 	 <li>
		                   <label>Нужна доставка:</label>
		                   <div class="value">
		                   		<c:if test="${requestScope.shipping>0}">
		                   			<input type="checkbox" checked name="shipping"/>
		                   		</c:if>
		                   		<c:if test="${requestScope.shipping==0}">
		                   			<input type="checkbox" name="shipping"/>
		                   		</c:if>
							</div>
		                 </li>
		                 
	                 </c:if>
	        	 </ul>
	         </div>
           	 <div class="col-xs-12 col-sm-3 no-margin">
	            <div class="buttons-holder">
	               <button class="le-button" type="submit" name="task" value="Save">Сохранить</button>
	               <button class="le-button" type="submit" name="task" value="SaveAndList">Сохранить и закрыть</button>
	               <button class="le-button" type="submit" name="task" value="home">К списку товаров</button>
	            </div>
	         </div>
     		
     		<c:if test="${requestScope.variant=='Pay'}">
     			<c:if test="${requestScope.listDemands.size()>0}">
					<div class="col-xs-12 col-md-9 items-holder no-margin">
					<label>Закрытые заявки:</label><br>
		            	<c:forEach var="currentDoc" items="${requestScope.listDemands}">
				            <div class="row no-margin cart-item">
				              <div class="col-xs-12 col-sm-5 ">
				                <div class="title">
				                  <c:if test="${!currentDoc.getDemand_id().isEmpty()}">
				                  	<c:out value="${currentDoc.getDemand_id()}" />
				                  </c:if>
				                  <c:if test="${currentDoc.getDemand_id().isEmpty()}">
				                  	<- предоплата ->
				                  </c:if>
				                </div>
				              </div>
				          	  <div class="col-xs-12 col-sm-3 no-margin">
				          	  	<div class="title">
				                	<c:out value="${currentDoc.showDate()}"/>
				                </div>
				              </div>
				              <div class="col-xs-12 col-sm-2 no-margin">
				                  <div class="price">
				               		<c:out value="${currentDoc.getSumm()}"/>
				                  </div>
				              </div>
				            </div>
				            <!-- /.cart-item -->
			          	</c:forEach>
		        	</div>
		        </c:if>
        	</c:if>			
     		<c:if test="${requestScope.variant!='Pay'}">
  				<div class="col-xs-12 col-md-9 items-holder no-margin">
	            	<c:if test="${requestScope.task=='New'}">
	            	<c:set var="name" value="basket" />
			            <c:forEach var="currentDoc" items="${sessionScope[name]}">
							<c:set  var="current" value="${currentDoc.getGood()}" />
				            <div class="row no-margin cart-item">
				              <div class="col-xs-12 col-sm-2 no-margin">
				                
				                <img class="lazy" height="73" width="" alt="${current.getName()}" src="resources/jpg/${current.getPhoto()}" />
				                </a>
				              </div>
				              <div class="col-xs-12 col-sm-4 ">
				                <div class="title">
				                  <c:out value="${current.getName()}" />
				                </div>
				                <div class="brand"><c:out value="${current.getManufacturer().getName()}"/></div>
				              </div>
				          	  <div class="col-xs-12 col-sm-3 no-margin">
				          	  	<div class="quantity">
				                      <c:out value="${currentDoc.getQauntity()}"/>
				                </div>
				               </div>
				               <sec:authorize access="!isAnonymous()">
					               <div class="col-xs-12 col-sm-2 no-margin">
					                  <div class="price">
						               		<c:out value="${currentDoc.getQauntity()*current.getPrice()}"/>
					                  </div>
					               </div>
					           </sec:authorize>
				            </div>
				            <!-- /.cart-item -->
			          	</c:forEach>
		          	</c:if>
		          	<c:if test="${requestScope.task!='New'}">
		            	<c:forEach var="currentDoc" items="${requestScope.listDoc}">
							<c:set  var="current" value="${currentDoc.getGood()}" />
				            <div class="row no-margin cart-item">
				              <div class="col-xs-12 col-sm-1 no-margin">
				                <img class="lazy" height="73" width="" alt="${current.getName()}" src="resources/jpg/${current.getPhoto()}" />
				              </div>
				              <div class="col-xs-12 col-sm-6 ">
				                <div class="title">
				                  <c:out value="${current.getName()}" />
				                </div>
				                <div class="brand">(<c:out value="${current.getManufacturer().getName()}"/>)</div>
				              </div>
				          	  <div class="col-xs-12 col-sm-1 no-margin">
				          	  	<div class="quantity">
				                	<c:out value="${currentDoc.getQuantity()}"/>
				                </div>
				              </div>
				              <div class="col-xs-12 col-sm-2 no-margin">
				                  <div class="price">
				               		<c:out value="${currentDoc.getQuantity()*current.getPrice()}"/>
				                  </div>
				              </div>
				            </div>
				            <!-- /.cart-item -->
			          	</c:forEach>
		  			</c:if>
	        	</div>
        	</c:if>
		</form>
      	</div>
      </div>