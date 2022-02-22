package com.example.instagramproject.service;


import com.example.instagramproject.exceptions.InvalidDataException;
import com.example.instagramproject.model.DTO.RequestPostDTO;
import com.example.instagramproject.model.DTO.ReturnPostDTO;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.model.entity.PostMediaEntity;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.PostMediaRepository;
import com.example.instagramproject.model.repository.PostRepository;
import com.example.instagramproject.model.repository.UserRepository;
import com.example.instagramproject.util.Validator;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;


@Service
public class PostService {

    public static final String ALL_POSTS_FOLDER = "allPosts";
    public static final int MAX_POST_TEXT_LENGTH = 1500;

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


    public ReturnPostDTO createPost(RequestPostDTO requestPostDTO, HttpServletRequest request) {
        Validator.validateStringLength(0, MAX_POST_TEXT_LENGTH, requestPostDTO.getText());
        if (requestPostDTO.getText().isBlank()
                || requestPostDTO.getUserId() == null) throw new InvalidDataException("Invalid data");

        sessionManager.authorizeSession(requestPostDTO.getUserId(), request.getSession(), request);

        UserEntity user = userRepository.findById(requestPostDTO.getUserId())
                .orElseThrow(() -> new InvalidDataException("User ID doesn't exist"));

        //TODO: Да помисля тия двете отдолу, дали не могат да се направят с mapper
        PostEntity postEntity = new PostEntity();
        postEntity.setUser(user);
        postEntity.setDateTime(LocalDateTime.now());
        postEntity.setText(requestPostDTO.getText());
        postRepository.save(postEntity);

        return modelMapper.map(postEntity, ReturnPostDTO.class);

    }

    @SneakyThrows
    public String addMediaToPost(Long userId, Long postId, MultipartFile multipartFile, HttpServletRequest request) {
        if (userId == null || postId == null || multipartFile.isEmpty() ) throw new InvalidDataException("Data missing from request");

        sessionManager.authorizeSession(userId, request.getSession(), request);
        PostEntity postEntity = userManipulatingPostCheck(userId, postId);

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

        return fileName;
    }

    public void deletePost(Long userId, Long postId, HttpServletRequest request) {
        sessionManager.authorizeSession(userId, request.getSession(), request);
        userManipulatingPostCheck(userId, postId);
        postRepository.deleteById(postId);

        String currentPostFolder = "post-" + postId;
        File mediaOfDeletedPost = new File(
                ALL_POSTS_FOLDER
                + File.separator
                + currentPostFolder);
        deleteDirectory(mediaOfDeletedPost);
    }

    @SneakyThrows
    public void getPostMedia(Long postId, String requestedFile, HttpServletRequest request, HttpServletResponse response) {
        if (postId == null || requestedFile == null) throw new InvalidDataException("Missing request parameters");
        sessionManager.authorizeSession(null, request.getSession(), request);

        String currentPostFolder = "post-" + postId;

        File mediaToGet = new File(
                    ALL_POSTS_FOLDER
                            + File.separator
                            + currentPostFolder
                            + File.separator
                            + requestedFile);
        if (!mediaToGet.exists()) throw new InvalidDataException("Picture not found on the server");

        Files.copy(mediaToGet.toPath(), response.getOutputStream());

    }

    public ReturnPostDTO editPostText(RequestPostDTO requestPostDTO, HttpServletRequest request) {
        sessionManager.authorizeSession(requestPostDTO.getUserId(), request.getSession(), request);

        PostEntity postEntity = userManipulatingPostCheck(requestPostDTO.getUserId(), requestPostDTO.getPostId());
        postEntity.setText(requestPostDTO.getText());
        postRepository.save(postEntity);

        return modelMapper.map(postEntity, ReturnPostDTO.class);

    }

    public ReturnPostDTO getPostById(Long postId, HttpServletRequest request) {
        if (postId == null) throw new InvalidDataException("Please provide Post ID!");
        sessionManager.authorizeSession(null, request.getSession(), request);
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new InvalidDataException("Post not found"));
        return modelMapper.map(postEntity, ReturnPostDTO.class);
    }

    public TreeSet<ReturnPostDTO> getAllPostsFromUser(Long userId, HttpServletRequest request) {
        userExistsCheck(userId);
        sessionManager.authorizeSession(null, request.getSession(), request);

        List<PostEntity> postEntities = postRepository.findAllByUserId(userId);
        if (postEntities.isEmpty()) throw new InvalidDataException("User doesn't have any posts yet");

        return modelMapper.map(postEntities, new TypeToken<TreeSet<ReturnPostDTO>>() {}.getType());
    }

    public TreeSet<ReturnPostDTO> getAllPostsByText(String t, HttpServletRequest request) {
        sessionManager.authorizeSession(null, request.getSession(), request);

        List<PostEntity> postEntities1 = postRepository.findAllByTextContaining(t);
        if (postEntities1.isEmpty()) throw new InvalidDataException("No posts with provided text are found");

        return modelMapper.map(postEntities1, new TypeToken<TreeSet<ReturnPostDTO>>() {}.getType());
    }

    private void userExistsCheck(Long userId) {
        if (userId == null || !userRepository.existsById(userId)) throw new InvalidDataException("User doesn't exist");
    }

    private PostEntity userManipulatingPostCheck(Long userId, Long postId){
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new InvalidDataException("Post doesn't exist"));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new InvalidDataException("User doesn't exist"));

        if (postEntity.getUser().getId() != userId) throw new InvalidDataException("User is trying to manipulate foreign post");
        return postEntity;
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
