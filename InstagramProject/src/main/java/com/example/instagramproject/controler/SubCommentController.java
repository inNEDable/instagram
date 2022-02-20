package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.RequestSubCommentDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.service.SubCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/sub-comments")
public class SubCommentController {

    @Autowired
    private SubCommentService subCommentService;

    @PostMapping("/add")
    public ResponseEntity<ReturnCommentDTO> createSubComment(@RequestBody RequestSubCommentDTO createSubCommentDTO, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = subCommentService.crateSubComment(createSubCommentDTO, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReturnCommentDTO> deleteSubComment(@RequestBody RequestSubCommentDTO createSubCommentDTO, HttpServletRequest request) {
        ReturnCommentDTO returnCommentDTO = subCommentService.deleteSubComment(createSubCommentDTO, request);
        return new ResponseEntity<>(returnCommentDTO, HttpStatus.OK);
    }

    @PostMapping("/{subCommentId}/like")
    public Long likeCommentPost(@PathVariable(name = "subCommentId") Long subCommentId, HttpServletRequest request) {
        return subCommentService.likeSubComment(subCommentId, request);
    }

    @PostMapping("/{subCommentId}/unlike")
    public Long unlikeCommentPost(@PathVariable(name = "subCommentId") Long subCommentId, HttpServletRequest request) {
        return subCommentService.unlikeSubComment(subCommentId, request);
    }

    @GetMapping("/get-all-likes-sub-comment/{subCommentId}")
    public Long getAllLikesCommentPost(@PathVariable(name = "subCommentId") Long subCommentId, HttpServletRequest request) {
        return subCommentService.getLikeCount(subCommentId, request);
    }
}
