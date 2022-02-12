package com.example.instagramproject.repository;

import com.example.instagramproject.model.Comment;
import org.springframework.data.repository.CrudRepository;

public interface MediaRepository extends CrudRepository<Comment, Long> {
}
