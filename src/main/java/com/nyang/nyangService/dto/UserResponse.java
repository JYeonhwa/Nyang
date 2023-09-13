package com.nyang.nyangService.dto;

//import com.nyang.nyangService.auth.jwt.JwtProvider;
import lombok.Builder;
import lombok.Getter;
//import org.springframework.security.core.userdetails.UserDetails;

//import javax.validation.constraints.NotEmpty;

public class UserResponse {

//    private JwtProvider jwtProvider;
//    private UserDetails userDetails;

    @Getter
    @Builder
    public static class LoginSuccessDto {

        private String nickname;
        private String appleAccessToken;
        private String appleRefreshToken;
    };

    @Getter
    @Builder
    public static class UserDetailsDto {
        private String userId;
        private String nickname;
        private String userPic;
    }
}
