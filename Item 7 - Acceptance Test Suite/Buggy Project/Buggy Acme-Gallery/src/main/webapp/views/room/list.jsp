 <%--
 * room/list.jsp
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

<h3 style="text-align:center; text-decoration:underline;"><spring:message code="room.museum.link"/><a href="museum/director/display.do?museumId=${museum.id}"><jstl:out value="${museum.name}"/></a></h3>

<display:table name="rooms" id="room" requestURI="room/${actorWS}list.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">

	<display:column titleKey="room.name" property="name"/>
	
	<display:column titleKey="room.area">
		<jstl:out value="${room.area}"/> m<sup>2</sup>
	</display:column>
	
	<display:column titleKey="room.inRepair">
		<jstl:if test="${room.inRepair eq true}">
			<spring:message code="room.inRepair.true"/>
		</jstl:if>
		<jstl:if test="${room.inRepair eq false}">
			<spring:message code="room.inRepair.false"/>
		</jstl:if>
	</display:column>
	
	<display:column>
		<a href="room/director/display.do?roomId=${room.id}"><spring:message code="room.display"/></a>
	</display:column>
	
</display:table>