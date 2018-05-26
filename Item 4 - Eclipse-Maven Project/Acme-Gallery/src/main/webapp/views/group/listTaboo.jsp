<%--
 * announcement/list.jsp
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

<display:table name="groups" id="group" requestURI="group/administrator/listTaboo.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">
	<display:column titleKey="group.name">
		<a href="group/${actorWS}display.do?groupId=${group.id}"><jstl:out value="${group.name}"/></a>
	</display:column>
	
	<display:column titleKey="group.creationMoment">
		<acme:dateFormat code="date.format" value="${group.creationMoment}"/> 
	</display:column>
	<display:column titleKey="group.meetingDate">
		<acme:dateFormat code="date.format" value="${group.meetingDate}"/> 
	</display:column>
	
	<display:column titleKey="group.isClosed">
		<jstl:if test="${group.isClosed eq true}">
			<spring:message code="group.isClosed.closed"/>
		</jstl:if>
		<jstl:if test="${group.isClosed eq false}">
			<spring:message code="group.isClosed.open"/>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="group.creator">
		<jstl:out value="${group.creator.name} ${group.creator.surnames}"/>
	</display:column>
	
	<display:column titleKey="group.museum">
		<a href="museum/${actorWS}display.do?museumId=${group.museum.id}"><jstl:out value="${group.museum.name}"/></a>
	</display:column>
	
	<display:column>
		<a href="group/administrator/delete.do?groupId=${group.id}&listTabooed=true"><spring:message code="group.delete" /></a>
	</display:column>
</display:table>