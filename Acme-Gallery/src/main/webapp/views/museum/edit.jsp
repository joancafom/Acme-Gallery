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

<form:form action="museum/director/edit.do" modelAttribute="museum">
	
	<form:hidden path="id"/>
	
   <acme:textbox code="museum.name" path="name" />
   
   <jstl:choose>
   		<jstl:when test="${museum.id == 0}">
   			<acme:textbox code="museum.identifier" path="identifier" placeholder="MdP"/>
   		</jstl:when>
   </jstl:choose>
   
   <acme:textbox code="museum.title" path="title" />
   <acme:textbox code="museum.address" path="address" />
   <acme:textbox code="museum.email" path="email" />
   <acme:textbox code="museum.phoneNumber" path="phoneNumber" />
   <acme:textbox code="museum.price" path="price" />
   <acme:textbox code="museum.coordinates.latitude" path="coordinates.latitude" />
   <acme:textbox code="museum.coordinates.longitude" path="coordinates.longitude" />
   <acme:textbox code="museum.banner" path="banner" />
   
   <br>
   
	<acme:cancel url="museum/director/listMine.do" code="museum.cancel"/>
	<acme:submit name="save" code="museum.save"/>
	
</form:form>
