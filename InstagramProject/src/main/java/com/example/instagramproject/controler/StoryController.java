package com.example.instagramproject.controler;

import com.example.instagramproject.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("api/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @PostMapping("/create/{userId}")
    public String createStory(@PathVariable Long userId, @RequestBody MultipartFile multipartFile, HttpServletRequest request){
        return storyService.createStory(userId, multipartFile, request);
    }

    @GetMapping("/get-story-media/{storyId}/{requestedFile}")
    public void getSingleStoryMedia(@PathVariable Long storyId, @PathVariable String requestedFile, HttpServletRequest request, HttpServletResponse response){
        storyService.getStoryMedia(storyId, requestedFile, request, response);
    }

    @GetMapping("/all-from-user/{userId}")
    public ResponseEntity<List<Long>> getAllStoriesFromUser(@PathVariable Long userId, HttpServletRequest request){
        List<Long> stories = storyService.getAllStoriesFromUser(userId, request);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("NumberOfStories", String.valueOf(stories.size()));

        return new ResponseEntity<>(stories,responseHeaders, HttpStatus.OK);
    }
}
