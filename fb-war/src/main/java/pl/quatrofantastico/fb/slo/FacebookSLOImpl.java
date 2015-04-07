package pl.quatrofantastico.fb.slo;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by marekmagik on 2015-04-05.
 */
@Service
public class FacebookSLOImpl implements FacebookSLO {

	@Override
	public void loguot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Facebook facebook = (Facebook) request.getSession().getAttribute(FACEBOOK_SESSION_ATTRIBUTE);
		String accessToken;
		try {
			accessToken = facebook.getOAuthAccessToken().getToken();
		} catch (Exception e) {
			throw new ServletException(e);
		}
		request.getSession().setAttribute(FACEBOOK_SESSION_ATTRIBUTE, null);

		StringBuffer returnURL = request.getRequestURL();
		int index = returnURL.lastIndexOf("/");
		returnURL.replace(index+1, returnURL.length(), "");
		response.sendRedirect("http://www.facebook.com/logout.php?next=" + returnURL.toString() + "&access_token=" + accessToken);
	}

	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Configuration config = createConfiguration();
		Facebook facebook = new FacebookFactory(config).getInstance();

		request.getSession().setAttribute(FACEBOOK_SESSION_ATTRIBUTE, facebook);
		StringBuffer callbackURL = request.getRequestURL();
		int index = callbackURL.lastIndexOf("/");
		callbackURL.replace(index, callbackURL.length(), "").append("/facebook-login-callback");
		response.sendRedirect(facebook.getOAuthAuthorizationURL(callbackURL.toString()));
	}

	@Override
	public void setAuthorization(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Facebook facebook = (Facebook) request.getSession().getAttribute(FACEBOOK_SESSION_ATTRIBUTE);

		String oAuthCode = request.getParameter("code");
		try {
			facebook.getOAuthAccessToken(oAuthCode);
		} catch (FacebookException e) {
			throw new ServletException(e);
		}
		response.sendRedirect(request.getContextPath() + "/fb/main");
	}

	public Configuration createConfiguration() {
		ConfigurationBuilder confBuilder = new ConfigurationBuilder();

		confBuilder.setDebugEnabled(true);
		confBuilder.setOAuthAppId("853327904736633");
		confBuilder.setOAuthAppSecret("0c28d52c059eec3c308e1cbb6d41c73d");
		confBuilder.setUseSSL(true);
		confBuilder.setJSONStoreEnabled(true);
		confBuilder.setOAuthPermissions("email,publish_stream,public_profile,user_friends");
		return confBuilder.build();
	}
}
