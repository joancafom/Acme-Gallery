<%--
 * exhibition/edit.jsp
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

<form:form action="exhibition/${actorWS}editDetails.do" modelAttribute="exhibition">

	<!-- Hidden Inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<!-- Inputs -->
	<br/>
	
	<acme:textbox code="exhibition.title" path="title"/><br/>
	<acme:textarea code="exhibition.description" path="description"/><br/>
	
	<br/>
	
	<acme:textarea code="exhibition.websites" path="websites"/><br/>

	<br/>
	
	<strong><form:label path="category"><spring:message code="exhibition.category"/>: </form:label></strong>
	<form:select path="category">
		<form:option value="0" label="---"/>
		<jstl:forEach items="${categories}" var="c">
			<jstl:choose>
				<jstl:when test="${c.parentCategory.name == 'CATEGORY'}">
					<form:option value="${c.id}" label="${c.name}"/>
				</jstl:when>
				<jstl:otherwise>
					<form:option value="${c.id}" label="${c.parentCategory.name} -> ${c.name}"/>
				</jstl:otherwise>
			</jstl:choose>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="category"/>
	
	<br/><br/>
	
	<acme:submit name="save" code="exhibition.save"/>
	<acme:cancel url="exhibition/${actorWS}listMine.do" code="exhibition.cancel"/>
	
</form:form>

