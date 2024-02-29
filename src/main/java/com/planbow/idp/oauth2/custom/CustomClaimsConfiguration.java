package com.planbow.idp.oauth2.custom;

import com.planbow.idp.entities.UserEntity;
import com.planbow.idp.repository.PlanbowIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import java.util.Optional;


@Configuration
public class CustomClaimsConfiguration {

    private PlanbowIdentityRepository planbowIdentityRepository;

    @Autowired
    public void setPlanbowIdentityRepository(PlanbowIdentityRepository planbowIdentityRepository) {
        this.planbowIdentityRepository = planbowIdentityRepository;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return (context) -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) || OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType()) ) {
                Optional<UserEntity> userEntityOptional = planbowIdentityRepository.getUserEntity(context.getAuthorization().getPrincipalName());
                userEntityOptional.ifPresent(userEntity -> context.getClaims().claims((claims) -> {
                    claims.put("userId", userEntity.getId());
                    claims.put("userName", userEntity.getName());
                    claims.put("email", userEntity.getEmail());
                }));
            }
        };
    }
}
