package com.example.instagramproject.model.repository;

import com.example.instagramproject.model.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    UserEntity findUserEntityByUsername (String name);

    UserEntity findUserEntityByEmail (String email);

    UserEntity findUserEntityByEmailAndPassword (String email, String password);

    UserEntity findUserEntityByUsernameAndPassword (String username, String password);

    Optional<UserEntity> findUserEntityByFullName(String fullName);
}
