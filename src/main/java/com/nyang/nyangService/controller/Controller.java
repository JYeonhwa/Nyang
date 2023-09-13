package com.nyang.nyangService.controller;

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

        String id_token = String.valueOf(data.get("id_token"));
        log.info(accessToken);
        log.info(refreshToken);

        UserResponse.LoginSuccessDto loginSuccessDto = UserResponse.LoginSuccessDto.builder()
                .type("Bearer")
                .appleAccessToken(accessToken)
                .appleRefreshToken(refreshToken)
                .refreshTokenExpirationTime(expireTime)
                .build();

        log.info("loginsuccessdto 만들어짐");
        UserEntity userEntity = userService.userSave(id_token, accessToken, refreshToken);

        log.info(userEntity.toString());
        log.info(userRepository.toString());
        userRepository.save(userEntity);
        log.info(userEntity.getNickname() + " 생성");


        log.info("userSave 완료");


        return ResponseEntity.ok().body(loginSuccessDto);
    }

    //로그인
    @PostMapping("/users/login")
    public ResponseEntity<UserResponse.LoginSuccessDto> userLogin(@RequestBody AppleUserDto.ClientAppleCode clientAppleCode) throws Exception {
        LinkedHashMap<String, Object> data
                = appleService.getAppleInfo(clientAppleCode.getIdentityToken(), clientAppleCode.getAuthorizationCode());
        log.info("apple 서버 통신 완료");

        log.info(data.get("id_token").toString());


        String accessToken = String.valueOf(data.get("access_token"));

        String refreshToken = String.valueOf(data.get("refresh_token"));

        Long expireTime = Long.valueOf(String.valueOf(data.get("expires_in")));

        String id_token = String.valueOf(data.get("id_token"));
        log.info(accessToken);
        log.info(refreshToken);

        UserResponse.LoginSuccessDto loginSuccessDto = UserResponse.LoginSuccessDto.builder()
                .type("Bearer")
                .appleAccessToken(accessToken)
                .appleRefreshToken(refreshToken)
                .refreshTokenExpirationTime(expireTime)
                .build();

        log.info("loginsuccessdto 만들어짐");


        return ResponseEntity.ok().body(loginSuccessDto);
    }
}
