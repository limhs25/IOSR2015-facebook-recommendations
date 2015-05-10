package pl.recommendations.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import pl.recommendations.slo.TwitterSLO;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by marekmagik on 2015-01-19.
 */
@Controller
@SessionAttributes(TwitterSLO.TWITTER_SESSION_ATTRIBUTE)
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

        /* Test users list until analiser is implemented */
        ArrayList<String> users = new ArrayList<String>();
        users.add("User1");
        users.add("User2");
        users.add("User3");
        mv.addObject("recommendedUsers", users);

        return mv;
    }

    @RequestMapping("/crawl")
    public ModelAndView crawl(HttpSession session) throws TwitterException {
        Twitter twitter = (Twitter) session.getAttribute(TwitterSLO.TWITTER_SESSION_ATTRIBUTE);

        if (twitter != null) {
            twitter.getFavorites();
        }
        return new ModelAndView(MAIN_VIEW_NAME);
    }

}
