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

</nav>
<div class="container">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
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
                    <@spring.bind "customFiles.dropRate"/>
                        <input type="text" name="dropRate" id="file" value="edge drop ratio">
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
                    <@spring.bind "customFiles.dropRate"/>
                        <input type="text" name="dropRate" id="file" value="edge drop ratio">
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
                    <@spring.bind "customFiles.dropRate"/>
                        <input type="text" name="dropRate" id="file" value="edge drop ratio">
                    <@spring.bind "customFiles.peopleNodes"/>
                        <input type="file" name="peopleNodes" id="file"/>
                    <@spring.bind "customFiles.peopleEdges"/>
                        <input type="file" name="peopleEdges" id="file"/>
                    </fieldset>
                    <button id="fill-button" value="Submit">Fill</button>
                </form>
            </div>

            <form id="clear" method="post" action="/twitter/clear" enctype="multipart/form-data">
                <button class="clear-db-button">clear</button>
            </form>

            <form id="clear" method="post" action="/twitter//suggest" enctype="multipart/form-data">
                <button class="clear-db-button">suggest</button>
            </form>
        </div>


        <div class="col-sm-9 col-md-10 main">
            <h3 class="page-header">Link prediction results [%]</h3>

            <h4>adamic: ${adamic}% </h4>
        <@spring.bind "adamicForm"/>
            <form id="clear" method="post" action="/twitter/show/adamic" enctype="multipart/form-data">
            <@spring.bind "adamicForm.count"/>
                <input type="text" name="count" id="file" value="removed edges count">
            <@spring.bind "adamicForm.maxNodes"/>
                <input type="text" name="maxNodes" id="file" value="max starting nodes">
                <button class="clear-db-button">Show</button>
            </form>
            <br>

            <h4>common neighbour: ${common}%</h4>
        <@spring.bind "commonForm"/>
            <form id="clear" method="post" action="/twitter/show/common" enctype="multipart/form-data">
            <@spring.bind "commonForm.count"/>
                <input type="text" name="count" id="file" value="removed edges count">
            <@spring.bind "commonForm.maxNodes"/>
                <input type="text" name="maxNodes" id="file" value="max starting nodes">
                <button class="clear-db-button">Show</button>
            </form>
            <br>

            <h4>resource allocation: ${resource}%</h4>
        <@spring.bind "resForm"/>
            <form id="clear" method="post" action="/twitter/show/resource" enctype="multipart/form-data">
            <@spring.bind "resForm.count"/>
                <input type="text" name="count" id="file" value="removed edges count">
            <@spring.bind "resForm.maxNodes"/>
                <input type="text" name="maxNodes" id="file" value="max starting nodes">
                <button class="clear-db-button">Show</button>
            </form>
            <br>

        </div>
    </div>
</div>
<div class="container">
    <hr>
    <footer>
        <p>&copy; TOiK 2015</p>
    </footer>
</div>

<script src="<@spring.url '/twitter/resources/js/jquery-2.1.3.js'/>"></script>
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