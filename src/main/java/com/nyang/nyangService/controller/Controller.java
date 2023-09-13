package com.nyang.nyangService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nyang.nyangService.dto.AppleUserDto;
import com.nyang.nyangService.dto.UserDto;
import com.nyang.nyangService.dto.UserResponse;
import com.nyang.nyangService.entity.UserEntity;
import com.nyang.nyangService.repository.PostRepository;
import com.nyang.nyangService.repository.UserRepository;
import com.nyang.nyangService.service.AppleService;
//import com.nyang.nyangService.service.UserService;
import com.nyang.nyangService.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.parameters.P;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class Controller {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    private final AppleService appleService;

    @GetMapping("/users/hello")
    public String sayHello() {
        return "성공했나?";
    }

    //회원가입
    @PostMapping("/users/save")
    public ResponseEntity<UserResponse.LoginSuccessDto> userSave(@RequestBody AppleUserDto.ClientAppleCode clientAppleCode) throws Exception {
        log.info("회원가입 시작");
        log.info(clientAppleCode.getIdentityToken());
        log.info(clientAppleCode.getAuthorizationCode());
        LinkedHashMap<String, Object> data = appleService.getAppleInfo(clientAppleCode.getIdentityToken(), clientAppleCode.getAuthorizationCode());
//        String appleUserId = appleService.getUserData();
        log.info("apple 서버 통신 완료");

        log.info(data.get("id_token").toString());


        String accessToken = String.valueOf(data.get("access_token"));

        String refreshToken = String.valueOf(data.get("refresh_token"));

        Long expireTime = Long.valueOf(String.valueOf(data.get("expires_in")));

        String idToken = String.valueOf(data.get("id_token"));
        log.info(accessToken);
        log.info(refreshToken);

        String appleUserId = userService.idTokenParsing(idToken);

        if (!userRepository.existsByAppleUserId(appleUserId)) {
            UserEntity userEntity = userService.userSave(appleUserId, accessToken, refreshToken);
            userRepository.save(userEntity);
            log.info(userEntity.getNickname() + " 생성");
            log.info("userSave 완료");
        } else {
            log.info("이미 있는 user니까 update");
            UserEntity user = userRepository.findByAppleUserId(appleUserId).get();
            LocalDateTime now = LocalDateTime.now();
            user.updateUser(user.getNickname(), user.getAppleAccessToken(), user.getAppleRefreshToken(), user.getUserPic(), now);
            userRepository.save(user);
            log.info("userupdate 완료");
        }

        UserEntity userData = userRepository.findByAppleUserId(appleUserId).get();

        UserResponse.LoginSuccessDto loginSuccessDto = UserResponse.LoginSuccessDto.builder()
                .nickname(userData.getNickname())
                .appleAccessToken(accessToken)
                .appleRefreshToken(refreshToken)
                .build();

        log.info("loginsuccessdto 만들어짐");


        return ResponseEntity.ok().body(loginSuccessDto);
    }
}
