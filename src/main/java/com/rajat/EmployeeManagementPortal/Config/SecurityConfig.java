package com.rajat.EmployeeManagementPortal.Config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static com.rajat.EmployeeManagementPortal.model.USER_ROLE.ADMIN;
import static com.rajat.EmployeeManagementPortal.model.USER_ROLE.EMPLOYEE;
import static com.rajat.EmployeeManagementPortal.model.USER_ROLE.MANAGER;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(
                        corsConfigurationSource()))
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/auth/**")
                                .permitAll() //whitelist (no authorization required)
                                .requestMatchers("/admin/**")
                                .hasAnyAuthority(ADMIN.name())
                                .requestMatchers("/manager/**")
                                .hasAnyAuthority(MANAGER.name())
                                .requestMatchers("/employee/**")
                                .hasAnyAuthority(EMPLOYEE.name())
                                .anyRequest().authenticated()
                        //any other requests need to be authenticated
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(
                    HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();

                //cors setAllowedOrigins for front-end requests
                cfg.setAllowedOrigins(List.of("http://localhost:3000"));
                cfg.setAllowedMethods(Collections.singletonList("*"));
                cfg.setAllowCredentials(true);
                cfg.setAllowedHeaders(Collections.singletonList("*"));
                cfg.setExposedHeaders(List.of("Authorization"));
                cfg.setMaxAge(3600L);

                return cfg;
            }
        };
    }
}
