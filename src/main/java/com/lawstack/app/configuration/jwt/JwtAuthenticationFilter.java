package com.lawstack.app.configuration.jwt;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lawstack.app.service.implementation.UserDetailServiceImp;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailServiceImp userDetails;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * @implNote This will perform the filtering on each request received from
     *           client side
     * @since v 1.0.0
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String RequestTokenHeader = request.getHeader("Authorization");

        // * Logging the request information
        log.info("Request Type: {}", request.getMethod());
        log.info("Request URI: {}", request.getRequestURI());

        String username = null;
        String jwttoken = null;

        if (RequestTokenHeader != null && RequestTokenHeader.startsWith("Bearer ")) {
            jwttoken = RequestTokenHeader.substring(7);
            try {
                username = this.jwtUtil.extractUsername(jwttoken);

            } catch (Exception e) {
                log.error("Cannot extract username from token or  expirerd token");
            }

        } else {
            log.error("Invalid token, not start with bearer string");
        }

        // * validate token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final UserDetails userDetail = this.userDetails.loadUserByUsername(username);

            if (this.jwtUtil.validateToken(jwttoken, userDetail)) {

                UsernamePasswordAuthenticationToken uPAT = new UsernamePasswordAuthenticationToken(userDetail, null,
                        userDetail.getAuthorities());
                uPAT.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("Authenticated user setting security context");
                SecurityContextHolder.getContext().setAuthentication(uPAT);
            }
        } else {
            log.error("Token validation error");
        }

        filterChain.doFilter(request, response);

    }

}
