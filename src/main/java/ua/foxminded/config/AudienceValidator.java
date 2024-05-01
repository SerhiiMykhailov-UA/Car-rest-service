package ua.foxminded.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

	@Value("${auth0.audience}")
	private final String audience;
	
		
	public AudienceValidator(String audience) {
		Assert.hasText(audience, "audience is null or empty");
		this.audience = audience;
	}


	@Override
	public OAuth2TokenValidatorResult validate(Jwt token) {
		List<String> audience = token.getAudience();
		if (audience.contains(this.audience)) {
			return OAuth2TokenValidatorResult.success();
		}
		
		OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN);
		return OAuth2TokenValidatorResult.failure(error);
	}

}
