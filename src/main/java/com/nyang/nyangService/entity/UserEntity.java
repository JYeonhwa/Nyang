package com.nyang.nyangService.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="users_tb")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private Long appleUserId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastLoginAt;

    @Column(nullable = false)
    private String userPic;

    @Column(nullable = false)
    private String appleAccessToken;

    @Column(nullable = false)
    private String appleRefreshToken;

    @OneToMany(mappedBy = "id")
    private List<PostEntity> posts = new ArrayList<>();

}