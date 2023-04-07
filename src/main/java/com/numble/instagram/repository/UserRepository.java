package com.numble.instagram.repository;

import com.numble.instagram.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByNickname(String nickname);
}