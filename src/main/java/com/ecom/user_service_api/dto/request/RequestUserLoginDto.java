package com.ecom.user_service_api.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
//@Getter
//@Setter
@Builder
public class RequestUserLoginDto {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
