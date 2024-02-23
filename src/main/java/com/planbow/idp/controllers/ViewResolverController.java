package com.planbow.idp.controllers;

import com.planbow.idp.services.PublicApiService;
import com.planbow.idp.utility.PlanbowUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

@Controller
public class ViewResolverController {

    private PublicApiService publicApiService;

    @Autowired
    public void setPublicApiService(PublicApiService publicApiService) {
        this.publicApiService = publicApiService;
    }


    @GetMapping("/create-account")
    public String createAccount() {
        return "register";
    }

    @GetMapping(value = {"/", "/error"})
    public String home(Model model) {
        model.addAttribute(PlanbowUtility.ACTIVE, PlanbowUtility.DASHBOARD);
        return PlanbowUtility.REDIRECT_TO_DASHBOARD;
    }

    @GetMapping(value = {"/login"})
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof AnonymousAuthenticationToken ? "login" : PlanbowUtility.REDIRECT_TO_DASHBOARD;
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute(PlanbowUtility.ACTIVE, PlanbowUtility.DASHBOARD);
        return PlanbowUtility.DASHBOARD;
    }


    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @GetMapping("/personal-info")
    public String personalInfo(Model model) {
        model.addAttribute(PlanbowUtility.ACTIVE, "personal-info");
        return "personal-info";
    }

    @GetMapping("/security")
    public String security(Model model) {
        model.addAttribute(PlanbowUtility.ACTIVE, "security");
        return "security";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute(PlanbowUtility.ACTIVE, "about");
        return "about";
    }

    @GetMapping("/payment")
    public String payment(Model model) {
        model.addAttribute(PlanbowUtility.ACTIVE, PlanbowUtility.PAYMENT);
        return PlanbowUtility.PAYMENT;
    }

    @GetMapping("/change-name")
    public String changeName(Model model) {
        model.addAttribute(PlanbowUtility.SIDE_NAV, PlanbowUtility.FALSE);
        return "change-name";
    }

    @GetMapping("/change-gender")
    public String changeGender(Model model) {
        model.addAttribute(PlanbowUtility.SIDE_NAV, PlanbowUtility.FALSE);
        return "change-gender";
    }


    @GetMapping("/change-dob")
    public String changeDob(Model model) {
        model.addAttribute(PlanbowUtility.SIDE_NAV, PlanbowUtility.FALSE);
        return "change-dob";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        model.addAttribute(PlanbowUtility.SIDE_NAV, PlanbowUtility.FALSE);
        return "profile";
    }

    @GetMapping("/change-phone")
    public String changePhone(Model model) {
        model.addAttribute(PlanbowUtility.SIDE_NAV, PlanbowUtility.FALSE);
        return "change-phone";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "login";
    }

    @GetMapping("/verify/{token}")
    public Mono<String> verifyToken(@PathVariable String token,Model model){
        return Mono.just(publicApiService.verifyToken(token,model));
    }

}
