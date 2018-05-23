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
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<script>
	document.title="${product.name}";
</script>

<div id="backToStore">
	<a href="store/${actorWS}display.do?storeId=${product.store.id}" style="font-size:18px;"><i class="material-icons" style="font-size:16px;">arrow_back_ios</i><spring:message code="product.goBack"/></a>
</div>
	<jstl:if test="${own}">
		<p style="color:#1ebf59; font-size:20px; text-align: center;"><spring:message code="product.own"/></p>
	</jstl:if>
<br><br>

<div style="width: 100%;">
	<div style="float:left; width: 50%; text-align:center; min-height:500px;">
   		<div class="container" style="width: 70%"> 
  		<div id="myCarousel" class="carousel slide" data-ride="carousel">
    	<div class="carousel-inner">
      		<div class="item active">
       			 <img src="${product.pictures[0]}" alt="Los Angeles" style="width:100%;">
      		</div>
      
      		<jstl:forEach items="${product.pictures}" var="picture" begin="1">
      			<div class="item">
       				<img src="${picture}" style="width:100%;">
      			</div>
      		</jstl:forEach>
    	</div>
	
		<jstl:if test="${fn:length(product.pictures) > 1}">
			<a class="left carousel-control" href="#myCarousel" data-slide="prev">
      			<span class="glyphicon glyphicon-chevron-left"></span>
      			<span class="sr-only">Previous</span>
    		</a>
    		<a class="right carousel-control" href="#myCarousel" data-slide="next">
      			<span class="glyphicon glyphicon-chevron-right"></span>
      			<span class="sr-only">Next</span>
    		</a>
		</jstl:if>   
 	 </div>
	</div>
   </div>
   <div style="margin-right: 100px; max-height:500px;">
   		<h2><jstl:out value="${product.name}"/></h2><br>
   		<p><jstl:out value="${product.description}"/></p><br></br>
   		<h3><acme:priceFormat code="price.format" value="${product.price}"/></h3>
   		<br><br><br>
   		<p><spring:message code="product.barcode"/>: <jstl:out value="${product.barcode}"/></p>
   		<br><br>
   		<jstl:if test="${own}">
   		<jstl:if test="${showConfirmation==false}">
   			<a href="product/director/display.do?productId=${product.id}&deleteConfirmation=true" style="color:red; font-size:20px;"><spring:message code="product.delete"/></a>
   		</jstl:if>
   		<jstl:if test="${showConfirmation==true}">
   			<form:form action="product/director/delete.do" modelAttribute="product">
				<form:hidden path="id"/>
	
				<p style="color:red; font-size:20px"><spring:message code="product.delete.confirmation"/></p>
				<br>
				<acme:cancel url="product/director/display.do?productId=${product.id}" code="product.cancel"/>
				<acme:cancel url="product/director/delete.do?productId=${product.id}" code="product.delete"/>
			</form:form>
   		</jstl:if>
   		</jstl:if>
   </div>
</div>
<div style="clear:both"></div>











