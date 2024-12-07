package com.ecom.user_service_api.dto.request;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
//@Getter
//@Setter
@Builder
public class RequestUserDto {
    private String email;
    private String password;
    private String fName;
    private String lName;

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

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }
}
