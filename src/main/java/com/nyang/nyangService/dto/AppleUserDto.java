package com.nyang.nyangService.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

public class AppleUserDto {
    private List<IdentityToken> keys;
    @Getter
    @Setter
    public class IdentityToken<T> {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }

    public Optional<AppleUserDto.IdentityToken> getMatchedKeyBy(String kid, String alg) {
        return this.keys.stream()
                .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
                .findFirst();
    }

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
