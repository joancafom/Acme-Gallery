<%--
 * actor/editVisitor.jsp
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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<jstl:if test="${action=='listUnlocked'}">
	<h3><spring:message code="sponsor.list.unlocked"/></h3>
</jstl:if>
<jstl:if test="${action=='listLocked'}">
	<h3><spring:message code="sponsor.list.locked"/></h3>
</jstl:if>

<display:table name="sponsors" id="sponsor" requestURI="sponsor/${actorWS}${action}.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">
		<display:column titleKey="sponsor.name" property="name" />
		<display:column titleKey="sponsor.surnames" property="surnames" />
		<display:column titleKey="sponsor.email" property="email">
			<a href="mailto:<jstl:out value="${sponsor.email}" />"><jstl:out value="${sponsor.email}" /></a>
		</display:column>
		<display:column titleKey="sponsor.address" property="address"/>
		<display:column titleKey="sponsor.phoneNumber" property="phoneNumber"/>
		<jstl:if test="${action=='listUnlocked'}">
			<display:column>
				<jstl:if test="${sponsor.userAccount.isLocked==false}">
					<a href="sponsor/administrator/ban.do?sponsorId=${sponsor.id}"><spring:message code="sponsor.ban"/></a>
				</jstl:if>
			</display:column>
		</jstl:if>
</display:table>