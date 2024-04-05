package com.customerApplicationService.Customer_Application_Service.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.client.RestTemplate;

@Configuration
class ApplicationConfiguration {


//     Defines a bean for providing user details for authentication.

    @Bean
    public UserDetailsService userDetailsService() {
//         Creates a user with username "test@sunbasedata.com", encoded password, and
//         role "ADMIN"
//         this is for login
        UserDetails userDetails = User.builder().username("test@sunbasedata.com")
                .password(passwordEncoder().encode("Test@123")).roles("ADMIN").build();
        return new InMemoryUserDetailsManager(userDetails);
    }

//     Defines a bean for password encoding.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//     Defines a bean for providing the authentication manager.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
