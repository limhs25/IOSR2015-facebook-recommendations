package pl.recommendations.auth;

import pl.recommendations.slo.TwitterSLO;
import twitter4j.Twitter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by marekmagik on 2015-04-06.
 */
public class TwitterLoggedFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        Twitter twitter = (Twitter) ((HttpServletRequest) request).getSession().getAttribute(TwitterSLO.TWITTER_SESSION_ATTRIBUTE);

        if (!isUriAccessibleWithoutLogIn(requestURI) && twitter == null) {
            ((HttpServletResponse) response).sendRedirect("/twitter/");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isUriAccessibleWithoutLogIn(String uri) {
        return uri.contains("twitter-login") || uri.contains("/resources/") || (uri.endsWith("/twitter/") || uri.endsWith("/twitter"));
    }

    @Override
    public void destroy() {
    }

}