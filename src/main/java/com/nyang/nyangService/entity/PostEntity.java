package com.nyang.nyangService.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="posts_tb")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @Column(nullable = false)
    private int latitude;

    @Column(nullable = false)
    private int longitude;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String postPic;

}