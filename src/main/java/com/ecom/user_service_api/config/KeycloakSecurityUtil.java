package com.ecom.user_service_api.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakSecurityUtil {
    Keycloak keycloak;

    @Value("http://localhost:8080/")
    private String serverUrl;

    @Value("${keycloak.config.realm}")
    private String realm;

    @Value("eadp-client")
    private String clientId;

    @Value("password")
    private String grantType;

    @Value("tharindut520@gmail.com")
    private String username;

    @Value("1234")
    private String password;

    @Value("vweVyBWsnxpfaHJ0noyoI152X8rYlwtY")
    private String secret;

    public Keycloak getKeycloakInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder().serverUrl(serverUrl)
                    .realm(realm)
                    .clientId(clientId)
                    .clientSecret(secret)
                    .grantType(grantType)
                    .username(username)
                    .password(password).build();
        }
        return keycloak;
    }
}
