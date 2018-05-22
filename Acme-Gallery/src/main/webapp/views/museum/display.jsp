<%--
 * museum.jsp
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
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<link rel="stylesheet" href="styles/museum.css" type="text/css">

<div id="mHeader" class="title">
	<jstl:if test="${museum.banner ne null}">
		<img src="${museum.banner}" alt="${museum.name}"/>
	</jstl:if>
	<h2><jstl:out value="${museum.name}" /></h2>
	<security:authorize access="hasRole('DIRECTOR')">
		<jstl:if test="${own}">
			<h3 class="messageOk"><spring:message code="museum.yours"/></h3>
		</jstl:if>
	</security:authorize>
	<jstl:if test="${museum.slogan ne null}">
		<p><jstl:out value="${museum.slogan}" /></p>
	</jstl:if>
	<jstl:if test="${museum.store ne null}">
		<p><a href="store/${actorWS}display.do?storeId=<jstl:out value="${museum.store.id}"/>"><img src="https://image.freepik.com/iconos-gratis/cesta-de-la-compra-de-diseno-a-cuadros_318-50865.jpg" style="max-width:50px;"></a></p>
	</jstl:if>
	<jstl:if test="${museum.store ==null}">
		<jstl:if test="${own}">
			<p><a href="store/director/create.do?museumId=${museum.id}"><i class="material-icons" style="font-size:16px;">add</i> <spring:message code="museum.store.create"/></a></p>
		</jstl:if>
	</jstl:if>
</div>

<security:authorize access="hasRole('VISITOR')">
	<h3 style="text-align:center;"><a href="dayPass/visitor/create.do?museumId=${museum.id}"><spring:message code="museum.buy"/></a></h3>
</security:authorize>

<div id="mInfo" class="info">
		<h4><spring:message code="museum.info"/></h4>
		
		<p><spring:message code="museum.identifier"/>: <em><jstl:out value="${museum.identifier}"/></em></p>
		<p><spring:message code="museum.address"/>:
		<jstl:choose>
			<jstl:when test="${museum.coordinates.latitude ne null and museum.coordinates.longitude ne null}">
				<a href="http://www.google.com/maps/place/${museum.coordinates.latitude},${museum.coordinates.longitude}" target="_blank"><jstl:out value="${museum.address}"/></a>
			</jstl:when>
			<jstl:otherwise>
				<jstl:out value="${museum.address}"/>
			</jstl:otherwise>
		</jstl:choose>
		 </p>
		 
		 <p><spring:message code="museum.email"/>:<a href="mailto:<jstl:out value="${museum.email}"/>"><jstl:out value="${museum.email}"/></a></p>
		 <p><spring:message code="museum.phoneNumber"/>: <jstl:out value="${museum.phoneNumber}"/></p>
		 <p><spring:message code="museum.director"/>: <a href="director/${actorWS}display.do?directorId=<jstl:out value="${museum.director.id}"/>"><jstl:out value="${museum.director.name}"/> <jstl:out value="${museum.director.surnames}"/></a></p> 
</div>

<jstl:if test="${own}">
	<h3 style="text-align:center; text-decoration:underline;"><a href="incident/${actorWS}list.do?museumId=${museum.id}"><spring:message code="museum.incidents"/></a></h3>
	<h3 style="text-align:center; text-decoration:underline;"><a href="room/${actorWS}list.do?museumId=${museum.id}"><spring:message code="museum.rooms"/></a></h3>
	<h3 style="text-align:center; text-decoration:underline;"><a href="group/${actorWS}list.do?museumId=${museum.id}"><spring:message code="museum.groups"/></a></h3>
	
</jstl:if>

<div id="mExhibitions" class="info">

	<security:authorize access="hasAnyRole('VISITOR','SPONSOR', 'CRITIC') or isAnonymous()">
		<h4><spring:message code="museum.exhibitions.currentAndFuture"/></h4>
	</security:authorize>
	<security:authorize access="hasAnyRole('ADMINISTRATOR','DIRECTOR', 'GUIDE')">
		<h4><spring:message code="museum.exhibitions"/></h4>
	</security:authorize>
	
	<spring:message code="price.format" var="priceFormat"></spring:message>
	<display:table name="exhibitions" id="exhibition" requestURI="museum/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSizeE}">
		<display:column titleKey="exhibition.title">
			<a href="exhibition/${actorWS}display.do?exhibitionId=<jstl:out value="${exhibition.id}"/>"><jstl:out value="${exhibition.title}"/></a>
		</display:column>
		<display:column titleKey="exhibition.description" property="description" style="width:60%" class="moreInfo"/>
		<display:column titleKey="exhibition.price" style="width:5%;">
			<fmt:formatNumber type="currency" currencySymbol="&#8364;" pattern="${priceFormat}" value="${exhibition.price}" />
		</display:column>
		<display:column titleKey="exhibition.dates" style="width:20%">
			<spring:message code="exhibition.from"/> <acme:dateFormat code="date.format" value="${exhibition.startingDate}"/> <spring:message code="exhibition.to"/> <acme:dateFormat code="date.format" value="${exhibition.endingDate}"/> 
		</display:column>
	</display:table>
</div>

<div id="mReviews" class="info">
	<h4><spring:message code="museum.reviews"/></h4>
	
	<display:table name="reviews" id="review" requestURI="museum/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSizeR}">
		<display:column titleKey="review.visitor.name" style="width:20%">
			<jstl:out value="${review.visitor.name}"></jstl:out> <jstl:out value="${review.visitor.surnames}"></jstl:out>
		</display:column>
		<display:column titleKey="review.body" property="body" style="width:60%" class="moreInfo" />
		<display:column titleKey="review.score" style="width:20%">
			<%-- Loop to print the stars--%>
			<jstl:set var="counter" value="5"/>
			<jstl:forEach var="i" begin="1" end="${review.score}">
				<span class="glyphicon glyphicon-star"></span>
				<jstl:set var="counter" value="${counter-1}"/>
			</jstl:forEach>
			<jstl:forEach var="i" begin="1" end="${counter}">
				<span class="glyphicon glyphicon-star-empty"></span>
			</jstl:forEach>
		</display:column>
		<display:column titleKey="review.creationDate" style="width:20%">
			<acme:dateFormat code="date.format" value="${review.creationDate}"/>
		</display:column>
		<security:authorize access="hasRole('ADMINISTRATOR')">
			<display:column>
				<a href="review/administrator/delete.do?reviewId=${review.id}"><spring:message code="review.delete"/></a>
			</display:column>
		</security:authorize>
	</display:table>
	
</div>

