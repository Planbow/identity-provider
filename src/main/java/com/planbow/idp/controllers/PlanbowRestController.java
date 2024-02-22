package com.planbow.idp.controllers;



import com.planbow.idp.entities.PlanbowUserDetails;
import com.planbow.idp.utility.PlanbowUtility;
import com.planbow.util.json.handler.request.RequestJsonHandler;
import com.planbow.util.json.handler.response.ResponseJsonHandler;
import com.planbow.idp.services.PlanbowIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.security.Principal;

@RestController
@RequestMapping("/account")
public class PlanbowRestController {

    private PlanbowIdentityService planbowIdentityService;

    @Autowired
    public void setNidavellirIdentityService(PlanbowIdentityService planbowIdentityService) {
        this.planbowIdentityService = planbowIdentityService;
    }

    @GetMapping("/principal")
    public Principal test(Principal principal){
        return principal;
    }

    @PostMapping("/update-profile")
    public Mono<ResponseJsonHandler> updateProfile(@RequestBody RequestJsonHandler requestJsonHandler){
        PlanbowUserDetails planbowUserDetails = PlanbowUtility.getNidavellirUserDetails();
        return Mono.just(planbowIdentityService.updateProfile(planbowUserDetails.getId(),requestJsonHandler));
    }

}
