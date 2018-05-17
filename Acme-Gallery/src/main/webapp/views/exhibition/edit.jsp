<%--
 * exhibition/edit.jsp
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

<script type="text/javascript">

	$(document).ready(function(){
		checkIsPublic();
	});

	function checkIsPublic() {
		
		var a = document.getElementById("isPublic").checked;
		
		if (a) {
			document.getElementById("price").disabled = true;
			document.getElementById("price").value = 0;
		} else {
			document.getElementById("price").disabled = false;
		}
		
	}

</script>


<form:form action="exhibition/${actorWS}edit.do" modelAttribute="exhibition">

	<!-- Hidden Inputs -->
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	<jstl:if test="${exhibition.id eq 0}">
	
	<!-- Inputs -->
	<br/>
	
	<acme:textbox code="exhibition.ticker" path="ticker"/><br/>
	<acme:textbox code="exhibition.title" path="title"/><br/>
	<acme:textarea code="exhibition.description" path="description"/><br/>
	
	<br/>
	
	<acme:date code="exhibition.startingDate" path="startingDate"/><br/>
	<acme:date code="exhibition.endingDate" path="endingDate"/><br/>
	
	<br/>
	
	<acme:textarea code="exhibition.websites" path="websites"/><br/>
	
	<br/>
	
	<form:label path="isPrivate"><spring:message code="exhibition.isPrivate"/></form:label>
	<form:radiobutton path="isPrivate" value="false" id="isPublic" onchange="checkIsPublic();"/><spring:message code="exhibition.public"/>
	<form:radiobutton path="isPrivate" value="true" onchange="checkIsPublic();"/><spring:message code="exhibition.private"/>
	<form:errors cssClass="error" path="isPrivate"/>
	
	<br/>
	
	<acme:textbox code="exhibition.price" path="price" id="price"/><br/>
	
	<br/>
	
	<form:label path="category"><spring:message code="exhibition.category"/>: </form:label>
	<form:select path="category">
		<form:option value="0" label="---"/>
		<jstl:forEach items="${categories}" var="c">
			<jstl:choose>
				<jstl:when test="${c.parentCategory.name == 'CATEGORY'}">
					<form:option value="${c.id}" label="${c.name}"/>
				</jstl:when>
				<jstl:otherwise>
					<form:option value="${c.id}" label="${c.parentCategory.name} -> ${c.name}"/>
				</jstl:otherwise>
			</jstl:choose>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="category"/>
	
	<br/>
	
	<form:label path="room"><spring:message code="exhibition.room"/>: </form:label>
	<form:select path="room">
		<form:option value="0" label="---"/>
		<jstl:forEach items="${rooms}" var="r">
			<form:option value="${r.id}" label="${r.name} - ${r.museum.name}"/>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="room"/>
	
	<br/>
	
	<acme:submit name="save" code="exhibition.save"/>
	<acme:cancel url="exhibition/${actorWS}listMine.do" code="exhibition.cancel"/>
	
	</jstl:if>
	
	<jstl:if test="${exhibition.id ne 0}">
	
	<!-- Inputs -->
	<br/>
	
	<acme:textbox code="exhibition.title" path="title"/><br/>
	<acme:textarea code="exhibition.description" path="description"/><br/>
	
	<br/>
	
	<acme:textarea code="exhibition.websites" path="websites"/><br/>
	
	<br/>
	
	<acme:submit name="save" code="exhibition.save"/>
	<acme:cancel url="exhibition/${actorWS}listMine.do" code="exhibition.cancel"/>
	
	</jstl:if>
	
</form:form>

