package com.customerApplicationService.Customer_Application_Service.Controller;

import com.customerApplicationService.Customer_Application_Service.JWTAuthentication.JwtRequest;
import com.customerApplicationService.Customer_Application_Service.JWTAuthentication.JwtResponse;
import com.customerApplicationService.Customer_Application_Service.Security.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sunbase/portal/api")
//http://localhost:8080/sunbase/portal/api
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtHelper helper;

//    Use logger to log messages for a specific system or application components
    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


//     Endpoint for handling user authentication and generating JWT token.
//    http://localhost:8080/sunbase/portal/api/assignment_auth.jsp
    @PostMapping("/assignment_auth.jsp")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

//         Authenticate user credentials.
        this.doAuthenticate(request.getEmail(), request.getPassword());

//         Retrieve user details and generate JWT token.
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

//         Build and return JWT response.
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .userName(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//     Helper method for authenticating user credentials.
    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new
                UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
//             Throw exception for invalid credentials if login details is invalid
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

//     Exception handler for BadCredentialsException.
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> exceptionHandler() {
        return new ResponseEntity("Invalid email and password", HttpStatus.BAD_REQUEST);
    }

}
