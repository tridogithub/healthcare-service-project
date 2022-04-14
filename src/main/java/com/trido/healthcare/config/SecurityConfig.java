package com.trido.healthcare.config;

import com.trido.healthcare.config.oauth2.CustomOAuth2UserService;
import com.trido.healthcare.config.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.trido.healthcare.config.oauth2.OAuth2AuthenticationFailureHandler;
import com.trido.healthcare.config.oauth2.OAuth2AuthenticationSuccessHandler;
import com.trido.healthcare.config.token.JwtAuthorizationTokenFilter;
import com.trido.healthcare.config.user_auth.MyUsernamePasswordAuthenticationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUsernamePasswordAuthenticationConfig myUsernamePasswordAuthenticationConfig;
    @Autowired
    private JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/webjars/**",
            "/actuator/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .authorizeRequests(authorizeRequest -> authorizeRequest
                        .antMatchers("/auth/token").permitAll()
                        .antMatchers("/api/users/registry").permitAll()
                        .antMatchers("/api/users").hasAuthority("ADMIN")
                        .antMatchers("/api/users/{userId}").access("@webAuthorization.checkUserWithIdAuthorization(httpServletRequest, #userId)")
                        .antMatchers("/api/practitioners").hasAnyAuthority("ADMIN", "PATIENT")
                        .antMatchers("/api/practitioners/{practitionerId}").access("@webAuthorization.checkPractitionerWithIdAuthorization(httpServletRequest, #practitionerId)")
                        .antMatchers("/api/patients").hasAnyAuthority("ADMIN", "PRACTITIONER")
                        .antMatchers("/api/patients/{patientId}").access("@webAuthorization.checkPatientWithIdAuthorization(httpServletRequest, #patientId)")
                        .antMatchers("/api/appointments").hasAnyAuthority("ADMIN", "PATIENT", "PRACTITIONER")
                        .antMatchers("/api/appointments/{appointmentId}").access("@webAuthorization.checkAppointmentWithIdAuthorization(httpServletRequest, #appointmentId)")
                        .anyRequest().authenticated())
                .apply(myUsernamePasswordAuthenticationConfig)
                .and().addFilterBefore(jwtAuthorizationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(httpSecurityOAuth2LoginConfigurer ->
                        httpSecurityOAuth2LoginConfigurer.authorizationEndpoint(
                                        authorizationEndpointConfig -> authorizationEndpointConfig
                                                .baseUri("/login/oauth2/authorize")
                                                .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                                )
                                .redirectionEndpoint(redirectionEndpointConfig -> redirectionEndpointConfig.baseUri("/login/oauth2/code/*"))
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
                                .successHandler(oAuth2AuthenticationSuccessHandler)
                                .failureHandler(oAuth2AuthenticationFailureHandler)
                )
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }
}
