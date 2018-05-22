 <%--
 * visitor/display.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<div style="text-align:center;">

	<strong><spring:message code="visitor.name"/>:</strong> <jstl:out value="${visitor.name}"/><br/>
	<strong><spring:message code="visitor.surnames"/>:</strong> <jstl:out value="${visitor.surnames}"/><br/>
	<strong><spring:message code="visitor.email"/>:</strong> <jstl:out value="${visitor.email}"/><br/>
	<strong><spring:message code="visitor.phoneNumber"/>:</strong> <jstl:out value="${visitor.phoneNumber}"/><br/>
	<strong><spring:message code="visitor.address"/>:</strong> <jstl:if test="${visitor.address ne null}"><jstl:out value="${visitor.address}"/></jstl:if><jstl:if test="${visitor.address eq null}"><jstl:out value="-"/></jstl:if><br/>
	<strong><spring:message code="visitor.gender"/>:</strong> <jstl:if test="${visitor.gender eq 'MALE'}"><spring:message code="visitor.gender.male"/></jstl:if><jstl:if test="${visitor.gender eq 'FEMALE'}"><spring:message code="visitor.gender.female"/></jstl:if><jstl:if test="${visitor.gender eq 'OTHER'}"><spring:message code="visitor.gender.other"/></jstl:if><jstl:if test="${visitor.gender eq null}"><jstl:out value="-"/></jstl:if><br/>
	<jstl:if test="${own==true}">
		<h4><a href="visitor/visitor/edit.do"><spring:message code="visitor.edit"/></a></h4>
	</jstl:if>
</div>