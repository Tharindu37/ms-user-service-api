package com.ecom.user_service_api.util;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class StandardResponse {
    private int code;
    private Object data;
    private String message;

    public StandardResponse(int code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

}
