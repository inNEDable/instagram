package com.example.instagramproject.model.repository;

import com.example.instagramproject.model.entity.StoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoryRepository extends JpaRepository<StoryEntity, Long> {

    List<StoryEntity> findAllByExpDateBefore(LocalDateTime now);

    @Query(value = "SELECT * FROM stories as s WHERE s.user_id = ?1", nativeQuery = true)
    List<StoryEntity> findAllByUserId(Long userId);
}
