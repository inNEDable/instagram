package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.CreateCommentPostDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
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
    public ResponseEntity<ReturnCommentDTO> createComment(@RequestBody CreateCommentPostDTO createCommentPostDTO, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = commentService.createCommentPost(createCommentPostDTO, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReturnCommentDTO> deleteComment(@RequestBody CreateCommentPostDTO commentToDelete, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = commentService.deleteComment(commentToDelete, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }


}
