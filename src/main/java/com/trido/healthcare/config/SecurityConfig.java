package com.trido.healthcare.config;

import com.trido.healthcare.config.token.JwtAuthorizationTokenFilter;
import com.trido.healthcare.config.user_auth.MyUsernamePasswordAuthenticationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyUsernamePasswordAuthenticationConfig myUsernamePasswordAuthenticationConfig;
    @Autowired
    private JwtAuthorizationTokenFilter jwtAuthorizationTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .csrf(csrf -> csrf.disable())
                .cors()
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .apply(myUsernamePasswordAuthenticationConfig)
                .and().addFilterBefore(jwtAuthorizationTokenFilter, UsernamePasswordAuthenticationFilter.class)
        ;
    }
}
