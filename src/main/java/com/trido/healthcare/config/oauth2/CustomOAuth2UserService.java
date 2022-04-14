package com.trido.healthcare.config.oauth2;

import com.trido.healthcare.config.oauth2.user.OAuth2UserInfo;
import com.trido.healthcare.config.oauth2.user.OAuth2UserInfoFactory;
import com.trido.healthcare.constants.Constants;
import com.trido.healthcare.entity.Role;
import com.trido.healthcare.entity.User;
import com.trido.healthcare.entity.customuserdetails.MyUserDetails;
import com.trido.healthcare.entity.enumm.AuthProvider;
import com.trido.healthcare.exception.OAuth2AuthenticationProcessingException;
import com.trido.healthcare.repository.RoleRepository;
import com.trido.healthcare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Optional;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo =
                OAuth2UserInfoFactory.getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }
        try {
            User user = saveOAuth2User(oAuth2UserInfo, userRequest);
            return getMyUserDetails(oAuth2User, user);
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private User saveOAuth2User(OAuth2UserInfo oAuth2UserInfo, OAuth2UserRequest oAuth2UserRequest) {
        Optional<User> existedUser = userRepository.findByUsername(oAuth2UserInfo.getEmail());
        if (existedUser.isPresent()) {
            if (AuthProvider.LOCAL
                    .equals(existedUser.get().getProvider())) {
                throw new OAuth2AuthenticationProcessingException("Look like you already have an account with this mail address. Please login by this one!");
            }
            updateExistedUser(existedUser.get(), oAuth2UserInfo, oAuth2UserRequest);
            return existedUser.get();
        }

        User newUser = new User();
        newUser.setUsername(oAuth2UserInfo.getEmail());
        newUser.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
        newUser.setProviderId(oAuth2UserInfo.getId());
        newUser.setRoleId(roleRepository.findByRoleName(Constants.USER_ROLE).get().getId());
        newUser.setAuthority("user");
        return userRepository.save(newUser);
    }

    private void updateExistedUser(User user, OAuth2UserInfo oAuth2UserInfo, OAuth2UserRequest oAuth2UserRequest) {
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase()));
        user.setProviderId(oAuth2UserInfo.getId());
        userRepository.save(user);
    }

    private MyUserDetails getMyUserDetails(OAuth2User oAuth2User, User user) {
        Collection<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(Constants.USER_ROLE);
        MyUserDetails myUserDetails =
                new MyUserDetails(user.getId(), Constants.USER_ROLE, user.getUsername(), "",
                        user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(),
                        authorities);
        myUserDetails.setAttributes(oAuth2User.getAttributes());
        return myUserDetails;
    }
}
