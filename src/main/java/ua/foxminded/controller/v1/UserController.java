package ua.foxminded.controller.v1;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.auth0.IdentityVerificationException;

@RestController
public class UserController {
	
    @GetMapping("/v1/users")
    public ResponseEntity<String> users(HttpServletRequest request, HttpServletResponse response) throws IOException, IdentityVerificationException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getManagementApiToken());
        
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        
        RestTemplate restTemplate = new RestTemplate();
        
    	ResponseEntity<String> result = restTemplate
    			.exchange("https://dev-yrxawf1etvqc6uz7.us.auth0.com/api/v2/users", HttpMethod.GET, entity, String.class);
        return result;
    }
    
    @GetMapping(value = "/v1/createUsers")
    public ResponseEntity<String> createUser(HttpServletResponse response) {
        JSONObject request = new JSONObject();
        request.put("email", "norman.lewis@email.com");
        request.put("given_name", "Norman");
        request.put("family_name", "Lewis");
        request.put("connection", "car-rest-service");
        request.put("password", "Pa33w0rd");
        
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<String> result = restTemplate
        		.postForEntity("https://dev-yrxawf1etvqc6uz7.us.auth0.com/api/v2/users", request.toString(), String.class);
        return result;
    }
    
	public String getManagementApiToken() {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);

	    JSONObject requestBody = new JSONObject();
	    requestBody.put("client_id", "clientId");
	    requestBody.put("client_secret", "clientSecret");
	    requestBody.put("audience", "http://car-rest-service");
	    requestBody.put("grant_type", "client_credentials"); 

	    HttpEntity<String> request = new HttpEntity<String>(requestBody.toString(), headers);

	    RestTemplate restTemplate = new RestTemplate();
	    @SuppressWarnings("unchecked")
		HashMap<String, String> result = restTemplate
	      .postForObject("https://dev-yrxawf1etvqc6uz7.us.auth0.com/oauth/token", request, HashMap.class);

	    return result.get("access_token");
	}

}
