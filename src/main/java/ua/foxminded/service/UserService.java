package ua.foxminded.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ua.foxminded.controller.v1.AuthController;

@Service
public class UserService {
    
	    private final AuthController controller;
	    
	    public UserService(AuthController controller) {
			this.controller = controller;
		}

		public ResponseEntity<String> getCall(String url) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Authorization", "Bearer " + controller.getManagementApiToken());
	        
	        HttpEntity<String> entity = new HttpEntity<String>(headers);
	        RestTemplate restTemplate = new RestTemplate();
	        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
	        
	        return result;
	    }
	    
	    public ResponseEntity<String> postCall(String url, String requestBody) {
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("Authorization", "Bearer " + controller.getManagementApiToken());
	        
	        HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);
	        RestTemplate restTemplate = new RestTemplate();
	        ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);
	        
	        return result;
	    }
	    
}
