package com.nyang.nyangService.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserDto {
    @Getter
    @Setter
    public class UserDataDto<T> {
        private String appleUserId;
        private String nickname;
        private LocalDateTime createdAt;
        private LocalDateTime lastLoginAt;
        private String userPic;
        private String appleAccessToken;
        private String appleRefreshToken;
    }
}
