package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.RequestCommentDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.service.SubCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.TreeSet;

@RestController
@RequestMapping("api/sub-comments")
public class SubCommentController {

    @Autowired
    private SubCommentService subCommentService;

    @PostMapping("/{commentId}")
    public ResponseEntity<ReturnCommentDTO> createSubComment(@PathVariable Long commentId, @RequestBody RequestCommentDTO createSubCommentDTO, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = subCommentService.crateSubComment(commentId, createSubCommentDTO, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{subCommentId}")
    public ResponseEntity<ReturnCommentDTO> deleteSubComment(@PathVariable Long subCommentId, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = subCommentService.deleteSubComment(subCommentId, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }

    @PostMapping("/like/{subCommentId}")
    public ResponseEntity<ReturnCommentDTO> likeCommentPost(@PathVariable Long subCommentId, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = subCommentService.likeSubComment(subCommentId, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }

    @PostMapping("/unlike/{subCommentId}")
    public ResponseEntity<ReturnCommentDTO> unlikeCommentPost(@PathVariable(name = "subCommentId") Long subCommentId, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = subCommentService.unlikeSubComment(subCommentId, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }

    @GetMapping("/get-all-likes-sub-comment/{subCommentId}")
    public Long getAllLikesCommentPost(@PathVariable(name = "subCommentId") Long subCommentId, HttpServletRequest request) {
        return subCommentService.getLikeCount(subCommentId, request);
    }

    @GetMapping("/all-sub-comments/{commentId}")
    public ResponseEntity<TreeSet<ReturnCommentDTO>> getAllSubComments(@PathVariable Long commentId, HttpServletRequest request) {
        TreeSet<ReturnCommentDTO> subComments = subCommentService.getAllSubComments(commentId, request);
        return new ResponseEntity<>(subComments, HttpStatus.OK);    }
}
