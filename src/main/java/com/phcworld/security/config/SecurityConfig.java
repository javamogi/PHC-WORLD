package com.phcworld.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2, css, js 무시
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .antMatchers("/css/**",
                        "/data/**",
                        "/daumeditor/**",
                        "/dist/**",
                        "/images/**",
                        "/js/**",
                        "/less/**",
                        "/psrt/**",
                        "/vendor/**")
                .mvcMatchers(
                "/h2-console/**",
                "/favicon.ico"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()

                .antMatchers("/", "/users/form", "/users/loginForm", "/users/login").permitAll()
                .anyRequest().authenticated()   // 나머지는 전부 인증 필요

                .and()
                .formLogin()
                .loginPage("/users/loginForm")

                .and().build();

    }

}
