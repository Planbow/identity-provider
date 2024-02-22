package com.planbow.idp.services;


import com.planbow.idp.entities.PlanbowUserDetails;
import com.planbow.idp.entities.UserEntity;
import com.planbow.idp.repository.PublicApiRepository;
import com.planbow.util.json.handler.request.RequestJsonHandler;
import com.planbow.util.json.handler.response.ResponseJsonHandler;
import com.planbow.util.json.handler.response.util.ResponseConstants;
import com.planbow.util.json.handler.response.util.ResponseJsonUtil;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PlanbowIdentityService {

    private PublicApiRepository publicApiRepository;

    @Autowired
    public void setPublicApiRepository(PublicApiRepository publicApiRepository) {
        this.publicApiRepository = publicApiRepository;
    }


    public ResponseJsonHandler updateProfile(Long id , RequestJsonHandler requestJsonHandler){
        UserEntity userEntity  = publicApiRepository.getUserEntity(id);
        if(userEntity==null)
            return ResponseJsonUtil.getResponse("User does not exists",404, ResponseConstants.NOT_FOUND.getStatus(),true);
        String name=requestJsonHandler.getStringValue("name");
        if(name!=null && !TextUtils.isEmpty(name))
            userEntity.setName(name);

        String gender=requestJsonHandler.getStringValue("gender");
        if(gender!=null && !TextUtils.isEmpty(gender))
            userEntity.setGender(gender);

        String dateOfBirth=requestJsonHandler.getStringValue("dateOfBirth");
        if(dateOfBirth!=null && !TextUtils.isEmpty(dateOfBirth))
            userEntity.setDateOfBirth(dateOfBirth);

        String contactNo=requestJsonHandler.getStringValue("contactNo");
        if(contactNo!=null && !TextUtils.isEmpty(contactNo)){
            userEntity.setContactNo(contactNo);
        }

        publicApiRepository.saveOrUpdateEntity(userEntity);
        Optional<UserEntity> userEntityOptional= Optional.of(userEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEntityOptional
                .map(PlanbowUserDetails::new)
                .get(),userEntityOptional.get().getPassword(),userEntityOptional.get().getRolesEntity().stream().map(e-> new SimpleGrantedAuthority(e.getRoleName())).collect(Collectors.toList()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseJsonUtil.getResponse(ResponseConstants.SUCCESS.getStatus(), 200, ResponseConstants.SUCCESS.getStatus(), false);
    }

}
