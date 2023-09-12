package com.nyang.nyangService.controller;

import com.nyang.nyangService.dto.AppleUserDto;
import com.nyang.nyangService.dto.UserDto;
import com.nyang.nyangService.dto.UserResponse;
import com.nyang.nyangService.entity.UserEntity;
import com.nyang.nyangService.repository.PostRepository;
import com.nyang.nyangService.repository.UserRepository;
import com.nyang.nyangService.service.AppleService;
//import com.nyang.nyangService.service.UserService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class Controller {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
//    private final UserService userService;

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
        UserResponse.LoginSuccessDto loginSuccessDto
                = appleService.getAppleInfo(clientAppleCode.getIdentityToken(), clientAppleCode.getAuthorizationCode());
//        String appleUserId = appleService.getUserData();
        log.info("apple 서버 통신 완료");



        return ResponseEntity.ok().body(loginSuccessDto);
    }

    //로그인
    @PostMapping("/users/login")
    public ResponseEntity<UserResponse.LoginSuccessDto> userLogin(@RequestBody AppleUserDto.ClientAppleCode clientAppleCode) throws Exception {
        UserResponse.LoginSuccessDto loginSuccessDto
                = appleService.getAppleInfo(clientAppleCode.getIdentityToken(), clientAppleCode.getAuthorizationCode());

        return ResponseEntity.ok().body(loginSuccessDto);
    }
}
