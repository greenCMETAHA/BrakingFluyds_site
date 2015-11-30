<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

     <form action="listDoc" method="POST" enctype="application/x-www-form-urlencoded">		
    <section id="category-grid">
        <div class="container">
        	<h1>
	        	<c:if test="${requestScope.variant=='Demand'}">
	    	  		Заявки:
	    	  	</c:if>
	    	  	<c:if test="${requestScope.variant=='Offer'}">
	    	  		Коммерческие предложения:
	    	  	</c:if>
	    	  	<c:if test="${requestScope.variant=='Pay'}">
	    	  		Оплаты:
	    	  	</c:if>
    	  	</h1>
       		<input name="variant" type="hidden" value="${requestScope.variant}" >
       		<div class="row">
       		
       			<div class="col-xs-5 col-sm-10">
       				За период с <input type="date" value="${dateBeginFilterString}" name="dateBeginFilterString"> 
    	    			по <input type="date" value="${dateEndFilterString}" name="dateEndFilterString"> &nbsp&nbsp&nbsp
                    <input type="submit" class="le-button" value="Обновить" name="button">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <c:if test="${requestScope.variant=='Pay'}">
                    	<input type="submit" class="le-button" value="Создать" name="button">
                    </c:if>
                </div>
               </div>
       	
			<section id="single-product-tab">
		    	<div class="container">
		        	
		        	<div class="col-xs-12 col-md-20 items-holder no-margin">
			            <c:forEach var="currentDoc" items="${requestScope.listDoc}">
			            	<c:url value="InsertUpdateDoc" var="OpenDoc">
			            		<c:param name="doc_id" value="${currentDoc.getNumDoc()}"/>
			            		<c:param name="variant" value="${requestScope.variant}"/>
			            		<c:param name="task" value="Open"/>
							</c:url>
			            	
							<c:if test="${requestScope.variant=='Pay'}">
								<div class="row no-margin cart-item">
					              <div class="col-xs-12 col-sm-2 no-margin">
					                	<a href="${OpenDoc}"><c:out value="${currentDoc.getNumDoc()}" /></a>
					              </div>
					              <div class="col-xs-12 col-sm-2 no-margin">
					                	<a href="${OpenDoc}"><c:out value="${currentDoc.showDate()}" /></a>
					              </div>
					              <div class="col-xs-12 col-sm-3 ">
					                  <a href="${OpenDoc}"><c:out value="${currentDoc.getClient().getName()}" /></a>
					              </div>
					               <div class="col-xs-12 col-sm-3 ">
					                  <a href="${OpenDoc}"><c:out value="${currentDoc.getManufacturer().getName()}" /></a>
					              </div>
					              <sec:authorize access="!isAnonymous()">
						               <div class="col-xs-12 col-sm-2 no-margin">
						                  <div class="price">
						                      <a href="${OpenDoc}"><c:out value="${currentDoc.getSumm()}"/></a>
						                  </div>
						              </div>
					              </sec:authorize>
					            </div>
							</c:if>
							<c:if test="${requestScope.variant!='Pay'}">
								<c:set  var="current" value="${currentDoc.getGood()}" />
					            <div class="row no-margin cart-item">
					              <div class="col-xs-12 col-sm-3 no-margin">
					                	<a href="${OpenDoc}"><c:out value="${currentDoc.getNumDoc()}" /></a>
					              </div>
					              <div class="col-xs-12 col-sm-2 no-margin">
					                	<a href="${OpenDoc}"><c:out value="${currentDoc.showDate()}" /></a>
					              </div>
					              <c:if test="${requestScope.variant=='Demand'}">
						              <div class="col-xs-12 col-sm-2 ">
						                <div class="row no-margin cart-item">
						                  <a href="${OpenDoc}"><c:out value="${currentDoc.getStatus().getName()}" /></a>
						                  <c:if test="${currentDoc.isPaid()}">
						                  	&nbsp;&nbsp;&nbsp;
						                  	<a href="${OpenDoc}"><font color="red">ОПЛАЧЕНО</font></a>
						                  </c:if>
						                  
						                </div>
						              </div>
	    				 	  	  </c:if>
					              <div class="col-xs-12 col-sm-1 ">
					                <div class="quantity">
					                  <a href="${OpenDoc}"><c:out value="${currentDoc.getQuantity()}" /></a>
					                </div>
					              </div>
					              <sec:authorize access="!isAnonymous()">
						               <div class="col-xs-12 col-sm-2 no-margin">
						                  <div class="price">
						                      <a href="${OpenDoc}"><c:out value="${currentDoc.getSumm()}"/></a>
						                  </div>
						              </div>
					              </sec:authorize>
	 				              <sec:authorize access="hasAnyRole('ROLE_MANAGER_SALE','ROLE_DELIVERY','ROLE_DISTR')">
						               <div class="col-xs-12 col-sm-2 no-margin">
						                  <div class="price">
						                      <a href="${OpenDoc}"><c:out value="${currentDoc.getExecuter().getName()}"/></a>
						                  </div>
						              </div>
	 				              </sec:authorize>
					            </div>
				            </c:if>
				            <!-- /.cart-item -->
			          	</c:forEach>
		        	</div>
	        	</div>
        	</section>
        </div>
        <!-- /.copyright-bar -->
     </section>
        </form>
      </footer>
      <!-- /#footer -->
      <!-- ============================================================= FOOTER : END ============================================================= -->	
    </div>
  
