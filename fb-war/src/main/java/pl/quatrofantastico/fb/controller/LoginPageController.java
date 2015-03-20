package pl.quatrofantastico.fb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by marekmagik on 2015-01-20.
 */
@Controller
public class LoginPageController {

	private static final String LOGIN_PAGE_VIEW = "login";

	@RequestMapping("/login")
	public ModelAndView showForm() {
		return new ModelAndView(LOGIN_PAGE_VIEW);
	}

}
