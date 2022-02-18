package com.example.instagramproject.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {

    // id, user_id, date_time, like_count, comment_count, text

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column
    private String text;

    @OneToMany(mappedBy= "post")
    private Set<PostMediaEntity> postMediaEntities;

    @OneToMany(mappedBy = "post")
    private Set<CommentEntity> commentEntities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostEntity)) return false;
        PostEntity entity = (PostEntity) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
