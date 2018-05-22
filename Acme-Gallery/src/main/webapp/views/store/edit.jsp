 <%--
 * sponsorship/edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<form:form action="store/director/edit.do?museumId=${museum.id}" modelAttribute="store">
	<br>
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<!-- Inputs -->
	
	<acme:textbox code="store.name" path="name"/><br>
	<acme:textbox code="store.logo" path="logo"/><br>
	<acme:textbox code="store.phoneNumber" path="phoneNumber"/><br>
	<acme:textbox code="store.email" path="email"/>
	<br>
	<acme:cancel url="museum/director/display.do?museumId=${museum.id}" code="store.cancel"/>
	<acme:submit name="save" code="store.save"/>
	
	
</form:form>
