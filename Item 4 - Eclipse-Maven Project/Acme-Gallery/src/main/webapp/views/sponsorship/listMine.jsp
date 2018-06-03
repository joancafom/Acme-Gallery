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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<%@taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<fmt:formatDate var="nextDate" value="${nextMonth}" pattern="dd/MM/yyyy HH:mm"/>
<fmt:parseDate value="${nextDate}" type="both" var="parsedNextDate" pattern="dd/MM/yyyy HH:mm" />

<display:table name="sponsorships" id="sponsorship" requestURI="sponsorship/${actorWS}listMine.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">

	<display:column titleKey="sponsorship.exhibition">
		<a href="exhibition/sponsor/display.do?exhibitionId=${sponsorship.exhibition.id}"><jstl:out value="${sponsorship.exhibition.title}"/></a>
	</display:column>

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
	
	<jsp:useBean id="now" class="java.util.Date" />
	<fmt:formatDate var="day" value="${now}" pattern="dd"/>
	<fmt:formatDate var="month" value="${now}" pattern="MM"/>
	<fmt:formatDate var="year" value="${now}" pattern="yyyy"/>
	<fmt:formatDate var="time" value="${now}" pattern="HH:mm"/>

	<jstl:if test="${month == 12}">
		<jstl:set var="nextMonth" value="01"></jstl:set>
		<jstl:set var="nextYear" value="${year+1}"></jstl:set>	
	</jstl:if>

	<jstl:if test="${month < 12}">
		<jstl:set var="nextMonth" value="${month+1}"></jstl:set>
		<jstl:set var="nextYear" value="${year}"></jstl:set>	
	</jstl:if>

	<%-- Block to both compute the color and the message --%>
	
	<fmt:formatDate var="startDate" value="${sponsorship.startingDate}" pattern="dd/MM/yyyy HH:mm"/>
	<fmt:parseDate value="${startDate}" type="both" var="startDate" pattern="dd/MM/yyyy HH:mm" />
	
	<jstl:choose>
		<jstl:when test="${sponsorship.status eq 'ACCEPTED'}">
			<spring:message var="sponsorshipStatus" code="sponsorship.status.accepted"/>
			<jstl:set var="backColor" value="#42f46b"/>
		</jstl:when>
		<jstl:when test="${sponsorship.status eq 'PENDING'}">
			<jstl:choose>
				<jstl:when test="${currentDate > sponsorship.exhibition.endingDate}">
					<spring:message var="sponsorshipStatus" code="sponsorship.status.expired"/>
					<jstl:set var="backColor" value="#d9baff"/>
				</jstl:when>
				<jstl:otherwise>
					<spring:message var="sponsorshipStatus" code="sponsorship.status.pending"/>
					<jstl:set var="backColor" value="#41a6f4"/>
				</jstl:otherwise>
			</jstl:choose>
		</jstl:when>
		<jstl:when test="${sponsorship.status eq 'REJECTED'}">
			<spring:message var="sponsorshipStatus" code="sponsorship.status.rejected"/>
			<jstl:set var="backColor" value="#f45642"/>
		</jstl:when>
		<jstl:when test="${(sponsorship.status eq 'TIME_NEGOTIATION') and (sponsorship.creditCard.number eq null) and (sponsorship.startingDate ne null) and (sponsorship.startingDate < currentDate)}">
			<spring:message var="sponsorshipStatus" code="sponsorship.status.expired"/>
			<jstl:set var="backColor" value="#d9baff"/>
		</jstl:when>
		<jstl:otherwise>
			<spring:message var="sponsorshipStatus" code="sponsorship.status.timeNegotiation"/>
			<jstl:choose>
				<jstl:when test="${startDate < parsedNextDate}">
					<jstl:set var="backColor" value="#f4aa42"></jstl:set>
				</jstl:when>
				<jstl:otherwise>
					<jstl:set var="backColor" value="#e9f241"/>
				</jstl:otherwise>
			</jstl:choose>
		</jstl:otherwise>
	</jstl:choose>
	
	<%-- End of the block --%>
	
	<display:column titleKey="sponsorship.status" style="background-color:${backColor};">
		<jstl:out value="${sponsorshipStatus}" />
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
	
	<display:column>
		<jstl:if test="${(sponsorship.status eq 'TIME_NEGOTIATION') and (sponsorship.creditCard.number eq null) and (sponsorship.startingDate ne null) and (sponsorship.startingDate > currentDate)}">
			<a href="sponsorship/sponsor/accept.do?sponsorshipId=${sponsorship.id}"><spring:message code="sponsorship.accept"/></a>
		</jstl:if>
	</display:column>
	
	<display:column>
		<jstl:if test="${(sponsorship.status eq 'TIME_NEGOTIATION') and (sponsorship.creditCard.number eq null) and (sponsorship.startingDate ne null) and (sponsorship.startingDate > currentDate)}">
			<a href="sponsorship/sponsor/reject.do?sponsorshipId=${sponsorship.id}"><spring:message code="sponsorship.reject"/></a>
		</jstl:if>
	</display:column>
</display:table>
