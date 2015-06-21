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
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="innerInlineElement">
            <img src="<@spring.url '/twitter/resources/images/top.png'/>" alt="top banner"/>
        </div>
        <div>
            <form class="navbar-form navbar-right">
                <a class="btn btn-success" href="logout.htm">Logout</a>
            </form>
        </div>
</nav>
<div class="container">
    <div class="row">
        <div class="col-sm-3 col-md-2 col-lg-2 sidebar">
            <ul class="nav nav-sidebar">
                <li><a href="main.htm">Return</a></li>
            </ul>
        </div>

        <div id="sigma-parent" class="main">
            <div id="sigma-container"><input type="hidden" id="graph-data" value='${graphData}'/></div>
        </div>
    </div>
</div>
<div class="container">
    <hr>
    <footer>
        <p>&copy; IOSR Twitter Recommendations
            2015</p>
    </footer>
</div>
<script type="text/javascript" src="<@spring.url '/twitter/resources/js/jquery-2.1.3.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/twitter/resources/js/bootstrap.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/twitter/resources/js/sigma.min.js'/>"></script>
<script type="text/javascript" src="<@spring.url '/twitter/resources/js/graph.js'/>"></script>
</body>
</html>