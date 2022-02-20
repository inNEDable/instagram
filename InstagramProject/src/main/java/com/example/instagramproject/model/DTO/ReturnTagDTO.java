package com.example.instagramproject.model.DTO;

import com.example.instagramproject.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnTagDTO {

    private Long id;

    private String text;
}
