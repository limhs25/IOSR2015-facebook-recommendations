package pl.recommendations.controller;

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
import pl.recommendations.controller.data.VisualizationForm;
import pl.recommendations.crawling.embedded.FileRepositoryCrawler;
import pl.recommendations.crawling.embedded.PajekNetRepositoryReader;
import pl.recommendations.crawling.embedded.RepositoryReader;
import pl.recommendations.crawling.embedded.StanfordRepositoryReader;
import pl.recommendations.db.SuggestionType;
import pl.recommendations.db.interest.InterestNodeRepository;
import pl.recommendations.db.person.PersonNodeRepository;
import pl.recommendations.slo.TwitterSLO;

import javax.servlet.http.HttpSession;

/**
 * Created by marekmagik on 2015-01-19.
 */
@RestController
public class MainPageController {
    private static final Logger logger = LogManager.getLogger(MainPageController.class.getSimpleName());


    private static final String GRAPH_VIEW_NAME = "graph";
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

    @Autowired
    private TwitterSLO twitterSLO;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @RequestMapping("/")
    public ModelAndView showLoginForm() {
        return new ModelAndView(LOGIN_VIEW_NAME);
    }

    @RequestMapping("/main")
    public ModelAndView showMainForm(HttpSession session) {
        ModelAndView mv = getMainModel(session);

        return mv;
    }

    @RequestMapping(value = "/upload/custom", method = RequestMethod.POST)
    public ModelAndView uploadCustom(@ModelAttribute("customFiles") CustomDatabaseFiles files) {
        ModelAndView mv = getMainModel(null);
        RepositoryReader.setDropRation(files.getDropRate());
        fileRepositoryCrawler.readPeopleNodes(files.getPeopleNodesStream());
        fileRepositoryCrawler.readPeopleEdges(files.getPeopleEdgesStream());

        fileRepositoryCrawler.persist();
        return mv;
    }

    @RequestMapping(value = "/upload/stanford", method = RequestMethod.POST)
    public ModelAndView uploadStanford(@ModelAttribute("stanfordInput") SingleDatabaseFile file) {
        ModelAndView mv = getMainModel(null);
        RepositoryReader.setDropRation(file.getDropRate());
        stanfordRepoReader.read(file.getPeopleEdgesStream());
        return mv;
    }

    @RequestMapping(value = "/upload/pajek", method = RequestMethod.POST)
    public ModelAndView uploadPajek(@ModelAttribute("pajekInput") SingleDatabaseFile file) {
        ModelAndView mv = getMainModel(null);
        RepositoryReader.setDropRation(file.getDropRate());
        pajekRepoReader.read(file.getPeopleEdgesStream());
        return mv;
    }

    @RequestMapping(value = "clear", method = RequestMethod.POST)
    public ModelAndView clearDatabase(HttpSession session) {
        ModelAndView mv = getMainModel(session);
        personRepo.clear();
        interestRepo.clear();
        return mv;
    }

    @RequestMapping(value = "/show/adamic", method = RequestMethod.POST)
    public ModelAndView showAdamic(@ModelAttribute("adamicForm") VisualizationForm form) {
        logger.info("form:" + form);
        ModelAndView view = new ModelAndView(GRAPH_VIEW_NAME);
        view.addObject("graphData", twitterSLO.getGraphData(SuggestionType.ADAMIC, form.getCountLong(), form.getMaxNodesLong()));
        return view;
    }

    @RequestMapping(value = "/show/common", method = RequestMethod.POST)
    public ModelAndView showCommon(@ModelAttribute("commonForm") VisualizationForm form) {
        logger.info("form:" + form);
        ModelAndView view = new ModelAndView(GRAPH_VIEW_NAME);
        view.addObject("graphData", twitterSLO.getGraphData(SuggestionType.NEIGHBOUR, form.getCountLong(), form.getMaxNodesLong()));
        return view;
    }

    @RequestMapping(value = "/show/resource", method = RequestMethod.POST)
    public ModelAndView showResource(@ModelAttribute("resForm") VisualizationForm form) {
        logger.info("form:" + form);
        ModelAndView view = new ModelAndView(GRAPH_VIEW_NAME);
        view.addObject("graphData", twitterSLO.getGraphData(SuggestionType.RESOURCE, form.getCountLong(), form.getMaxNodesLong()));
        return view;
    }

    @RequestMapping(value = "suggest", method = RequestMethod.POST)
    public ModelAndView getSuggestions(HttpSession session) {
        ModelAndView mv = getMainModel(session);
        try {
            logger.info("Starting analysis");
            Double adamicQuality = getMetricQuality(adamicAdarMetric) * 100.0d;
            Double resourceQuality = getMetricQuality(resourceAllocationMetric) * 100.0d;
            Double commonQuality = getMetricQuality(commonNeighbourMetric) * 100.0d;

            mv.addObject("adamic", adamicQuality);
            mv.addObject("resource", resourceQuality);
            mv.addObject("common", commonQuality);

            mv.addObject("display", "block");
        } catch (Exception e) {
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

    private ModelAndView getMainModel(HttpSession session) {
        ModelAndView mv = new ModelAndView(MAIN_VIEW_NAME);
        if (session != null)
            mv.addObject(TwitterSLO.TWITTER_SESSION_ATTRIBUTE, session.getAttribute(TwitterSLO.TWITTER_SESSION_ATTRIBUTE));

        mv.addObject("customFiles", new CustomDatabaseFiles());
        mv.addObject("stanfordInput", new SingleDatabaseFile());
        mv.addObject("pajekInput", new SingleDatabaseFile());

        mv.addObject("adamic", "NaN");
        mv.addObject("resource", "NaN");
        mv.addObject("common", "NaN");

        mv.addObject("display", "none");

        mv.addObject("adamicForm", new VisualizationForm());
        mv.addObject("commonForm", new VisualizationForm());
        mv.addObject("resForm", new VisualizationForm());

        return mv;
    }
}
