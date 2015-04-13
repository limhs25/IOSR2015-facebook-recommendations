package pl.recommendations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.recommendations.slo.TwitterSLO;
import twitter4j.TwitterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by marekmagik on 2015-01-20.
 */
@Controller
@SessionAttributes(TwitterSLO.TWITTER_SESSION_ATTRIBUTE)
public class LoginPageController {

	@Autowired
	private TwitterSLO twitterSLO;

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logOff(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		twitterSLO.loguot(request, response);
	}

	@RequestMapping(value = "/twitter-login", method = RequestMethod.GET)
	public void facebookLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, TwitterException {
		twitterSLO.login(request, response);
	}

	@RequestMapping(value = "/twitter-login-callback", method = RequestMethod.GET)
	public void facebookLoginCallback(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		twitterSLO.setAuthorization(request, response);
	}
}
