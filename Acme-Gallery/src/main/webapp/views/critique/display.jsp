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

<h1 style="text-align:center"><strong><jstl:out value="${critique.title}"/></strong></h1>
<p style="text-align:center"><spring:message code="critique.by"/> ${critique.reviewer.name} ${critique.reviewer.surnames} - <acme:dateFormat code="date.format" value="${critique.creationDate}"/></p>

<p style="text-align:center"><strong><spring:message code="critique.exhibition"/> <em><a style="text-decoration:underline;" href="exhibition/${actorWS}display.do?exhibitionId=${critique.exhibition.id}"><jstl:out value="${critique.exhibition.title}"/></a></em>.</strong></p>

<div style="display:block; margin-left: auto; margin-right:auto; text-align:center;">
	<jstl:forEach var="i" begin="1" end="5">
		<jstl:if test="${critique.score >= i}">
			<img src="images/yellow_star.png" alt="+" style="width: 5%"/>
		</jstl:if>
		<jstl:if test="${critique.score < i}">
			<img src="images/gray_star.png" alt="-" style="width: 5%"/>
		</jstl:if>
	</jstl:forEach>
	<br/>
</div>

<div style="padding-left:70px; padding-right:70px; padding-bottom:70px; padding-top:20px;">
	<jstl:out value="${critique.description}"/>
</div>