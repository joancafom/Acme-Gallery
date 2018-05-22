<%--
 * dayPass/edit.jsp
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

<spring:message code="dayPass.exhibition"/> <a href="exhibition/visitor/display.do?exhibitionId=${dayPass.exhibition.id}"><jstl:out value="${dayPass.exhibition.title}"/></a>.<br/>
<spring:message code="dayPass.priceWithTaxes"/> <acme:priceFormat code="price.format" value="${dayPass.price}"/>.<br/>
<p><spring:message code="dayPass.exhibition.from"/> <acme:dateFormat code="date.format" value="${dayPass.exhibition.startingDate}"/> <spring:message code="dayPass.exhibition.to"/> <acme:dateFormat code="date.format" value="${dayPass.exhibition.endingDate}"/></p>

<form:form action="dayPass/${actorWS}edit.do?exhibitionId=${dayPass.exhibition.id}" modelAttribute="dayPass">

	<!-- Hidden Inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<!-- Inputs -->
	<br/>
	
	<div>
		<form:label path="visitDate">
			<strong><spring:message code="dayPass.visitDate"/>:</strong>
		</form:label>
		<form:input path="visitDate" placeholder="dd/MM/yyyy"/>
		<form:errors cssClass="error" path="visitDate"/><br><br>
	</div>	
	
	<br/>
	
	<acme:textbox code="creditCard.holderName" path="creditCard.holderName"/>
	<acme:textbox code="creditCard.brandName" path="creditCard.brandName" />
	<acme:textbox code="creditCard.number" path="creditCard.number" />
	<acme:textbox code="creditCard.CVV" path="creditCard.CVV"/>
	<acme:textbox code="creditCard.month" path="creditCard.month"/>
	<acme:textbox code="creditCard.year" path="creditCard.year"/>
	
	<br/>
	
	<acme:submit name="save" code="dayPass.save"/>
	<acme:cancel url="exhibition/${actorWS}display.do?exhibitionId=${dayPass.exhibition.id}" code="dayPass.cancel"/>
	
</form:form>

