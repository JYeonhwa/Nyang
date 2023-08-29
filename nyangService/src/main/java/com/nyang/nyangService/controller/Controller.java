package com.nyang.nyangService.controller;

import com.nyang.nyangService.entity.UserEntity;
import com.nyang.nyangService.repository.PostRepository;
import com.nyang.nyangService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public Controller(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/users/hello")
    public String sayHello() {
        return "성공했나";
    }
    @PostMapping("/users/save")
    public void userSave(@RequestBody UserEntity user) {
        userRepository.save(user);
    }
}
