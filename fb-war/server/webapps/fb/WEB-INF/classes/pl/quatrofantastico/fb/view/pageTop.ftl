<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

<#macro topBanner>
	<div class="topDiv">
		<div class="innerInlineElement">
	    	<img src="<@spring.url '/bwm/resources/images/top.png'/>" alt="top banner"/>
		</div>
		<div class="innerInlineElement bannerLoggedAsCenteredVertically">
			Logged as: mm
		</div>
		<#-- TODO: spring security authentication -->
	</div>
</#macro>

<#macro commonHeader>
	<link href="<@spring.url '/bwm/resources/css/style.css'/>" rel="stylesheet" type="text/css">
	<link href="<@spring.url '/bwm/resources/css/buttons.css'/>" rel="stylesheet" type="text/css">
	<title>Business Workflow Manager</title>
</#macro>