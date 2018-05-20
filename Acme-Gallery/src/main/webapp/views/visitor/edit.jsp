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

<spring:message code="visitorTitle.edit" var="actorTitle"></spring:message>

<script type="text/javascript">

	$(document).ready(function(){
		document.title = '${actorTitle}';
	});

</script>

<h3><jstl:out value="${actorTitle}" /></h3>

<form:form action="visitor/visitor/edit.do" modelAttribute="actorEditionForm">

	<!-- Hidden Inputs -->
	
	<!-- Inputs -->
	
	<h3 style="text-decoration:underline"><spring:message code="visitor.personalData"/></h3>
	<acme:textbox code="visitor.name" path="name"/><br>
	<acme:textbox code="visitor.surnames" path="surnames"/><br>
	<acme:textbox code="visitor.email" path="email"/><br>
	<acme:textbox code="visitor.phoneNumber" path="phoneNumber" placeholder="(+)333333333"/><br>
	<acme:textbox code="visitor.address" path="address"/><br>
	
	<spring:message code="visitor.gender.male" var="genderMale" />
	<spring:message code="visitor.gender.female" var="genderFemale" />
	<spring:message code="visitor.gender.other" var="genderOther" />
	<form:label path="gender">
		<spring:message code="visitor.gender" />
	</form:label>	
	<form:select path="gender">
		<form:option value="" label="----" />
		<form:option value="MALE" label="${genderMale}" />
		<form:option value="FEMALE" label="${genderFemale}" />
		<form:option value="OTHER" label="${genderOther}" />
	</form:select>
	<form:errors path="gender" cssClass="error" />
	<br><br>
	
	<input type="submit" name="save" value="<spring:message code="visitor.save"/>"/>
	<acme:cancel url="visitor/visitor/display.do" code="visitor.cancel"/>	
	
</form:form>