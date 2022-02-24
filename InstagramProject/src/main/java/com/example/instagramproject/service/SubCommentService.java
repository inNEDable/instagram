package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidDataException;
import com.example.instagramproject.model.DTO.RequestSubCommentDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.model.entity.CommentEntity;
import com.example.instagramproject.model.entity.SubCommentEntity;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.CommentRepository;
import com.example.instagramproject.model.repository.SubCommentRepository;
import com.example.instagramproject.model.repository.UserRepository;
import com.example.instagramproject.util.SessionManager;
import com.example.instagramproject.util.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubCommentService {

    public static final int MAX_SUB_COMMENT_LENGTH = 500;
    public static final int MIN_SUB_COMMENT_LENGTH = 1;
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


    public ReturnCommentDTO crateSubComment(RequestSubCommentDTO createSubCommentDTO, HttpServletRequest request) {
        Validator.nullChecker(createSubCommentDTO.getUserId(), createSubCommentDTO.getParentCommentId(), createSubCommentDTO.getText());
        Validator.validateStringLength(MIN_SUB_COMMENT_LENGTH, MAX_SUB_COMMENT_LENGTH, createSubCommentDTO.getText());

        sessionManager.authorizeSession(createSubCommentDTO.getUserId(), request.getSession(), request);

        UserEntity user = Validator.getEntity(createSubCommentDTO.getUserId(), userRepository);
        CommentEntity comment = Validator.getEntity(createSubCommentDTO.getParentCommentId(), commentRepository);

        SubCommentEntity subComment = new SubCommentEntity();
        subComment.setDateCreated(LocalDateTime.now());
        subComment.setUser(user);
        subComment.setText(createSubCommentDTO.getText());
        subComment.setComment(comment);
        subCommentRepository.save(subComment);

        return modelMapper.map(subComment, ReturnCommentDTO.class);
    }

    public ReturnCommentDTO deleteSubComment(RequestSubCommentDTO createSubCommentDTO, HttpServletRequest request) {
        Validator.nullChecker(createSubCommentDTO.getId(), createSubCommentDTO.getUserId());

        sessionManager.authorizeSession(createSubCommentDTO.getUserId(), request.getSession(), request);
        SubCommentEntity subComment = Validator.getEntity(createSubCommentDTO.getId(), subCommentRepository);
        subCommentRepository.deleteById(subComment.getId());

        return modelMapper.map(subComment, ReturnCommentDTO.class);
    }

    public Long likeSubComment(Long subCommentId, HttpServletRequest request) {
        Validator.nullChecker(subCommentId);
        sessionManager.authorizeSession(null, request.getSession(), request);

        SubCommentEntity subComment = Validator.getEntity(subCommentId, subCommentRepository);
        UserEntity user = Validator.getEntity((long) request.getSession().getAttribute(SessionManager.USER_ID), userRepository);
        if (user.getLikedSubComments().contains(subComment)) {
            throw new InvalidDataException("User already liked this comment");
        }
        subComment.getLikers().add(user);
        subCommentRepository.save(subComment);

        return (long) subComment.getLikers().size();
    }

    public Long unlikeSubComment(Long subCommentId, HttpServletRequest request) {
        Validator.nullChecker(subCommentId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        SubCommentEntity subComment = Validator.getEntity(subCommentId, subCommentRepository);
        UserEntity user = Validator.getEntity((long) request.getSession().getAttribute(SessionManager.USER_ID), userRepository);
        if (!user.getLikedSubComments().contains(subComment)) {
            throw new InvalidDataException("User did not like this comment");
        }
        subComment.getLikers().remove(user);
        subCommentRepository.save(subComment);

        return (long) subComment.getLikers().size();
    }

    public Long getLikeCount(Long subCommentId, HttpServletRequest request) {
        Validator.nullChecker(subCommentId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        SubCommentEntity subComment = Validator.getEntity(subCommentId, subCommentRepository);
        return (long) subComment.getLikers().size();
    }

    public List<String> getAllSubComments(Long commentId, HttpServletRequest request) {
        Validator.nullChecker(commentId);


        sessionManager.authorizeSession(null, request.getSession(), request);

        List<String> commentEntities = subCommentRepository.findAllSubCommentByCommentId(commentId);
        if (commentEntities.isEmpty()) throw new InvalidDataException("Comment doesn't have any comments yet");

        return commentEntities;
    }

}
