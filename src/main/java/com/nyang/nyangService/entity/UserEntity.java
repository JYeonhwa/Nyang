package com.nyang.nyangService.entity;


import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@Table(name="users_tb")
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

}