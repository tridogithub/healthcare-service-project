package com.trido.healthcare.entity.customuserdetails;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
public class MyUserDetails extends User {
    private final UUID userID;
    private final String roleName;

    public MyUserDetails(UUID userID, String roleName, String username, String password, boolean enabled,
                         boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userID = userID;
        this.roleName = roleName;
    }
}
