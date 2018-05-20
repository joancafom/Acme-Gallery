 <%--
 * room/display.jsp
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

<spring:message code="price.format" var="priceFormat"></spring:message>

<h3 style="text-align:center;"><spring:message code="room.museum.link"/><a href="museum/director/display.do?museumId=${museum.id}"><jstl:out value="${room.museum.name}"/></a></h3>

<p><strong><spring:message code="room.name"/>:</strong> <jstl:out value="${room.name}"/></p>
<p><strong><spring:message code="room.area"/>:</strong> <jstl:out value="${room.area}"/> m<sup>2</sup></p>
<p><strong><spring:message code="room.isAvailable"/>:</strong> <jstl:if test="${room.isAvailable eq true}"><spring:message code="room.isAvailable.true"/></jstl:if><jstl:if test="${room.isAvailable eq false}"><spring:message code="room.isAvailable.false"/></jstl:if></p>
<p><strong><spring:message code="room.inRepair"/>:</strong> <jstl:if test="${room.inRepair eq true}"><spring:message code="room.inRepair.true"/></jstl:if><jstl:if test="${room.inRepair eq false}"><spring:message code="room.inRepair.false"/></jstl:if></p>

<br/>

<jstl:if test="${canBeMarkedAsInRepair}">
	<h3 style="text-align:center; text-decoration:underline;"><a href="room/${actorWS}inRepair.do?roomId=${room.id}"><spring:message code="room.markInRepair"/></a></h3>
</jstl:if>
<jstl:if test="${canBeMarkedAsNotInRepair}">
	<h3 style="text-align:center; text-decoration:underline;"><a href="room/${actorWS}notInRepair.do?roomId=${room.id}"><spring:message code="room.markNotInRepair"/></a></h3>
</jstl:if>

<br/>

<h3><spring:message code="room.currentExhibition"/></h3>

<display:table name="currentExhibition" id="currentExhibition" requestURI="room/${actorWS}display.do" pagesize="1" class="displaytag" style="width:100%">
	<display:column titleKey="exhibition.title">
		<a href="exhibition/${actorWS}display.do?exhibitionId=<jstl:out value="${currentExhibition.id}"/>"><jstl:out value="${currentExhibition.title}"/></a>
	</display:column>

	<display:column titleKey="exhibition.description" property="description" style="width:60%"/>
	<display:column titleKey="exhibition.price" style="width:5%;">
		<fmt:formatNumber type="currency" currencySymbol="&#8364;" pattern="${priceFormat}" value="${currentExhibition.price}" />
	</display:column>

	<display:column titleKey="exhibition.dates" style="width:20%">
		<spring:message code="exhibition.from"/> <acme:dateFormat code="date.format" value="${currentExhibition.startingDate}"/> <spring:message code="exhibition.to"/> <acme:dateFormat code="date.format" value="${currentExhibition.endingDate}"/> 
	</display:column>
</display:table>

<h3><spring:message code="room.allExhibitions"/></h3>

<display:table name="allExhibitions" id="exhibition" requestURI="room/${actorWS}display.do" pagesize="5" class="displaytag" style="width:100%">
	<display:column titleKey="exhibition.title">
		<a href="exhibition/${actorWS}display.do?exhibitionId=<jstl:out value="${exhibition.id}"/>"><jstl:out value="${exhibition.title}"/></a>
	</display:column>

	<display:column titleKey="exhibition.description" property="description" style="width:60%"/>
	<display:column titleKey="exhibition.price" style="width:5%;">
		<fmt:formatNumber type="currency" currencySymbol="&#8364;" pattern="${priceFormat}" value="${exhibition.price}" />
	</display:column>

	<display:column titleKey="exhibition.dates" style="width:20%">
		<spring:message code="exhibition.from"/> <acme:dateFormat code="date.format" value="${exhibition.startingDate}"/> <spring:message code="exhibition.to"/> <acme:dateFormat code="date.format" value="${exhibition.endingDate}"/> 
	</display:column>
</display:table>

<h3><spring:message code="room.incidents"/></h3>

<display:table name="incidents" id="incident" requestURI="incident/${actorWS}list.do" pagesize="5" class="displaytag" style="width:100%">

	<display:column titleKey="incident.text" property="text"/>
	
	<display:column titleKey="incident.level">
		<jstl:if test="${incident.level eq 'LOW'}">
			<spring:message code="incident.level.low"/>
		</jstl:if>
		<jstl:if test="${incident.level eq 'MEDIUM'}">
			<spring:message code="incident.level.medium"/>
		</jstl:if>
		<jstl:if test="${incident.level eq 'HIGH'}">
			<spring:message code="incident.level.high"/>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="incident.isChecked">
		<jstl:if test="${incident.isChecked eq true}">
			<p style="color:green;"><spring:message code="incident.isChecked.true"/></p>
		</jstl:if>
		<jstl:if test="${incident.isChecked eq false}">
			<p style="color:red;"><spring:message code="incident.isChecked.false"/></p>
		</jstl:if>
	</display:column>
	
	<display:column>
		<jstl:if test="${incident.isChecked eq false}">
			<a href="incident/director/check.do?incidentId=${incident.id}"><spring:message code="incident.check" /></a>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="incident.guide">
		<jstl:out value="${incident.guide.name} ${incident.guide.surnames}"/>
	</display:column>
	
	<display:column>
		<jstl:if test="${incident.isChecked eq false}">
			<a href="incident/director/delete.do?incidentId=${incident.id}"><spring:message code="incident.delete" /></a>
		</jstl:if>
	</display:column>
	
</display:table>