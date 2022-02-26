package com.example.instagramproject.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnStoryDTO implements Comparable<ReturnStoryDTO> {

    private Long id;

    private Long userId;

    private String media;

    private LocalDateTime expDate;

    @JsonFormat(pattern="yyyy-MM-dd || hh-mm-ss")
    public LocalDateTime getExpDate() {
        return expDate;
    }

    @Override
    public int compareTo(ReturnStoryDTO otherStory) {
        if (otherStory.getExpDate().isBefore(this.expDate)){
            return -1;
        } else {
            return 1;
        }
    }
}
