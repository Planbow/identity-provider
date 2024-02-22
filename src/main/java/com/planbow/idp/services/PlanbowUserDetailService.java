package com.planbow.idp.services;


import com.planbow.idp.entities.UserEntity;
import com.planbow.idp.repository.PlanbowIdentityRepository;
import com.planbow.idp.entities.PlanbowUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanbowUserDetailService implements UserDetailsService {

    private PlanbowIdentityRepository planbowIdentityRepository;

    @Autowired
    public void setNidavellirIdentityRepository(PlanbowIdentityRepository planbowIdentityRepository) {
        this.planbowIdentityRepository = planbowIdentityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOptional = planbowIdentityRepository.getUserEntity(email);
        userEntityOptional
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
        return userEntityOptional
                .map(PlanbowUserDetails::new)
                .get();
    }
}
