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
<@spring.bind "graphFiles"/>
<form id="updateUser" method="post" action="/twitter/upload" >
    <fieldset>
    <@spring.bind "graphFiles.peopleNodes"/>
        <input type="file" name="peopleNodes" id="file"/>
    <@spring.bind "graphFiles.interestNodes"/>
        <input type="file" name="interestNodes" id="file"/>
    <@spring.bind "graphFiles.peopleRelations"/>
        <input type="file" name="peopleRelations" id="file"/>
    <@spring.bind "graphFiles.interestRelations"/>
        <input type="file" name="interestRelations" id="file"/>
    </fieldset>
    <input type="submit"/>
</form>
</body>
</html>