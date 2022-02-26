package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidDataException;
import com.example.instagramproject.model.DTO.RequestCommentDTO;
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
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

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


    public ReturnCommentDTO crateSubComment(Long commentId, RequestCommentDTO createSubCommentDTO, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute(SessionManager.USER_ID);
        Validator.nullChecker(userId, commentId, createSubCommentDTO.getText());
        Validator.validateStringLength(MIN_SUB_COMMENT_LENGTH, MAX_SUB_COMMENT_LENGTH, createSubCommentDTO.getText());

        sessionManager.authorizeSession(null, request.getSession(), request);

        UserEntity user = Validator.getEntity(userId, userRepository);
        CommentEntity comment = Validator.getEntity(commentId, commentRepository);

        SubCommentEntity subComment = new SubCommentEntity();
        subComment.setDateCreated(LocalDateTime.now());
        subComment.setUser(user);
        subComment.setText(createSubCommentDTO.getText());
        subComment.setComment(comment);
        subCommentRepository.save(subComment);

        return modelMapper.map(subComment, ReturnCommentDTO.class);
    }

    public ReturnCommentDTO deleteSubComment(Long subCommentId, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute(SessionManager.USER_ID);
        Validator.nullChecker(subCommentId, userId);

        sessionManager.authorizeSession(null, request.getSession(), request);
        SubCommentEntity subComment = Validator.getEntity(subCommentId, subCommentRepository);
        subCommentRepository.deleteById(subComment.getId());

        return modelMapper.map(subComment, ReturnCommentDTO.class);
    }

    public ReturnCommentDTO likeSubComment(Long subCommentId, HttpServletRequest request) {
        Validator.nullChecker(subCommentId);
        sessionManager.authorizeSession(null, request.getSession(), request);

        SubCommentEntity subComment = Validator.getEntity(subCommentId, subCommentRepository);
        UserEntity user = Validator.getEntity((long) request.getSession().getAttribute(SessionManager.USER_ID), userRepository);
        if (user.getLikedSubComments().contains(subComment)) {
            throw new InvalidDataException("User already liked this comment");
        }
        subComment.getLikers().add(user);
        subCommentRepository.save(subComment);

        ReturnCommentDTO returnCommentDTO = modelMapper.map(subComment, ReturnCommentDTO.class);
        returnCommentDTO.setLikes(subComment.getLikers().size());

        return returnCommentDTO;
    }

    public ReturnCommentDTO unlikeSubComment(Long subCommentId, HttpServletRequest request) {
        Validator.nullChecker(subCommentId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        SubCommentEntity subComment = Validator.getEntity(subCommentId, subCommentRepository);
        UserEntity user = Validator.getEntity((long) request.getSession().getAttribute(SessionManager.USER_ID), userRepository);
        if (!user.getLikedSubComments().contains(subComment)) {
            throw new InvalidDataException("User did not like this comment");
        }
        subComment.getLikers().remove(user);
        subCommentRepository.save(subComment);

        ReturnCommentDTO returnCommentDTO = modelMapper.map(subComment, ReturnCommentDTO.class);
        returnCommentDTO.setLikes(subComment.getLikers().size());

        return returnCommentDTO;
    }

    public Long getLikeCount(Long subCommentId, HttpServletRequest request) {
        Validator.nullChecker(subCommentId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        SubCommentEntity subComment = Validator.getEntity(subCommentId, subCommentRepository);
        return (long) subComment.getLikers().size();
    }

    public TreeSet<ReturnCommentDTO> getAllSubComments(Long commentId, HttpServletRequest request) {
        Validator.nullChecker(commentId);

        sessionManager.authorizeSession(null, request.getSession(), request);

        List<SubCommentEntity> subCommentEntities = subCommentRepository.findAllSubCommentByCommentId(commentId);
        if (subCommentEntities.isEmpty()) throw new InvalidDataException("Comment doesn't have any comments yet");

        return modelMapper.map(subCommentEntities, new TypeToken<TreeSet<ReturnCommentDTO>>() {}.getType());
    }

}
