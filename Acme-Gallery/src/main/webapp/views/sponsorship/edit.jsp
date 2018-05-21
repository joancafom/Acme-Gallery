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

<script type="text/javascript">

	function statusChanged(){
		
		/* 
			Used for hidding inputs accordingly to what is selected.
		*/
		
		var selection = document.getElementById("statusSelector").value;
		var datesDiv = document.getElementById("dates");
		var startingDateInput = document.getElementById("startingDateInput");
		var endingDateInput = document.getElementById("endingDateInput");
		
		if(selection == "TIME_NEGOTIATION")
		{
			datesDiv.style.display = "block";
		}else{
			datesDiv.style.display = "none";
			startingDateInput.value = "";
			endingDateInput.value = "";
		}
	}
	
	$(document).ready(function(){
		statusChanged();
	});
	
</script>

<form:form action="sponsorship/${actorWS}edit.do" modelAttribute="sponsorship">

	<form:hidden path="id"/>
	
	<spring:message code="sponsorship.status.rejected" var="statusRejected"/>
	<spring:message code="sponsorship.status.timeNegotiation" var="statusTimeNegotiation"/>
	<form:label path="status">
		<strong><spring:message code="sponsorship.status" /></strong>
	</form:label>	
	<form:select id="statusSelector" path="status" onchange="statusChanged()">
		<form:option value="" label="----" />		
		<form:option value="TIME_NEGOTIATION" label="${statusTimeNegotiation}" />		
		<form:option value="REJECTED" label="${statusRejected}" />		
	</form:select>
	<form:errors path="status" cssClass="error" />
	
   <br><br>
   
   <div id="dates">
   		<acme:date id="startingDateInput" code="sponsorship.startingDate" path="startingDate" />
   		<acme:date id="endingDateInput" code="sponsorship.endingDate" path="endingDate"/>
   		
   		<jstl:if test="${not empty sponsorships}">
   			<spring:message code="dates.unavailable.info" />:
   			<br>
   			<br>
   			<ul>
		   		<jstl:forEach items="${sponsorships}" var="currentSponsorship">
		   			<li><acme:dateFormat code="date.format" value="${currentSponsorship.startingDate}"/> - <acme:dateFormat code="date.format" value="${currentSponsorship.endingDate}"/></li>
		   		</jstl:forEach>
	   		</ul>
	   		<br>
   		</jstl:if>
   		
   </div>
   
	<acme:cancel url="sponsorship/${actorWS}list.do?exhibitionId=${exhibitionId}" code="sponsorship.cancel"/>
	<acme:submit name="save" code="sponsorship.save"/>
	
</form:form>
