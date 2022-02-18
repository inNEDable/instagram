package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidData;
import com.example.instagramproject.model.DTO.CreateSubCommentDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.model.entity.CommentEntity;
import com.example.instagramproject.model.entity.SubCommentEntity;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.CommentRepository;
import com.example.instagramproject.model.repository.SubCommentRepository;
import com.example.instagramproject.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class SubCommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SubCommentRepository subCommentRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    public ReturnCommentDTO crateSubComment(CreateSubCommentDTO createSubCommentDTO, HttpServletRequest request) {
        if (createSubCommentDTO.getText().isBlank()
                || createSubCommentDTO.getUserId() == null) throw new InvalidData("Invalid data");

        sessionManager.authorizeSession(createSubCommentDTO.getUserId(), request.getSession(), request);

        UserEntity user = userRepository.findById(createSubCommentDTO.getUserId())
                .orElseThrow(() -> new InvalidData("User ID doesn't exist"));
        CommentEntity comment = commentRepository.findById(createSubCommentDTO.getParentCommentId())
                .orElseThrow(() -> new InvalidData("Comment ID doesn't exist"));
        SubCommentEntity subComment = new SubCommentEntity();
        subComment.setDateCreated(LocalDateTime.now());
        subComment.setUser(user);
        subComment.setText(createSubCommentDTO.getText());
        subComment.setComment(comment);
        subCommentRepository.save(subComment);

        return modelMapper.map(subComment, ReturnCommentDTO.class);
    }

    public ReturnCommentDTO deleteSubComment(CreateSubCommentDTO createSubCommentDTO, HttpServletRequest request) {
        if (createSubCommentDTO.getId() == null) throw new InvalidData("Invalid date");

        sessionManager.authorizeSession(createSubCommentDTO.getUserId(), request.getSession(), request);
        SubCommentEntity subComment = subCommentRepository.findById(createSubCommentDTO.getId()).orElseThrow(() -> new InvalidData("Comment ID doesn't exist"));
        subCommentRepository.deleteById(subComment.getId());

        return modelMapper.map(subComment, ReturnCommentDTO.class);
    }
}
