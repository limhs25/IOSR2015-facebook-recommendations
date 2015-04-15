<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

<#import "spring.ftl" as spring/>
<#import "buttons.ftl" as button/>
<#import "pageTop.ftl" as top/>

<html>
	<head>
		<@top.commonHeader/>
	</head>
	<body>
		<@top.topBanner/>
		Zalogowany jako: ${twitter.screenName}<br/>
        TwitterID: ${twitter.id}<br/>
        <a href="logout.htm">Wyloguj</a><br/>
        <a href="crawl.htm">CRAWL NOW!</a><br/>
	</body>
</html>