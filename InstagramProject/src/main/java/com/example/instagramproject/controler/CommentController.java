package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.RequestCommentPostDTO;
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
    public ResponseEntity<ReturnCommentDTO> createComment(@RequestBody RequestCommentPostDTO createCommentPostDTO, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = commentService.createCommentPost(createCommentPostDTO, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReturnCommentDTO> deleteComment(@RequestBody RequestCommentPostDTO commentToDelete, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = commentService.deleteComment(commentToDelete, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }

    @PostMapping("/{commentId}/like")
    public Long likeCommentPost(@PathVariable(name = "commentId") Long commentId, HttpServletRequest request) {
        return commentService.likeCommentPost(commentId, request);
    }

    @PostMapping("/{commentId}/unlike")
    public Long unlikeCommentPost(@PathVariable(name = "commentId") Long commentId, HttpServletRequest request) {
        return commentService.unlikeCommentPost(commentId, request);
    }

    @GetMapping("/get-all-likes-comment/{commentId}")
    public Long getAllLikesCommentPost(@PathVariable(name = "commentId") Long commentId, HttpServletRequest request) {
        return commentService.getLikeCount(commentId, request);
    }
}
