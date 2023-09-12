package com.nyang.nyangService.repository;

import com.nyang.nyangService.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    Optional<UserEntity> findByAppleId(String userAppleId);
    boolean existsByAppleId(String userAppleId);
    boolean existsByNickname(String nickname);

}