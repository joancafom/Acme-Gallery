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

<br>

<jstl:if test="${empty remainingMembers}">
	<h4 style="color:#4286f4" ><spring:message code="invitation.message.noRemainingMembers" /></h4>
	<br>
	<acme:cancel url="group/visitor/display.do?groupId=${invitation.group.id}" code="invitation.back"/>
</jstl:if>
<jstl:if test="${not empty remainingMembers}">
	<form:form action="invitation/visitor/edit.do" modelAttribute="invitation">
		<%-- id and version are not hidden as they always must be 0 --%>
		<form:hidden path="group"/>
		
	   	<acme:textarea code="invitation.message" path="message" />
	   
	   	<form:label path="guest">
			<strong><spring:message code="invitation.guest" /></strong>:
		</form:label>	
		<form:select path="guest">
			<form:option value="0" label="----" />		
			<jstl:forEach var="visitor" items="${remainingMembers}">
				<form:option value="${visitor.id}" label="${visitor.name} ${visitor.surnames}" />	
			</jstl:forEach>
		</form:select>
		<form:errors path="guest" cssClass="error" />
		 
	   	<br>
	   	<br>
	   
	   	<acme:submit name="save" code="invitation.save"/>
		<acme:cancel url="group/visitor/display.do?groupId=${invitation.group.id}" code="invitation.cancel"/>
		
	</form:form>
</jstl:if>