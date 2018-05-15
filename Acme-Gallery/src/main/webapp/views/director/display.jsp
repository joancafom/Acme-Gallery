 <%--
 * category/display.jsp
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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>
<br>
<h2><jstl:out value="${director.name}"/> <jstl:out value="${director.surnames}"/></h2>
<br>
<p><strong><spring:message code="director.email"/>:</strong> <jstl:out value="${director.email}"/></p>
<p><strong><spring:message code="director.phoneNumber"/>:</strong> <jstl:out value="${director.phoneNumber}"/></p>
<p><strong><spring:message code="director.address"/>:</strong> <jstl:out value="${director.address}"/></p>
<jstl:if test="${director.gender=='MALE'}">
	<p><strong><spring:message code="director.gender"/>:</strong> <spring:message code="director.gender.male"/></p>
</jstl:if>
<jstl:if test="${director.gender=='FEMALE'}">
	<p><strong><spring:message code="director.gender"/>:</strong> <spring:message code="director.gender.female"/></p>
</jstl:if>
<jstl:if test="${director.gender=='OTHER'}">
	<p><strong><spring:message code="director.gender"/>:</strong> <spring:message code="director.gender.other"/></p>
</jstl:if>
<jstl:if test="${director.gender==null}">
	<p><strong><spring:message code="director.gender"/>:</strong> <spring:message code="director.gender.nonSpecified"/></p>
</jstl:if>
<br>
<h4><spring:message code="director.museums"/> <jstl:out value="${director.name}"/></h4>
<display:table name="museums" id="museum" requestURI="director/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${resultSize}">

	<display:column titleKey="museum.identifier" property="identifier"/>
	
	<display:column titleKey="museum.name">
		<a href="museum/${actorWS}display.do?museumId=${museum.id}"><jstl:out value="${museum.name}"/></a>
	</display:column>
	
	<display:column titleKey="museum.title">
		<jstl:if test="${museum.title ne null}">
			<jstl:out value="${museum.title}"/>
		</jstl:if>
		<jstl:if test="${museum.title eq null}">
			<jstl:out value="-"/>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="museum.price">
		<acme:priceFormat code="price.format" value="${museum.price}"/>
	</display:column>
	
	<display:column titleKey="museum.address" property="address"/>
	<display:column titleKey="museum.email" property="email"/>
</display:table>
