 <%--
 * category/edit.jsp
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

<script type="text/javascript" src="scripts/moment-with-locales.js" charset="UTF-8"></script>
<script type="text/javascript" src="scripts/bootstrap-datetimepicker.js" charset="UTF-8"></script>

<link rel="stylesheet" href="styles/bootstrap-datetimepicker.css" type="text/css">

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
	
	<br>
	<br>
    <div style="max-width:200px;">
    	<form:label path="startingDate">
			<strong><spring:message code="sponsorship.startingDate" /></strong>
		</form:label>	
		<br>
        <div class="form-group">
        	<div class='input-group date' id='datepicker1'>
            	<form:input path="startingDate" class="form-control"/>
                <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
               	</span>
           	</div>
        </div>
    </div>
    
    <spring:message code="datepicker.locale" var="datePickerLocale"/>
    <script type="text/javascript">
        $(function () {
            $('#datepicker1').datetimepicker({
            	locale: moment.locale('${datePickerLocale}'),
            	format: 'MM/DD/YYYY',
            	disabledDates: [
                                moment("05/17/2018")
                            ]
            });
        });
    </script>
    
	<form:errors path="startingDate" cssClass="error" />
	<br>
	<br>
	<jstl:if test="${sponsorship.exhibition ne null}">
		<acme:cancel url="sponsorship/${actorWS}list.do?exhibitionId=${sponsorship.exhibition.id}" code="sponsorship.cancel"/>
	</jstl:if>
	<jstl:if test="${sponsorship.exhibition eq null}">
		<acme:cancel url="exhibition/director/listMine.do" code="sponsorship.cancel"/>
	</jstl:if>
	
	<acme:submit name="save" code="sponsorship.save"/>
</form:form>
