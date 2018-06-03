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

<jstl:if test="${toCreateEdit==true}">
	<form:form action="store/director/edit.do" modelAttribute="store">
		<br>
		<form:hidden path="id"/>
		<form:hidden path="version"/>
	
		<!-- Inputs -->
		
		<acme:textbox code="store.name" path="name"/><br>
		<acme:textbox code="store.logo" path="logo"/><br>
		<acme:textbox code="store.phoneNumber" path="phoneNumber" placeholder="(+)333333333"/><br>
		<acme:textbox code="store.email" path="email"/>
		<br>
		
		<acme:submit name="save" code="store.save"/>
		<jstl:if test="${store.id==0}">
			<form:hidden path="museum"/>
			<acme:cancel url="museum/director/display.do?museumId=${museum.id}" code="store.cancel"/>
		</jstl:if>
		<jstl:if test="${store.id!=0}">
			<acme:cancel url="store/director/display.do?storeId=${store.id}" code="store.cancel"/>
		</jstl:if>
	</form:form>
</jstl:if>
<jstl:if test="${toDelete==true }">
	<form:form action="store/director/delete.do" modelAttribute="store" style="text-align:center;">
		<br>
		<form:hidden path="id"/>
		<form:hidden path="version"/>
	
		<p style="color:red; font-size:20px"><spring:message code="store.delete.confirmation"/></p>
		<br>
		
		<acme:submit name="delete" code="store.delete"/>
		<jstl:if test="${store.id!=0}">
			<acme:cancel url="store/director/display.do?storeId=${store.id}" code="store.cancel"/>
		</jstl:if>
		
	</form:form>
</jstl:if>