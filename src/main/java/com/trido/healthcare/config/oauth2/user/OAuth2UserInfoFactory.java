package com.trido.healthcare.config.oauth2.user;

import com.trido.healthcare.entity.enumm.AuthProvider;
import com.trido.healthcare.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
