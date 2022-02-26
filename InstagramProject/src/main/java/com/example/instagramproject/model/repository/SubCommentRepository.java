package com.example.instagramproject.model.repository;

import com.example.instagramproject.model.entity.SubCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCommentRepository extends JpaRepository<SubCommentEntity, Long> {

    @Query(value = "SELECT * FROM sub_comments as c WHERE c.parent_comment_id = ?1", nativeQuery = true)
    List<SubCommentEntity> findAllSubCommentByCommentId(Long commentId);
}
