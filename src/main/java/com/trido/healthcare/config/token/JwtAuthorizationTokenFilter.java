package com.trido.healthcare.config.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trido.healthcare.config.auth.BearerContext;
import com.trido.healthcare.config.auth.BearerContextHolder;
import com.trido.healthcare.constants.ConstantMessages;
import com.trido.healthcare.constants.Constants;
import com.trido.healthcare.controller.dto.ErrorResponse;
import com.trido.healthcare.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Configuration
@Order(1)
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
    private TokenUtils tokenUtils;

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
                saveBearerContext(jwtToken);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (InvalidTokenException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getError(), e.getError_description());
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
        } catch (Exception e) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
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

    private BearerContext saveBearerContext(String jwtToken) {
        //check if the token match with the one on database or not.
        Claims claims = tokenUtils.checkValidAccessToken(jwtToken);

        Optional<String> userId = Optional.ofNullable((String) claims.get(Constants.USER_ID_CLAIMS_NAME));
        Optional<String> userName = Optional.ofNullable(claims.getSubject());
        Optional<String> role = Optional.ofNullable((String) claims.get(Constants.ROLE_CLAIMS_NAME));

        BearerContext bearerContext = BearerContextHolder.getContext();
        bearerContext.setBearerToken(jwtToken);
        bearerContext.setUserId(userId.orElseThrow(() ->
                new InvalidTokenException(ConstantMessages.INVALID_TOKEN, ConstantMessages.INVALID_USER_ID))
        );
        bearerContext.setUserName(userName.orElseThrow(() ->
                new InvalidTokenException(ConstantMessages.INVALID_TOKEN, ConstantMessages.INVALID_USERNAME))
        );
        bearerContext.setRoleName(role.orElseThrow(() ->
                new InvalidTokenException(ConstantMessages.INVALID_TOKEN, ConstantMessages.INVALID_ROLE))
        );
        return bearerContext;
    }
}
