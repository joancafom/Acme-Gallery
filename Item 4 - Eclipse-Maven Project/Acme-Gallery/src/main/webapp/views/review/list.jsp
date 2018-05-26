<%--
 * actor/editVisitor.jsp
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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<br>

<display:table name="reviews" id="review" requestURI="review/${actorWS}listTaboo.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">
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
		<display:column>
			<a href="review/administrator/delete.do?reviewId=${review.id}&redirect=listTaboo.do"><spring:message code="review.delete"/></a>
		</display:column>
	</display:table>