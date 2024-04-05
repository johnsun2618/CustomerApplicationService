package com.customerApplicationService.Customer_Application_Service.Configuration;

import com.customerApplicationService.Customer_Application_Service.Security.JwtAuthenticationEntryPoint;
import com.customerApplicationService.Customer_Application_Service.Security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {


    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;

//     Defines a security filter chain with custom configurations.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
//                 Configures URL permissions, allowing access to certain paths without authentication.
                .authorizeRequests().
                requestMatchers("/home/**").authenticated().requestMatchers("/sunbase/portal/api/assignment_auth.jsp").permitAll()
                .anyRequest()
                .authenticated()
//                 Configures exception handling, specifying the authentication entry point.
                .and().exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//         Adds a custom JWT authentication filter before the UsernamePasswordAuthenticationFilter.
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
//         Builds and returns the configured security filter chain.
        return http.build();
    }


}
