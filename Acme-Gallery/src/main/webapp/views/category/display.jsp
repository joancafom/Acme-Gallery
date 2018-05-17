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

<jstl:if test="${category.name ne 'CATEGORY'}">
	<h3 style="text-align:center"><spring:message code="category"/> <span style="text-decoration:underline;"><jstl:out value="${category.name}"/></span></h3><br/>
</jstl:if>

<jstl:if test="${category.parentCategory ne null and category.parentCategory.name ne 'CATEGORY'}">
	<h4 style="text-align:center"><spring:message code="category.parentCategory.message"/> <a href="category/${actorWS}display.do?categoryId=${category.parentCategory.id}"><jstl:out value="${category.parentCategory.name}"/></a></h4><br/><br/>
</jstl:if>

<!-- Children Categories -->

<h3><spring:message code="category.childrenCategories"/></h3>

<display:table name="childrenCategories" id="childCategory" requestURI="category/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${categoriesSize}">
	<display:column titleKey="category.name">
		<a href="category/${actorWS}display.do?categoryId=${childCategory.id}"><jstl:out value="${childCategory.name}"/></a>
	</display:column>
	<security:authorize access="hasRole('ADMINISTRATOR')">
		<display:column style="width:10%;">
			<a href="category/administrator/delete.do?categoryId=${childCategory.id}"><spring:message code="category.delete"/></a>
		</display:column>
	</security:authorize>
</display:table>

<!-- Exhibitions -->

<h3><spring:message code="category.exhibitions"/></h3>

<display:table name="exhibitions" id="exhibition" requestURI="category/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${exhibitionsSize}">

	<display:column titleKey="exhibition.ticker" property="ticker"/>
		
	<display:column titleKey="exhibition.title">
		<a href="exhibition/${actorWS}display.do?exhibitionId=${exhibition.id}"><jstl:out value="${exhibition.title}"/></a>
	</display:column>
	
	<display:column titleKey="exhibition.startingDate">
		<acme:dateFormat code="date.format" value="${exhibition.startingDate}"/>
	</display:column>
	
	<display:column titleKey="exhibition.endingDate">
		<acme:dateFormat code="date.format" value="${exhibition.endingDate}"/>
	</display:column>
	
	<display:column titleKey="exhibition.price">
		<acme:priceFormat code="price.format" value="${exhibition.price}"/>
	</display:column>
	
</display:table>

<hr>
<security:authorize access="hasRole('ADMINISTRATOR')">
	<a href="category/administrator/create.do?parentCategoryId=${category.id}"><spring:message code="category.create"/></a>
</security:authorize>