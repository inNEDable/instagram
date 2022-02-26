package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidDataException;
import com.example.instagramproject.model.DTO.RequestCommentDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.model.entity.CommentEntity;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.CommentRepository;
import com.example.instagramproject.model.repository.PostRepository;
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
public class CommentService {

    public static final int MAX_COMMENT_LENGTH = 500;
    public static final int MIN_COMMENT_LENGTH = 1;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;


    public ReturnCommentDTO createCommentPost(Long postId, RequestCommentDTO createCommentPostDTO, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute(SessionManager.USER_ID);
        Validator.nullChecker(postId, createCommentPostDTO.getText(), userId);
        Validator.validateStringLength(MIN_COMMENT_LENGTH, MAX_COMMENT_LENGTH, createCommentPostDTO.getText());
        UserEntity user = Validator.getEntity(userId, userRepository);
        sessionManager.authorizeSession(null, request.getSession(), request);
        PostEntity post = Validator.getEntity(postId, postRepository);

        CommentEntity comment = new CommentEntity();
        comment.setDateCreated(LocalDateTime.now());
        comment.setUser(user);
        comment.setText(createCommentPostDTO.getText());
        comment.setPost(post);
        commentRepository.save(comment);

        return modelMapper.map(comment, ReturnCommentDTO.class);
    }

    public ReturnCommentDTO deleteComment(Long commentId, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute(SessionManager.USER_ID);
        Validator.nullChecker(commentId, userId);

        sessionManager.authorizeSession(null, request.getSession(), request);
        CommentEntity comment = Validator.getEntity(commentId, commentRepository);
        commentRepository.deleteById(commentId);

        return modelMapper.map(comment, ReturnCommentDTO.class);
    }

    public ReturnCommentDTO likeCommentPost(Long commentId, HttpServletRequest request) {
        Validator.nullChecker(commentId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        CommentEntity comment = Validator.getEntity(commentId, commentRepository);

        UserEntity user = Validator.getEntity((long) request.getSession().getAttribute(SessionManager.USER_ID), userRepository);
        if (user.getLikedComments().contains(comment)) {
            throw new InvalidDataException("User already liked this comment");
        }
        comment.getLikers().add(user);
        commentRepository.save(comment);
        ReturnCommentDTO returnCommentDTO = modelMapper.map(comment, ReturnCommentDTO.class);
        returnCommentDTO.setLikes(comment.getLikers().size());

        return returnCommentDTO;
    }

    public ReturnCommentDTO unlikeCommentPost(Long commentId, HttpServletRequest request) {
        Validator.nullChecker(commentId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        CommentEntity comment = Validator.getEntity(commentId, commentRepository);
        UserEntity user = Validator.getEntity((long) request.getSession().getAttribute(SessionManager.USER_ID), userRepository);
        if (!user.getLikedComments().contains(comment)) {
            throw new InvalidDataException("User did not like this comment");
        }
        comment.getLikers().remove(user);
        commentRepository.save(comment);
        ReturnCommentDTO returnCommentDTO = modelMapper.map(comment, ReturnCommentDTO.class);
        returnCommentDTO.setLikes(comment.getLikers().size());

        return returnCommentDTO;
    }

    public Long getLikeCount(Long commentId, HttpServletRequest request) {
        Validator.nullChecker(commentId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        CommentEntity comment = Validator.getEntity(commentId, commentRepository);
        return (long) comment.getLikers().size();
    }

    public TreeSet<ReturnCommentDTO> getAllPostComments(Long postId, HttpServletRequest request) {
        Validator.nullChecker(postId);
        sessionManager.authorizeSession(null, request.getSession(), request);

        List<CommentEntity> commentEntities = commentRepository.findAllCommentsByPostId(postId);
        if (commentEntities.isEmpty()) throw new InvalidDataException("Post doesn't have any comments yet");

        return modelMapper.map(commentEntities, new TypeToken<TreeSet<ReturnCommentDTO>>() {}.getType());
    }
}
