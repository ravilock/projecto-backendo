package com.example.mapaCife.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Autowired
  SecurityFilter securityFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity.csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html")
            .permitAll()
            .requestMatchers(HttpMethod.GET, "/healthcheck").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/touristic-spots/{slug}/comments").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/api/touristic-spots/{slug}/comments/{id}").hasRole("USER")
            .requestMatchers(HttpMethod.POST, "/api/touristic-spots/{slug}/comments").hasRole("USER")
            .requestMatchers(HttpMethod.DELETE, "/api/touristic-spots/{slug}/ratings/{id}").hasRole("USER")
            .requestMatchers(HttpMethod.POST, "/api/touristic-spots/{slug}/ratings").hasRole("USER")
            .requestMatchers(HttpMethod.GET, "/api/touristic-spots").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/touristic-spots").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/touristic-spots/{slug}").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/touristic-spots/{slug}").hasRole("ADMIN")
            .anyRequest().authenticated())
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
