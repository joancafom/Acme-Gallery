 <%--
 * incident/list.jsp
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

<h3 style="text-align:center; text-decoration:underline;"><spring:message code="incident.museum.link"/><a href="museum/${actorWS}display.do?museumId=${museum.id}"><jstl:out value="${museum.name}"/></a></h3>

<display:table name="incidents" id="incident" requestURI="incident/${actorWS}list.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">

	<display:column titleKey="incident.text" property="text"/>
	
	<display:column titleKey="incident.level" sortable="true">
		<jstl:if test="${incident.level eq 'LOW'}">
			<spring:message code="incident.level.low"/>
		</jstl:if>
		<jstl:if test="${incident.level eq 'MEDIUM'}">
			<spring:message code="incident.level.medium"/>
		</jstl:if>
		<jstl:if test="${incident.level eq 'HIGH'}">
			<spring:message code="incident.level.high"/>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="incident.isChecked">
		<jstl:if test="${incident.isChecked eq true}">
			<p style="color:green;"><spring:message code="incident.isChecked.true"/></p>
		</jstl:if>
		<jstl:if test="${incident.isChecked eq false}">
			<p style="color:red;"><spring:message code="incident.isChecked.false"/></p>
		</jstl:if>
	</display:column>
	
	<security:authorize access="hasRole('DIRECTOR')">
		<display:column>
			<jstl:if test="${incident.isChecked eq false}">
				<a href="incident/director/check.do?incidentId=${incident.id}&listMine=false"><spring:message code="incident.check" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>
	
	<display:column titleKey="incident.guide">
		<jstl:out value="${incident.guide.name} ${incident.guide.surnames}"/>
	</display:column>
	
	<display:column titleKey="incident.room">
		<jstl:out value="${incident.room.name}"/>
	</display:column>
	
	<security:authorize access="hasRole('DIRECTOR')">
		<display:column>
			<jstl:if test="${incident.isChecked eq false}">
				<a href="incident/director/delete.do?incidentId=${incident.id}&listMine=false"><spring:message code="incident.delete" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>
</display:table>

<security:authorize access="hasRole('GUIDE')">
	<a href="incident/guide/create.do?museumId=${museum.id}"><spring:message code="incident.create" /></a>
</security:authorize>