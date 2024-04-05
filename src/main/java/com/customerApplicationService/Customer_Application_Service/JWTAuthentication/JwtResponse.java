package com.customerApplicationService.Customer_Application_Service.JWTAuthentication;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JwtResponse {

//     The JWT token generated for successful authentication.
    private String jwtToken;

//     The username associated with the authenticated user.
    private String userName;
}
