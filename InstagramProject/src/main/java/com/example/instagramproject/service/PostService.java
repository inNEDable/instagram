package com.example.instagramproject.service;


import com.example.instagramproject.exceptions.InvalidData;
import com.example.instagramproject.model.DTO.CreatePostDTO;
import com.example.instagramproject.model.DTO.ReturnPostDTO;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.model.entity.PostMediaEntity;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.PostMediaRepository;
import com.example.instagramproject.model.repository.PostRepository;
import com.example.instagramproject.model.repository.UserRepository;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

@Service
public class PostService {

    public static final String ALL_POSTS_FOLDER = "allPosts";

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


    public ReturnPostDTO createPost(CreatePostDTO createPostDTO, HttpServletRequest request) {
        if (createPostDTO.getText().isBlank()
                || createPostDTO.getUserId() == null) throw new InvalidData("Invalid data");

        sessionManager.authorizeSession(createPostDTO.getUserId(), request.getSession(), request);

        UserEntity user = userRepository.findById(createPostDTO.getUserId())
                .orElseThrow(() -> new InvalidData("User ID doesn't exist"));

        //TODO: Да помисля тия двете отдолу, дали не могат да се направят с mapper
        PostEntity postEntity = new PostEntity();
        postEntity.setUser(user);
        postEntity.setDateTime(LocalDateTime.now());
        postEntity.setText(createPostDTO.getText());
        postRepository.save(postEntity);

        ReturnPostDTO returnPostDTO = new ReturnPostDTO();
        returnPostDTO.setId(postEntity.getId());
        returnPostDTO.setUser_id(user.getId());
        returnPostDTO.setDateTime(postEntity.getDateTime());
        returnPostDTO.setText(postEntity.getText());
        return returnPostDTO;

    }

    @SneakyThrows
    public String addMediaToPost(Long userId, Long postId, MultipartFile multipartFile, HttpServletRequest request) {
        if (userId == null || postId == null || multipartFile.isEmpty() ) throw new InvalidData("Data missing from request");
        sessionManager.authorizeSession(userId, request.getSession(), request);

        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new InvalidData("Post doesn't exist"));

        String fileName = System.nanoTime() + "."
                + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        String currentPostFolder = "post-" + postId;
        new File(ALL_POSTS_FOLDER + File.separator + currentPostFolder).mkdir();



        Path fullPath = Path.of(ALL_POSTS_FOLDER //allPosts/post-?/3151613135161.jpg
                + File.separator
                + currentPostFolder
                + File.separator
                + fileName);

        Files.copy(multipartFile.getInputStream(), fullPath);

        PostMediaEntity postMediaEntity = new PostMediaEntity();
        postMediaEntity.setUrl(fullPath.toString());
        postMediaEntity.setPost(postEntity);

        postMediaRepository.save(postMediaEntity);


        return fullPath.toString();
    }

    public MultipartFile getPostMedia(Long postId, String mediaUrl, HttpServletRequest request) {
        if (postId == null || mediaUrl == null) throw new InvalidData("Missing request parameters");
        sessionManager.authorizeSession(null, request.getSession(), request);

        File mediaToGet = new File(mediaUrl);
        if (!mediaToGet.exists()) throw new InvalidData("Picture not found on the server");
        // TODO : да дорърша
        return null;
    }
}
