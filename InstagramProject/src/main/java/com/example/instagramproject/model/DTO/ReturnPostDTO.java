package com.example.instagramproject.model.DTO;

import com.example.instagramproject.model.entity.PostMediaEntity;
import com.example.instagramproject.model.entity.TagEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnPostDTO implements Comparable<ReturnPostDTO> {


    private Long id;

//    private UserEntity user;

    private LocalDateTime dateTime;

    private String text;

    private Set<PostMediaEntity> postMediaEntities;

    private Set<TagEntity> tags;


    @Override
    public String toString() {
        return "ReturnPostDTO{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", text='" + text + '\'' +
                ", postMediaEntities=" + postMediaEntities +
                '}';
    }

    @JsonFormat(pattern="yyyy-MM-dd || hh-mm-ss")
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public int compareTo(ReturnPostDTO otherPost) {
        if (otherPost.getDateTime().isBefore(this.dateTime)){
            return -1;
        } else {
            return 1;
        }
    }
}
