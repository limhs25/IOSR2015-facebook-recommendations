package pl.recommendations.slo;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.recommendations.crawling.twitter.TwitterConfiguration;
import pl.recommendations.db.SuggestionType;
import pl.recommendations.db.person.PersonNode;
import pl.recommendations.db.person.PersonNodeRepository;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by marekmagik on 2015-04-12.
 */
@Service
public class TwitterSLOImpl implements TwitterSLO {
    public static final Logger logger = LogManager.getLogger(TwitterSLOImpl.class.getName());

    private static final String USER_COLOUR = "#143389";
    private static final String SUGGESTION_COLOUR = "#f62424";
    private static final String FRIEND_COLOUR = "#000000";
    private static final String COMMON_COLOUR = "#0b04d2";

    @Autowired
    private PersonNodeRepository personRepo;

    @Override
    public void setAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        Twitter twitter = (Twitter) request.getSession().getAttribute(TWITTER_SESSION_ATTRIBUTE);
//        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
//
//        String verifier = request.getParameter("oauth_verifier");
//        try {
//            twitter.getOAuthAccessToken(requestToken, verifier);
//            request.getSession().removeAttribute("requestToken");
//        } catch (TwitterException e) {
//            throw new ServletException(e);
//        }
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
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().setAttribute(TWITTER_SESSION_ATTRIBUTE, null);

        StringBuffer callbackURL = request.getRequestURL();
        int index = callbackURL.lastIndexOf("/");
        callbackURL.replace(index + 1, callbackURL.length(), "");
        response.sendRedirect(callbackURL.toString());
    }

    @Override
    public String getGraphData(SuggestionType type) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        JsonArrayBuilder nodeArray = Json.createArrayBuilder();
        JsonArrayBuilder edgeArray = Json.createArrayBuilder();

        Collection<PersonNode> peopleWithRetainedEdges = personRepo.getPeopleWithRetainedEdges();
        logger.info("People with retained edges: {}", peopleWithRetainedEdges.size());

        Set<Long> nodes = new HashSet<>();
        Set<String> edges = new HashSet<>();

        Set<Long> notMatchedSuggestions = new HashSet<>();
        Set<Long> matchedSuggestions = new HashSet<>();

        for (PersonNode p1 : peopleWithRetainedEdges) {
            addNode(nodeArray, nodes, p1);

            long start = System.currentTimeMillis();
            Set<PersonNode> retained = new HashSet<>(personRepo.getRetainedEdges(p1.getUuid()));
            Set<PersonNode> suggestions = new HashSet<>(personRepo.getSuggestionOf(p1.getUuid(), type.toString()));

            Set<PersonNode> common = Sets.intersection(retained, suggestions);
            logger.info("for {}: Retained: {}, Suggestions: {}, common: {}", p1.getUuid(), retained, suggestions,
                    common);
            logger.info("for {}: Retained: {}, Suggestions: {}, common: {}", p1.getUuid(), retained.size(), suggestions.size(),
                    common);

            retained = Sets.difference(retained, common);
            suggestions = Sets.difference(suggestions, common);

            for (PersonNode friend : retained) {
                addNode(nodeArray, nodes, friend);

                if (!edges.contains(id(p1, friend, FRIEND_COLOUR))) {
                    edges.add(id(p1, friend, FRIEND_COLOUR));
                    JsonObjectBuilder edgeParam = newFriendshipEdge(p1, friend, FRIEND_COLOUR);
                    edgeArray.add(edgeParam);
                }
            }

            for (PersonNode suggestion : suggestions) {
                addNode(nodeArray, nodes, suggestion);

                if (!edges.contains(id(p1, suggestion, FRIEND_COLOUR))) {
                    edges.add(id(p1, suggestion, FRIEND_COLOUR));
                    JsonObjectBuilder edgeParam = newFriendshipEdge(p1, suggestion, SUGGESTION_COLOUR);
                    edgeArray.add(edgeParam);
                }
            }

            for (PersonNode suggestion : common) {
                addNode(nodeArray, nodes, suggestion);

                if (!edges.contains(id(p1, suggestion, COMMON_COLOUR))) {
                    edges.add(id(p1, suggestion, COMMON_COLOUR));
                    JsonObjectBuilder edgeParam = newFriendshipEdge(p1, suggestion, COMMON_COLOUR);
                    edgeArray.add(edgeParam);
                }
            }

            logger.info("in {}s", (System.currentTimeMillis() - start) / 1000.0);
            logger.info("after: Retained: {}, Suggestions: {}, common: {}", retained.size(), suggestions.size(), common
                    .size());

            notMatchedSuggestions.addAll(suggestions.stream().map(PersonNode::getUuid).collect(Collectors.toSet()));
            matchedSuggestions.addAll(common.stream().map(PersonNode::getUuid).collect(Collectors.toSet()));
        }

        jsonObjectBuilder.add("nodes", nodeArray);
        jsonObjectBuilder.add("edges", edgeArray);

        JsonObject jsonObject = jsonObjectBuilder.build();

        logger.info("Resulting json:\n {}", jsonObject);

        logger.info("Not matching suggestions: {}", notMatchedSuggestions.size());
        logger.info("Matching suggestions: {}", matchedSuggestions.size());

        return jsonObject.toString();
    }

    private void addNode(JsonArrayBuilder nodeArray, Set<Long> nodes, PersonNode suggestion) {
        if (!nodes.contains(suggestion.getUuid())) {
            nodes.add(suggestion.getUuid());
            JsonObjectBuilder friendNode = newPersonNode(suggestion);
            nodeArray.add(friendNode);
        }
    }

    private String id(PersonNode p1, PersonNode p2, String color) {
        String id = null;

        if (Objects.equals(color, SUGGESTION_COLOUR))
            id = p1.getUuid().toString() + "-" + p2.getUuid().toString() + "S";
        else if (Objects.equals(color, FRIEND_COLOUR))
            id = p1.getUuid().toString() + "-" + p2.getUuid().toString() + "F";
        else
            id = p1.getUuid().toString() + "-" + p2.getUuid().toString() + "C";
        return id;
    }

    private JsonObjectBuilder newFriendshipEdge(PersonNode p1, PersonNode p2, String color) {
        JsonObjectBuilder edgeParam;
        edgeParam = Json.createObjectBuilder();
        String size = "1";

        if (Objects.equals(color, COMMON_COLOUR))
            size = "8";

        edgeParam.add("id", id(p1, p2, color));
        edgeParam.add("source", p1.getUuid().toString());
        edgeParam.add("target", p2.getUuid().toString());
        edgeParam.add("size", size);
        edgeParam.add("color", color);
        return edgeParam;
    }

    private JsonObjectBuilder newPersonNode(PersonNode p) {
        JsonObjectBuilder nodeParam = Json.createObjectBuilder();
        nodeParam.add("id", String.valueOf(p.getUuid().toString()));
        nodeParam.add("label", p.getUuid().toString());
        nodeParam.add("x", String.valueOf(Math.random()));
        nodeParam.add("y", String.valueOf(Math.random()));
        nodeParam.add("size", "12");
        nodeParam.add("color", USER_COLOUR);
        return nodeParam;
    }
}
