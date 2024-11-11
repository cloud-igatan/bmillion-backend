package com.example.bmillion_backend.core.config;

import com.example.bmillion_backend.core.security.JwtAuthTokenFilter;
import com.example.bmillion_backend.entity.custom.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final JwtAuthTokenFilter jwtAuthTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .httpBasic().disable()
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(header -> {
                    header.frameOptions().sameOrigin().contentTypeOptions().and()
                            .httpStrictTransportSecurity().includeSubDomains(true)
                            .maxAgeInSeconds(31536000);
                })
                .formLogin().disable()
                .logout(logout-> logout
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/error/**"),
                                AntPathRequestMatcher.antMatcher("/auth/**"),
                                AntPathRequestMatcher.antMatcher("/normal/home/**"),
                                AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                                AntPathRequestMatcher.antMatcher("/v3/api-docs/**")

                        ).permitAll()
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/admin/**")
                        ).hasAnyAuthority(UserRole.Admin.name())
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}