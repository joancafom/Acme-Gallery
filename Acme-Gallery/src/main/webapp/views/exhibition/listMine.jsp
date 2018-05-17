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

<display:table name="exhibitions" id="exhibition" requestURI="exhibition/${actorWS}listMine.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">

	<display:column titleKey="exhibition.ticker" property="ticker"/>
	
	<display:column titleKey="exhibition.title">
		<a href="exhibition/${actorWS}display.do?exhibitionId=${exhibition.id}"><jstl:out value="${exhibition.title}"/></a>
	</display:column>
		
	<display:column titleKey="exhibition.startingDate">
		<acme:dateFormat code="date.format" value="${exhibition.startingDate}"/>
	</display:column>
	
	<display:column titleKey="exhibition.endingDate">
		<acme:dateFormat code="date.format" value="${exhibition.endingDate}"/>
	</display:column>
	
	<display:column titleKey="exhibition.price">
		<acme:priceFormat code="price.format" value="${exhibition.price}"/>
	</display:column>
	
	<security:authorize access="hasRole('DIRECTOR')">
		<display:column titleKey="exhibition.sponsorships">
			<a href="sponsorship/director/list.do?exhibitionId=${exhibition.id}"><spring:message code="sponsorship.list"/></a>
		</display:column>
	</security:authorize>
	
	<display:column>
		<jstl:if test="${now < exhibition.startingDate}">
			<a href="exhibition/director/edit.do?exhibitionId=${exhibition.id}"><spring:message code="exhibition.edit"/></a>
		</jstl:if>
	</display:column>
	
	<display:column>
		<jstl:if test="${now < exhibition.startingDate}">
			<a href="exhibition/director/addGuide.do?exhibitionId=${exhibition.id}"><spring:message code="exhibition.addGuide"/></a>
		</jstl:if>
	</display:column>
	
</display:table>

<br/>
<br/>
<a href="exhibition/director/create.do"><spring:message code="exhibition.create"/></a>
