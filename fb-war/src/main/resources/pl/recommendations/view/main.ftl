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

            <form action="/dev/null">
                <label>
                    <select class="data-input-type">
                        <option value="pajek">Pajek</option>
                        <option value="stanford">Stanford</option>
                        <option value="custom">Custom</option>
                    </select>
                </label>
            </form>

            <div class="pajek-input">
            <@spring.bind "pajekInput"/>
                <form id="fill" method="post" action="/twitter/upload/pajek" enctype="multipart/form-data">
                    <fieldset>
                    <@spring.bind "pajekInput.edges"/>
                        <input type="file" name="edges" id="file"/>
                    </fieldset>
                    <button id="fill-button" value="Submit">Fill</button>
                </form>
            </div>
            <div class="stanford-input" style="display: none;">
            <@spring.bind "stanfordInput"/>
                <form id="fill" method="post" action="/twitter/upload/stanford" enctype="multipart/form-data">
                    <fieldset>
                    <@spring.bind "stanfordInput.edges"/>
                        <input type="file" name="edges" id="file"/>
                    </fieldset>
                    <button id="fill-button" value="Submit">Fill</button>
                </form>
            </div>
            <div class="custom-input" style="display: none;">
            <@spring.bind "customFiles"/>
                <form id="fill" method="post" action="/twitter/upload/custom" enctype="multipart/form-data">
                    <fieldset>
                    <@spring.bind "customFiles.peopleNodes"/>
                        <input type="file" name="peopleNodes" id="file"/>
                    <@spring.bind "customFiles.interestNodes"/>
                        <input type="file" name="interestNodes" id="file"/>
                    <@spring.bind "customFiles.peopleEdges"/>
                        <input type="file" name="peopleEdges" id="file"/>
                    <@spring.bind "customFiles.interestEdges"/>
                        <input type="file" name="interestEdges" id="file"/>
                    </fieldset>
                    <button id="fill-button" value="Submit">Fill</button>
                </form>
            </div>

            <form id="clear" method="post" action="/twitter/clear" enctype="multipart/form-data">
                <button class="clear-db-button">clear</button>
            </form>
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
<script>

    $('.data-input-type').change(function () {
        var val = $('.data-input-type').val();
        if (val === 'stanford') {
            $('.custom-input').hide();
            $('.pajek-input').hide();
            $('.stanford-input').show();
        } else if (val === 'pajek') {
            $('.custom-input').hide();
            $('.stanford-input').hide();
            $('.pajek-input').show();
        } else {
            $('.stanford-input').hide();
            $('.pajek-input').hide();
            $('.custom-input').show();
        }
    })
    
</script>
<script>
    $("#fill-button").onclick(function () {
        $("#fill").submit({url: '/twitter/upload', type: 'post'});
    });
</script>

</body>
</html>