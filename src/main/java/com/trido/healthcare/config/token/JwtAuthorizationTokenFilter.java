package com.trido.healthcare.config.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trido.healthcare.config.auth.BearerContext;
import com.trido.healthcare.config.auth.BearerContextHolder;
import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.constants.Constants;
import com.trido.healthcare.controller.dto.ErrorResponse;
import com.trido.healthcare.exception.InvalidRequestException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
    private final TokenUtils tokenUtils;

    @Autowired
    public JwtAuthorizationTokenFilter(TokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = getJwtToken(httpServletRequest);
            if (jwtToken != null) {
                //Set auth information to Bearer
                saveToSecurityContext(jwtToken);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (InvalidRequestException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getTimestamp(), e.getError(), e.getError_description(), e.getHttpStatus());
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        } finally {
            BearerContextHolder.clearContext();
        }
    }

    private String getJwtToken(HttpServletRequest request) {
        if (request.getHeader(Constants.AUTHORIZATION_HEADER) != null
                && request.getHeader(Constants.AUTHORIZATION_HEADER).startsWith(Constants.TOKEN_PREFIX)) {
            return request.getHeader(Constants.AUTHORIZATION_HEADER).replace(Constants.TOKEN_PREFIX, "");
        }
        return null;
    }

    private void saveToSecurityContext(String jwtToken) {
        //check if the token match with the one on database or not.
        Claims claims = tokenUtils.checkValidAccessToken(jwtToken);

        Optional<String> userId = Optional.ofNullable((String) claims.get(Constants.USER_ID_CLAIMS_NAME));
        Optional<String> userName = Optional.ofNullable(claims.getSubject());
        Optional<String> role = Optional.ofNullable((String) claims.get(Constants.ROLE_CLAIMS_NAME));

        BearerContext bearerContext = BearerContextHolder.getContext();
        bearerContext.setBearerToken(jwtToken);
        bearerContext.setUserId(userId.orElseThrow(() ->
                new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_TOKEN,
                        ConstantMessages.TOKEN_INVALID_USER_ID, HttpStatus.UNAUTHORIZED))
        );
        bearerContext.setUserName(userName.orElseThrow(() ->
                new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_TOKEN,
                        ConstantMessages.TOKEN_INVALID_USER_NAME, HttpStatus.UNAUTHORIZED))
        );
        bearerContext.setRoleName(role.orElseThrow(() ->
                new InvalidRequestException(LocalDateTime.now(), ConstantMessages.INVALID_TOKEN,
                        ConstantMessages.TOKEN_INVALID_ROLE, HttpStatus.UNAUTHORIZED))
        );
        Collection<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role.get());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userName.get(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
