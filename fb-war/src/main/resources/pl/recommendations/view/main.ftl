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
        <div class="col-sm-3 col-md-2 sidebar">
            <ul class="nav nav-sidebar">
                <li><a href="crawl.htm">Run crawler</a></li>
            </ul>
        </div>
        <div class="col-sm-9 col-md-10 main">
            <h2 class="page-header">Dashboard</h2>
            <h4>Username: ${twitter.screenName}</h4>
            <h4>Twitter ID: ${twitter.id}</h4>

            <h2 class="page-header">People You should stalk !</h2>

            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list recommendedUsers as user>
                    <tr>
                        <td>${user_index + 1}</td>
                        <td>${user}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
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