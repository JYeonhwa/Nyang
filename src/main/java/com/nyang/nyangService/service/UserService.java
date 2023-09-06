//package com.nyang.nyangService.service;
//
//import com.nyang.nyangService.auth.jwt.JwtProvider;
//import com.nyang.nyangService.entity.UserEntity;
//import com.nyang.nyangService.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class UserService {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtProvider jwtProvider;
//
//    private final AuthenticationManager authenticationManager;
//
//
//    public ResponseEntity<?> signUp(UserRequest.SignUp signUp) throws Exception {
//        if (userRepository.existsByEmail(signUp.getEmail())) {
//            return response.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
//        }
//        userRepository.save(
//                UserRequest.SignUp.toEntity(signUp, passwordEncoder.encode(signUp.getPassword()))
//        );
//
//        UsernamePasswordAuthenticationToken authenticationToken = signUp.toAuthentication();
//        Authentication authentication = authenticationManager.authenticate(authenticationToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserResponse.TokenDto token = jwtProvider.createToken(authentication);
//
//        return response.success("회원가입 성공!", HttpStatus.CREATED);
//    };
//
//    public ResponseEntity<?> login(UserRequest.Login login) throws Exception {
//        UserEntity user;
//        try {
//            user = userRepository.findByEmail(login.getEmail())
//                    .orElseThrow(()-> new Exception(String.valueOf(response.fail("존재하지 않는 이메일입니다.", HttpStatus.BAD_REQUEST))));
//            if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
//                return response.fail("비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);
//            }
//
//            UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();
//            Authentication authentication = authenticationManager.authenticate(authenticationToken);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            UserResponse.TokenDto token = jwtProvider.createToken(authentication);
//            UserResponse.LoginSuccessDto loginSuccessDto = UserResponse.LoginSuccessDto.builder()
//                    .tokenDto(token)
//                    .settings(user.getSettings())
//                    .email(user.getEmail())
//                    .build();
//
//            return response.success(loginSuccessDto,"로그인 성공!", HttpStatus.OK);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    };
//
//    public ResponseEntity<?> logout(Authentication authentication) throws Exception {
//
//        // 토큰 지우기
////        tokenRepository.delete(
////                tokenRepository.findByEmail(authentication.getName())
////                        .orElseThrow(() -> new Exception(String.valueOf(response.fail("사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST))))
////        );
//        return response.success("로그아웃 성공!", HttpStatus.OK);
//    };
//
//    public ResponseEntity<?> saveSettings(Authentication authentication, String newSettings) throws Exception {
//        // 유저 업데이트 settings만
//        UserEntity user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new Exception(String.valueOf(response.fail("사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST))));
//        user.updateUser(user.getEmail(), user.getPassword(), user.getNickname(), newSettings);
//        userRepository.save(user);
//        return response.success("설정 저장 성공!", HttpStatus.ACCEPTED);
//    };
//
//    public ResponseEntity<?> checkPassword(String email, String newPassword) throws Exception {
//        // 유저 업데이트 settings만
//        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new Exception(String.valueOf(response.fail("사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST))));
//        if (!passwordEncoder.matches(newPassword, user.getPassword())) {
//            return response.fail("비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);
//        }
//        return response.success("비밀번호 확인 성공!", HttpStatus.ACCEPTED);
//    };
//
//    public ResponseEntity<?> userdetailsend(String email) throws Exception {
//        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new Exception(String.valueOf(response.fail("사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST))));
//        UserResponse.UserDetailsDto userDetailsDto = UserResponse.UserDetailsDto.builder()
//                .email(user.getEmail())
//                .nickname(user.getNickname())
//                .settings(user.getSettings())
//                .build();
//        return response.success(userDetailsDto, "유저 정보 보내기", HttpStatus.ACCEPTED);
//    };
//
//    public ResponseEntity<?> isDuplicatedEmail(String email) throws Exception {
//        log.info(String.valueOf(userRepository.existsByEmail(email)));
//        log.info(email);
//        if (!userRepository.existsByEmail(email)) {
//            return response.success("가입 가능한 이메일입니다.", HttpStatus.OK);
//        } else {
//            return response.fail("이미 가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    public ResponseEntity<?> withdrawal(String email) throws Exception {
//        log.info("withdrawal email: " + email);
//        // 유저 지우기
//        if (userRepository.existsByEmail(email)) {
//            userRepository.delete(
//                    userRepository.findByEmail(email)
//                            .orElseThrow(() -> new Exception(String.valueOf(response.fail("사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST))))
//            );
//            return response.success("회원탈퇴 성공!", HttpStatus.OK);
//        } else {
//            return response.fail("사용자를 찾을 수 없습니다..", HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    public ResponseEntity<?> updateUserPassword(String email, String newPassword) throws Exception {
//        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new Exception(String.valueOf(response.fail("사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST))));
//
//        user.updateUser(user.getEmail(), passwordEncoder.encode(newPassword), user.getNickname(), user.getSettings());
//        userRepository.save(user);
//        return response.success("정보 수정 성공!", HttpStatus.OK);
//    }
//
//    public ResponseEntity<?> updateUserNick(String email, String newNick) throws Exception {
//        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new Exception(String.valueOf(response.fail("사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST))));
//
//        user.updateUser(user.getEmail(), user.getPassword(), newNick, user.getSettings());
//        userRepository.save(user);
//        return response.success("정보 수정 성공!", HttpStatus.OK);
//    }