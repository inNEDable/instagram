package com.example.instagramproject.repositories;

import com.example.instagramproject.model.CommentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<CommentEntity, Long> {
}
