package com.gabriel.keycloak.demo.controller.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Set;

@Value
@RequiredArgsConstructor
@Builder
public class UserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<String> roles;
}

//Also you can use this way
/*@Builder
public record UserDTO(String username, String email, String firstName, String lastName, String password,
                      Set<String> roles) {
}*/
