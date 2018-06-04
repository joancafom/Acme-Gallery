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

	<form:form action="artwork/guide/edit.do" modelAttribute="artwork">
		<br>
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		<form:hidden path="exhibition"/>
	
		<!-- Inputs -->
		
		<acme:textbox code="artwork.title" path="title"/><br>
		<acme:textbox code="artwork.photograph" path="photograph"/><br>
		<acme:textbox code="artwork.creatorName" path="creatorName"/><br>
		<acme:textarea code="artwork.remark" path="remark"/><br>
		<acme:textbox code="artwork.year" path="year"/><br>
		
		<form:label path="isFinal"><spring:message code="artwork.isFinal"/>:</form:label>
		<form:radiobutton path="isFinal" value="true"/><spring:message code="artwork.isFinal.yes"/>
		<form:radiobutton path="isFinal" value="false"/><spring:message code="artwork.isFinal.no"/>
		<form:errors cssClass="errors" path="isFinal"/><br><br>
		
		<form:label path="isHighlight"><spring:message code="artwork.isHighlight"/>:</form:label>
		<form:radiobutton path="isHighlight" value="true"/><spring:message code="artwork.isHighlight.yes"/>
		<form:radiobutton path="isHighlight" value="false"/><spring:message code="artwork.isHighlight.no"/>
		<form:errors cssClass="errors" path="isHighlight"/><br><br>
		
		<br>
		<acme:submit name="save" code="artwork.save"/>
		<acme:cancel url="exhibition/guide/display.do?exhibitionId=${exhibition.id}" code="artwork.cancel"/>
		
	</form:form>

