package pl.recommendations.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by marekmagik on 2015-01-20.
 */
@Controller
//@SessionAttributes(FacebookSLO.FACEBOOK_SESSION_ATTRIBUTE)
public class LoginPageController {

//	@Autowired
//	private FacebookSLO facebookSLO;

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public void logOff(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		facebookSLO.loguot(request, response);
	}

	@RequestMapping(value = "/facebook-login", method = RequestMethod.GET)
	public void facebookLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		facebookSLO.login(request, response);
	}

	@RequestMapping(value = "/facebook-login-callback", method = RequestMethod.GET)
	public void facebookLoginCallback(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		facebookSLO.setAuthorization(request, response);
	}
}
