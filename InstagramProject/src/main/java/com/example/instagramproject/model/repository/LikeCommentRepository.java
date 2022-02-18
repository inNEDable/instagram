package com.example.instagramproject.model.repository;

import com.example.instagramproject.model.entity.LikeCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeCommentRepository extends JpaRepository<LikeCommentEntity, Long> {
}
