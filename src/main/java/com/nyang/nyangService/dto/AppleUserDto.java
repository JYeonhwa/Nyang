package com.nyang.nyangService.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@NoArgsConstructor
public class AppleUserDto {

    @Getter
    @Setter
    public static class ClientAppleCode<T> {
        private String identityToken;
        private String authorizationCode;
    }

    @Getter
    @Setter
    public class AuthorizationDto<T> {
        private String responseCode;
        private T response;
        private String message;
    }


}

