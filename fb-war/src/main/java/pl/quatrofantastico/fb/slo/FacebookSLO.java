package pl.quatrofantastico.fb.slo;

import facebook4j.conf.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by marekmagik on 2015-04-06.
 */
public interface FacebookSLO {

	public static final String FACEBOOK_SESSION_ATTRIBUTE = "facebook";

	void loguot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	void login(HttpServletRequest request, HttpServletResponse response) throws IOException;

	void setAuthorization(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

	Configuration createConfiguration();
}
