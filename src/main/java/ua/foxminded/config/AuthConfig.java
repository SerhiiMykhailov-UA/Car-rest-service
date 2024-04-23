package ua.foxminded.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.auth0.AuthenticationController;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;

import ua.foxminded.controller.v1.LogoutController;

@Configuration
@EnableWebSecurity
public class AuthConfig {
	
	@Value(value = "${com.auth0.domain}")
	private String domain;
	
	@Value(value = "${com.auth0.clientId}")
	private String clientId;
	
	@Value(value = "${com.auth0.clientSecret}")
	private String clientSecret;
	
    @Value(value = "${com.auth0.managementApi.clientId}")
    private String managementApiClientId;

    @Value(value = "${com.auth0.managementApi.clientSecret}")
    private String managementApiClientSecret;

    @Value(value = "${com.auth0.managementApi.grantType}")
    private String grantType;
	
	@Bean
	protected LogoutSuccessHandler logoutSuccessHandler() {
		return new LogoutController();
	}
	
	@Bean
	protected SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                .disable())
                .authorizeRequests(requests -> requests
                        .antMatchers("/car-rest-service", "/", "/v1/login")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/v1/login"))
 //                       .defaultSuccessUrl("/v1/makers", true))
                .logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler()).permitAll());
		return http.build();
	}

    @Bean
    AuthenticationController authenticationController() {
    	JwkProvider jwkProvider = new JwkProviderBuilder(domain).build();
    	return AuthenticationController.newBuilder(domain, managementApiClientId, managementApiClientSecret)
    			.withJwkProvider(jwkProvider).build();
    }
    
    String getDomain() {
		return domain;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}
	
    public String getManagementApiClientId() {
        return managementApiClientId;
    }

    public String getManagementApiClientSecret() {
        return managementApiClientSecret;
    }
	
    public String getUserInfoUrl() {
        return "https://" + getDomain() + "/userinfo";
    }

    public String getUsersUrl() {
        return "https://" + getDomain() + "/api/v2/users";
    }

    public String getUsersByEmailUrl() {
        return "https://" + getDomain() + "/api/v2/users-by-email?email=";
    }

    public String getLogoutUrl() {
        return "https://" + getDomain() +"/v2/logout";
    }

	public String getContextPath(HttpServletRequest request) {
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		return path;
	}
}
