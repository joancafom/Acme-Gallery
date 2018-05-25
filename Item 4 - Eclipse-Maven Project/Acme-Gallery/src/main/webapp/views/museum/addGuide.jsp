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

<jstl:if test="${empty guides}">
	<h4 style="color:red;"><spring:message code="museum.guides.empty"/></h4>
	
	<acme:cancel url="museum/director/listMine.do" code="museum.cancel"/>
</jstl:if>

<jstl:if test="${not empty guides}">
<form:form action="museum/director/addGuide.do" modelAttribute="museum">

	<!-- Hidden Inputs -->
	<form:hidden path="museum"/>
	
	<!-- Inputs -->
	<br/>
	
	<form:label path="guides"><spring:message code="museum.guides.add"/>: </form:label>
	<form:select path="guides">
		<jstl:forEach items="${guides}" var="g">
			<form:option value="${g.id}" label="${g.name} ${g.surnames}"/>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="guides"/>
	
	
	<br/>
	
	<acme:submit name="save" code="museum.save"/>
	<acme:cancel url="museum/director/listMine.do" code="museum.cancel"/>
	
</form:form>
</jstl:if>

