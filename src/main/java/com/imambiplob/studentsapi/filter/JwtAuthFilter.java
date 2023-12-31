package com.imambiplob.studentsapi.filter;

import com.imambiplob.studentsapi.service.JwtService;
import com.imambiplob.studentsapi.service.StudentDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final StudentDetailsService studentDetailsService;

    private String email = null;

    private List role = null;

    public JwtAuthFilter(JwtService jwtService, StudentDetailsService studentDetailsService) {
        this.jwtService = jwtService;
        this.studentDetailsService = studentDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().matches("/authenticate|/Grades|HSCSubjects/|/SSCSubjects|/getRole|/getId")) {
            filterChain.doFilter(request, response);
        }
        else {
            String authHeader = request.getHeader("Authorization");

            String token = null;

            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                email = jwtService.extractUsername(token);
                role = jwtService.extractRole(token);
            }

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = studentDetailsService.loadUserByUsername(email);

                if(jwtService.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        }

    }

    public String getCurrentUser() {
        return email;
    }

    public Boolean isAdmin() {
        return role.get(0).toString().equalsIgnoreCase("{authority=Admin}");
    }

}