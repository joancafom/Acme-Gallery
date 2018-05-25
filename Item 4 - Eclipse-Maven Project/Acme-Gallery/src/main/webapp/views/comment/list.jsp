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

<br>
<display:table name="comments" id="comment" requestURI="comment/administrator/listTaboo.do" pagesize="5" class="displaytag" style="width:79%" partialList="true" size="${resultSize}">
	<display:column>
		<div style="margin: 20px;">
			<p><strong><jstl:out value="${comment.visitor.name}"/> <jstl:out value="${comment.visitor.surnames}"/></strong> <spring:message code="comment.said"/>: <jstl:out value="${comment.title}"/></p>
			<p><jstl:out value="${comment.description}"/></p><br>
			<jstl:if test="${comment.picture!=null or comment.picture!=''}">
				<img src="${comment.picture}" style="text-align:center;"/>
			</jstl:if>
		</div>
	</display:column>
	<display:column>
		<div style="margin: 20px;">
			<a href="comment/administrator/delete.do?commentId=${comment.id}"><spring:message code="comment.remove"/></a>
		</div>
	</display:column>
</display:table>