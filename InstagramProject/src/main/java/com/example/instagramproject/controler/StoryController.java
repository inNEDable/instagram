package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.ReturnStoryDTO;
import com.example.instagramproject.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TreeSet;


@RestController
@RequestMapping("api/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @PostMapping("/{userId}")
    public ResponseEntity<ReturnStoryDTO> createStory(@PathVariable Long userId, @RequestBody MultipartFile multipartFile, HttpServletRequest request) {
        ReturnStoryDTO returnStoryDTO = storyService.createStory(userId, multipartFile, request);
        return new ResponseEntity<>(returnStoryDTO, HttpStatus.CREATED);
    }

    @GetMapping("/get-story-media/{storyId}/{requestedFile}")
    public ResponseEntity<ReturnStoryDTO> getSingleStoryMedia(@PathVariable Long storyId, @PathVariable String requestedFile, HttpServletRequest request, HttpServletResponse response) {
        ReturnStoryDTO returnStoryDTO = storyService.getStoryMedia(storyId, requestedFile, request, response);
        return new ResponseEntity<>(returnStoryDTO, HttpStatus.OK);
    }

    @GetMapping("/all-from-user")
    public ResponseEntity<TreeSet<ReturnStoryDTO>> getAllStoriesFromUser(HttpServletRequest request) {
        TreeSet<ReturnStoryDTO> stories = storyService.getAllStoriesFromUser(request);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("NumberOfStories", String.valueOf(stories.size()));

        return new ResponseEntity<>(stories, responseHeaders, HttpStatus.OK);
    }
}
