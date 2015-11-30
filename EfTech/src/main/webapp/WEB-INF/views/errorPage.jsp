<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

	<main id="faq" class="inner">
        <div class="container">
          <div class="row">
            <div class="col-md-8 center-block">
              <div class="info-404 text-center">
                <h2 class="primary-color inner-bottom-xs"><c:out value="${requestScope.errNumber}"/></h2>
                <p class="lead"><c:out value="${requestScope.errMessage}"/></p>
                <div class="sub-form-row inner-top-xs inner-bottom-xs">
<!--                   <form role="form" action="find"> -->
<!--                     <input placeholder="Поиск по каталогу" autocomplete="off"> -->
<!--                     <button class="le-button">Искать</button> -->
<!--                   </form> -->
                </div>
                <div class="text-center">
                  <a href="index" class="btn-lg huge"><i class="fa fa-home"></i> Перейти на главную страницу</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>

