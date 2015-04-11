package pl.recommendations.auth;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by marekmagik on 2015-04-06.
 */
public class FacebookLoggedFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String requestURI = ((HttpServletRequest) request).getRequestURI();
		//Facebook facebook = (Facebook) ((HttpServletRequest) request).getSession().getAttribute(FacebookSLO.FACEBOOK_SESSION_ATTRIBUTE);

		if (!isUriAccessibleWithoutLogIn(requestURI)) {
			((HttpServletResponse) response).sendRedirect("/fb/");
			return;
		}

		chain.doFilter(request, response);
	}

	private boolean isUriAccessibleWithoutLogIn(String uri){
		return uri.contains("facebook-login") || uri.contains("/resources/") || (uri.endsWith("/fb/") || uri.endsWith("/fb"));
	}

	@Override
	public void destroy() {
	}
}