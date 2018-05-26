<%--
 * room/edit.jsp
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

<form:form action="room/${actorWS}edit.do" modelAttribute="room">

	<!-- Hidden Inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<!-- Inputs -->
	<br/>
	
	<jstl:if test="${empty museums}">
		<p style="color:red; font-weight: bold;"><spring:message code="museums.size"/></p><br/>
	</jstl:if>
	
	<br/>
	
	<jstl:if test="${not empty museums}">
	
		<acme:textbox code="room.name" path="name"/><br/>
		<acme:textbox code="room.area" path="area"/><br/>
	
		<br/>
	
		<strong><form:label path="museum"><spring:message code="room.museum"/>: </form:label></strong>
		<form:select path="museum">
			<form:option value="0" label="---"/>
			<jstl:forEach items="${museums}" var="m">
				<form:option value="${m.id}" label="${m.name}"/>
			</jstl:forEach>
		</form:select>
		<form:errors cssClass="error" path="museum"/>
	
		<br/><br/>
	
		<acme:submit name="save" code="room.save"/>
		<acme:cancel url="room/${actorWS}listMine.do" code="room.cancel"/>
	
	</jstl:if>

	<jstl:if test="${empty museums}">
		<acme:cancel url="room/${actorWS}listMine.do" code="room.back"/>
	</jstl:if>
	
</form:form>

