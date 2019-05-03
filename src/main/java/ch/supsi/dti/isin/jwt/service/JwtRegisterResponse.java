package ch.supsi.dti.isin.jwt.service;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public class JwtRegisterResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483578L;

    private final String username;
    Collection<? extends GrantedAuthority> authorities;

    public JwtRegisterResponse(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public String getUsername() {
        return this.username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}