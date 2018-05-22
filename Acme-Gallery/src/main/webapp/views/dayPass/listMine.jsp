 <%--
 * dayPass/listMine.jsp
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
<%@taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<spring:message code="number.format" var="numberFormat"/>

<display:table name="dayPasses" id="dayPass" requestURI="dayPass/${actorWS}listMine.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">

	<display:column titleKey="dayPass.ticker" property="ticker"/>
	
	<display:column titleKey="dayPass.visitDate">
		<acme:dateFormat code="date.format2" value="${dayPass.visitDate}"/>
	</display:column>
	
	<display:column titleKey="dayPass.museum">
		<jstl:out value="${dayPass.museum.name}"/>
	</display:column>
	
	<display:column titleKey="dayPass.exhibition.name">
		<jstl:if test="${dayPass.exhibition.title ne null}">
			<jstl:out value="${dayPass.exhibition.title}"/>
		</jstl:if>
		<jstl:if test="${dayPass.exhibition.title eq null}">
			<jstl:out value="-"/>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="dayPass.room">
		<jstl:if test="${dayPass.exhibition.room.name ne null}">
			<jstl:out value="${dayPass.exhibition.room.name}"/>
		</jstl:if>
		<jstl:if test="${dayPass.exhibition.room.name eq null}">
			<jstl:out value="-"/>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="creditCard.number">
		<jstl:out value="**** **** **** ${fn:substring(dayPass.creditCard.number, 12, 16)}"/>
	</display:column>
	
	<display:column titleKey="dayPass.price">
		<acme:priceFormat code="price.format" value="${dayPass.price}"/>
	</display:column>
	
	<display:column titleKey="dayPass.VAT">
		<fmt:formatNumber pattern="${numberFormat}" value="${dayPass.VAT}" /> %
	</display:column>
	
	<display:column titleKey="dayPass.purchaseMoment">
		<acme:dateFormat code="date.format" value="${dayPass.purchaseMoment}"/>
	</display:column>
</display:table>