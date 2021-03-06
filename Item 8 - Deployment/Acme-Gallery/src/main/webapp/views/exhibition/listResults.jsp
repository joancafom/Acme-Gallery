 <%--
 * exhibition/listResults.jsp
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
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="now" class="java.util.Date" />

<h3><spring:message code="exhibition.searchResults"/> <span style="text-decoration:underline;"><jstl:out value="${keyword}"/></span></h3><br>

<display:table name="exhibitions" id="exhibition" requestURI="exhibition/${actorWS}listResults.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">

	<display:column titleKey="exhibition.ticker" property="ticker"/>
	
	<display:column titleKey="exhibition.title">
		<a href="exhibition/${actorWS}display.do?exhibitionId=${exhibition.id}"><jstl:out value="${exhibition.title}"/></a>
	</display:column>
	
	<display:column titleKey="exhibition.museum">
		<a href="museum/${actorWS}display.do?museumId=${exhibition.room.museum.id}"><jstl:out value="${exhibition.room.museum.name}"/></a>
	</display:column>
	
	<display:column titleKey="exhibition.startingDate">
		<acme:dateFormat code="date.format" value="${exhibition.startingDate}"/>
	</display:column>
	
	<display:column titleKey="exhibition.endingDate">
		<acme:dateFormat code="date.format" value="${exhibition.endingDate}"/>
	</display:column>
	
	<display:column>
		<jstl:if test="${now > exhibition.endingDate}">
			<p style="color:red;"><spring:message code="exhibition.passed"/></p>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="exhibition.price">
		<acme:priceFormat code="price.format" value="${exhibition.price}"/>
	</display:column>
	
</display:table>