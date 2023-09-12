package com.nyang.nyangService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nyang.nyangService.entity.UserEntity;
import com.nyang.nyangService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private static UserRepository userRepository;

    public static void userSave(String idToken, String accessToken, String refreshToken) throws ParseException, IOException {
        log.info("userSave 시작");
        log.info(idToken);

        SignedJWT signedJWT = SignedJWT.parse(idToken);
        ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), JSONObject.class);

        String appleUserId = String.valueOf(payload.get("sub"));
        log.info(appleUserId);

        Long number = userRepository.count();

        if (number == null) {
            number = 1L;
        }

        number = number + 1;

        UserEntity userEntity = UserEntity.builder()
                .appleUserId(appleUserId)
                .nickname("냥냥" + number.toString())
                .createdAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .userPic("dd")
                .appleAccessToken(accessToken)
                .appleRefreshToken(refreshToken)
                .build();

        userRepository.save(userEntity);
        log.info("냥냥" + number.toString() + " 생성");
        log.info("회원가입 완료");
    }
}
