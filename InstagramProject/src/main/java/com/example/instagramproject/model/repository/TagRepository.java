package com.example.instagramproject.model.repository;

import com.example.instagramproject.model.entity.CommentEntity;
import com.example.instagramproject.model.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    TagEntity findByText (String text);
}
