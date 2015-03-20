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

		<div class="loginDiv">
    		<div class="innerLoginBox">
				<div class="loginBoxLine">
					<div class="singleLinePadding">Login</div>
					<div class="loginBoxInputLine">
						<input class="inputLogin" name="login" type="text"/>
					</div>
				</div>
				<div class="loginBoxLine">
                	<div class="singleLinePadding">Password</div>
                	<div class="loginBoxInputLine">
						<input class="inputLogin" name="password" type="password"/>
					</div>
				</div>
				<div class="loginBoxLine">
					<@button.greenButton "Log in" "loginBoxButton"/>
				</div>
			</div>
		</div>
	</body>
</html>