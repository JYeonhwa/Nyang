package com.nyang.nyangService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nyang.nyangService.entity.PostEntity;
import com.nyang.nyangService.entity.UserEntity;
import com.nyang.nyangService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserEntity userSave(String appleUserId, String accessToken, String refreshToken) throws ParseException, IOException {
        log.info("userSave 시작");

        Long number;

        try {
            number = userRepository.count() + 1;
        } catch (Exception e) {
            number = 1L;
        }
        log.info("----------------null exception 왜 나냐-----------------");
        log.info(appleUserId);
        log.info(number.toString());
        log.info(accessToken);
        log.info(refreshToken);

        List<PostEntity> posts = new ArrayList<>();

        UserEntity userEntity = UserEntity.builder()
                .appleUserId(appleUserId)
                .nickname("냥냥" + number.toString())
                .createdAt(LocalDateTime.now())
                .lastLoginAt(LocalDateTime.now())
                .userPic("dd")
                .appleAccessToken(accessToken)
                .appleRefreshToken(refreshToken)
                .posts(posts)
                .build();
        return userEntity;
    }

    public String idTokenParsing(String idToken) throws ParseException, JsonProcessingException {
        SignedJWT signedJWT = SignedJWT.parse(idToken);
        ReadOnlyJWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();

        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toJSONString(), JSONObject.class);

        String appleUserId = String.valueOf(payload.get("sub"));
        log.info(appleUserId);
        return appleUserId;
    }
}
