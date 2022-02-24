package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidDataException;
import com.example.instagramproject.model.DTO.RequestCommentPostDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

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


    public ReturnCommentDTO createCommentPost(RequestCommentPostDTO createCommentPostDTO, HttpServletRequest request) {
        Validator.nullChecker(createCommentPostDTO.getPostId(), createCommentPostDTO.getText(), createCommentPostDTO.getUserId());
        Validator.validateStringLength(MIN_COMMENT_LENGTH, MAX_COMMENT_LENGTH, createCommentPostDTO.getText());
        UserEntity user = Validator.getEntity(createCommentPostDTO.getUserId(), userRepository);
        sessionManager.authorizeSession(createCommentPostDTO.getUserId(), request.getSession(), request);
        PostEntity post = Validator.getEntity(createCommentPostDTO.getPostId(), postRepository);

        CommentEntity comment = new CommentEntity();
        comment.setDateCreated(LocalDateTime.now());
        comment.setUser(user);
        comment.setText(createCommentPostDTO.getText());
        comment.setPost(post);
        commentRepository.save(comment);

        return modelMapper.map(comment, ReturnCommentDTO.class);
    }

    public ReturnCommentDTO deleteComment(RequestCommentPostDTO commentToDelete, HttpServletRequest request) {
        Validator.nullChecker(commentToDelete.getId(), commentToDelete.getUserId());

        sessionManager.authorizeSession(commentToDelete.getUserId(), request.getSession(), request);
        CommentEntity comment = commentRepository.findById(commentToDelete.getId())
                .orElseThrow(() -> new InvalidDataException("Comment ID doesn't exist"));


        commentRepository.deleteById(commentToDelete.getId());

        return modelMapper.map(comment, ReturnCommentDTO.class);
    }

    public Long likeCommentPost(Long commentId, HttpServletRequest request) {
        Validator.nullChecker(commentId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        CommentEntity comment = Validator.getEntity(commentId, commentRepository);

        UserEntity user = Validator.getEntity((long) request.getSession().getAttribute(SessionManager.USER_ID), userRepository);
        if (user.getLikedComments().contains(comment)) {
            throw new InvalidDataException("User already liked this comment");
        }
        comment.getLikers().add(user);
        commentRepository.save(comment);

        return (long) comment.getLikers().size();
    }

    public Long unlikeCommentPost(Long commentId, HttpServletRequest request) {
        Validator.nullChecker(commentId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        CommentEntity comment = Validator.getEntity(commentId, commentRepository);
        UserEntity user = Validator.getEntity((long) request.getSession().getAttribute(SessionManager.USER_ID), userRepository);
        if (!user.getLikedComments().contains(comment)) {
            throw new InvalidDataException("User did not like this comment");
        }
        comment.getLikers().remove(user);
        commentRepository.save(comment);
        return (long) comment.getLikers().size();
    }

    public Long getLikeCount(Long commentId, HttpServletRequest request) {
        Validator.nullChecker(commentId);
        sessionManager.authorizeSession(null, request.getSession(), request);
        CommentEntity comment = Validator.getEntity(commentId, commentRepository);
        return (long) comment.getLikers().size();
    }

    public List<String> getAllPostComments(Long postId, HttpServletRequest request) {
        Validator.nullChecker(postId);
        sessionManager.authorizeSession(null, request.getSession(), request);

        List<String> commentEntities = commentRepository.findAllCommentsByPostId(postId);
        if (commentEntities.isEmpty()) throw new InvalidDataException("Post doesn't have any comments yet");

        return commentEntities;
    }
}
