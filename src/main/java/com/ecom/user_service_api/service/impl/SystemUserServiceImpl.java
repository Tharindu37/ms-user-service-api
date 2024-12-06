package com.ecom.user_service_api.service.impl;

import com.ecom.user_service_api.config.KeycloakSecurityUtil;
import com.ecom.user_service_api.dto.request.RequestUserDto;
import com.ecom.user_service_api.dto.request.RequestUserLoginDto;
import com.ecom.user_service_api.repo.SystemUserRepo;
import com.ecom.user_service_api.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
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

    @Override
    public void signup(RequestUserDto requestUserDto) {

    }

    @Override
    public Object login(RequestUserLoginDto requestUserDto) {
        return null;
    }
}
