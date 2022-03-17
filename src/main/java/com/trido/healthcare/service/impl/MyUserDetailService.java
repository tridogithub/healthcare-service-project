package com.trido.healthcare.service.impl;

import com.trido.healthcare.entity.Role;
import com.trido.healthcare.entity.User;
import com.trido.healthcare.entity.customuserdetails.MyUserDetails;
import com.trido.healthcare.repository.RoleRepository;
import com.trido.healthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndIsDeleted(username, false).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        if (!user.isEnabled()) {
            throw new DisabledException("Account is disable");
        }
        Role role = roleRepository.findById(user.getRoleId()).orElseThrow(() -> new UsernameNotFoundException("User's role not found."));
        Collection<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role.getRoleName());
        return new MyUserDetails(user.getId(), role.getRoleName(), user.getUsername(), user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), authorities);
    }
}
