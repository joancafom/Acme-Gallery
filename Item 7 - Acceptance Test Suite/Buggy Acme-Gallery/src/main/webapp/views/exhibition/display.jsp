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

<jsp:useBean id="now" class="java.util.Date" />

<div id="eHeader" class="title">
	<jstl:if test="${ad ne null}">
		<div class="advert">
			<a href="<jstl:out value="${ad.link}" />" target="_blank"><img src="<jstl:out value="${ad.banner}" />" /></a>
		</div>
	</jstl:if>
	<h2><jstl:out value="${exhibition.title}" /></h2>
	<jstl:if test="${now > exhibition.endingDate}">
		<h4 style="color:red;"><spring:message code="exhibition.passed"/></h4>
	</jstl:if>
</div>

<security:authorize access="hasRole('DIRECTOR')">
	<jstl:if test="${canBeDeleted}">
		<h3 style="text-align:center; text-decoration:underline;"><a href="exhibition/director/delete.do?exhibitionId=${exhibition.id}"><spring:message code="exhibition.delete" /></a></h3>
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('VISITOR')">
	<jstl:if test="${canBuyADayPass}">
		<h3 style="text-align:center;"><a href="dayPass/visitor/create.do?exhibitionId=${exhibition.id}"><spring:message code="exhibition.buy"/></a></h3>
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('SPONSOR')">
	<jstl:if test="${canBeSponsored}">
		<h3 style="text-align:center;"><a href="sponsorship/sponsor/create.do?exhibitionId=${exhibition.id}"><spring:message code="exhibition.sponsor"/></a></h3>
	</jstl:if>
</security:authorize>

<div id="eInfo" class="info container">

	<div id="left">
		<h4><spring:message code="exhibition.info"/></h4>
		
		<p><spring:message code="exhibition.from"/> <acme:dateFormat code="date.format" value="${exhibition.startingDate}"/> <spring:message code="exhibition.to"/> <acme:dateFormat code="date.format" value="${exhibition.endingDate}"/></p>
		<p><spring:message code="exhibition.museum"/>: <a href="museum/${actorWS}display.do?museumId=<jstl:out value="${exhibition.room.museum.id}"/>"><jstl:out value="${exhibition.room.museum.name}"/></a></p>
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
		<p><spring:message code="exhibition.ticker"/>: <em><jstl:out value="${exhibition.ticker}"/></em></p>
		<p><spring:message code="exhibition.websites"/>:</p>
		<jstl:forEach items="${exhibition.websites}" var="link">
			<a href="<jstl:out value="${link}" />" target="_blank"><jstl:out value="${link}" /></a> 
			<br>
		</jstl:forEach>
		<br>
		<p><spring:message code="exhibition.category"/>: <a href="category/${actorWS}display.do?categoryId=<jstl:out value="${exhibition.category.id}"/>"><jstl:out value="${exhibition.category.name}"/></a></p>
	</div>
	<div id="right">
		<h4><spring:message code="exhibition.description"/></h4>
		<p><jstl:out value="${exhibition.description}"/></p>
	</div>
</div>

<div id="eArtworks" class="info">

	<h4><spring:message code="exhibition.artworks"/></h4>
	
	<display:table name="artworks" id="artwork" requestURI="exhibit1on/${actorWS}display.do#eArtworks" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSizeA}">
		<display:column titleKey="artwork.title">
			<a href="artwork/${actorWS}display.do?artworkId=<jstl:out value="${artwork.id}" />"><jstl:out value="${artwork.title}" /></a>
		</display:column>
		<display:column titleKey="artwork.photograph">
			<img src="${artwork.photograph}" alt="${artwork.title}" title="${artwork.title}" style="max-width:200px;"/>
		</display:column>
		<display:column titleKey="artwork.creatorName">
			<jstl:choose>
				<jstl:when test="${artwork.creatorName eq null}">
					-
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="${artwork.creatorName}"></jstl:out>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
		<display:column titleKey="artwork.year">
			<jstl:choose>
				<jstl:when test="${artwork.year eq null}">
					-
				</jstl:when>
				<jstl:otherwise>
					<jstl:out value="${artwork.year}"></jstl:out>
				</jstl:otherwise>
			</jstl:choose>
		</display:column>
		<security:authorize access="hasAnyRole('ADMINISTRATOR', 'CRITIC', 'DIRECTOR', 'GUIDE', 'SPONSOR')">
			<display:column titleKey="artwork.mode">
				<jstl:if test="${artwork.isFinal==false}">
					<p><spring:message code="artwork.mode.draft" /></p>
				</jstl:if>
				<jstl:if test="${artwork.isFinal==true}">
					<p><spring:message code="artwork.mode.final" /></p>
				</jstl:if>
			</display:column>
		</security:authorize>
		<security:authorize access="hasRole('GUIDE')">
			<display:column>
				<jstl:if test="${artwork.isFinal==false}">
				<a href="artwork/guide/edit.do?artworkId=${artwork.id}"><spring:message code="artwork.edit"/></a>
				<a href="artwork/guide/delete.do?artworkId=${artwork.id}" style="color:red;"><spring:message code="artwork.delete"/></a>
				</jstl:if>
			</display:column>
		</security:authorize>
	</display:table>
	<jstl:if test="${worksIn==true}">
		<a href="artwork/guide/create.do?exhibitionId=${exhibition.id}"><spring:message code="artwork.create"/></a>
	</jstl:if>
	
</div>

<div id="eCritiques" class="info">
	<h4><spring:message code="exhibition.critiques"/></h4>
	
	<display:table name="critiques" id="critique" requestURI="exhibition/${actorWS}display.do#eCritiques" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSizeC}">
		<display:column titleKey="critique.critic">
			<jstl:out value="${critique.critic.name}" /> <jstl:out value="${critique.critic.surnames}" />
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
	<security:authorize access="hasRole('CRITIC')">
		<jstl:if test="${now < exhibition.startingDate and canCreateCritic}">
			<br>
			<br>
			<a href="critique/critic/create.do?exhibitionId=${exhibition.id}"><spring:message code="critique.create"/></a>
		</jstl:if>
</security:authorize>
</div>

<div id="eGuides" class="info">
	<h4><spring:message code="exhibition.guides"/></h4>
	
	<display:table name="guides" id="guide" requestURI="exhibition/${actorWS}display.do#eGuides" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSizeG}">
		<display:column titleKey="guide.name" property="name" />
		<display:column titleKey="guide.surnames" property="surnames" />
		<display:column titleKey="guide.email">
			<a href="mailto:<jstl:out value="${guide.email}" />"><jstl:out value="${guide.email}" /></a>
		</display:column>
	</display:table>
</div>

