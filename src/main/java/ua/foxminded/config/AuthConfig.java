package ua.foxminded.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthConfig {
	
	@Value(value = "${com.auth0.domain}")
	private String domain;
	
	@Value(value = "${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
	private String clientId;
	
	@Value(value = "${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
	private String clientSecret;
	
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuer;
	
	@Value(value = "${auth0.audience}")
    private String audience;
	
    @Value(value = "${com.auth0.managementApi.grantType}")
    private String grantType;
	
	
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                .disable())
                .authorizeRequests(requests -> requests
                        .antMatchers("/car-rest-service/**", "/", "/v1/login").permitAll()
                        .antMatchers(HttpMethod.GET).permitAll()
                        .antMatchers(HttpMethod.POST).authenticated()
                        .antMatchers(HttpMethod.DELETE).authenticated()
                        .antMatchers(HttpMethod.PATCH).authenticated()
                        .antMatchers(HttpMethod.PUT).authenticated())
                .oauth2ResourceServer(server -> server
                        .jwt()
                        .decoder(jwtDecoder()));
        return http.build();
    }



	 JwtDecoder jwtDecoder() {
		    OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);
		    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
		    OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withAudience, withIssuer);

		    NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);
		    jwtDecoder.setJwtValidator(validator);
		    return jwtDecoder;
		  }
}
