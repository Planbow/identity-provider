package com.planbow.idp.services;

import com.planbow.idp.oauth2.custom.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PlanbowOAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private CustomOAuth2UserService oauthUserService;

    @Autowired
    public void setOauthUserService(CustomOAuth2UserService oauthUserService) {
        this.oauthUserService = oauthUserService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
        oauthUserService.createGoogleUser(oauthUser);
        super.onAuthenticationSuccess(request, response, authentication);
        /*HttpSession session = request.getSession();
        if (session != null) {
            String redirectUrl = (String) session.getAttribute("url_prior_login");
            if (redirectUrl != null) {
                System.out.println("sumit : "+redirectUrl);
                session.removeAttribute("url_prior_login");
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }*/
    }
}
