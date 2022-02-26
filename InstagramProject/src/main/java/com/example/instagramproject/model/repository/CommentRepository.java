package com.example.instagramproject.model.repository;

import com.example.instagramproject.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query(value = "SELECT * FROM post_comments as c WHERE c.post_id = ?1", nativeQuery = true)
    List<CommentEntity> findAllCommentsByPostId(Long postId);
}

