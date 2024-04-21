package ua.foxminded.controller.v1;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Controller;

import ua.foxminded.config.AuthConfig;

@Controller
public class LogoutController implements LogoutSuccessHandler {

	@Autowired
	private AuthConfig authConfig;
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		if (request.getSession() != null) {
			request.getSession().invalidate();
		}
		String returnTo = "http://localhost:8080/";
		String logoutUrl = "http://localhost:8080/car-rest-service/v1/logout?client_id="
		+ authConfig.getClientId() + "&returnTo=" + returnTo;
		response.sendRedirect(logoutUrl);
	}

}
