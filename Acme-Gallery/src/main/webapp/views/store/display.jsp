<%--
 * museum.jsp
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

<link rel="stylesheet" href="styles/store.css" type="text/css">
<link rel="stylesheet" href="styles/displayproducts.css" type="text/css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<script>
	document.title="${store.name}";
</script>

<div id="backToMuseum">
	<a href="museum/${actorWS}display.do?museumId=${store.museum.id}" style="font-size:18px;"><i class="material-icons" style="font-size:16px;">arrow_back_ios</i><spring:message code="store.goBack"/></a>
</div>

<br><br>
<div id="storeHeader" class="title">
	<img src="${store.logo}" alt="${store.name}">
	<br><br>
	<p><strong><i class="material-icons" style="font-size:12px">contacts</i> <spring:message code="store.contact"/>:</strong> <spring:message code="store.email"/>: <jstl:out value="${store.email}"/> - <spring:message code="store.phoneNumber"/>: <jstl:out value="${store.phoneNumber}"/></p>
	<br>
	<jstl:if test="${own==true}">
		<p style="color:#1ebf59; font-size:20px;"><spring:message code="store.own"/></p>
		<p><a href="store/director/edit.do?storeId=${store.id}"><spring:message code="store.edit"/></a> - <a href="store/director/delete.do?storeId=${store.id}"><spring:message code="store.delete"/></a></p>
	</jstl:if>
</div>
<br><br>

<div class="storeProducts">
	<display:table name="products" id="product" requestURI="store/${actorWS}display.do" pagesize="5" class="displayproducts" partialList="true" size="${resultSize}">

	<display:column style="width: 50%;">
		<div style="text-align:center;">
			<a href="product/${actorWS}display.do?productId=${product.id}"><img src="${product.pictures[0]}" style="width:70%;"></a><br><br>
			<a href="product/${actorWS}display.do?productId=${product.id}"><jstl:out value="${product.name}"/></a><br><br><br><br>
		</div>
	</display:column>
	
	<display:column>
		<p><jstl:out value="${product.description}"/></p>
	</display:column>
	
	<display:column style="width:20%; text-align: right;">
		<p style="font-size:25px"><acme:priceFormat code="price.format" value="${product.price}"/></p>
	</display:column>
</display:table>
	
</div>






