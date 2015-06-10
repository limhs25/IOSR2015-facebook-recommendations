package pl.recommendations.controller;

import com.google.common.collect.Iterators;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import pl.recommendations.analyse.AnalyseService;
import pl.recommendations.analyse.Metric;
import pl.recommendations.controller.data.CustomDatabaseFiles;
import pl.recommendations.controller.data.SingleDatabaseFile;
import pl.recommendations.crawling.embedded.FileRepositoryCrawler;
import pl.recommendations.crawling.embedded.PajekNetRepositoryReader;
import pl.recommendations.crawling.embedded.StanfordRepositoryReader;
import pl.recommendations.db.interest.InterestNodeRepository;
import pl.recommendations.db.person.PersonNodeRepository;
import pl.recommendations.slo.TwitterSLO;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by marekmagik on 2015-01-19.
 */
@RestController
public class MainPageController {
    private static final Logger logger = LogManager.getLogger(MainPageController.class.getSimpleName());

    private static final String LOGIN_VIEW_NAME = "login";
    private static final String MAIN_VIEW_NAME = "main";
    private static final String RESULTS_NAME = "results";

    @Autowired
    @Qualifier("fileRepositoryCrawlerService")
    private FileRepositoryCrawler fileRepositoryCrawler;
    @Autowired
    private StanfordRepositoryReader stanfordRepoReader;
    @Autowired
    private PajekNetRepositoryReader pajekRepoReader;
    @Autowired
    private PersonNodeRepository personRepo;
    @Autowired
    private InterestNodeRepository interestRepo;

    @Autowired
    private AnalyseService analysis;

    @Autowired
    @Qualifier("AdamicAdarMetric")
    private Metric adamicAdarMetric;
    @Autowired
    @Qualifier("CommonNeighbourMetric")
    private Metric commonNeighbourMetric;
    @Autowired
    @Qualifier("ResourceAllocationMetric")
    private Metric resourceAllocationMetric;



    @RequestMapping("/")
    public ModelAndView showLoginForm() {
        return new ModelAndView(LOGIN_VIEW_NAME);
    }

    @RequestMapping("/main")
    public ModelAndView showMainForm(HttpSession session) {
        ModelAndView mv = new ModelAndView(MAIN_VIEW_NAME);
        mv.addObject(TwitterSLO.TWITTER_SESSION_ATTRIBUTE, session.getAttribute(TwitterSLO.TWITTER_SESSION_ATTRIBUTE));
        mv.addObject("customFiles", new CustomDatabaseFiles());
        mv.addObject("stanfordInput", new SingleDatabaseFile());
        mv.addObject("pajekInput", new SingleDatabaseFile());

        /* Test users list until analiser is implemented */
        ArrayList<String> users = new ArrayList<String>();
        users.add("User1");
        users.add("User2");
        users.add("User3");
        mv.addObject("recommendedUsers", users);

        return mv;
    }

    @RequestMapping(value = "/upload/custom", method = RequestMethod.POST)
    public void uploadCustom(@ModelAttribute("customFiles") CustomDatabaseFiles files) {
        fileRepositoryCrawler.readPeopleNodes(files.getPeopleNodesStream());
        fileRepositoryCrawler.readInterestNodes(files.getInterestNodesStream());
        fileRepositoryCrawler.readPeopleEdges(files.getPeopleEdgesStream());
        fileRepositoryCrawler.readInterestEdges(files.getInterestEdgesStream());

        fileRepositoryCrawler.persist();
    }

    @RequestMapping(value = "/upload/stanford", method = RequestMethod.POST)
    public void uploadStanford(@ModelAttribute("stanfordInput") SingleDatabaseFile file) {
        stanfordRepoReader.read(file.getPeopleEdgesStream());
    }

    @RequestMapping(value = "/upload/pajek", method = RequestMethod.POST)
    public void uploadPajek(@ModelAttribute("pajekInput") SingleDatabaseFile file) {
        pajekRepoReader.read(file.getPeopleEdgesStream());
    }

    @RequestMapping(value = "clear", method = RequestMethod.POST)
    public void clearDatabase() {
        System.out.println("size before" + Iterators.size(personRepo.findAll().iterator()));
        System.out.println("size before" + Iterators.size(interestRepo.findAll().iterator()));

        personRepo.deleteAll();
        interestRepo.deleteAll();

        System.out.println("size after" + Iterators.size(personRepo.findAll().iterator()));
        System.out.println("size after" + Iterators.size(interestRepo.findAll().iterator()));
    }

    @RequestMapping(value = "suggest", method = RequestMethod.POST)
    public ModelAndView getSuggestions(){
        ModelAndView mv = new ModelAndView(RESULTS_NAME);
        try {
            logger.info("Starting analysis");
            Double adamicQuality = getMetricQuality(adamicAdarMetric);
            Double resourceQuality = getMetricQuality(resourceAllocationMetric);
            Double commonQuality = getMetricQuality(commonNeighbourMetric);

            mv.addObject("adamic", adamicQuality);
            mv.addObject("resource", resourceQuality);
            mv.addObject("common", commonQuality);
        }catch(Exception e){
            logger.error("Error during suggesting: {}", e.getMessage(), e);
        }

        return mv;
    }

    private Double getMetricQuality(Metric metric) {
        analysis.setMetric(metric);
        analysis.analyse();
        Double quality = personRepo.getSuggestionQuality(metric.getType());
        logger.info("Quality for {} = {}", metric.getName(), quality);
        return quality;
    }
}
