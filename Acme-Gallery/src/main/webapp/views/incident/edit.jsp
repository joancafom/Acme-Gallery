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

<form:form action="incident/guide/edit.do" modelAttribute="incident">
	
   <acme:textarea code="incident.text" path="text" />
   
   <spring:message code="incident.level.low" var="levelLow"/>
   <spring:message code="incident.level.medium" var="levelMedium"/>
   <spring:message code="incident.level.high" var="levelHigh"/>
   	<strong><form:label path="level"><spring:message code="incident.level"/>: </form:label></strong>
	<form:select path="level">
		<form:option value="LOW" label="${levelLow}"/>
		<form:option value="MEDIUM" label="${levelMedium}"/>
		<form:option value="HIGH" label="${levelHigh}"/>
	</form:select>
	<form:errors cssClass="error" path="room"/>
   
   <br>
   
	<strong><form:label path="room"><spring:message code="incident.room"/>: </form:label></strong>
	<form:select path="room">
		<form:option value="0" label="---"/>
		<jstl:forEach items="${rooms}" var="r">
			<form:option value="${r.id}" label="${r.name} - ${r.museum.name}"/>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="room"/>
   
   <br>
   <br>
   
   	<acme:submit name="save" code="incident.save"/>
	<acme:cancel url="incident/guide/list.do" code="incident.cancel"/>
	
</form:form>
