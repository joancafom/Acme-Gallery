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

<security:authorize access="hasRole('GUIDE')">
	<jstl:if test="${landing eq 'listWorkplaces'}">
		<br>
		<h4><spring:message code="museum.workplaces" /></h4>
	</jstl:if>
</security:authorize>

<display:table name="museums" id="museum" requestURI="museum/${actorWS}${landing}.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">

	<display:column titleKey="museum.identifier" property="identifier"/>
	
	<display:column titleKey="museum.name">
		<a href="museum/${actorWS}display.do?museumId=${museum.id}"><jstl:out value="${museum.name}"/></a>
	</display:column>
	
	<display:column titleKey="museum.slogan">
		<jstl:if test="${museum.slogan ne null}">
			<jstl:out value="${museum.slogan}"/>
		</jstl:if>
		<jstl:if test="${museum.slogan eq null}">
			<jstl:out value="-"/>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="museum.address" property="address"/>
	<display:column titleKey="museum.email" property="email"/>
	
	<security:authorize access="hasRole('DIRECTOR')">
	<jstl:if test="${own}">
		<display:column>
			<a href="museum/director/edit.do?museumId=${museum.id}"><spring:message code="museum.edit"/></a>
		</display:column>
		<display:column>
			<a href="museum/director/addGuide.do?museumId=${museum.id}"><spring:message code="museum.addGuide"/></a>
		</display:column>
	</jstl:if>
</security:authorize>

</display:table>

<security:authorize access="hasRole('DIRECTOR')">
	<jstl:if test="${own}">
		<a href="museum/director/create.do"><spring:message code="museum.create" /></a>
	</jstl:if>
</security:authorize>
