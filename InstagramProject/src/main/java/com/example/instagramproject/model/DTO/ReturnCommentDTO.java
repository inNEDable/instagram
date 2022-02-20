package com.example.instagramproject.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnCommentDTO {

    private Long id;

    private Long userId;

    private LocalDateTime dateTime;

    private String text;

    @JsonFormat(pattern="yyyy-MM-dd || hh-mm-ss")
    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
