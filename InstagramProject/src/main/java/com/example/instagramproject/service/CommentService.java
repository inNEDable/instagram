package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidData;
import com.example.instagramproject.model.DTO.RequestCommentPostDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.model.entity.*;
import com.example.instagramproject.model.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class CommentService {

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
        if (createCommentPostDTO.getText().isBlank()
                || createCommentPostDTO.getUserId() == null) throw new InvalidData("Invalid data");

        sessionManager.authorizeSession(createCommentPostDTO.getUserId(), request.getSession(), request);

        UserEntity user = userRepository.findById(createCommentPostDTO.getUserId())
                .orElseThrow(() -> new InvalidData("User ID doesn't exist"));
        PostEntity post = postRepository.findById(createCommentPostDTO.getPostId())
                .orElseThrow(() -> new InvalidData("Post ID doesn't exist"));

        CommentEntity comment = new CommentEntity();
        comment.setDateCreated(LocalDateTime.now());
        comment.setUser(user);
        comment.setText(createCommentPostDTO.getText());
        comment.setPost(post);
        commentRepository.save(comment);

        return modelMapper.map(comment, ReturnCommentDTO.class);
    }

    public ReturnCommentDTO deleteComment(RequestCommentPostDTO commentToDelete, HttpServletRequest request) {
        if (commentToDelete.getId() == null) throw new InvalidData("Invalid date");

        sessionManager.authorizeSession(commentToDelete.getUserId(), request.getSession(), request);
        CommentEntity comment = commentRepository.findById(commentToDelete.getId())
                .orElseThrow(() -> new InvalidData("Comment ID doesn't exist"));
        commentRepository.deleteById(commentToDelete.getId());

        return modelMapper.map(comment, ReturnCommentDTO.class);
    }

    public Long likeCommentPost(Long commentId, HttpServletRequest request) {
        sessionManager.authorizeSession(null, request.getSession(), request);
        CommentEntity comment = getCommentById(commentId);
        UserEntity user = getUserById((long)request.getSession().getAttribute(SessionManager.USER_ID));
        if (user.getLikedComments().contains(comment)) {
            throw new InvalidData("User already liked this comment");
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
            throw new InvalidData("User did not like this comment");
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
        return commentRepository.findById(id).orElseThrow(() -> new InvalidData("Comment ID doesn't exist"));
    }

    private UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new InvalidData("User ID doesn't exist"));
    }
}
