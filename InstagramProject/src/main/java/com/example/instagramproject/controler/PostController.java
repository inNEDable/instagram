package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.CreatePostDTO;
import com.example.instagramproject.model.DTO.ReturnPostDTO;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/create")
    public ResponseEntity<ReturnPostDTO> createPost (@RequestBody CreatePostDTO createPostDTO, HttpServletRequest request){
        ReturnPostDTO returnPostDTO = postService.createPost(createPostDTO, request);
        return new ResponseEntity<>(returnPostDTO, HttpStatus.CREATED);
    }

    @PostMapping("/add-media")
    public List<String> addMediaToPost  (@RequestParam Long postId, @RequestParam Long userId, @RequestBody MultipartFile [] multipartFile, HttpServletRequest request){
        List<String> urlsGenerated = new ArrayList<>();
        Arrays.stream(multipartFile)
                .forEach(file -> urlsGenerated.add(postService.addMediaToPost(userId, postId, file, request)));
        return urlsGenerated;
    }

    @GetMapping("/get-post-media/{postId}/{mediaUrl}")
    public MultipartFile getSinglePostMedia (@RequestParam Long postId, @RequestParam String mediaUrl, HttpServletRequest request){
        return postService.getPostMedia(postId, mediaUrl, request);
    }

    // GET SINGLE MEDIA MEADIA FROM POST
    // edit post text
    // delete media from post
    // like post
    // add comment to post
    // add tag to post
    // get all tags from post

}
