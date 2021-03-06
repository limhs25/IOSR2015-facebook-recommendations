package pl.recommendations.slo;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by marekmagik on 2015-04-12.
 */
public interface TwitterSLO {
    public static final String TWITTER_SESSION_ATTRIBUTE = "twitter";

    void setAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    void login(HttpServletRequest request, HttpServletResponse response) throws TwitterException, IOException;

    void loguot(HttpServletRequest request, HttpServletResponse response) throws IOException;

    List<String> getRecommendations(Twitter twitter) throws TwitterException;

    String getGraphData(Twitter twitter) throws TwitterException;
}
