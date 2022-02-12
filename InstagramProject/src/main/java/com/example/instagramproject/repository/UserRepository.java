package com.example.instagramproject.repository;

import com.example.instagramproject.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
