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
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<p>
	<strong><spring:message code="invitation.host" /></strong>: <jstl:out value="${invitation.host.name}" /> <jstl:out value="${invitation.host.surnames}" />
</p>
<p>
	<jstl:if test="${invitation.message ne null}">
	  <strong><spring:message code="invitation.message" /></strong>: <jstl:out value="${invitation.message}" />
	</jstl:if>
</p>
<p>
	<strong><spring:message code="invitation.sentMoment" /></strong>: <jstl:out value="${invitation.sentMoment}" />
</p>
<p>
	<jstl:choose>
	  <jstl:when test="${invitation.isAccepted eq null}">
	    <spring:message code="invitation.isAccepted.pending" />
	  </jstl:when>
	  <jstl:when test="${invitation.isAccepted}">
	    <spring:message code="invitation.isAccepted.accepted" />
	  </jstl:when>
	  <jstl:otherwise>
	  	<spring:message code="invitation.isAccepted.rejected" />
	  </jstl:otherwise>
	</jstl:choose>
</p>
<hr>

<h4><spring:message code="group.details2" /></h4>

<p>
	<jstl:choose>
		<jstl:when test="${invitation.isAccepted}">
			<strong><spring:message code="group.name" /></strong>: <a href="group/visitor/display.do?groupId=${invitation.group.id}"><jstl:out value="${invitation.group.name}" /></a>
		</jstl:when>
		<jstl:otherwise>
			<strong><spring:message code="group.name" /></strong>: <jstl:out value="${invitation.group.name}" />
		</jstl:otherwise>
	</jstl:choose>
	
</p>
<p>
	<strong><spring:message code="group.description" /></strong>: <jstl:out value="${invitation.group.description}" />
</p>
<p>
	<strong><spring:message code="group.meetingDate" /></strong>: <acme:dateFormat code="date.format" value="${invitation.group.meetingDate}"/>
</p>

<jstl:if test="${invitation.isAccepted eq null}">
	<form:form action="invitation/visitor/process.do?invitationId=${invitation.id}" modelAttribute="invitation">
		<acme:submit name="accept" code="invitation.accept"/>
		<acme:submit name="reject" code="invitation.reject"/>
	</form:form>
</jstl:if>











