package com.planbow.idp.services;


import com.planbow.idp.entities.PlanbowUserDetails;
import com.planbow.idp.oauth2.custom.CustomOAuth2User;
import com.planbow.idp.entities.UserEntity;
import com.planbow.idp.repository.PlanbowIdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private PublicApiService publicApiService;
    private PlanbowIdentityRepository planbowIdentityRepository;

    @Autowired
    public void setNidavellirIdentityRepository(PlanbowIdentityRepository planbowIdentityRepository) {
        this.planbowIdentityRepository = planbowIdentityRepository;
    }

    @Autowired
    public void setPublicApiService(PublicApiService publicApiService) {
        this.publicApiService = publicApiService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User =  super.loadUser(userRequest);
        CustomOAuth2User user=new CustomOAuth2User(oAuth2User);
       /* String domain = user.getEmail().substring(user.getEmail().lastIndexOf("@")+1);
        Optional<AuthorizedDomainEntity> authorizedDomainEntity = planbowIdentityRepository.getAuthorizedDomainEntity(domain);
        if(authorizedDomainEntity.isEmpty()){
            throw new OAuth2AuthenticationException("Unauthorized Domain");
        }else{
            Optional<AuthorizedEmailEntity> authorizedEmailEntity= planbowIdentityRepository.getAuthorizedEmailEntity(user.getEmail(),authorizedDomainEntity.get().getId());
            if(authorizedEmailEntity.isEmpty())
                throw new OAuth2AuthenticationException("Unauthorized Domain");
        }*/
        return new CustomOAuth2User(oAuth2User);
    }

    public void createGoogleUser(CustomOAuth2User oAuth2User) {
       Map<String,Object> attributes = oAuth2User.getAttributes();
        publicApiService.createAccount(oAuth2User.getName(),oAuth2User.getEmail(), UserEntity.PROVIDER_GOOGLE,null,null,attributes.get("picture").toString());
        Optional<UserEntity> userEntity = planbowIdentityRepository.getUserEntity(oAuth2User.getEmail());

        if(userEntity.isPresent()){
            Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity
                    .map(PlanbowUserDetails::new)
                    .get(),userEntity.get().getPassword(),userEntity.get().getRolesEntity().stream().map(e-> new SimpleGrantedAuthority(e.getRoleName())).collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
   }

}
