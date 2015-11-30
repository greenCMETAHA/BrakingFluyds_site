<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div class="main-content" id="main-content">
	<div class="container">
        <div class="inner-xs">
            <div class="page-header">
                <h2 class="page-title">
                    Сравним товары           
                </h2>
            </div>
            
      	<c:set var="name" value="compare" />
        </div><!-- /.section-page-title -->
        <div class="table-responsive inner-bottom-xs inner-top-xs">
             <table class="table table-bordered table-striped compare-list">
                <thead>
                    <tr>
                        <td>&nbsp;</td>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
							<td class="text-center">
								<div class="image-wrap">
									<c:url value="Comparison" var="UpdateBrakingFluid">
										<c:param name="id" value="${currentBFluid.getId()}"/>
										<c:param name="variant" value="deleteFromCompare"/>
									</c:url>
	                                <a data-product_id="${currentBFluid.getId()}" href="${UpdateBrakingFluid}" class="remove-link"><i class="fa fa-times-circle"></i></a>
	                                <img alt="${currentBFluid.getName()}" class="attachment-yith-woocompare-image" src="resources/jpg/${currentBFluid.getPhoto()}">                        
	                            </div>
								<c:url value="ShowOne" var="UpdateBrakingFluid">
									<c:param name="id" value="${currentBFluid.getId()}"/>
								</c:url>	
								<p><strong><a href="${UpdateBrakingFluid}"> <c:out value="${currentBFluid.getName()}"/></a></strong></p>
							</td>
						</c:forEach>
                    </tr>
                    <tr class="tr-add-to-cart">
                        <td>&nbsp;</td>
                         <c:forEach var="currentBFluid" items="${sessionScope[name]}">
							<td class="text-center">
                            <div class="add-cart-button">
                            	<c:url value="Comparison" var="UpdateBrakingFluid">
									<c:param name="id" value="${currentBFluid.getId()}"/>
									<c:param name="variant" value="inBasket"/>
								</c:url>
                                <a class="le-button add_to_cart_button  product_type_simple" href="${UpdateBrakingFluid}">В корзину</a>
                            </div>                    
                            </td>                         
                         </c:forEach>
                    </tr>
                </thead>
                <tbody>
                                                                                                     
                     <tr class="comparison-item price">
                        <th>Цена</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
                        	<td class="comparison-item-cell odd product_${current.getId()}">
                            	<span class="amount">
                           			$<c:out value="${currentBFluid.getPrice()}"/>
                            	</span>                        
                        	</td>
                        </c:forEach>
                    </tr>
                    <tr class="comparison-item price">
                        <th>Цена со скидкой</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
                        	<td class="comparison-item-cell odd product_${current.getId()}">
                            	<span class="amount">
		                      		$<c:out value="${currentBFluid.getPriceWithDiscount()}"/>
                            	</span>                        
                        	</td>
                        </c:forEach>
                    </tr>
                    <tr class="comparison-item description">
                        <th>Производитель</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${currentBFluid.getId()}">
	                            <p><c:out value="${currentBFluid.getManufacturer().getName()}"  /> </p>
	                        </td>
                        </c:forEach>
                    </tr>
                    <tr class="comparison-item description">
                        <th>Класс жидкости</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${currentBFluid.getId()}">
	                            <p><c:out value="${currentBFluid.getFluidClass().getName()}"  /> </p>
	                        </td>
                        </c:forEach>
                    </tr>
	                <tr class="comparison-item description">
                        <th>Температура кипения (сух.)</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${currentBFluid.getId()}">
	                            <p><c:out value="${currentBFluid.getBoilingTemperatureDry()}"  /></p>
	                        </td>
                        </c:forEach>
	                </tr>                                                       
                 	<tr class="comparison-item description">
                        <th>Температура кипения (вл.)</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${currentBFluid.getId()}">
	                            <p><c:out value="${currentBFluid.getBoilingTemperatureWet()}"  /></p>
	                        </td>
                        </c:forEach>
                    </tr>
	                <tr class="comparison-item description">
                        <th>Объём</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${currentBFluid.getId()}">
	                            <p><c:out value="${currentBFluid.getValue()}"  /></p>
	                        </td>
                        </c:forEach>
	                </tr>                                                       
                 	<tr class="comparison-item description">
                        <th>Описание</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${currentBFluid.getId()}">
	                            <p><c:out value="${currentBFluid.getDescription()}"  /></p>
	                        </td>
                        </c:forEach>
                    </tr>                    
	                <tr class="comparison-item description">
                        <th>Вязкость (-40)</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${currentBFluid.getId()}">
	                            <p><c:out value="${currentBFluid.getViscosity40()}"  /></p>
	                        </td>
                        </c:forEach>
	                </tr>                                                       
                 	<tr class="comparison-item description">
                        <th>Вязкость (100)</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${currentBFluid.getId()}">
	                            <p><c:out value="${currentBFluid.getViscosity100()}"  /></p>
	                        </td>
                        </c:forEach>
                    </tr>                    
	                <tr class="comparison-item description">
                        <th>Спецификация</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${currentBFluid.getId()}">
	                            <p><c:out value="${currentBFluid.getSpecification()}"  /></p>
	                        </td>
                        </c:forEach>
	                </tr>                                                       
                 	<tr class="comparison-item description">
                        <th>Оценка покупателей</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
	                        <td class="comparison-item-cell odd product_${currentBFluid.getId()}">
	                            <p><c:out value="${currentBFluid.getJudgement()}"  /></p>
	                        </td>
                        </c:forEach>
                    </tr>           
                    <tr class="comparison-item stock">
                        <th>В наличии</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
                        	<td class="comparison-item-cell odd product_${currentBFluid.getId()}">
                        		<c:if test="${currentBFluid.getInStock()>0}">
                            		<span class="label label-success"><span class="">В наличии</span></span>
                            	</c:if>
                        		<c:if test="${currentBFluid.getInStock()==0}">
                            		<span class="label label-warning"><span class="">Нет на складе</span></span>
                            	</c:if>                        
                        	</td>
                        </c:forEach>
                    </tr>
                    <tr class="price repeated">
                        <th>Цена</th>
                        <c:forEach var="currentBFluid" items="${sessionScope[name]}">
                        	<td class="odd product_${currentBFluid.getId()}">
                            	<span class="amount">
                           			$<c:out value="${currentBFluid.getPriceWithDiscount()}"/>
                            	</span>                        
                        	</td>
                        </c:forEach>
                    </tr>
                    
                </tbody>
            </table>
        </div><!-- /.table-responsive -->
    </div><!-- /.container -->	
</div>	