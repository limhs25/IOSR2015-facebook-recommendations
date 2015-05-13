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

<@spring.bind "graphFiles"/>
<form id="fill" method="post" action="/twitter/upload" enctype="multipart/form-data">
    <fieldset>
    <@spring.bind "graphFiles.peopleNodes"/>
        <input type="file" name="peopleNodes" id="file"/>
    <@spring.bind "graphFiles.interestNodes"/>
        <input type="file" name="interestNodes" id="file"/>
    <@spring.bind "graphFiles.peopleEdges"/>
        <input type="file" name="peopleEdges" id="file"/>
    <@spring.bind "graphFiles.interestEdges"/>
        <input type="file" name="interestEdges" id="file"/>

    <@spring.formInput "graphFiles.separator"/>
        <label for="text">Separator</label>
    </fieldset>
    <button id="fill-button" value="Submit">
</form>

<form id="clear" method="post" action="/twitter/clear" enctype="multipart/form-data">
    <button class="clear-db-button">clear</button>
</form>
</body>
<script>
    $("#fill-button").onclick(function () {
        $("#fill").submit({url: '/twitter/upload', type: 'post'});
    });


</script>
</html>