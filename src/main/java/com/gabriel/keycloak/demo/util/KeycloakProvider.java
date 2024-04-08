package com.gabriel.keycloak.demo.util;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

public class KeycloakProvider {
    private static final String SERVER_URL = "http://localhost:8081";//keycloak server address
    private static final String REALM_NAME = "spring-keycloak-integration-demo";
    private static final String REALM_MASTER = "master";
    private static final String ADMIN_CLI = "admin-cli";
    private static final String USER_CONSOLE = "keycloak";
    private static final String PASSWORD_CONSOLE = "keycloak";
    private static final String CLIENT_SECRET = "pEKSjEPVLDIlapGp2Kn9J8BAAQV3YRoe";

    public static RealmResource getRealmResource() {
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USER_CONSOLE)
                .password(PASSWORD_CONSOLE)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build())
                .build();

        return keycloak.realm(REALM_NAME);
    }

    public static UsersResource getUserResource() {
        RealmResource realmResource = getRealmResource();
        return realmResource.users();
    }
}
