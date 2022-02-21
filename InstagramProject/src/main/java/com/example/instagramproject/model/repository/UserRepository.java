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

    Optional<UserEntity> findUserEntityByUsernameContaining (String name);

    Optional<UserEntity> findUserEntityByUsername (String name);

    Optional<UserEntity> findUserEntityByEmail (String email);

    Optional<UserEntity> findUserEntityByFullName(String fullName);

    Optional<UserEntity> findUserEntityByFullNameContaining(String fullName);
}
