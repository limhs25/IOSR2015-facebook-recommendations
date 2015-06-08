package pl.recommendations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import pl.recommendations.slo.TwitterSLO;
import twitter4j.JSONException;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.servlet.http.HttpSession;

/**
 * Created by Taz on 2015-05-27.
 */
@Controller
@SessionAttributes(TwitterSLO.TWITTER_SESSION_ATTRIBUTE)
public class GraphPageController {

    private static final String GRAPH_VIEW_NAME = "graph";

    @Autowired
    private TwitterSLO twitterSLO;

    @RequestMapping("/graph")
    public ModelAndView showGraphForm(HttpSession session) throws TwitterException, JSONException {
        ModelAndView mv = new ModelAndView(GRAPH_VIEW_NAME);
        mv.addObject(TwitterSLO.TWITTER_SESSION_ATTRIBUTE, session.getAttribute(TwitterSLO.TWITTER_SESSION_ATTRIBUTE));

        Twitter twitter = (Twitter) session.getAttribute(TwitterSLO.TWITTER_SESSION_ATTRIBUTE);
        mv.addObject("graphData", twitterSLO.getGraphData(twitter));

        return mv;
    }
}
