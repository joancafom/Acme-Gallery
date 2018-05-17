<%--
 * systemConfiguration/edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jstl:if test="${editTaboo}">
	<form action="systemConfiguration/administrator/edit.do" method="get">
		<label><spring:message code="systemConfiguration.tabooWords.tabooWord"/>: </label>
		<input type="text" name="tabooWord" value="${tabooWord}" />
	
		<acme:submit name="" code="systemConfiguration.save"/>
		<acme:cancel url="systemConfiguration/administrator/display.do" code="systemConfiguration.cancel"/>
	</form>
</jstl:if>
<jstl:if test="${!editTaboo}">
	<form:form action="systemConfiguration/administrator/edit.do" modelAttribute="systemConfiguration">
		<acme:textbox code="systemConfiguration.VAT" path="VAT" />
		
		<acme:submit name="saveVAT" code="systemConfiguration.save"/>
		<acme:cancel url="systemConfiguration/administrator/display.do" code="systemConfiguration.cancel"/>
	</form:form>
</jstl:if>

