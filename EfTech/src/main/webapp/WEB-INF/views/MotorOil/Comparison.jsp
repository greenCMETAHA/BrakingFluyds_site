<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<c:set var="name" value="compare" />

<div class="main-content" id="main-content">
	<div class="container">
        <div class="inner-xs">
            <div class="page-header">
                <h2 class="page-title">
                    Сравним товары           
                </h2>
            </div>
        </div><!-- /.section-page-title -->
        <div class="table-responsive inner-bottom-xs inner-top-xs">
             <table class="table table-bordered table-striped compare-list">
                <thead>
                    <tr>
                        <td>&nbsp;</td>
                        <c:forEach var="current" items="${sessionScope[name]}">
							<td class="text-center">
								<div class="image-wrap">
									<c:url value="Comparison" var="UpdateGood">
										<c:param name="id" value="${current.getId()}"/>
										<c:param name="variant" value="deleteFromCompare"/>
										<c:param name="task" value="Oil"/>
									</c:url>
	                                <a data-product_id="${current.getId()}" href="${UpdateGood}" class="remove-link"><i class="fa fa-times-circle"></i></a>
	                                <img alt="${current.getName()}" class="attachment-yith-woocompare-image" src="resources/jpg/${current.getPhoto()}">                        
	                            </div>
								<c:url value="ShowOneMotorOil" var="UpdateGood">
									<c:param name="id" value="${current.getId()}"/>
								</c:url>	
								<p><strong><a href="${UpdateGood}"> <c:out value="${current.getName()}"/></a></strong></p>
							</td>
						</c:forEach>
                    </tr>
                    <tr class="tr-add-to-cart">
                        <td>&nbsp;</td>
                         <c:forEach var="current" items="${sessionScope[name]}">
							<td class="text-center">
                            <div class="add-cart-button">
                            	<c:url value="Comparison" var="UpdateGood">
									<c:param name="id" value="${current.getId()}"/>
									<c:param name="variant" value="inBasket"/>
								</c:url>
                                <a class="le-button add_to_cart_button  product_type_simple" href="${UpdateGood}">В корзину</a>
                            </div>                    
                            </td>                         
                         </c:forEach>
                    </tr>
                </thead>
                <tbody>
                                                                                                     
                    <tr class="comparison-item price">
                        <th>Цена</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
                        	<td class="comparison-item-cell odd product_${current.getId()}">
                            	<span class="amount">
                            			$<c:out value="${current.getPrice()}"/>
                             	</span>                        
                        	</td>
                        </c:forEach>
                    </tr>
                    <tr class="comparison-item price">
                        <th>Цена со скидкой</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
                        	<td class="comparison-item-cell odd product_${current.getId()}">
                            	<span class="amount">
		                      		$<c:out value="${current.getPriceWithDiscount()}"/>
                            	</span>                        
                        	</td>
                        </c:forEach>
                    </tr>
                <tr class="comparison-item description">
                        <th>Производитель</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${current.getId()}">
	                            <p><c:out value="${current.getManufacturer().getName()}"  /> </p>
	                        </td>
                        </c:forEach>
                    </tr>
                    <tr class="comparison-item description">
                        <th>Тип двигателя</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${current.getId()}">
	                            <p><c:out value="${current.getEngineType().getName()}"  /> </p>
	                        </td>
                        </c:forEach>
                    </tr>
	                <tr class="comparison-item description">
                        <th>Тип масла</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${current.getId()}">
	                            <p><c:out value="${current.getOilStuff().getName()}"  /></p>
	                        </td>
                        </c:forEach>
	                </tr> 
	                <tr class="comparison-item description">
                        <th>Вязкость:</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${current.getId()}">
	                            <p><c:out value="${current.getViscosity()}"  /></p>
	                        </td>
                        </c:forEach>
	                </tr>   
	                <tr class="comparison-item description">
                        <th>Объём</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${current.getId()}">
	                            <p><c:out value="${current.getValue()}"  /></p>
	                        </td>
                        </c:forEach>
	                </tr>                                                       
                 	<tr class="comparison-item description">
                        <th>Описание</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${current.getId()}">
	                            <p><c:out value="${current.getDescription()}"  /></p>
	                        </td>
                        </c:forEach>
                    </tr>                    
	                <tr class="comparison-item description">
                        <th>Спецификация</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${current.getId()}">
	                            <p><c:out value="${current.getSpecification()}"  /></p>
	                        </td>
                        </c:forEach>
	                </tr>                                                       
                 	<tr class="comparison-item description">
                        <th>Оценка покупателей</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${current.getId()}">
	                            <p><c:out value="${current.getJudgement()}"  /></p>
	                        </td>
                        </c:forEach>
                    </tr>           
                    <tr class="comparison-item stock">
                        <th>В наличии</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
                        	<td class="comparison-item-cell odd product_${current.getId()}">
                        		<c:if test="${current.getInStock()>0}">
                            		<span class="label label-success"><span class="">В наличии</span></span>
                            	</c:if>
                        		<c:if test="${current.getInStock()==0}">
                            		<span class="label label-warning"><span class="">Нет на складе</span></span>
                            	</c:if>                        
                        	</td>
                        </c:forEach>
                    </tr>
                    <tr class="price repeated">
                        <th>Цена</th>
                        <c:forEach var="current" items="${sessionScope[name]}">
                        	<td class="odd product_${current.getId()}">
                            	<span class="amount">
                            		$<c:out value="${current.getPriceWithDiscount()}"/>
                            	</span>                        
                        	</td>
                        </c:forEach>
                    </tr>
                    
                </tbody>
            </table>
        </div><!-- /.table-responsive -->
    </div><!-- /.container -->	
</div>