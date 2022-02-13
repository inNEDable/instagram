package com.example.instagramproject.repositories;

import com.example.instagramproject.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

}
