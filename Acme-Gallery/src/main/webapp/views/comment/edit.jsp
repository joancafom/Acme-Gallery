<%--
 * comment/edit.jsp
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

<jstl:if test="${comment.parentComment eq null}">

<form:form action="comment/${actorWS}edit.do?groupId=${comment.group.id}" modelAttribute="comment">

	<!-- Hidden Inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<!-- Inputs -->
	<br/>
	
	<acme:textbox code="comment.title" path="title"/><br/>
	<acme:textarea code="comment.description" path="description"/><br/>
	<acme:textbox code="comment.picture" path="picture"/><br/>
	
	<br/>
	
	<acme:submit name="save" code="comment.save"/>
	<acme:cancel url="group/${actorWS}display.do?groupId=${comment.group.id}" code="comment.cancel"/>
	
</form:form>

</jstl:if>

<jstl:if test="${comment.parentComment ne null}">

<form:form action="comment/${actorWS}edit.do?commentId=${comment.parentComment.id}" modelAttribute="comment">

	<!-- Hidden Inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<!-- Inputs -->
	<br/>
	
	<acme:textbox code="comment.title" path="title"/><br/>
	<acme:textarea code="comment.description" path="description"/><br/>
	<acme:textbox code="comment.picture" path="picture"/><br/>
	
	<br/>
	
	<acme:submit name="save" code="comment.save"/>
	<acme:cancel url="group/${actorWS}display.do?groupId=${comment.parentComment.group.id}" code="comment.cancel"/>
	
</form:form>

</jstl:if>