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


<display:table name="announcements" id="announcement" requestURI="announcement/visitor/stream.do" pagesize="5" class="displaytag" style="width:79%" partialList="true" size="${resultSize}">
	<display:column style="text-align:center;">
		<h4><acme:dateFormat code="date.format" value="${announcement.creationMoment}"/> - <jstl:out value="${announcement.title}"/></h4>
		<p><spring:message code="announcement.atGroup"/> <a href="group/visitor/display.do?groupId=${announcement.group.id}"><jstl:out value="${announcement.group.name}"/></a></p>
		
		<jstl:if test="${announcement.picture!=null and announcement.picture!=''}">
			<img src="${announcement.picture}" style="width:10%;"/>
			<br>
		</jstl:if>
		<br>
		<p><jstl:out value="${announcement.description}"/></p>
	</display:column>
</display:table>
