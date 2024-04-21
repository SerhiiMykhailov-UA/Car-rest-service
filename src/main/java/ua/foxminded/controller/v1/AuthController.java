package ua.foxminded.controller.v1;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import ua.foxminded.config.AuthConfig;



@Controller
@RequestMapping()
public class AuthController {
	
	@Autowired
	private AuthConfig authConfig;
	
	@Autowired
	private AuthenticationController authenticationController;

	@GetMapping("/v1/login")
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String redirectUrl = "http://localhost:8080/car-rest-service/v1/callback";
		String authorizeUrl = authenticationController
				.buildAuthorizeUrl(request, response, redirectUrl)
				.withScope("openid email")
				.build();
		response.sendRedirect(authorizeUrl);
	}
	
	@GetMapping("/v1/callback")
	public void callback(HttpServletRequest request, HttpServletResponse response) throws IdentityVerificationException, IOException {
		Tokens tokens = authenticationController.handle(request, response);
		
		DecodedJWT jwt = JWT.decode(tokens.getIdToken());
		TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(jwt.getSubject(), jwt.getToken());
		authenticationToken.setAuthenticated(true);
		
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		response.sendRedirect(authConfig.getContextPath(request) + "/");
	}
	
}
