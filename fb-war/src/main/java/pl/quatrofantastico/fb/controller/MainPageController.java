package pl.quatrofantastico.fb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by marekmagik on 2015-01-19.
 */
@Controller
public class MainPageController {

	private static final String VIEW_NAME = "main";

	@RequestMapping("/main")
	public ModelAndView showForm(){
		return new ModelAndView(VIEW_NAME);
	}
}
