<%--
 * register.jsp
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

<spring:message code="actor.userAccount.passwordMatchMessage" var="passwordMatchError"></spring:message>

<jstl:if test="${actor == 'visitor'}">
	<spring:message code="visitorTitle" var="actorTitle"></spring:message>
</jstl:if>
<jstl:if test="${actor == 'sponsor'}">
	<spring:message code="sponsorTitle" var="actorTitle"></spring:message>
</jstl:if>
<jstl:if test="${actor == 'director'}">
	<spring:message code="directorTitle" var="actorTitle"></spring:message>
</jstl:if>
<jstl:if test="${actor == 'guide'}">
	<spring:message code="guideTitle" var="actorTitle"></spring:message>
</jstl:if>
<jstl:if test="${actor == 'critic'}">
	<spring:message code="criticTitle" var="actorTitle"></spring:message>
</jstl:if>

<script type="text/javascript">

	$(document).ready(function(){
		document.title = '${actorTitle}';
	});

	function checkData(){
		
		var pass1 = document.getElementById('pass1').value;
		var pass2 = document.getElementById('pass2').value;
		
		var messageDiv = document.getElementById('passwordMatchMessage');
		var saveButton = document.getElementById('boton');
		var checkboxTerms = document.getElementById('checkbox');
		
		if(!checkboxTerms.checked || pass1 != pass2 || pass1 == ''){
			
			if(pass1 != pass2){
				messageDiv.innerHTML="${passwordMatchError}";
			}
			else{
				messageDiv.innerHTML="";
			}
			
			saveButton.disabled=true;
		}
		else{
			
			messageDiv.innerHTML="";
			saveButton.disabled=false;
		}
	}
</script>

<h3><jstl:out value="${actorTitle}" /></h3>

<form:form action="${actor}/${actorWS}register.do" modelAttribute="actorRegistrationForm">

	<!-- Hidden Inputs -->
	
	<!-- Inputs -->
	
	<h3 style="text-decoration:underline"><spring:message code="actor.userAccount"/></h3>
	
	<acme:textbox code="actor.userAccount.userName" path="username"/><br>
	
	<form:label path="password"><strong><spring:message code="actor.userAccount.password" />: </strong></form:label>
	<form:password id="pass1" path="password" onkeyup="checkData()"/>
	<form:errors cssClass="error" path="password"/>
	
	<br><br>
	<label for="pass2"><strong><spring:message code="actor.userAccount.repeatPassword" />:</strong></label>
	<form:password id="pass2" path="passwordConfirmation" onkeyup="checkData()" /><br><br>
	<div id="passwordMatchMessage" style="color:red; text-decoration:underline"></div>
	<form:errors cssClass="error" path="password"/>
	
	<h3 style="text-decoration:underline"><spring:message code="actor.personalData"/></h3>
	<acme:textbox code="actor.name" path="name"/><br>
	<acme:textbox code="actor.surnames" path="surnames"/><br>
	<acme:textbox code="actor.email" path="email"/><br>
	<acme:textbox code="actor.phoneNumber" path="phoneNumber" placeholder="(+)333333333"/><br>
	<acme:textbox code="actor.address" path="address"/><br>
	
	<spring:message code="actor.gender.male" var="genderMale" />
	<spring:message code="actor.gender.female" var="genderFemale" />
	<spring:message code="actor.gender.other" var="genderOther" />
	<strong><form:label path="gender">
		<spring:message code="actor.gender" />:
	</form:label></strong>	
	<form:select path="gender">
		<form:option value="" label="----" />
		<form:option value="MALE" label="${genderMale}" />
		<form:option value="FEMALE" label="${genderFemale}" />
		<form:option value="OTHER" label="${genderOther}" />
	</form:select>
	<form:errors path="gender" cssClass="error" />
	<br><br>
	<form:checkbox path="acceptedTerms" onchange="checkData()" id="checkbox" name="checkbox" /> <spring:message code="actor.accept"/> <a href="misc/termsAndConditions.do" target="_blank"><spring:message code="actor.termsAndConditions"/></a>
	<br><br>
	
	<input type="submit" name="save" value="<spring:message code="actor.save"/>" id="boton" disabled="disabled"/>
	<acme:cancel url="welcome/index.do" code="actor.cancel"/>	
	
</form:form>