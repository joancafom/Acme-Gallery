 <%--
 * exhibition/search.jsp
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

<h1 style="text-align:center"><strong><jstl:out value="${artwork.title}"/></strong></h1>
<img src="${artwork.photograph}" alt="${artwork.title}" title="${artwork.title}" style="max-width:200px; display:block; margin-left: auto; margin-right:auto; text-align:center;"/><br/>

<jstl:if test="${artwork.isHighlight eq true}">
	<h3 style="color:gold; text-align:center;"><spring:message code="artwork.highlight"/></h3>
</jstl:if>

<security:authorize access="hasAnyRole('ADMINISTRATOR','DIRECTOR', 'GUIDE', 'CRITIC', 'SPONSOR')">
	<jstl:if test="${artwork.isFinal eq true}">
		<h3 style="text-align: center; font-style: italic;"><spring:message code="artwork.isFinal.true"/></h3>
	</jstl:if>
	<jstl:if test="${artwork.isFinal eq false}">
		<h3 style="text-align: center; font-style: italic;"><spring:message code="artwork.isFinal.false"/></h3>
	</jstl:if>
</security:authorize>

<div style="display:block; margin-left: auto; margin-right:auto; text-align:center; font-weight: bold;">
	<jstl:if test="${artwork.creatorName ne null}">
		<jstl:out value="${artwork.creatorName}"/> - 
	</jstl:if>
	<jstl:if test="${artwork.creatorName eq null}">
		<spring:message code="artwork.creatorName.unknown"/> - 
	</jstl:if>
	
	<jstl:if test="${artwork.year ne null}">
		<jstl:out value="${artwork.year}"/>
	</jstl:if>
	<jstl:if test="${artwork.year eq null}">
		<spring:message code="artwork.year.unknown"/>
	</jstl:if>
</div>

<p style="text-align:center"><strong><spring:message code="artwork.exhibition"/> <em><a style="text-decoration:underline;" href="exhibition/${actorWS}display.do?exhibitionId=${artwork.exhibition.id}"><jstl:out value="${artwork.exhibition.title}"/></a></em>.</strong></p>

<div style="padding-left:70px; padding-right:70px; padding-bottom:70px; padding-top:20px;">
	<jstl:out value="${artwork.remark}"/>
</div>