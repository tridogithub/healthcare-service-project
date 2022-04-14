package com.trido.healthcare.entity.customuserdetails;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@Getter
public class MyUserDetails extends User implements OAuth2User {
    private final UUID userID;
    private final String roleName;
    private Map<String, Object> attributes;

    public MyUserDetails(UUID userID, String roleName, String username, String password, boolean enabled,
                         boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userID = userID;
        this.roleName = roleName;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.getUsername();
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
