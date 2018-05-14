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

<jstl:choose>
	<jstl:when test="${listTabooed}">
		<h3><spring:message code="announcement.list.taboo" /></h3>
	</jstl:when>
</jstl:choose>

<display:table name="announcements" id="announcement" requestURI="announcement/administrator/list.do" pagesize="5" class="displaytag" style="width:90%" partialList="true" size="${resultSize}">
	<display:column titleKey="announcement.title" property="title"/>
	<display:column titleKey="announcement.description" property="description"/>
	<display:column titleKey="announcement.picture">
		<jstl:if test="${announcement.picture ne null}">
			<img src="${announcement.picture}" alt="${announcement.title}" title="${announcement.title}" style="max-width:200px;"/>
		</jstl:if>
	</display:column>
	<display:column titleKey="announcement.creationMoment">
		<acme:dateFormat code="date.format" value="${announcement.creationMoment}"/>
	</display:column>
	<display:column>
		<a href="announcement/administrator/delete.do?announcementId=${announcement.id}&listTabooed=${listTabooed}"><spring:message code="announcement.delete" /></a>
	</display:column>
</display:table>
