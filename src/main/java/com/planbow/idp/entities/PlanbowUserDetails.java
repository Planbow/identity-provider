package com.planbow.idp.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class PlanbowUserDetails extends UserEntity implements UserDetails {

    public PlanbowUserDetails(final UserEntity userEntity) {
        super(userEntity);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getRolesEntity().stream().map(e-> new SimpleGrantedAuthority(e.getRoleName())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
