package com.trido.healthcare.config.oauth2;

import com.trido.healthcare.config.token.TokenUtils;
import com.trido.healthcare.controller.dto.JwtResponse;
import com.trido.healthcare.entity.customuserdetails.MyUserDetails;
import com.trido.healthcare.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth2.authorizedRedirectUrl}")
    private List<String> authorizedRedirectUrl;
    private final TokenUtils tokenUtils;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(TokenUtils tokenUtils, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenUtils = tokenUtils;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        JwtResponse jwtResponse = tokenUtils.createJwtResponse(myUserDetails);

        Optional<Cookie> clientRedirectUrl = Arrays.stream(httpServletRequest.getCookies())
                .filter(cookie -> cookie.getName().equals(HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME))
                .findFirst();
        if (clientRedirectUrl.isEmpty() && !isAuthorizedRedirectUrl(clientRedirectUrl.get().getValue())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String expandedTargetUrl = getExpandedUrl(clientRedirectUrl.get().getValue(), jwtResponse);

        clearAuthenticationAttributes(httpServletRequest, httpServletResponse);
        getRedirectStrategy().sendRedirect(httpServletRequest, httpServletResponse, expandedTargetUrl);
    }

    private String getExpandedUrl(String clientRedirectUrl, JwtResponse jwtResponse) {
        return UriComponentsBuilder.fromUriString(clientRedirectUrl)
                .queryParam("access_token", jwtResponse.getAccess_token())
                .queryParam("user_id", jwtResponse.getUser_id())
                .queryParam("token_type", jwtResponse.getToken_type())
                .queryParam("exp", jwtResponse.getExp().getTime() / 1000)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUrl(String redirectUrl) {
        return authorizedRedirectUrl.stream().anyMatch(s -> s.equals(redirectUrl));
    }
}
