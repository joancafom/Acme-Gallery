<%--
 * announcement/list.jsp
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

<br>

<div style="margin: 0 auto; width:1200px; display:block;">
<h2><strong><jstl:out value="${group.name}"/></strong></h2>
<h3><jstl:out value="${group.description}"/></h3>
<br>
<div>
<h3><spring:message code="group.announcements"/></h3>
<display:table name="announcements" id="announcement" requestURI="group/${actorWS}display.do" pagesize="5" class="displaytag" style="width:79%" partialList="true" size="${resultSizeAnnouncements}">
	<display:column style="text-align:center;">
		<h4><acme:dateFormat code="date.format" value="${announcement.creationMoment}"/> - <jstl:out value="${announcement.title}"/></h4>
		
		<jstl:if test="${announcement.picture!=null or announcement.picture!=''}">
			<img src="${announcement.picture}" style="width:10%;"/>
		</jstl:if>
		<br><br>
		<p><jstl:out value="${announcement.description}"/></p>
	</display:column>
</display:table>
</div>
<br><br>

<div style="width: 100%;">
	<div style="float:left; width: 60%; min-height: 300px;">
   		<h3><spring:message code="group.participants"/></h3>
		<display:table name="participants" id="participant" requestURI="group/${actorWS}display.do" pagesize="5" class="displaytag" style="width:79%;" partialList="true" size="${resultSizeParticipants}">
			<display:column titleKey="group.participants.name">
				<jstl:out value="${participant.name}"/> <jstl:out value="${participant.surnames}"/>
			</display:column>
		</display:table>
   </div>
   <div style="margin-right: 50px;">
   		<br>
   		<h3><spring:message code="group.details"/></h3>
			<p><strong><spring:message code="group.creator"/>:</strong> <jstl:out value="${group.creator.name}"/> <jstl:out value="${group.creator.surnames}"/></p>
			<p><strong><spring:message code="group.creationMoment"/>:</strong> <acme:dateFormat code="date.format" value="${group.creationMoment}"/></p>
			<p><strong><spring:message code="group.meetingDate"/>:</strong> <acme:dateFormat code="date.format" value="${group.meetingDate}"/></p>
			<p><strong><spring:message code="group.participants"/>:</strong> <jstl:out value="${fn:length(group.participants)}"/>/<jstl:out value="${group.maxParticipants}"/></p>
			<p><strong><spring:message code="group.privacy"/>:</strong>
				<jstl:if test="${group.isClosed==true}">
					<spring:message code="group.private"/>
				</jstl:if>
				<jstl:if test="${group.isClosed==false}">
					<spring:message code="group.public"/>
				</jstl:if>
			</p>
   </div>
</div>
<br><br><br>
<h3><spring:message code="group.comments"/></h3>
<display:table name="comments" id="comment" requestURI="group/${actorWS}display.do" pagesize="5" class="displaytag" style="width:79%" partialList="true" size="${resultSizeComments}">
	<display:column>
		<p><strong><jstl:out value="${comment.visitor.name}"/> <jstl:out value="${comment.visitor.surnames}"/></strong> <spring:message code="comment.said"/>: <jstl:out value="${comment.title}"/></p>
		<p><jstl:out value="${comment.description}"/></p><br>
		<jstl:if test="${comment.picture!=null or comment.picture!=''}">
			<img src="${comment.picture}" style="text-align:center;"/>
		</jstl:if>
	</display:column>
</display:table>
</div>


