<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

<#macro topBanner>
	<div class="topDiv">
		<div class="innerInlineElement">
	    	<img src="<@spring.url '/fb/resources/images/top.png'/>" alt="top banner"/>
		</div>
		<div class="innerInlineElement bannerLoggedAsCenteredVertically">

		</div>
		<#-- TODO: spring security authentication -->
	</div>
</#macro>

<#macro commonHeader>
	<link href="<@spring.url '/fb/resources/css/style.css'/>" rel="stylesheet" type="text/css">
	<link href="<@spring.url '/fb/resources/css/buttons.css'/>" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<@spring.url '/fb/resources/js/jquery-2.1.3.js'/>"></script>
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>Facebook Recommendations</title>
</#macro>