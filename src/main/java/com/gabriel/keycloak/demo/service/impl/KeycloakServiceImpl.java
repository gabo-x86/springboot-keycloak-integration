package com.gabriel.keycloak.demo.service.impl;

import com.gabriel.keycloak.demo.SpringBootKeycloakApplication;
import com.gabriel.keycloak.demo.controller.dto.UserDTO;
import com.gabriel.keycloak.demo.service.IkeycloakService;
import com.gabriel.keycloak.demo.util.KeycloakProvider;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KeycloakServiceImpl implements IkeycloakService {

    private static final Logger log = LoggerFactory.getLogger(KeycloakServiceImpl.class);


    public List<UserRepresentation> findAllUsers() {
        /*RealmResource realmResource = KeycloakProvider.getRealmResource();
        List<UserRepresentation> userResource = realmResource.users().list();
        log.info("-----------------USER RESOURCE-------------------------");
        log.info(userResource.get(0).toString());
        log.info("------------------------------------------");
        return userResource;*/

        return KeycloakProvider.getRealmResource()
                .users()
                .list();
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }

    @Override
    public String createUser(UserDTO userDTO) {
        int status = 0;
        UsersResource usersResource = KeycloakProvider.getUserResource();

        //Setting user resource properties
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        Response response = usersResource.create(userRepresentation);

        status = response.getStatus();

        //password creation
        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);//it's not a temporary password
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);//it indicates the password type
            credentialRepresentation.setValue(userDTO.getPassword());//set the password

            //tell to keycloak to reset the password
            usersResource.get(userId).resetPassword(credentialRepresentation);

            //get all the resources from keycloak
            RealmResource realmResource = KeycloakProvider.getRealmResource();

            List<RoleRepresentation> rolesRepresentation = null;
            if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {//set default role
                rolesRepresentation = List.of(realmResource.roles().get("user").toRepresentation());
            } else {
                rolesRepresentation = realmResource.roles()//get all roles
                        .list()
                        .stream()
                        .filter(role -> userDTO.getRoles()
                                .stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }

            realmResource.users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(rolesRepresentation);

            return "User created successfully!!";

        } else if (status == 409) {
            log.error("User already exist!");
            return "User exist already!";
        } else {
            log.error("Error creating user, please contact with the administrator.");
            return "Error creating user, please contact with the administrator.";
        }
    }

    @Override
    public void deleteUser(String userId) {
        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();
    }

    @Override
    public void updateUser(String userId, @NonNull UserDTO userDTO) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());

        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        user.setEmailVerified(true);
        user.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource usersResource = KeycloakProvider.getUserResource().get(userId);
        usersResource.update(user);
    }
}
