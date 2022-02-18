package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.RequestCommentPostDTO;
import com.example.instagramproject.model.DTO.RequestLikeDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.model.DTO.ReturnLikeDTO;
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
    public ResponseEntity<ReturnCommentDTO> createComment(@RequestBody RequestCommentPostDTO createCommentPostDTO, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = commentService.createCommentPost(createCommentPostDTO, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReturnCommentDTO> deleteComment(@RequestBody RequestCommentPostDTO commentToDelete, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = commentService.deleteComment(commentToDelete, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }

    @PostMapping("/like")
    public ResponseEntity<ReturnLikeDTO> likeComment(@RequestBody RequestLikeDTO requestLikeDTO, HttpServletRequest request) {
        ReturnLikeDTO returnLikeDTO = commentService.likeComment(requestLikeDTO, request);
        return new ResponseEntity<>(returnLikeDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/like-delete")
    public ResponseEntity<ReturnLikeDTO> deleteLikeComment(@RequestBody RequestLikeDTO requestLikeDTO, HttpServletRequest request) {
        ReturnLikeDTO returnLikeDTO = commentService.deleteLike(requestLikeDTO, request);
        return new ResponseEntity<>(returnLikeDTO, HttpStatus.OK);
    }
}
