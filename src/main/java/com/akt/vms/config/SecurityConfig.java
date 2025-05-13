package com.akt.vms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.akt.vms.security.JwtFilter;

@Configuration
public class SecurityConfig {

    @Autowired private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers("/dealer/**").hasAnyAuthority("DEALER", "ADMIN")
                .requestMatchers("/driver/**").hasAnyAuthority("DRIVER", "ADMIN")
                .requestMatchers("/api/trips/**").hasAnyAuthority("DRIVER", "ADMIN")
                .requestMatchers("/api/routes/**").hasAnyAuthority("DRIVER", "ADMIN")
                .requestMatchers("/user/**").hasAnyAuthority("USER", "ADMIN")
                .anyRequest().hasAuthority("ADMIN") // fallback: only ADMIN can access other unknown routes
            )
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}