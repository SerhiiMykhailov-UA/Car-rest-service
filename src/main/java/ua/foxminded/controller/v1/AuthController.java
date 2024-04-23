package ua.foxminded.controller.v1;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

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
		System.out.println(1111);
		String redirectUrl = authConfig.getContextPath(request) + "/car-rest-service/v1/callback";
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
		response.sendRedirect(authConfig.getContextPath(request) + "/car-rest-service/v1/users");
	}
	
	public String getManagementApiToken() {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    JSONObject requestBody = new JSONObject();
	    requestBody.put("client_id", "auth0ManagementAppClientId");
	    requestBody.put("client_secret", "auth0ManagementAppClientSecret");
	    requestBody.put("audience", "https://dev-yrxawf1etvqc6uz7.us.auth0.com/api/v2/");
	    requestBody.put("grant_type", "client_credentials"); 

	    HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);

	    RestTemplate restTemplate = new RestTemplate();
	    @SuppressWarnings("unchecked")
		HashMap<String, String> result = restTemplate
	      .postForObject("https://dev-yrxawf1etvqc6uz7.us.auth0.com/oauth/token", request, HashMap.class);

	    return result.get("access_token");
	}
}
