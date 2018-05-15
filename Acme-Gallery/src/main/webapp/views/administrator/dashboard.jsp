 <%--
 * administrator/dashboard.jsp
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

<spring:message code="number.format" var="numberFormat"/>

<p><strong><spring:message code="avg.museumsPerDirector"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgMuseumsPerDirector}" /></p>
<p><strong><spring:message code="min.museumsPerDirector"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${minMuseumsPerDirector}" /></p>
<p><strong><spring:message code="max.museumsPerDirector"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${maxMuseumsPerDirector}" /></p>
<p><strong><spring:message code="std.museumsPerDirector"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdMuseumsPerDirector}" /></p>

<br/>

<p><strong><spring:message code="avg.ratioPrivateVSPublicExhibitionsPerMuseum"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgRatioPrivateVSPublicExhibitionsPerMuseum}" /></p>

<br/>

<h4><spring:message code="top5MoreVisitedPrivateExhibitions"/>:</h4>
<display:table name="top5MoreVisitedPrivateExhibitions" id="exhibitionA" requestURI="administrator/dashboard.do" pagesize="5" class="displaytag" style="width:100%">
	<display:column titleKey="exhibition.title">
		<a href="exhibition/${actorWS}display.do?exhibitionId=<jstl:out value="${exhibitionA.id}"/>"><jstl:out value="${exhibitionA.title}"/></a>
	</display:column>
	
	<display:column titleKey="exhibition.description" property="description" style="width:60%" class="moreInfo"/>
	
	<display:column titleKey="exhibition.price" style="width:5%;">
		<acme:priceFormat code="price.format" value="${exhibitionA.price}"/>
	</display:column>
	
	<display:column titleKey="exhibition.dates" style="width:20%">
		<spring:message code="exhibition.from"/> <acme:dateFormat code="date.format" value="${exhibitionA.startingDate}"/> <spring:message code="exhibition.to"/> <acme:dateFormat code="date.format" value="${exhibitionA.endingDate}"/> 
	</display:column>
</display:table>

<br/>

<p><strong><spring:message code="avg.pricePrivateDayPasses"/>:</strong> <acme:priceFormat code="price.format" value="${avgPricePrivateDayPasses}"/></p>
<p><strong><spring:message code="min.pricePrivateDayPasses"/>:</strong> <acme:priceFormat code="price.format" value="${minPricePrivateDayPasses}"/></p>
<p><strong><spring:message code="max.pricePrivateDayPasses"/>:</strong> <acme:priceFormat code="price.format" value="${maxPricePrivateDayPasses}"/></p>
<p><strong><spring:message code="std.pricePrivateDayPasses"/>:</strong> <acme:priceFormat code="price.format" value="${stdPricePrivateDayPasses}"/></p>

<br/>

<p><strong><spring:message code="avg.dayPassesPerMuseum"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgDayPassesPerMuseum}" /></p>
<p><strong><spring:message code="min.dayPassesPerMuseum"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${minDayPassesPerMuseum}" /></p>
<p><strong><spring:message code="max.dayPassesPerMuseum"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${maxDayPassesPerMuseum}" /></p>
<p><strong><spring:message code="std.dayPassesPerMuseum"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdDayPassesPerMuseum}" /></p>

<br/>

<p><strong><spring:message code="ratio.rejectedSponsorships"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${ratioRejectedSponsorships}" /></p>

<br/>

<p><strong><spring:message code="ratio.acceptedSponsorships"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${ratioAcceptedSponsorships}" /></p>

<br/>
<h4><spring:message code="exhibitions10MoreSponsorhipsThanAvg"/>:</h4>
<display:table name="exhibitions10MoreSponsorhipsThanAvg" id="exhibitionB" requestURI="administrator/dashboard.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${exhibitionsSize}">
	<display:column titleKey="exhibition.title">
		<a href="exhibition/${actorWS}display.do?exhibitionId=<jstl:out value="${exhibitionB.id}"/>"><jstl:out value="${exhibitionB.title}"/></a>
	</display:column>
	
	<display:column titleKey="exhibition.description" property="description" style="width:60%" class="moreInfo"/>
	
	<display:column titleKey="exhibition.price" style="width:5%;">
		<acme:priceFormat code="price.format" value="${exhibitionB.price}"/>
	</display:column>
	
	<display:column titleKey="exhibition.dates" style="width:20%">
		<spring:message code="exhibition.from"/> <acme:dateFormat code="date.format" value="${exhibitionB.startingDate}"/> <spring:message code="exhibition.to"/> <acme:dateFormat code="date.format" value="${exhibitionB.endingDate}"/> 
	</display:column>
