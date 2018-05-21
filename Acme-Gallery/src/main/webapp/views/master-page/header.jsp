<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<spring:message code="cookies" var="cookiesMessage"/>

<script>

window.onload = function(){
	
	// If the banner cookie isn't on
	if(cookieCurrentValue(window.cookieName) != window.cookieValue){
    	createDiv('${cookiesMessage}'); 
    	$("#readMore").hide();
    }
};

</script>

<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
    
      <!-- TODO: Place here the name and logo of the company -->
      <img src="images/logo.png" alt="Acme, Inc." style="max-height: 50px;"/>
      <a class="navbar-brand" href="#">Acme</a>
    </div>
  
  	<!--------------------------- ADMINISTRATOR --------------------------->  
  
    <security:authorize access="hasRole('ADMINISTRATOR')">
    	<ul class="nav navbar-nav">
    		
			<li ><a href="exhibition/administrator/search.do"><spring:message code="master.page.exhibition.search"/></a></li>
			<li ><a href="category/administrator/display.do"><spring:message code="master.page.categories"/></a></li>
			<li ><a href="announcement/administrator/list.do"><spring:message code="master.page.list.announcement"/></a></li>
			<li ><a href="announcement/administrator/list.do?listTabooed=true"><spring:message code="master.page.taboo.announcement"/></a></li>
			<li ><a href="group/administrator/list.do"><spring:message code="master.page.groups"/></a></li>
			<li ><a href="group/administrator/listTaboo.do"><spring:message code="master.page.taboo.group"/></a></li>
			<li ><a href="comment/administrator/listTaboo.do"><spring:message code="master.page.taboo.comment"/></a></li>
			<li ><a href="museum/administrator/list.do"><spring:message code="master.page.museum.list"/></a></li>
			<li ><a href="director/administrator/list.do"><spring:message code="master.page.director.list"/></a></li>
			<li ><a href="visitor/administrator/listUnlocked.do"><spring:message code="master.page.visitors.unlocked"/></a></li>
			<li ><a href="visitor/administrator/listLocked.do"><spring:message code="master.page.visitors.locked"/></a></li>
			<li ><a href="sponsor/administrator/listUnlocked.do"><spring:message code="master.page.sponsor.unlocked"/></a></li>
			<li ><a href="sponsor/administrator/listLocked.do"><spring:message code="master.page.sponsor.locked"/></a></li>
			<li ><a href="administrator/dashboard.do"><spring:message code="master.page.dashboard"/></a></li>
			<li ><a href="systemConfiguration/administrator/display.do"><spring:message code="master.page.systemConfiguration"/></a></li>
			
			<li ><a href="#"><security:authentication property="principal.username" /></a></li>
		</ul>
		
		<ul class="nav navbar-nav navbar-right">
			<li><a href="director/administrator/register.do"><span class="glyphicon glyphicon-user"></span> <spring:message code="master.page.director.signup" /></a></li>
			<li><a href="guide/administrator/register.do"><span class="glyphicon glyphicon-user"></span> <spring:message code="master.page.guide.signup" /></a></li>
			<li><a href="critic/administrator/register.do"><span class="glyphicon glyphicon-user"></span> <spring:message code="master.page.critic.signup" /></a></li>
			<li ><a href="j_spring_security_logout"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="master.page.logout" /></a></li>
    	</ul>
	</security:authorize>
	
	<!------------------------------ DIRECTOR ----------------------------->
	
	<security:authorize access="hasRole('DIRECTOR')">
    	<ul class="nav navbar-nav">
    		
			<li ><a href="exhibition/director/search.do"><spring:message code="master.page.exhibition.search"/></a></li>
			<li ><a href="category/director/display.do"><spring:message code="master.page.category.display"/></a></li>
			<li ><a href="exhibition/director/listMine.do"><spring:message code="master.page.exhibition.myList"/></a></li>
			<li ><a href="museum/director/list.do"><spring:message code="master.page.museum.list"/></a></li>
			<li ><a href="museum/director/listMine.do"><spring:message code="master.page.museum.listMine"/></a></li>
			<li ><a href="room/director/listMine.do"><spring:message code="master.page.room.listMine"/></a></li>
			<li ><a href="incident/director/listMine.do"><spring:message code="master.page.incident.listMine"/></a></li>
			<li ><a href="director/director/list.do"><spring:message code="master.page.director.list"/></a></li>
			
			<li ><a href="#"><security:authentication property="principal.username" /></a></li>
		</ul>
		
		<ul class="nav navbar-nav navbar-right">
      		<li ><a href="j_spring_security_logout"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="master.page.logout" /></a></li>
    	</ul>
	</security:authorize>
	
	<!------------------------------- GUIDE ------------------------------->
	
	<security:authorize access="hasRole('GUIDE')">
    	<ul class="nav navbar-nav">
    		
			<li ><a href="exhibition/guide/search.do"><spring:message code="master.page.exhibition.search"/></a></li>
			<li ><a href="category/guide/display.do"><spring:message code="master.page.category.display"/></a></li>
			<li ><a href="museum/guide/list.do"><spring:message code="master.page.museum.list"/></a></li>
			<li ><a href="director/guide/list.do"><spring:message code="master.page.director.list"/></a></li>
			
			<li ><a href="#"><security:authentication property="principal.username" /></a></li>
		</ul>
		
		<ul class="nav navbar-nav navbar-right">
      		<li ><a href="j_spring_security_logout"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="master.page.logout" /></a></li>
    	</ul>
	</security:authorize>
	
	<!------------------------------ CRITIC ----------------------------->
	
	<security:authorize access="hasRole('CRITIC')">
    	<ul class="nav navbar-nav">
    		
			<li ><a href="exhibition/critic/search.do"><spring:message code="master.page.exhibition.search"/></a></li>
			<li ><a href="category/critic/display.do"><spring:message code="master.page.category.display"/></a></li>
			<li ><a href="museum/critic/list.do"><spring:message code="master.page.museum.list"/></a></li>
			<li ><a href="director/critic/list.do"><spring:message code="master.page.director.list"/></a></li>
			
			<li ><a href="#"><security:authentication property="principal.username" /></a></li>
		</ul>
		
		<ul class="nav navbar-nav navbar-right">
      		<li ><a href="j_spring_security_logout"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="master.page.logout" /></a></li>
    	</ul>
	</security:authorize>
	
	<!------------------------------ SPONSOR ------------------------------>
	
	<security:authorize access="hasRole('SPONSOR')">
    	<ul class="nav navbar-nav">
    		
			<li ><a href="exhibition/sponsor/search.do"><spring:message code="master.page.exhibition.search"/></a></li>
			<li ><a href="category/sponsor/display.do"><spring:message code="master.page.category.display"/></a></li>
			<li ><a href="museum/sponsor/list.do"><spring:message code="master.page.museum.list"/></a></li>
			<li ><a href="director/sponsor/list.do"><spring:message code="master.page.director.list"/></a></li>
			
			<li ><a href="#"><security:authentication property="principal.username" /></a></li>
		</ul>
		
		<ul class="nav navbar-nav navbar-right">
      		<li ><a href="j_spring_security_logout"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="master.page.logout" /></a></li>
    	</ul>
	</security:authorize>
	
	<!------------------------------ VISITOR ------------------------------>
	
	<security:authorize access="hasRole('VISITOR')">
    	<ul class="nav navbar-nav">
    		
			<li ><a href="exhibition/visitor/search.do"><spring:message code="master.page.exhibition.search"/></a></li>
			<li ><a href="category/visitor/display.do"><spring:message code="master.page.category.display"/></a></li>
			<li ><a href="museum/visitor/list.do"><spring:message code="master.page.museum.list"/></a></li>
			<li ><a href="group/visitor/list.do"><spring:message code="master.page.group.list"/></a></li>
			<li ><a href="director/visitor/list.do"><spring:message code="master.page.director.list"/></a></li>
			<li ><a href="visitor/visitor/display.do"><spring:message code="master.page.visitor.display"/></a></li>
			
			<li ><a href="#"><security:authentication property="principal.username" /></a></li>
		</ul>
		
		<ul class="nav navbar-nav navbar-right">
      		<li ><a href="j_spring_security_logout"><span class="glyphicon glyphicon-log-out"></span> <spring:message code="master.page.logout" /></a></li>
    	</ul>
	</security:authorize>
	
	<!-------------------------- UNAUTHENTICATED -------------------------->
	
    <security:authorize access="isAnonymous()">
    	<ul class="nav navbar-nav">
    	
			<!-- TODO: Place here the entities, actions and messageCodes -->
			<li ><a href="exhibition/search.do"><spring:message code="master.page.exhibition.search"/></a></li>
			<li ><a href="category/display.do"><spring:message code="master.page.category.display"/></a></li>
			<li ><a href="museum/list.do"><spring:message code="master.page.museum.list"/></a></li>
			<li ><a href="director/list.do"><spring:message code="master.page.director.list"/></a></li>
			
		</ul>
		<ul class="nav navbar-nav navbar-right">
			<li><a href="visitor/register.do"><span class="glyphicon glyphicon-user"></span> <spring:message code="master.page.visitor.signup" /></a></li>
			<li><a href="sponsor/register.do"><span class="glyphicon glyphicon-user"></span> <spring:message code="master.page.sponsor.signup" /></a></li>
      		<li ><a href="security/login.do"><span class="glyphicon glyphicon-log-in"></span> <spring:message code="master.page.login" /></a></li>
    	</ul>
	</security:authorize>
  </div>
</nav>