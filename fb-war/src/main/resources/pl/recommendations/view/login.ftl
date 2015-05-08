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
    <link href="<@spring.url '/twitter/resources/css/jumbotron.css'/>" rel="stylesheet">
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="innerInlineElement">
            <img src="<@spring.url '/twitter/resources/images/top.png'/>" alt="top banner"/>
        </div>
    </div>
</nav>
<div class="jumbotron">
    <div class="container">
        <h1>Need more friends ? We can lend a hand !</h1>

        <p>To run application login by Twitter !</p>

        <p><a class="btn btn-primary btn-lg" href="twitter-login.htm" role="button">Login &raquo;</a></p>
    </div>
</div>
<div class="container">
    <hr>
    <footer>
        <p>&copy; IOSR 2015</p>
    </footer>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="<@spring.url '/twitter/resources/js/bootstrap.js'/>"></script>
</body>
</html>
