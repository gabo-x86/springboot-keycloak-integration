package com.gabriel.keycloak.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello-1")
    @PreAuthorize("hasRole('admin')") //Spring use roles like this ROLE_xxxx By default
    public String helloAdmin() {
        return "Hello Spring Boot with Keycloak - ADMIN";
    }

    @GetMapping("/hello-2")
    @PreAuthorize("hasRole('user') or hasRole('admin')")
    public String helloUser() {
        return "Hello Spring Boot with Keycloak - USER";
    }


    /*
    * admin
student
professor
admin
    * */
}
