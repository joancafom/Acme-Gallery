<%--
 * index.jsp
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
<script>
	document.title="Acme Gallery";
</script>

<style>
	#backgroundBlur{
		background-image: url('images/wallpapers/${wallpaperName}');
		background-size: 100%;
		
	}

	#content-wrapper{
		margin:0 auto;
		min-height:570px;
  		
  		width:100%;
  		


	}
	#menuSkinny{
		margin-bottom:0px;
	}
	
	.content {
		

	}
	
</style>
<div class="content" style="text-align:center;color:white;">
<h1><strong><spring:message code="welcome.welcome"/></strong></h1>
<h2><spring:message code="welcome.greeting.current.time" /> ${moment}</h2> 
<br>
<a href="?language=en"><img src="https://upload.wikimedia.org/wikipedia/commons/f/f2/Flag_of_Great_Britain_%281707%E2%80%931800%29.svg" style="width:3%"></a> <a href="?language=es"><img src="https://upload.wikimedia.org/wikipedia/en/thumb/9/9a/Flag_of_Spain.svg/1280px-Flag_of_Spain.svg.png" style="width:3%"></a>
</div>