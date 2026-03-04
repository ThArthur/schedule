package dev.rokku.schedule.domain.security.util;

import dev.rokku.schedule.domain.model.user.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
@SuperBuilder
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String username;
    private final String name;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private static List<GrantedAuthority> getAuthorities(Users usuario) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()));
    }

    public static UserDetailsImpl buildUserDetailsImpl(Users usuario) {
        List<GrantedAuthority> authorities = getAuthorities(usuario);
        String username = getUsername(usuario);
        String name = getName(usuario);

        return new UserDetailsImpl(
                usuario.getId(),
                username,
                name,
                usuario.getPasswordHash(),
                authorities
        );
    }

    private static String getUsername(Users usuario) {
        return usuario.getEmail();
    }

    private static String getName(Users usuario) {
        return usuario.getName();
    }

}