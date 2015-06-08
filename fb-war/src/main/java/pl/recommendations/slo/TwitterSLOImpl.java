package pl.recommendations.slo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.recommendations.crawling.CrawlerService;
import pl.recommendations.crawling.twitter.TwitterConfiguration;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;
import pl.recommendations.db.person.SuggestionEdge;
import pl.recommendations.db.queue.PersistentQueueFacade;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by marekmagik on 2015-04-12.
 */
@Transactional
@Service
public class TwitterSLOImpl implements TwitterSLO {

    @Autowired
    @Qualifier("crawlerScheduler")
    private CrawlerService crawler;

    @Autowired
    private PersonNodeRepository personRepo;

    @Autowired
    private Neo4jTemplate neo4jTemplate;

    @Override
    public void setAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Twitter twitter = (Twitter) request.getSession().getAttribute(TWITTER_SESSION_ATTRIBUTE);
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");

        String verifier = request.getParameter("oauth_verifier");
        try {
            twitter.getOAuthAccessToken(requestToken, verifier);
            request.getSession().removeAttribute("requestToken");
            crawler.scheduleCrawling(twitter.getId(), true);
        } catch (Exception e) {
            throw new ServletException(e);
        }

        response.sendRedirect(request.getContextPath() + "/twitter/main");
    }

    @Override
    public void login(HttpServletRequest request, HttpServletResponse response) throws TwitterException, IOException {
        Twitter twitter = new TwitterFactory(TwitterConfiguration.getAppConfigurationForUsers()).getInstance();

        request.getSession().setAttribute(TWITTER_SESSION_ATTRIBUTE, twitter);
        StringBuffer callbackURL = request.getRequestURL();
        int index = callbackURL.lastIndexOf("/");
        callbackURL.replace(index, callbackURL.length(), "").append("/twitter-login-callback");

        RequestToken requestToken = twitter.getOAuthRequestToken(callbackURL.toString());
        request.getSession().setAttribute("requestToken", requestToken);
        response.sendRedirect(requestToken.getAuthenticationURL());
    }

    @Override
    public void loguot(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().setAttribute(TWITTER_SESSION_ATTRIBUTE, null);

        StringBuffer callbackURL = request.getRequestURL();
        int index = callbackURL.lastIndexOf("/");
        callbackURL.replace(index + 1, callbackURL.length(), "");
        response.sendRedirect(callbackURL.toString());
    }

    @Override
    public List<String> getRecommendations(Twitter twitter) throws TwitterException {

        Long uuid = twitter.getId();
        Collection<SuggestionEdge> suggestionEdges = personRepo.getSuggestionOf(uuid);
        ArrayList<String> users = new ArrayList<String>();

        for(SuggestionEdge suggestion : suggestionEdges) {
            neo4jTemplate.fetch(suggestion.getSuggestion());
            if (suggestion.getSuggestion().getUuid() != null) {
                String username = suggestion.getSuggestion().getName();
                users.add(username);
            }
        }
        return users;
    }

    @Override
    public String getGraphData(Twitter twitter) throws TwitterException {

        String USER_COLOUR = "#143389";
        String SUGGESTION_COLOUR = "#7D0E0E";
        String FRIEND_COLOUR = "#24D736";

        Long uuid = twitter.getId();
        Collection<SuggestionEdge> suggestionEdges = personRepo.getSuggestionOf(uuid);

        Collection<PersonNode> friends = personRepo.getFriendsOf(uuid);

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        JsonArrayBuilder nodeArray = Json.createArrayBuilder();
        JsonArrayBuilder edgeArray = Json.createArrayBuilder();

        JsonObjectBuilder nodeParam = Json.createObjectBuilder();
        JsonObjectBuilder edgeParam;

        nodeParam.add("id", String.valueOf(twitter.getId()));
        nodeParam.add("label", twitter.getScreenName());
        nodeParam.add("x", String.valueOf(Math.random()));
        nodeParam.add("y", String.valueOf(Math.random()));
        nodeParam.add("size", "10");
        nodeParam.add("color", USER_COLOUR);
        nodeArray.add(nodeParam);


        for (SuggestionEdge suggestion : suggestionEdges) {
            neo4jTemplate.fetch(suggestion.getSuggestion());
            if (suggestion.getSuggestion().getUuid() != null) {
                nodeParam = Json.createObjectBuilder();
                nodeParam.add("id", suggestion.getSuggestion().getUuid().toString());
                nodeParam.add("label", suggestion.getSuggestion().getName());
                nodeParam.add("x", String.valueOf(Math.random()));
                nodeParam.add("y", String.valueOf(Math.random()));
                nodeParam.add("size", "5");
                nodeParam.add("color", SUGGESTION_COLOUR);

                edgeParam = Json.createObjectBuilder();
                edgeParam.add("id", "e" + suggestion.getSuggestion().getUuid().toString());
                edgeParam.add("source", uuid.toString());
                edgeParam.add("target", suggestion.getSuggestion().getUuid().toString());
                edgeParam.add("size", "2");
                edgeParam.add("color", SUGGESTION_COLOUR);

                nodeArray.add(nodeParam);
                edgeArray.add(edgeParam);
            }
        }

        for (PersonNode friend : friends) {
            neo4jTemplate.fetch(friend);
            nodeParam = Json.createObjectBuilder();
            nodeParam.add("id", friend.getUuid().toString());
            nodeParam.add("label", friend.getName());
            nodeParam.add("x", String.valueOf(Math.random()));
            nodeParam.add("y", String.valueOf(Math.random()));
            nodeParam.add("size", "5");
            nodeParam.add("color", FRIEND_COLOUR);

            edgeParam = Json.createObjectBuilder();
            edgeParam.add("id", "e" + friend.getUuid().toString());
            edgeParam.add("source", uuid.toString());
            edgeParam.add("target", friend.getUuid().toString());
            edgeParam.add("size", "2");
            edgeParam.add("color", FRIEND_COLOUR);

            nodeArray.add(nodeParam);
            edgeArray.add(edgeParam);
        }

        jsonObjectBuilder.add("nodes", nodeArray);
        jsonObjectBuilder.add("edges", edgeArray);

        JsonObject jsonObject = jsonObjectBuilder.build();

        return jsonObject.toString();
    }
}
