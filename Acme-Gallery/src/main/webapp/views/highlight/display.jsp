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

<h1 style="text-align:center"><strong><jstl:out value="${highlight.title}"/></strong></h1>
<img src="${highlight.photograph}" alt="${highlight.title}" title="${highlight.title}" style="max-width:200px; display:block; margin-left: auto; margin-right:auto; text-align:center;"/><br/>

<div style="display:block; margin-left: auto; margin-right:auto; text-align:center; font-weight: bold;">
	<jstl:if test="${highlight.creatorName ne null}">
		<jstl:out value="${highlight.creatorName}"/> - 
	</jstl:if>
	<jstl:if test="${highlight.creatorName eq null}">
		<spring:message code="highlight.creatorName.unknown"/> - 
	</jstl:if>
	
	<jstl:if test="${highlight.year ne null}">
		<jstl:out value="${highlight.year}"/>
	</jstl:if>
	<jstl:if test="${highlight.year eq null}">
		<spring:message code="highlight.year.unknown"/>
	</jstl:if>
</div>

<p style="text-align:center"><strong><spring:message code="highlight.exhibition"/> <em><a style="text-decoration:underline;" href="exhibition/${actorWS}display.do?exhibitionId=${highlight.exhibition.id}"><jstl:out value="${highlight.exhibition.title}"/></a></em>.</strong></p>

<div style="padding-left:70px; padding-right:70px; padding-bottom:70px; padding-top:20px;">
	<jstl:out value="${highlight.remark}"/>
</div>