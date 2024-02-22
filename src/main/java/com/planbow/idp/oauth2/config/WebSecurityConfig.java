package com.planbow.idp.oauth2.config;


import com.planbow.idp.services.PlanbowOAuthSuccessHandler;
import com.planbow.idp.utility.PlanbowUtility;
import com.planbow.idp.services.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return web-> web.ignoring().requestMatchers("/bootstrap/**","/css/**","/font/**","/fontawesome/**","/img/**","/js/**");
    }

    private CustomOAuth2UserService oauthUserService;

    @Autowired
    public void setOauthUserService(CustomOAuth2UserService oauthUserService) {
        this.oauthUserService = oauthUserService;
    }

    // @formatter:off
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/create-account","/forgot-password" ,"/verify/**","/public/**",
                                 "/oauth/**"
                                ).permitAll().anyRequest().authenticated()
                )
                .formLogin(formLogin->
                        formLogin.loginPage(PlanbowUtility.LOGIN)
                                .loginProcessingUrl(PlanbowUtility.LOGIN)
                                .defaultSuccessUrl("/",false)
                                .permitAll()

                )
                .oauth2Login(e->
                        e.loginPage(PlanbowUtility.LOGIN)
                                .userInfoEndpoint(r-> r.userService(oauthUserService))
                                .successHandler(successHandler())
                        )
                .logout(logout-> logout.logoutSuccessUrl(PlanbowUtility.LOGIN))
                .sessionManagement(Customizer.withDefaults())

                ;
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        PlanbowOAuthSuccessHandler planbowOAuthSuccessHandler =new PlanbowOAuthSuccessHandler();
        planbowOAuthSuccessHandler.setDefaultTargetUrl("/");
        planbowOAuthSuccessHandler.setUseReferer(true);
        return planbowOAuthSuccessHandler;
    }
}
