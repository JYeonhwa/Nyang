package com.nyang.nyangService.entity;


import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@Entity
@Table(name="users_tb")
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @Column(nullable = false)
    private String appleUserId;

    @NotNull
    @Column(nullable = false)
    private String nickname;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime lastLoginAt;

    @NotNull
    @Column(nullable = false)
    private String userPic;

    @NotNull
    @Column(nullable = false)
    private String appleAccessToken;

    @NotNull
    @Column(nullable = false)
    private String appleRefreshToken;

    @OneToMany(mappedBy = "userId")
    private List<PostEntity> posts;


    public void updateUser(String nickname, String appleAccessToken, String appleRefreshToken, String userPic, LocalDateTime lastLoginAt) {
        this.nickname = nickname;
        this.appleAccessToken = appleAccessToken;
        this.appleRefreshToken = appleRefreshToken;
        this.userPic = userPic;
        this.lastLoginAt = lastLoginAt;
    }

}