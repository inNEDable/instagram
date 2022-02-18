package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidData;
import com.example.instagramproject.model.DTO.RequestCommentPostDTO;
import com.example.instagramproject.model.DTO.RequestLikeDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.model.DTO.ReturnLikeDTO;
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
    private LikeCommentRepository likeCommentRepository;

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

    public ReturnLikeDTO likeComment(RequestLikeDTO likeComment, HttpServletRequest request) {
        if (likeComment.getCommentId() == null) throw new InvalidData("Invalid data");

        sessionManager.authorizeSession(likeComment.getUserId(), request.getSession(), request);
        UserEntity user = userRepository.findById(likeComment.getUserId())
                .orElseThrow(() -> new InvalidData("User ID doesn't exist"));
        CommentEntity comment = commentRepository.findById(likeComment.getCommentId())
                .orElseThrow(() -> new InvalidData("Comment ID doesn't exist"));
        //tODO Проверка дали съм го лайкнал
        LikeCommentEntity like = new LikeCommentEntity();
        like.setUser(user);
        like.setComment(comment);
        likeCommentRepository.save(like);
        ReturnLikeDTO returnLikeDTO = new ReturnLikeDTO();
        returnLikeDTO.setId(like.getId());
        returnLikeDTO.setUserId(user.getId());
        returnLikeDTO.setPostId(likeComment.getPostId());
        returnLikeDTO.setCommentId(comment.getId());

        return returnLikeDTO;
    }


    public ReturnLikeDTO deleteLike(RequestLikeDTO likeToDelete, HttpServletRequest request) {
        if (likeToDelete.getId() == null) throw new InvalidData("Invalid date");

        sessionManager.authorizeSession((long)request.getSession().getAttribute(SessionManager.USER_ID), request.getSession(), request);
        LikeCommentEntity likeComment = likeCommentRepository.findById(likeToDelete.getId())
                .orElseThrow(() -> new InvalidData("Like ID doesn't exist"));
        UserEntity user = userRepository.findById(likeToDelete.getUserId())
                .orElseThrow(() -> new InvalidData("User ID doesn't exist"));
        CommentEntity comment = commentRepository.findById(likeToDelete.getCommentId())
                .orElseThrow(() -> new InvalidData("Comment ID doesn't exist"));

        if (likeComment.getComment().equals(comment) && likeComment.getUser().equals(user)) {
            likeCommentRepository.deleteById(likeToDelete.getId());
        }
        else {
            throw new InvalidData("Invalid data");
        }

        ReturnLikeDTO returnLikeDTO = new ReturnLikeDTO();
        returnLikeDTO.setId(likeComment.getId());
        returnLikeDTO.setUserId(user.getId());
        returnLikeDTO.setPostId(likeToDelete.getPostId());
        returnLikeDTO.setCommentId(comment.getId());

        return returnLikeDTO;
    }
}
