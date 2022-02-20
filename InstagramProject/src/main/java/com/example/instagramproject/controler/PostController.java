package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.RequestPostDTO;
import com.example.instagramproject.model.DTO.ReturnPostDTO;
import com.example.instagramproject.model.DTO.ReturnTagDTO;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.service.PostService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    PostService postService;

    @PostMapping("/create")
    public ResponseEntity<ReturnPostDTO> createPost (@RequestBody RequestPostDTO requestPostDTO, HttpServletRequest request){
        ReturnPostDTO returnPostDTO = postService.createPost(requestPostDTO, request);
        return new ResponseEntity<>(returnPostDTO, HttpStatus.CREATED);
    }

    @PostMapping("/add-media")
    public List<String> addMediaToPost  (@RequestParam Long postId, @RequestParam Long userId, @RequestBody MultipartFile [] multipartFile, HttpServletRequest request){
        List<String> urlsGenerated = new ArrayList<>();
        Arrays.stream(multipartFile)
                .forEach(file -> urlsGenerated.add(postService.addMediaToPost(userId, postId, file, request)));
        return urlsGenerated;
    }

    @GetMapping("/get-post-media/{postId}/{requestedFile}")
    public void getSinglePostMedia (@PathVariable Long postId, @PathVariable String requestedFile, HttpServletRequest request, HttpServletResponse response){
        postService.getPostMedia(postId, requestedFile, request, response);
    }

    @PutMapping("/edit-post-text")
    public ResponseEntity<ReturnPostDTO> editPostText (@RequestBody RequestPostDTO requestPostDTO, HttpServletRequest request){
        ReturnPostDTO returnPostDTO = postService.editPostText(requestPostDTO, request);
        return new ResponseEntity<>(returnPostDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{userId}/{postId}")
    @SneakyThrows
    public void deletePost (@PathVariable Long userId, @PathVariable Long postId, HttpServletRequest request, HttpServletResponse response){
        postService.deletePost(userId, postId, request);
        response.getWriter().println("Post " + postId + "successfully deleted");
    }

    @GetMapping("/get/{postId}")
    public ResponseEntity<ReturnPostDTO> getPostById(@PathVariable Long postId, HttpServletRequest request){
        ReturnPostDTO returnPostDTO = postService.getPostById(postId, request);
        return new ResponseEntity<>(returnPostDTO, HttpStatus.OK);
    }

    @GetMapping("/all-from-user/{userId}")
    public ResponseEntity<List<ReturnPostDTO>> getAllPostsFromUser(@PathVariable Long userId, HttpServletRequest request){
        List<ReturnPostDTO> posts = postService.getAllPostsFromUser(userId, request);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("NumberOfPosts", String.valueOf(posts.size()));

        return new ResponseEntity<>(posts,responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/get-by-text")
    public ResponseEntity<List<ReturnPostDTO>> getAllPostsByText (@RequestParam String t, HttpServletRequest request){
        List<ReturnPostDTO> posts = postService.getAllPostsByText(t, request);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("NumberOfPosts", String.valueOf(posts.size()));

        return new ResponseEntity<>(posts, responseHeaders, HttpStatus.OK);
    }



    // ++++ GET SINGLE MEDIA MEADIA FROM POST
    // +++ edit post text
    // +++ delete media from post
    // like post
    // add comment to post
    // add tag to post
    // get all tags from post

}
