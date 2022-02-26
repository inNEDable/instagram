package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.RequestCommentDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.TreeSet;

@RestController
@RequestMapping("api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{postId}")
    public ResponseEntity<ReturnCommentDTO> createComment(@PathVariable Long postId, @RequestBody RequestCommentDTO createCommentPostDTO, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = commentService.createCommentPost(postId, createCommentPostDTO, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ReturnCommentDTO> deleteComment(@PathVariable Long commentId, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = commentService.deleteComment(commentId, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }

    @PostMapping("/like/{commentId}")
    public ResponseEntity<ReturnCommentDTO> likeCommentPost(@PathVariable Long commentId, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = commentService.likeCommentPost(commentId, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }

    @PostMapping("/unlike/{commentId}")
    public ResponseEntity<ReturnCommentDTO> unlikeCommentPost(@PathVariable Long commentId, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = commentService.unlikeCommentPost(commentId, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }

    @GetMapping("/get-all-likes-comment/{commentId}")
    public Long getAllLikesCommentPost(@PathVariable Long commentId, HttpServletRequest request) {
        return commentService.getLikeCount(commentId, request);
    }

    @GetMapping("/all-post-comments/{postId}")
    public ResponseEntity<TreeSet<ReturnCommentDTO>> getAllPostComments(@PathVariable Long postId, HttpServletRequest request) {
        TreeSet<ReturnCommentDTO> comments = commentService.getAllPostComments(postId, request);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
