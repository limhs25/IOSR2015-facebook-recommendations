package pl.recommendations.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.recommendations.slo.TwitterSLO;

import javax.servlet.http.HttpSession;

/**
 * Created by marekmagik on 2015-01-19.
 */
@RestController
public class MainPageController {
    private static final String LOGIN_VIEW_NAME = "login";
    private static final String MAIN_VIEW_NAME = "main";

    @RequestMapping("/")
    public ModelAndView showLoginForm() {
        return new ModelAndView(LOGIN_VIEW_NAME);
    }

    @RequestMapping("/main")
    public ModelAndView showMainForm(HttpSession session) {
        ModelAndView mv = new ModelAndView(MAIN_VIEW_NAME);
        mv.addObject(TwitterSLO.TWITTER_SESSION_ATTRIBUTE, session.getAttribute(TwitterSLO.TWITTER_SESSION_ATTRIBUTE));
        mv.addObject("graphFiles", new GraphFiles());
        return mv;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String fillDatabase(@ModelAttribute("graphFiles") GraphFiles form, BindingResult result) {
        if (result.hasErrors()) {
            return "user_create";
        }
        System.out.println(form.getPeopleNodes());

        return "redirect:/user_list.html";
    }
}
