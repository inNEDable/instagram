package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.CreatePostDTO;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostEntity> createPost (@RequestBody CreatePostDTO createPostDTO, HttpSession session, HttpServletRequest request){
        PostEntity postResponseDTO =  postService.createPost(createPostDTO, session, request);
        return new ResponseEntity<>(postResponseDTO, HttpStatus.CREATED);
    }
}
