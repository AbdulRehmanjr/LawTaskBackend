package com.lawstack.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lawstack.app.model.User;

public interface UserRespository extends JpaRepository<User, String> {

    List<User> findByUserName(String userName);

    List<User> findByUserNameContains(String userNameLike);

    User findByEmail(String email);

}
