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

<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<br>

<div style="margin: 0 auto; width:1200px; display:block;">
<h2><strong><jstl:out value="${group.name}"/></strong></h2>
<h3><jstl:out value="${group.description}"/></h3>
<security:authorize access="hasRole('VISITOR')">

	<jstl:choose>
		<jstl:when test="${isCreator}">
			<h4 style="color: #1ebf59;"><spring:message code="group.yours"/></h4>
		</jstl:when>
		<jstl:when test="${isMember}">
			<h4 style="color: #1ebf59;"><spring:message code="group.member"/></h4>
		</jstl:when>
		<jstl:otherwise>
			<jsp:useBean id="now" class="java.util.Date" />
			
			<jstl:if test="${fn:length(group.participants) lt group.maxParticipants and now < group.meetingDate}">
				<h4><a href="group/visitor/joinGroup.do?groupId=${group.id}"><spring:message code="group.notMember"/></a></h4>
			</jstl:if>
			<jstl:if test="${fn:length(group.participants) ge group.maxParticipants}">
				<h4 style="color: #f44b42;"><spring:message code="group.full"/></h4>
			</jstl:if>
			<jstl:if test="${now >= group.meetingDate}">
				<h4 style="color: #f4e842;"><spring:message code="group.past"/></h4>
			</jstl:if>
		</jstl:otherwise>
	</jstl:choose>
</security:authorize>
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

<div id="comments">
	<jstl:if test="${hasReplies==null}">
		<h3><spring:message code="group.comments"/></h3>
		<display:table name="comments" id="comment" requestURI="group/${actorWS}display.do" pagesize="5" class="displaytag" style="width:79%" partialList="true" size="${resultSizeComments}">
			<display:column>
				<div style="margin: 20px;">
				<p><strong><jstl:out value="${comment.visitor.name}"/> <jstl:out value="${comment.visitor.surnames}"/></strong> <spring:message code="comment.said"/>: <jstl:out value="${comment.title}"/></p>
				<p><jstl:out value="${comment.description}"/></p><br>
				<jstl:if test="${comment.picture!=null or comment.picture!=''}">
					<img src="${comment.picture}" style="text-align:center;"/>
				</jstl:if>
				<jstl:if test="${fn:length(comment.childrenComments)!=0}">
					<a href="group/${actorWS}display.do?groupId=${group.id}&commentId=${comment.id}#comments"><spring:message code="group.comment.replies"/></a>
				</jstl:if>
				</div>
			</display:column>
			
		</display:table>
	</jstl:if><br><br>
	<jstl:if test="${hasReplies==true}">
			<jstl:if test="${parentComment.parentComment==null}">
				<a href="group/${actorWS}display.do?groupId=${group.id}#comments" style="font-size:18px;"><i class="material-icons" style="font-size:16px;">arrow_back_ios</i><spring:message code="group.goBack"/></a>
			</jstl:if>
			<jstl:if test="${parentComment.parentComment!=null}">
				<a href="group/${actorWS}display.do?groupId=${group.id}&commentId=${parentComment.parentComment.id}#comments" style="font-size:18px;"><i class="material-icons" style="font-size:16px;">arrow_back_ios</i><spring:message code="group.goBack"/></a>
			</jstl:if>
		<h3><spring:message code="group.replies"/>: <jstl:out value="${parentComment.title}"/></h3>
		<display:table name="replies" id="reply" requestURI="group/${actorWS}display.do" pagesize="5" class="displaytag" style="width:79%" partialList="true" size="${resultSizeReplies}">
			<display:column>
				<div style="margin: 20px;">
				<p><strong><jstl:out value="${reply.visitor.name}"/> <jstl:out value="${reply.visitor.surnames}"/></strong> <spring:message code="comment.said"/>: <jstl:out value="${reply.title}"/></p>
				<p><jstl:out value="${reply.description}"/></p><br>
				<jstl:if test="${reply.picture!=null or reply.picture!=''}">
					<img src="${reply.picture}" style="text-align:center;"/>
				</jstl:if>
				<jstl:if test="${fn:length(reply.childrenComments)!=0}">
					<a href="group/${actorWS}display.do?groupId=${group.id}&commentId=${reply.id}#comments"><spring:message code="group.comment.replies"/></a>
				</jstl:if>
				</div>
			</display:column>
		</display:table>
	</jstl:if>
</div>

</div>



