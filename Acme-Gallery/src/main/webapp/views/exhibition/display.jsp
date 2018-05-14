<%--
 * exhibition/display.jsp
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

<link rel="stylesheet" href="styles/museum.css" type="text/css">

<div id="eHeader" class="title">
	<jstl:if test="${ad ne null}">
		<div class="advert">
			<a href="<jstl:out value="${ad.link}" />" target="_blank"><img src="<jstl:out value="${ad.banner}" />" /></a>
		</div>
	</jstl:if>
	<h2><jstl:out value="${exhibition.title}" /></h2>
</div>

<div id="eInfo" class="info container">

	<div id="left">
		<h4><spring:message code="exhibition.info"/></h4>
		
		<p><spring:message code="exhibition.from"/> <acme:dateFormat code="date.format" value="${exhibition.startingDate}"/> <spring:message code="exhibition.to"/> <acme:dateFormat code="date.format" value="${exhibition.endingDate}"/></p>
		<p><spring:message code="exhibition.type"/>: 
			<jstl:if test="${exhibition.isPrivate}">
				<spring:message code="exhibition.private"/>
			</jstl:if>
			<jstl:if test="${!exhibition.isPrivate}">
				<spring:message code="exhibition.public"/>
			</jstl:if>
		</p>
		<jstl:if test="${exhibition.isPrivate}">
			<p><spring:message code="exhibition.price"/>: <fmt:formatNumber type="currency" currencySymbol="&#8364;" pattern="${priceFormat}" value="${exhibition.price}" /></p>
		</jstl:if>
		<p><spring:message code="exhibition.identifier"/>: <em><jstl:out value="${exhibition.identifier}"/></em></p>
		<p><spring:message code="exhibition.websites"/>:</p>
		<jstl:forEach items="${exhibition.websites}" var="link">
			<a href="<jstl:out value="${link}" />" target="_blank"><jstl:out value="${link}" /></a> 
			<br>
		</jstl:forEach>
	</div>
	<div id="right">
		<h4><spring:message code="exhibition.description"/></h4>
		<p><jstl:out value="${exhibition.description}"/></p>
	</div>
</div>

<div id="eHighlights" class="info">

	<h4><spring:message code="exhibition.highlights"/></h4>
	
	<display:table name="highlights" id="highlight" requestURI="exhibition/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSizeH}">
		<display:column titleKey="highlight.title">
			<a href="highlight/${actorWS}display.do?highlightId=<jstl:out value="${highlight.id}" />"><jstl:out value="${highlight.title}" /></a>
		</display:column>
		<display:column titleKey="highlight.photograph">
			<img src="${highlight.photograph}" alt="${highlight.title}" title="${highlight.title}" style="max-width:200px;"/>
		</display:column>
		<display:column titleKey="highlight.creatorName">
			<jstl:choose>
				<jstl:when test="${highlight.creatorName eq null}">
					-
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="${highlight.creatorName}"></jstl:out>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
		<display:column titleKey="highlight.year">
			<jstl:choose>
				<jstl:when test="${highlight.year eq null}">
					-
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="${highlight.year}"></jstl:out>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
	</display:table>
</div>

<div id="eCritiques" class="info">
	<h4><spring:message code="exhibition.critiques"/></h4>
	
	<display:table name="critiques" id="critique" requestURI="exhibition/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSizeC}">
		<display:column titleKey="critique.reviewer">
			<jstl:out value="${critique.reviewer.name}" /> <jstl:out value="${critique.reviewer.surnames}" />
		</display:column>
		<display:column titleKey="critique.title">
			<a href="critique/${actorWS}display.do?critiqueId=<jstl:out value="${critique.id}" />"><jstl:out value="${critique.title}" /></a>
		</display:column>
		<display:column titleKey="critique.score">
			<%-- Loop to print the stars--%>
			<jstl:set var="counter" value="${5 - critique.score}"/>
			<jstl:forEach var="i" begin="1" end="${critique.score}">
				<span class="glyphicon glyphicon-star"></span>
			</jstl:forEach>
			<jstl:forEach var="i" begin="1" end="${counter}">
				<span class="glyphicon glyphicon-star-empty"></span>
			</jstl:forEach>
		</display:column>
	</display:table>
</div>

<div id="eGuides" class="info">
	<h4><spring:message code="exhibition.guides"/></h4>
	
	<display:table name="guides" id="guide" requestURI="exhibition/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSizeG}">
		<display:column titleKey="guide.name" property="name" />
		<display:column titleKey="guide.surnames" property="surnames" />
		<display:column titleKey="guide.email">
			<a href="mailto:<jstl:out value="${guide.email}" />"><jstl:out value="${guide.email}" /></a>
		</display:column>
	</display:table>
</div>

