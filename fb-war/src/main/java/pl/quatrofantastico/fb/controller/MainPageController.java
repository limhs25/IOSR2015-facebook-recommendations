package pl.quatrofantastico.fb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Created by marekmagik on 2015-01-19.
 */
@Controller
@SessionAttributes("facebook")
public class MainPageController {

	private static final String LOGIN_VIEW_NAME = "login";

	private static final String MAIN_VIEW_NAME = "main";

	private static final String FACEBOOK_SESSION_ATTRIBUTE = "facebook";

	@RequestMapping("/")
	public ModelAndView showLoginForm(){
		return new ModelAndView(LOGIN_VIEW_NAME);
	}

	@RequestMapping("/main")
	public ModelAndView showMainForm(HttpSession session) {

		ModelAndView mv = new ModelAndView(MAIN_VIEW_NAME);
		mv.addObject(FACEBOOK_SESSION_ATTRIBUTE, session.getAttribute(FACEBOOK_SESSION_ATTRIBUTE));

		return mv;
	}
}
