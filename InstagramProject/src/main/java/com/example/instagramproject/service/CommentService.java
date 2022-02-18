package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidData;
import com.example.instagramproject.model.DTO.CreateCommentPostDTO;
import com.example.instagramproject.model.DTO.CreateSubCommentDTO;
import com.example.instagramproject.model.DTO.ReturnCommentDTO;
import com.example.instagramproject.model.entity.CommentEntity;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.model.entity.SubCommentEntity;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.CommentRepository;
import com.example.instagramproject.model.repository.PostRepository;
import com.example.instagramproject.model.repository.SubCommentRepository;
import com.example.instagramproject.model.repository.UserRepository;
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
    private SubCommentRepository subCommentRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public ReturnCommentDTO createCommentPost(CreateCommentPostDTO createCommentPostDTO, HttpServletRequest request) {
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

    public ReturnCommentDTO deleteComment(CreateCommentPostDTO commentToDelete, HttpServletRequest request) {
        if (commentToDelete.getId() == null) throw new InvalidData("Invalid date");

        sessionManager.authorizeSession(commentToDelete.getUserId(), request.getSession(), request);
        CommentEntity comment = commentRepository.findById(commentToDelete.getId()).orElseThrow(() -> new InvalidData("Comment ID doesn't exist"));
        commentRepository.deleteById(commentToDelete.getId());

        return modelMapper.map(comment, ReturnCommentDTO.class);
    }
}
