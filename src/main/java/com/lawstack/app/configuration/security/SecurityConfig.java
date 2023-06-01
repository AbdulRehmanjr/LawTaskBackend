package com.lawstack.app.configuration.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lawstack.app.configuration.jwt.JwtAuthenticationEntryPoint;
import com.lawstack.app.configuration.jwt.JwtAuthenticationFilter;
import com.lawstack.app.service.implementation.UserDetailServiceImp;

@EnableWebSecurity
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

        @Autowired
        private JwtAuthenticationEntryPoint unauthorizedHandler;
        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;
        @Autowired
        private UserDetailServiceImp userDetailService;

        private final  String[] origins = { "http://localhost:4200", "https://hooks.stripe.com","https://r.stripe.com",
                        "http://139.59.215.241",
                        "http://lawtasks.pro", "https://lawtasks.pro", "https://139.59.215.241",
                        "https://dashboard.stripe.com",
                        "https://154.192.170.22",
                        "https://api.stripes.com",
                        "https://js.stripe.com",
                        "https://eventhooks.stripe.com"
                 };

       
        @Bean
        AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider dao = new DaoAuthenticationProvider();

                dao.setUserDetailsService(this.userDetailService);
                dao.setPasswordEncoder(this.encoder());
                return dao;
        }

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

                return config.getAuthenticationManager();
        }

        @Bean
        PasswordEncoder encoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf().disable()
                                .cors()
                                .and()
                                .headers()
                                .frameOptions().sameOrigin()
                                .and()
                                .authorizeHttpRequests(
                                                (req) -> req
                                                                .requestMatchers("/user/**",
                                                                                "/sellerrequest/**", "/order/**",
                                                                                "/subscription/**", "/file/**",
                                                                                "/userChat/**", "/checkout/**",
                                                                                "/coupon/**",
                                                                                "/dashboard/**", "/chatlist/**",
                                                                                "/chat/**", "/userChat/**",
                                                                                "/seller/**", "/job/**",
                                                                                "/userdashboard/**",
                                                                                "/role/**", "/social/**", "/order/**",
                                                                                "/token/**",
                                                                                "/join/**", "/ws/**", "/freelancer/**",
                                                                                "/app/**","/v1/**","/api/v1/**")
                                                                .permitAll()
                                                                .anyRequest().authenticated())
                                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                                .and()
                                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and()
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
                ;
                return http.build();
        }

        @Bean
        CorsFilter corsFilter() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Arrays.asList(origins));
                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(Arrays.asList("*"));
                configuration.setAllowCredentials(true);
                configuration.setExposedHeaders(Arrays.asList("x-Auth-Token", "Acess-Control-Allow-Origin"));
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return new CorsFilter(source);
        }
}
