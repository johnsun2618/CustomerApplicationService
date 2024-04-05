package com.customerApplicationService.Customer_Application_Service.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

//     Logger for logging information.
    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

//     Autowired instance of JwtHelper for JWT-related operations.
    @Autowired
    private JwtHelper jwtHelper;

//     Autowired instance of UserDetailsService for loading user details.
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

//         Logs the headers of the incoming request.
        logRequestHeaders(request);

//        1. Get token
//         Retrieves the "Authorization" header from the request.
        String requestHeader = request.getHeader("Authorization");
//        System.out.println(requestHeader); to print the requestToken
//        Bearer 16545dsgbggbtr
        logger.info("Header: {}", requestHeader);
        String username = null;
        String token = null;

//         Checks if the header is present and starts with "Bearer".
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            token = requestHeader.substring(7);
            try {
//                 Extracts the username from the JWT token.
                username = this.jwtHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
//                System.out.println("Unable to get Jwt Token");
                logger.error("Illegal Argument while fetching the username !!", e);
            } catch (ExpiredJwtException e) {
//                System.out.println("Jwt token has expired");
                logger.error("Given jwt token is expired !!", e);
            } catch (MalformedJwtException e) {
//                System.out.println("Invalid Jwt");
                logger.error("Some changes have been done in token !! Invalid Token", e);
            } catch (Exception e) {
                logger.error("An unexpected error occurred", e);
            }
        } else {
//            System.out.println("Jwt token does not begin with Bearer");
            logger.info("Invalid Header Value !! ");
        }

//      2.  Once we get the token, now we validate
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load UserDetails from the userDetailsService.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);

//             If token is valid, set the authentication context.
            if (validateToken) {
                UsernamePasswordAuthenticationToken authentication = new
                        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
//                System.out.println("Invalid Jwt token");
                logger.info("Validation fails !!");
            }
        }
//        else {
//            System.out.println("Username is null or context is not null");
//        }

        filterChain.doFilter(request, response);
    }

//     Logs the headers of the incoming request.
    private void logRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.info("Request Header - {}: {}", headerName, headerValue);
        }
    }
}