</display:table>

<br/>

<h4><spring:message code="top3GuidesLessExhibitions"/>:</h4>
<display:table name="top3GuidesLessExhibitions" id="guide" requestURI="administrator/dashboard.do" pagesize="3" class="displaytag" style="width:100%">
	<display:column titleKey="guide.name" property="name" />
	<display:column titleKey="guide.surnames" property="surnames" />
	<display:column titleKey="guide.email">
		<a href="mailto:<jstl:out value="${guide.email}" />"><jstl:out value="${guide.email}" /></a>
	</display:column>
</display:table>

<br/>

<p><strong><spring:message code="avg.critiquesPerExhibition"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgCritiquesPerExhibition}" /></p>
<p><strong><spring:message code="std.critiquesPerExhibition"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdCritiquesPerExhibition}" /></p>

<br/>

<p><strong><spring:message code="avg.highlightsPerExhibition"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgHighlightsPerExhibition}" /></p>
<p><strong><spring:message code="std.highlightsPerExhibition"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdHighlightsPerExhibition}" /></p>

<br/>

<p><strong><spring:message code="ratio.bannedVisitors"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${ratioBannedVisitors}" /></p>

<br/>

<p><strong><spring:message code="ratio.bannedSponsors"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${ratioBannedSponsors}" /></p>

<br/>

<p><strong><spring:message code="avg.participantsPerOpenGroup"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgParticipantsPerOpenGroup}" /></p>
<p><strong><spring:message code="std.participantsPerOpenGroup"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdParticipantsPerOpenGroup}" /></p>

<br/>

<p><strong><spring:message code="ratio.visitorsCreatedGroups"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${ratioVisitorsCreatedGroups}" /></p>

<br/>

<h4><spring:message code="groups75MoreAnnouncementsThanAvg"/>:</h4>
<display:table name="groups75MoreAnnouncementsThanAvg" id="group" requestURI="administrator/dashboard.do" pagesize="5" class="displaytag" style="width:100%" partialList="true" size="${groupsSize}">
	<display:column titleKey="group.name" property="name"/>
	
	<display:column titleKey="group.creationMoment">
		<acme:dateFormat code="date.format" value="${group.creationMoment}"/> 
	</display:column>
	<display:column titleKey="group.meetingDate">
		<acme:dateFormat code="date.format" value="${group.meetingDate}"/> 
	</display:column>
	
	<display:column titleKey="group.isClosed">
		<jstl:if test="${group.isClosed eq true}">
			<spring:message code="group.isClosed.closed"/>
		</jstl:if>
		<jstl:if test="${group.isClosed eq false}">
			<spring:message code="group.isClosed.open"/>
		</jstl:if>
	</display:column>
	
	<display:column titleKey="group.creator">
		<jstl:out value="${group.creator.name} ${group.creator.surnames}"/>
	</display:column>
	
	<display:column titleKey="group.museum">
		<a href="museum/${actorWS}display.do?museumId=${group.museum.id}"><jstl:out value="${group.museum.name}"/></a>
	</display:column>
</display:table>

<br/>

<p><strong><spring:message code="avg.repliesPerComment"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${avgRepliesPerComment}" /></p>
<p><strong><spring:message code="std.repliesPerComment"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${stdRepliesPerComment}" /></p>

<br/>

<p><strong><spring:message code="ratio.museumsWithStore"/>:</strong> <fmt:formatNumber pattern="${numberFormat}" value="${ratioMuseumsWithStore}" /></p>

<br/>

<h4><spring:message code="top3MuseumsHighIncidents"/>:</h4>
<display:table name="top3MuseumsHighIncidents" id="museum" requestURI="administrator/dashboard.do" pagesize="3" class="displaytag" style="width:100%">

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