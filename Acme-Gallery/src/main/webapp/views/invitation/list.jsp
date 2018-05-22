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

<display:table name="invitations" id="invitation" requestURI="invitation/${actorWS}list.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">
	
	<display:column titleKey="invitation.host">
		<jstl:out value="${invitation.host.name}" /> <jstl:out value="${invitation.host.surnames}" />
	</display:column>
	
	<display:column titleKey="invitation.group">
		<jstl:out value="${invitation.group.name}" />
	</display:column>
	
	<display:column titleKey="invitation.message" property="message" />
	
	<display:column titleKey="invitation.sentMoment">
		<acme:dateFormat code="date.format" value="${invitation.sentMoment}"/> 
	</display:column>
	
	<display:column titleKey="invitation.status">
		<jstl:if test="${invitation.isAccepted eq true}">
			<spring:message code="invitation.isAccepted.accepted"/>
		</jstl:if>
		<jstl:if test="${invitation.isAccepted eq false}">
			<spring:message code="invitation.isAccepted.rejected"/>
		</jstl:if>
		<jstl:if test="${invitation.isAccepted eq null}">
			<spring:message code="invitation.isAccepted.pending"/>
		</jstl:if>
		
	</display:column>
	<display:column>
		<a href="invitation/${actorWS}display.do?invitationId=${invitation.id}"><spring:message code="invitation.show"/></a>
	</display:column>

</display:table>