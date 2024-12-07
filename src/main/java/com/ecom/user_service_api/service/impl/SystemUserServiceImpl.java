package com.ecom.user_service_api.service.impl;

import com.ecom.user_service_api.config.KeycloakSecurityUtil;
import com.ecom.user_service_api.dto.request.RequestUserDto;
import com.ecom.user_service_api.dto.request.RequestUserLoginDto;
import com.ecom.user_service_api.entity.SystemUser;
import com.ecom.user_service_api.exception.DuplicateEntryException;
import com.ecom.user_service_api.exception.EntryNotFoundException;
import com.ecom.user_service_api.repo.SystemUserRepo;
import com.ecom.user_service_api.service.SystemUserService;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//@RequiredArgsConstructor
@Service
public class SystemUserServiceImpl implements SystemUserService {

    private final KeycloakSecurityUtil securityUtil;
    private final SystemUserRepo systemUserRepo;
    @Value("${keycloak.config.realm}")
    private String realm;
    @Value("${keycloak.config.client-id}")
    private String clientId;
    @Value("${keycloak.config.secret}")
    private String secret;
    @Value("${spring.security.oauth2.resourceserver.jwt.token-uri}")
    private String apiUrl;

    public SystemUserServiceImpl(KeycloakSecurityUtil securityUtil, SystemUserRepo systemUserRepo) {
        this.securityUtil = securityUtil;
        this.systemUserRepo = systemUserRepo;
    }

    @Override
    public void signup(RequestUserDto requestUserDto) {
        String userId ="";
        Keycloak keycloak = null;

        UserRepresentation representation = null;
        keycloak = securityUtil.getKeycloakInstance();
        
        representation = keycloak.realm(realm).users()
                .search(requestUserDto.getEmail()).stream().findFirst().orElse(null);
        
        if (representation != null) {
            throw new DuplicateEntryException("email is already in use");
        }
        
        UserRepresentation userR = convertUser(requestUserDto);
        Response response = keycloak.realm(realm).users().create(userR);
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            RoleRepresentation roleRepresentation = keycloak.realm(realm).roles().get("user").toRepresentation();
            userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Arrays.asList(roleRepresentation));

            SystemUser su = new SystemUser(
                    userId, requestUserDto.getEmail(), requestUserDto.getFName(), requestUserDto.getLName()
            );
            systemUserRepo.save(su);
        }
    }

    private UserRepresentation convertUser(RequestUserDto requestUserDto) {
        UserRepresentation userR = new UserRepresentation();
        userR.setUsername(requestUserDto.getEmail());
        userR.setFirstName(requestUserDto.getFName());
        userR.setLastName(requestUserDto.getLName());
        userR.setEnabled(true);
        userR.setEmailVerified(true);
        List<CredentialRepresentation> cre = new ArrayList<>();
        CredentialRepresentation r = new CredentialRepresentation();
        r.setTemporary(false);
        r.setValue(requestUserDto.getPassword());
        cre.add(r);
        userR.setCredentials(cre);
        return userR;
    }

    @Override
    public Object login(RequestUserLoginDto requestUserDto) {
        try {
            Optional<SystemUser> selectedUserObj = systemUserRepo.findByEmail(requestUserDto.getEmail());
            if (selectedUserObj.isEmpty()) {
                throw new EntryNotFoundException("email not found");
            }
            MultiValueMap<Object, Object> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("client_id",clientId);
            requestBody.add("grant_type", OAuth2Constants.PASSWORD);
            requestBody.add("username", requestUserDto.getEmail());
            requestBody.add("client_secret", requestUserDto.getPassword());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> response = restTemplate.postForEntity(apiUrl, requestBody, Object.class);
            return response.getBody();


        }catch (Exception e){
            throw new InternalServerErrorException("Something went wrong");
        }
    }
}
