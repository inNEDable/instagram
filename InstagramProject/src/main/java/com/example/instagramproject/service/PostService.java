package com.example.instagramproject.service;


import com.example.instagramproject.exceptions.InvalidUserData;
import com.example.instagramproject.model.DTO.CreatePostDTO;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.PostMediaRepository;
import com.example.instagramproject.model.repository.PostRepository;
import com.example.instagramproject.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostMediaRepository postMediaRepository;

    @Autowired
    private ModelMapper modelMapper;


    public PostEntity createPost(CreatePostDTO createPostDTO, HttpSession session, HttpServletRequest request) {
        sessionManager.authorizeSession(createPostDTO.getUserId(), session, request);
        UserEntity userEntity = userRepository.findById(createPostDTO.getUserId()).orElseThrow(() -> new InvalidUserData("Owner not found"));


//        PostEntity postEntity = new PostEntity();
////        postEntity.setUserId(createPostDTO.getUserId());
//        postEntity.setDateTime(LocalDateTime.now());
//        postEntity.setText(createPostDTO.getText());
//        postEntity.setCreator(userEntity);
//
//        for (String url : createPostDTO.getMedia()){
//            PostMediaEntity postMediaEntity = new PostMediaEntity();
//            postMediaEntity.setUrl(url);
//            postEntity.getMedia().add(new PostMediaEntity());
//        }
//        return modelMapper.map(createPostDTO, PostResponseDTO.class);
        return null;
    }
}
