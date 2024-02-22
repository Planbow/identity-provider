package com.planbow.idp.controllers;


import com.planbow.idp.services.PublicApiService;
import com.planbow.idp.utility.PlanbowUtility;
import jakarta.servlet.http.HttpServletRequest;
import com.planbow.util.json.handler.request.RequestJsonHandler;
import com.planbow.util.json.handler.response.ResponseJsonHandler;
import com.planbow.util.json.handler.response.util.ResponseConstants;
import com.planbow.util.json.handler.response.util.ResponseJsonUtil;
import com.planbow.util.utility.core.Utility;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/public")
public class PublicApiController {

    private PublicApiService publicApiService;



    @Autowired
    public void setPublicApiService(PublicApiService publicApiService) {
        this.publicApiService = publicApiService;
    }


    @PostMapping("/get-clients")
    public Mono<ResponseJsonHandler> getClients(@RequestBody RequestJsonHandler requestJsonHandler){
        boolean secretsRequired = requestJsonHandler.getBooleanValue("secretsRequired");
        return Mono.just(publicApiService.getClients(secretsRequired));
    }

    @PostMapping("/get-client")
    public Mono<ResponseJsonHandler> getClient(@RequestBody RequestJsonHandler requestJsonHandler){
        String clientId  = requestJsonHandler.getStringValue("clientId");
        if(TextUtils.isEmpty(clientId))
            return Mono.just(ResponseJsonUtil.getResponse("Please provide clientId", 400, ResponseConstants.BAD_REQUEST.getStatus(),true));
        boolean secretsRequired = requestJsonHandler.getBooleanValue("secretsRequired");
        return Mono.just(publicApiService.getClient(clientId,secretsRequired));
    }


    @GetMapping("/get-countries")
    public Mono<Object> getCountries(HttpServletRequest request) {
        return Mono.just(publicApiService.getCountries());
    }

    @PostMapping("/get-country")
    public Mono<ResponseJsonHandler> getCountry(@RequestBody RequestJsonHandler requestJsonHandler){
        Long countryId  = requestJsonHandler.getLongValue("countryId");
        if(countryId==null)
            return Mono.just(ResponseJsonUtil.getResponse("Please provide countryId", 400, ResponseConstants.BAD_REQUEST.getStatus(),true));
        return Mono.just(publicApiService.getCountry(countryId));
    }

    @PostMapping("/get-states")
    public Mono<ResponseJsonHandler> getStates(@RequestBody RequestJsonHandler requestJsonHandler){
        Long countryId  = requestJsonHandler.getLongValue("countryId");
        if(countryId==null)
            return Mono.just(ResponseJsonUtil.getResponse("Please provide countryId", 400, ResponseConstants.BAD_REQUEST.getStatus(),true));
        return Mono.just(publicApiService.getStates(countryId));
    }

    @PostMapping("/get-cities")
    public Mono<ResponseJsonHandler> getCities(@RequestBody RequestJsonHandler requestJsonHandler){
        Long stateId  = requestJsonHandler.getLongValue("stateId");
        if(stateId==null)
            return Mono.just(ResponseJsonUtil.getResponse("Please provide stateId", 400,ResponseConstants.BAD_REQUEST.getStatus(),true));
        return Mono.just(publicApiService.getCities(stateId));
    }

    @PostMapping("/create-account")
    public Mono<ResponseJsonHandler> createAccount(@RequestBody RequestJsonHandler requestJsonHandler){

        String name =requestJsonHandler.getStringValue("name");
        if(name==null || TextUtils.isBlank(name))
            return Mono.just(ResponseJsonUtil.getResponse("Please provide name",400, ResponseConstants.BAD_REQUEST.getStatus(),true));

        String email  = requestJsonHandler.getStringValue("email");
        if(email==null || TextUtils.isBlank(email))
            return Mono.just(ResponseJsonUtil.getResponse("Please provide email",400,ResponseConstants.BAD_REQUEST.getStatus(),true));
        else if(!Pattern.compile(Utility.EMAIL_PATTERN).matcher(email).find())
            return Mono.just(ResponseJsonUtil.getResponse("Invalid email pattern",400,ResponseConstants.BAD_REQUEST.getStatus(),true));

        String password=requestJsonHandler.getStringValue("password");
        if(password==null||TextUtils.isBlank(password))
            return Mono.just(ResponseJsonUtil.getResponse("Please provide password",400,ResponseConstants.BAD_REQUEST.getStatus(),true));
        else if(!Pattern.compile(Utility.PASSWORD_PATTERN).matcher(password).find())
            return Mono.just(ResponseJsonUtil.getResponse(PlanbowUtility.IMPROPER_PASSWORD_PATTERN,400,ResponseConstants.BAD_REQUEST.getStatus(),true));


        String contactNo=requestJsonHandler.getStringValue("contactNo");
        if(contactNo==null||TextUtils.isBlank(contactNo))
            return Mono.just(ResponseJsonUtil.getResponse("Please provide contactNo",400,ResponseConstants.BAD_REQUEST.getStatus(),true));

        String provider  = requestJsonHandler.getStringValue("provider");
        return Mono.just(publicApiService.createAccount(name.trim(),email.trim(),provider,password,contactNo,null));
    }

    @GetMapping("/callback")
    public Mono<ResponseJsonHandler> callback(@RequestParam String code){
        return Mono.just(ResponseJsonUtil.getResponse(code));
    }

}
