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
@RequestMapping("api/sub-comment")
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
}
