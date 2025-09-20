package com.sid.gl.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; //couplage faible

    public JwtAuthFilter(JwtService jwtService, UserDetailsImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
       log.info("Filtering request for {}", request.getRequestURI());
       String authHeader = request.getHeader("Authorization");
       log.info("Authorization header: {}", authHeader);
       String jwt ;
       String username ;
       if(authHeader == null || !authHeader.startsWith("Bearer ")){
           log.warn("JWT Token does not begin with Bearer string");
           filterChain.doFilter(request, response);
           return;
       }
       jwt = authHeader.substring(7);
       log.info("JWT Token: {}", jwt);
       username = jwtService.extractUserName(jwt);
       log.info("Username: {}", username);
      try{
          if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
              UserDetails userDetails = userDetailsService.loadUserByUsername(username);
              if(jwtService.validateToken(jwt,userDetails)){
                  log.info("User {} is valid", username);
                  UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                          userDetails,
                          null,
                          userDetails.getAuthorities()
                  );
                  authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(authenticationToken); //in memory authentication
                  log.info("User {} is authenticated", username);
              }

          }
      }catch (Exception e){
          log.error("Failed to process JWT token", e);
          throw new RuntimeException(e);
      }finally {
          filterChain.doFilter(request, response);
      }


    }


}
