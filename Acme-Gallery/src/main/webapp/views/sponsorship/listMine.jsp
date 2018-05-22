 <%--
 * sponsorship/listMine.jsp
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
<%@taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<display:table name="sponsorships" id="sponsorship" requestURI="sponsorship/${actorWS}listMine.do" pagesize="5" class="displaytag" style="width:100%">

	<display:column titleKey="sponsorship.banner">
		<a href="<jstl:out value="${sponsorship.banner}" />" target="_blank"><img src="<jstl:out value="${sponsorship.banner}" />" style="max-width: 200px;"></a>
	</display:column>
	
	<display:column titleKey="sponsorship.link">
		<a href="<jstl:out value="${sponsorship.link}" />"><jstl:out value="${sponsorship.link}" /></a>
	</display:column>
	
	<display:column titleKey="sponsorship.creditcard.number">
		<jstl:if test="${sponsorship.creditCard.number ne null}">
			<jstl:out value="**** **** **** ${fn:substring(sponsorship.creditCard.number, 12, 16)}"/>
		</jstl:if>
		<jstl:if test="${sponsorship.creditCard.number eq null}">
			<jstl:out value="-"/>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="sponsorship.status">
		<jstl:choose>
			<jstl:when test="${sponsorship.status eq 'ACCEPTED'}">
				<spring:message code="sponsorship.status.accepted"/>
			</jstl:when>
			<jstl:when test="${sponsorship.status eq 'PENDING'}">
				<spring:message code="sponsorship.status.pending"/>
			</jstl:when>
			<jstl:when test="${sponsorship.status eq 'REJECTED'}">
				<spring:message code="sponsorship.status.rejected"/>
			</jstl:when>
			<jstl:when test="${(sponsorship.status eq 'TIME_NEGOTIATION') and (sponsorship.creditCard.number eq null) and (sponsorship.startingDate ne null) and (sponsorship.startingDate < currentDate)}">
				<spring:message code="sponsorship.status.expired"/>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="sponsorship.status.timeNegotiation"/>
			</jstl:otherwise>
		</jstl:choose>
	</display:column>
	
	<display:column titleKey="sponsorship.startingDate">
		<jstl:if test="${sponsorship.startingDate ne null}">
			<acme:dateFormat code="date.format" value="${sponsorship.startingDate}"/>
		</jstl:if>
		<jstl:if test="${sponsorship.startingDate eq null}">
			<jstl:out value="-"/>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="sponsorship.endingDate">
		<jstl:if test="${sponsorship.endingDate ne null}">
			<acme:dateFormat code="date.format" value="${sponsorship.endingDate}"/>
		</jstl:if>
		<jstl:if test="${sponsorship.endingDate eq null}">
			<jstl:out value="-"/>
		</jstl:if>
	</display:column>
</display:table>
