package com.example.instagramproject.model.repository;

import com.example.instagramproject.model.entity.SubCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCommentRepository extends JpaRepository<SubCommentEntity, Long> {
}
