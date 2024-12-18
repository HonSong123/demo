package com.example.demo.Controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Cấu hình authorizeRequests
            .authorizeHttpRequests(authorize -> authorize
                // Các trang không yêu cầu đăng nhập
                .requestMatchers("/", "/login", "/forgotpass", 
                                 "/base.html", "/login2.html", "/forgotpass.html", 
                                 "/static/**", "/layouts/**", "/assets/**","/oauth2/**").permitAll()
                .requestMatchers("/api/sanpham/**", "/api/auth/**", 
                                 "/api/cart/**", "/api/category/**", 
                                 "/api/customer/**", "/api/checkout/**", 
                                 "/api/promotionpromotion/**").permitAll()
                // Yêu cầu đăng nhập với các trang khác
                .requestMatchers("/profit", "/checkout", 
                                 "/check-out.html", "/profit.html").authenticated()
                .anyRequest().authenticated()
            )
            // Cấu hình OAuth2 login (Google)
            .oauth2Login(oauth -> oauth
            .loginPage("/login")
            .defaultSuccessUrl("/api/auth/google/login-success", true)
            .failureUrl("/login?error=true")
        )
            // Cấu hình logout
            .logout(logout -> logout
                .logoutUrl("/logout") // Đường dẫn xử lý logout
                .logoutSuccessUrl("/") // Chuyển về trang chủ sau khi logout
                .invalidateHttpSession(true) // Xóa session sau khi logout
                .deleteCookies("JSESSIONID") // Xóa cookie JSESSIONID
            )
            // Bật CORS và tắt CSRF nếu cần thiết
            .cors().and()
            .csrf().disable() // Tắt CSRF nếu không dùng (cẩn thận với việc này)
            // Cấu hình CSP nếu cần thiết (CSP có thể giúp tránh các lỗi tải script từ Google và AngularJS)
            .headers(headers -> headers
            .contentSecurityPolicy("script-src 'self' https://ajax.googleapis.com https://www.gstatic.com https://accounts.google.com https://apis.google.com 'unsafe-inline' 'unsafe-eval' blob: data:")
            
            // Add more headers as needed
        );
            

        return http.build();
    }



}


