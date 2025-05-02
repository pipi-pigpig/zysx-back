package com.nurturing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http)) // ✅ 新版 CORS 配置方式
                .csrf(csrf -> csrf.disable())     // ✅ 新版 CSRF 配置方式
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/userInfo").permitAll()
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                );
        return http.build();
    }
}
