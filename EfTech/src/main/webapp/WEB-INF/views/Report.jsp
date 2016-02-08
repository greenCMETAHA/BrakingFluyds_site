<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

      <form action="listDoc" method="POST" enctype="application/x-www-form-urlencoded">
      	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>		
    <section id="category-grid">
        <div class="container">
        	<h1>
	        	<c:if test="${requestScope.variant=='Pay'}">
	    	  		Отчет по платежам:
	    	  	</c:if>
    	  	</h1>
    	  	<form action="report" method="POST" enctype="application/x-www-form-urlencoded">
    	  		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
       			<input name="variant" type="hidden" value="${requestScope.variant}" >
       			<div class="row">
       		
	       			<div class="col-xs-5 col-sm-10">
	       				За период с <input type="date" value="${dateBeginFilterString}" name="dateBeginFilterString"> 
	    	    			по <input type="date" value="${dateEndFilterString}" name="dateEndFilterString"> &nbsp&nbsp&nbsp
	                    <input type="submit" class="le-button" value="Обновить" name="button">
	                </div>
               </div>
       		</form>
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
