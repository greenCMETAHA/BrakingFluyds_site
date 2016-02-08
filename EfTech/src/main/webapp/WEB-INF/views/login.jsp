<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

     <main id="authentication" class="inner-bottom-md">
        <div class="container">
          <div class="row">
            <div class="col-md-6">
              <section class="section sign-in inner-right-xs">
                <h2 class="bordered">Авторизируйтесь:</h2>
                <form role="form" class="login-form cf-style-1" action='/greenCM/login' method='POST'>
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                  <div class="field-row">
                    <label>Имя пользователя</label>
                    <input type="text" class="le-input" name="username">
                  </div>
                  <!-- /.field-row -->
                  <div class="field-row">
                    <label>Пароль</label>
                    <input type="password" class="le-input" name="password">
                  </div>
                  <!-- /.field-row -->
                  <div class="field-row clearfix">
                    <span class="pull-left">
                    <label class="content-color"><input type="checkbox" name="remeber_me" class="le-checbox auto-width inline"> <span class="bold">Запомнить меня</span></label>
                    </span>
                  </div>
                  <div class="buttons-holder">
                    <button type="submit" class="le-button huge">Авторизация</button>
                  </div>
                </form>
              </section>
            </div>
          </div>
        </div>
      </main>
