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
        Validator.validateStringLength(0, MAX_SUB_COMMENT_LENGTH, createSubCommentDTO.getText());
        if (createSubCommentDTO.getText().isBlank()
                || createSubCommentDTO.getUserId() == null) throw new InvalidDataException("Invalid data");

        sessionManager.authorizeSession(createSubCommentDTO.getUserId(), request.getSession(), request);

        UserEntity user = userRepository.findById(createSubCommentDTO.getUserId())
                .orElseThrow(() -> new InvalidDataException("User ID doesn't exist"));
        CommentEntity comment = commentRepository.findById(createSubCommentDTO.getParentCommentId())
                .orElseThrow(() -> new InvalidDataException("Comment ID doesn't exist"));
        SubCommentEntity subComment = new SubCommentEntity();
        subComment.setDateCreated(LocalDateTime.now());
        subComment.setUser(user);
        subComment.setText(createSubCommentDTO.getText());
        subComment.setComment(comment);
        subCommentRepository.save(subComment);

        return modelMapper.map(subComment, ReturnCommentDTO.class);
    }

    public ReturnCommentDTO deleteSubComment(RequestSubCommentDTO createSubCommentDTO, HttpServletRequest request) {
        if (createSubCommentDTO.getId() == null) throw new InvalidDataException("Invalid date");

        sessionManager.authorizeSession(createSubCommentDTO.getUserId(), request.getSession(), request);
        SubCommentEntity subComment = subCommentRepository.findById(createSubCommentDTO.getId()).orElseThrow(() -> new InvalidDataException("Comment ID doesn't exist"));
        subCommentRepository.deleteById(subComment.getId());

        return modelMapper.map(subComment, ReturnCommentDTO.class);
    }

    public Long likeSubComment(Long subCommentId, HttpServletRequest request) {
        sessionManager.authorizeSession(null, request.getSession(), request);
        SubCommentEntity subComment = getSubCommentById(subCommentId);
        UserEntity user = getUserById((long)request.getSession().getAttribute(SessionManager.USER_ID));
        if (user.getLikedSubComments().contains(subComment)) {
            throw new InvalidDataException("User already liked this comment");
        }
        subComment.getLikers().add(user);
        subCommentRepository.save(subComment);

        return (long)subComment.getLikers().size();
    }

    public Long unlikeSubComment(Long subCommentId, HttpServletRequest request) {
        sessionManager.authorizeSession(null, request.getSession(), request);
        SubCommentEntity subComment = getSubCommentById(subCommentId);
        UserEntity user = getUserById((long)request.getSession().getAttribute(SessionManager.USER_ID));
        if (!user.getLikedSubComments().contains(subComment)) {
            throw new InvalidDataException("User did not like this comment");
        }
        subComment.getLikers().remove(user);
        subCommentRepository.save(subComment);

        return (long)subComment.getLikers().size();
    }

    public Long getLikeCount(Long subCommentId, HttpServletRequest request) {
        SubCommentEntity subComment = getSubCommentById(subCommentId);
        return (long)subComment.getLikers().size();
    }

    private SubCommentEntity getSubCommentById(Long id) {
        return subCommentRepository.findById(id).orElseThrow(() -> new InvalidDataException("Comment ID doesn't exist"));
    }

    public List<String> getAllSubComments(Long commentId, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute(SessionManager.USER_ID);
        sessionManager.authorizeSession(userId, request.getSession(), request);

        List<String> commentEntities = subCommentRepository.findAllCommentByCommentId(commentId);
        if (commentEntities.isEmpty()) throw new InvalidDataException("Comment doesn't have any comments yet");

        return commentEntities;
    }

    private UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new InvalidDataException("User ID doesn't exist"));
    }
}
