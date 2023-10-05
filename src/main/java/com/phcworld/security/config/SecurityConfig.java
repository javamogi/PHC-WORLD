package com.phcworld.security.config;

import com.phcworld.jwt.TokenProvider;
import com.phcworld.jwt.config.JwtSecurityConfig;
import com.phcworld.jwt.entry.JwtAuthenticationEntryPoint;
import com.phcworld.jwt.filter.JwtExceptionFilter;
import com.phcworld.jwt.handler.JwtAccessDeniedHandler;
import com.phcworld.jwt.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtExceptionFilter jwtExceptionFilter;

    private final CustomUserDetailsService userDetailsService;

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
                        "/vendor/**",
                        "/h2-console/**")
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

                .antMatchers("/",
                        "/users/form",
                        "/users/loginForm",
                        "/users/login",
                        "/login").permitAll()
                .anyRequest().authenticated()   // 나머지는 전부 인증 필요

//                .and()
//                .headers(headers -> headers.cacheControl(cache -> cache.disable()))
////                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
//                // exception handling 할 때 우리가 만든 클래스를 추가
//                .exceptionHandling()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .accessDeniedHandler(jwtAccessDeniedHandler)
//
//                .and()
//                .headers()
//                .frameOptions()
//                .sameOrigin()
//
//                // 시큐리티는 기본적으로 세션을 사용
//                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .formLogin()
                .loginPage("/users/loginForm")

//                .and()
//                .apply(new JwtSecurityConfig(tokenProvider, jwtExceptionFilter))
                .and().build();

    }

}
