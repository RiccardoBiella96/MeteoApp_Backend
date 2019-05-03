package ch.supsi.dti.isin.jwt.security;
import java.util.List;
import java.util.stream.Collectors;

import ch.supsi.dti.isin.jwt.security.dto.JwtUser;
import ch.supsi.dti.isin.jwt.model.Authority;
import ch.supsi.dti.isin.jwt.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getUsername(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getAuthorities()),
                user.getEnabled()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }
}