package com.example.instagramproject.repository;

import com.example.instagramproject.model.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
