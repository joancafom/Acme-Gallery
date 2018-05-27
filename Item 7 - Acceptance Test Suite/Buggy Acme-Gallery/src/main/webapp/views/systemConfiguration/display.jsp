<%--
 * index.jsp
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

<hr>
<h4><strong><spring:message code="systemConfiguration.VAT"/></strong>:  <jstl:out value="${VAT}" /><a href="systemConfiguration/administrator/editVat.do">  <spring:message code="systemConfiguration.VAT.edit"/></a></h4>
<hr>
<display:table name="tabooWords" id="tabooWord" requestURI="systemConfiguration/administrator/display.do" pagesize="5" class="displaytag">
	<display:column titleKey="systemConfiguration.tabooWords.tabooWord" sortable="true">
		<jstl:out value="${tabooWord}"/>
	</display:column>
	<display:column>
		<a href="systemConfiguration/administrator/deleteTabooWord.do?tabooWord=${tabooWord}"><spring:message code="systemConfiguration.tabooWords.delete"/></a>
	</display:column>
</display:table>
<a href="systemConfiguration/administrator/addTabooWord.do"><spring:message code="systemConfiguration.tabooWords.create"/></a>
