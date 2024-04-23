package ua.foxminded.controller.v1;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.IdentityVerificationException;

import ua.foxminded.config.AuthConfig;
import ua.foxminded.service.UserService;

@RestController
public class UserController {
	
	private final UserService userService;
	private final AuthConfig config;
	
	public UserController(UserService userService, AuthConfig config) {
		this.userService = userService;
		this.config = config;
	}
	
    @GetMapping(value="/v1/users")
    public ResponseEntity<String> users(HttpServletRequest request, HttpServletResponse response) throws IOException, IdentityVerificationException {
        ResponseEntity<String> result = userService.getCall(config.getUsersUrl());
        return result;
    }

    @GetMapping(value = "/v1/userByEmail")
    public ResponseEntity<String> userByEmail(HttpServletResponse response, @RequestParam String email) {
        ResponseEntity<String> result = userService.getCall(config.getUsersByEmailUrl()+email);
        return result;
    }
    
    @GetMapping(value = "/v1/createUser")
    public ResponseEntity<String> createUser(HttpServletResponse response) {
        JSONObject request = new JSONObject();
        request.put("email", "norman.lewis@email.com");
        request.put("given_name", "Norman");
        request.put("family_name", "Lewis");
        request.put("connection", "Username-Password-Authentication");
        request.put("password", "Pa33w0rd");
        
        ResponseEntity<String> result = userService.postCall(config.getUsersUrl(), request.toString());
        return result;
    }

}
