package com.planbow.idp.utility;

import com.planbow.idp.entities.PlanbowUserDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
public class PlanbowUtility {
    public static final String IMPROPER_PASSWORD_PATTERN = "Improper password pattern -> Password must be 3 Capital Alphabets,1 Special character , 3 Small alphabet and 3 digits";
    public static final String ACTIVE="active";
    public static final String SIDE_NAV="sideNav";
    public static final String FALSE="false";
    public static final String DASHBOARD="dashboard";
    public static final String REDIRECT_TO_DASHBOARD="redirect:/dashboard";
    public static final String PAYMENT="payment";
    public static final String LOGIN="/login";
    public static final String IS_ACTIVE="isActive";

    private PlanbowUtility(){
        log.info("Restricting instantiation");
    }

    public static PlanbowUserDetails getNidavellirUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (PlanbowUserDetails)authentication.getPrincipal();
    }

    public static String formatDate(Date date){
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        return  outputFormat.format(date);
    }


}
