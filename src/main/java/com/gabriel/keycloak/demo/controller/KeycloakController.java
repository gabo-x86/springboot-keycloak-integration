package com.gabriel.keycloak.demo.controller;


import com.gabriel.keycloak.demo.SpringBootKeycloakApplication;
import com.gabriel.keycloak.demo.controller.dto.UserDTO;
import com.gabriel.keycloak.demo.service.IkeycloakService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@CrossOrigin
@RequestMapping("/keycloak/user")
@PreAuthorize("hasRole('admin')")
public class KeycloakController {

    @Autowired
    IkeycloakService keycloakService;

    @GetMapping("/search")
    public ResponseEntity<?> findAllusers(){
        return ResponseEntity.ok(keycloakService.findAllUsers());
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<?> findAllUsers (@PathVariable String username){
        return ResponseEntity.ok(keycloakService.searchUserByUsername(username));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser (@RequestBody UserDTO userDTO) throws URISyntaxException {
        String response = keycloakService.createUser(userDTO);
        return ResponseEntity.created(new URI("/keycloak/user/create")).body(response);

    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserDTO userDTO){
        keycloakService.updateUser(userId, userDTO);
        return ResponseEntity.ok("User updated successfully!");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser (@PathVariable String userId){
        keycloakService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
