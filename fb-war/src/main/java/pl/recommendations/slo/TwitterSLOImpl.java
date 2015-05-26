package pl.recommendations.slo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.recommendations.crawling.twitter.TwitterConfiguration;
import pl.recommendations.db.queue.PersistentQueueFacade;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by marekmagik on 2015-04-12.
 */
@Service
public class TwitterSLOImpl implements TwitterSLO {

    @Autowired
    private PersistentQueueFacade queue;

    @Override
    public void setAuthorization(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Twitter twitter = (Twitter) request.getSession().getAttribute(TWITTER_SESSION_ATTRIBUTE);
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");

        String verifier = request.getParameter("oauth_verifier");
        try {
            twitter.getOAuthAccessToken(requestToken, verifier);
            request.getSession().removeAttribute("requestToken");
            queue.enqueueUser(twitter.getId(), true);
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
}
