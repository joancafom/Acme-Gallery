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
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<link rel="stylesheet" href="styles/museum.css" type="text/css">

<div id="mHeader" class="title">
	<jstl:if test="${museum.banner ne null}">
		<img src="${museum.banner}" alt="${museum.name}"/>
	</jstl:if>
	<h2><jstl:out value="${museum.name}" /></h2>
	<jstl:if test="${museum.title ne null}">
		<p><jstl:out value="${museum.title}" /></p>
	</jstl:if>
</div>

<div id="mReviews" class="info">
	<h4><spring:message code="museum.reviews"/></h4>
	
	<display:table name="reviews" id="review" requestURI="museum/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">
		<display:column titleKey="review.visitor.name" property="${review.visitor.name}" style="width:20%" />
		<display:column titleKey="review.body" property="body" style="width:60%" />
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
	</display:table>
</div>

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
</div>

