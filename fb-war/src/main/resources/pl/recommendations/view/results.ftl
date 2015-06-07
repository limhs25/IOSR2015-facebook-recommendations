<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">

<#import "spring.ftl" as spring/>
<#import "buttons.ftl" as button/>
<#import "pageTop.ftl" as top/>

<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="<@spring.url '/twitter/resources/images/Twitter_logo_blue_48.ico'/>">
<@top.commonHeader/>
    <link href="<@spring.url '/twitter/resources/css/bootstrap.min.css'/>" rel="stylesheet">
    <link href="<@spring.url '/twitter/resources/css/main.css'/>" rel="stylesheet">
</head>
<body>
    adamic: ${adamic}
</body>
</html>