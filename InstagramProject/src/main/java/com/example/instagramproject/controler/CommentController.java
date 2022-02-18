package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.CreateCommentPostDTO;
import com.example.instagramproject.model.DTO.ReturnCommentPostDTO;
import com.example.instagramproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<ReturnCommentPostDTO> createComment(@RequestBody CreateCommentPostDTO createCommentPostDTO, HttpServletRequest request) {
        ReturnCommentPostDTO returnCommentDTO = commentService.createCommentPost(createCommentPostDTO, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReturnCommentPostDTO> deleteComment(@RequestBody CreateCommentPostDTO commentToDelete, HttpServletRequest request) {
        ReturnCommentPostDTO returnCommentDTO = commentService.deleteComment(commentToDelete, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }


}
