package com.nyang.nyangService.repository;

import com.nyang.nyangService.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByAppleUserId(String appleUserId);
    boolean existsByAppleUserId(String appleUserId);
    boolean existsByNickname(String nickname);

}