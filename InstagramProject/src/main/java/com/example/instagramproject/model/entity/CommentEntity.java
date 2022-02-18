package com.example.instagramproject.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "post_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_time")
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

    @Column
    private String text;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable=false)
    private PostEntity post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity that = (CommentEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
