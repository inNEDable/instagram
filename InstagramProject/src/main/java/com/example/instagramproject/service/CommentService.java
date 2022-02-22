package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidDataException;
import com.example.instagramproject.model.DTO.RequestCommentPostDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.model.entity.*;
import com.example.instagramproject.model.repository.*;
import com.example.instagramproject.util.SessionManager;
import com.example.instagramproject.util.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    public static final int MAX_COMMENT_LENGTH = 500;
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
        Validator.validateStringLength(0, MAX_COMMENT_LENGTH, createCommentPostDTO.getText());
        if (createCommentPostDTO.getText().isBlank()
                || createCommentPostDTO.getUserId() == null) throw new InvalidDataException("Invalid data");

        sessionManager.authorizeSession(createCommentPostDTO.getUserId(), request.getSession(), request);

        UserEntity user = userRepository.findById(createCommentPostDTO.getUserId())
                .orElseThrow(() -> new InvalidDataException("User ID doesn't exist"));
        PostEntity post = postRepository.findById(createCommentPostDTO.getPostId())
                .orElseThrow(() -> new InvalidDataException("Post ID doesn't exist"));

        CommentEntity comment = new CommentEntity();
        comment.setDateCreated(LocalDateTime.now());
        comment.setUser(user);
        comment.setText(createCommentPostDTO.getText());
        comment.setPost(post);
        commentRepository.save(comment);

        return modelMapper.map(comment, ReturnCommentDTO.class);
    }

    public ReturnCommentDTO deleteComment(RequestCommentPostDTO commentToDelete, HttpServletRequest request) {
        if (commentToDelete.getId() == null) throw new InvalidDataException("Invalid date");

        sessionManager.authorizeSession(commentToDelete.getUserId(), request.getSession(), request);
        CommentEntity comment = commentRepository.findById(commentToDelete.getId())
                .orElseThrow(() -> new InvalidDataException("Comment ID doesn't exist"));
        commentRepository.deleteById(commentToDelete.getId());

        return modelMapper.map(comment, ReturnCommentDTO.class);
    }

    public Long likeCommentPost(Long commentId, HttpServletRequest request) {
        sessionManager.authorizeSession(null, request.getSession(), request);
        CommentEntity comment = getCommentById(commentId);
        UserEntity user = getUserById((long)request.getSession().getAttribute(SessionManager.USER_ID));
        if (user.getLikedComments().contains(comment)) {
            throw new InvalidDataException("User already liked this comment");
        }
        comment.getLikers().add(user);
        commentRepository.save(comment);

        return (long)comment.getLikers().size();
    }

    public Long unlikeCommentPost(Long commentId, HttpServletRequest request) {
        sessionManager.authorizeSession(null, request.getSession(), request);
        CommentEntity comment = getCommentById(commentId);
        UserEntity user = getUserById((long)request.getSession().getAttribute(SessionManager.USER_ID));
        if (!user.getLikedComments().contains(comment)) {
            throw new InvalidDataException("User did not like this comment");
        }
        comment.getLikers().remove(user);
        commentRepository.save(comment);

        return (long)comment.getLikers().size();
    }

    public Long getLikeCount(Long commentId, HttpServletRequest request) {
        CommentEntity comment = getCommentById(commentId);
        return (long)comment.getLikers().size();
    }

    private CommentEntity getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new InvalidDataException("Comment ID doesn't exist"));
    }

    private UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new InvalidDataException("User ID doesn't exist"));
    }

    public List<String> getAllPostComments(Long postId, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute(SessionManager.USER_ID);
        sessionManager.authorizeSession(userId, request.getSession(), request);

        List<String> commentEntities = commentRepository.findAllCommentsByPostId(postId);
        if (commentEntities.isEmpty()) throw new InvalidDataException("User doesn't have any posts yet");

        return commentEntities;
    }
}
