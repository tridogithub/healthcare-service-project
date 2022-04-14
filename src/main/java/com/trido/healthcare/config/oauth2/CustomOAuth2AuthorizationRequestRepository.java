package com.trido.healthcare.config.oauth2;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String REDIRECT_URI_PARAM_NAME = "redirect_url";
    public final static String CLIENT_REDIRECT_URL = "client_redirect_url";
    private final HttpSessionOAuth2AuthorizationRequestRepository httpSessionOAuth2AuthorizationRequestRepository;

    public CustomOAuth2AuthorizationRequestRepository() {
        this.httpSessionOAuth2AuthorizationRequestRepository = new HttpSessionOAuth2AuthorizationRequestRepository();
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest httpServletRequest) {
        return httpSessionOAuth2AuthorizationRequestRepository.loadAuthorizationRequest(httpServletRequest);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest oAuth2AuthorizationRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String redirectUrl = httpServletRequest.getParameter(REDIRECT_URI_PARAM_NAME);
        System.out.println(httpServletRequest.getServletPath());
        System.out.println(redirectUrl);
//        httpServletRequest.getSession().setAttribute(CLIENT_REDIRECT_URL, redirectUrl);
//        httpServletRequest.getCookies().
        Cookie cookie = new Cookie(CLIENT_REDIRECT_URL, redirectUrl);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(180);
        httpServletResponse.addCookie(cookie);
        httpSessionOAuth2AuthorizationRequestRepository.saveAuthorizationRequest(oAuth2AuthorizationRequest, httpServletRequest, httpServletResponse);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest httpServletRequest) {
        return httpSessionOAuth2AuthorizationRequestRepository.removeAuthorizationRequest(httpServletRequest);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return httpSessionOAuth2AuthorizationRequestRepository.removeAuthorizationRequest(request, response);
    }
}
