package com.example.instagramproject.model.repository;

import com.example.instagramproject.model.entity.PostMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMediaRepository extends JpaRepository<PostMediaEntity, Long> {
}
