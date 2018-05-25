 <%--
 * sponsorship/edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

	<form:form action="group/visitor/edit.do" modelAttribute="group">
		<br>
		<form:hidden path="id"/>
		<form:hidden path="version"/>
	
		<!-- Inputs -->
		
		<acme:textbox code="group.name" path="name"/><br>
		<acme:textarea code="group.description" path="description"/><br>
		<acme:textbox code="group.maxParticipants" path="maxParticipants"/><br>
		<acme:date code="group.meetingDate" path="meetingDate"/>
		
		<form:label path="isClosed"><spring:message code="group.privacy"/>:</form:label>
		<form:radiobutton path="isClosed" value="true"/><spring:message code="group.private"/>
		<form:radiobutton path="isClosed" value="false"/><spring:message code="group.public"/>
		<form:errors cssClass="errors" path="isClosed"/><br><br>
		
		<form:label path="museum"><spring:message code="group.museum"/>:</form:label>
		<form:select path="museum">
			<form:option value="0" label="----"/>
			<form:options items="${museums}" itemLabel="name" itemValue="id"/>
		</form:select>
		<form:errors cssClass="error" path="museum"/>
		<br><br>
		<acme:submit name="save" code="group.save"/>
		<acme:cancel url="group/visitor/list.do" code="group.cancel"/>
		
	</form:form>

