package com.example.instagramproject.model.repository;

import com.example.instagramproject.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query(value = "SELECT * FROM posts as p WHERE p.user_id = ?1", nativeQuery = true)
    List<PostEntity> findAllByUserId (Long userId);

    List<PostEntity> findAllByTextContaining (String text);

}
