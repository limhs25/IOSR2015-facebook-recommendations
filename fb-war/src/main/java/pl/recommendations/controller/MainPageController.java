package pl.recommendations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.recommendations.crawling.embedded.FileRepositoryCrawler;
import pl.recommendations.slo.TwitterSLO;

import javax.servlet.http.HttpSession;

/**
 * Created by marekmagik on 2015-01-19.
 */
@RestController
public class MainPageController {
    private static final String LOGIN_VIEW_NAME = "login";
    private static final String MAIN_VIEW_NAME = "main";

    @Autowired
    FileRepositoryCrawler fileRepositoryCrawler;

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
    public void fillDatabase(@ModelAttribute("graphFiles") GraphFiles files) {
        String separator = files.getSeparator();

        fileRepositoryCrawler.readPeopleNodes(files.getPeopleNodesStream(), separator);
        fileRepositoryCrawler.readInterestNodes(files.getInterestNodesStream(), separator);
        fileRepositoryCrawler.readPeopleEdges(files.getPeopleEdgesStream(), separator);
        fileRepositoryCrawler.readInterestEdges(files.getInterestEdgesStream(), separator);

        fileRepositoryCrawler.persist();
    }
}
