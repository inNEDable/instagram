package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidDataException;
import com.example.instagramproject.model.entity.PostEntity;
import com.example.instagramproject.model.entity.StoryEntity;
import com.example.instagramproject.model.repository.StoryRepository;
import com.example.instagramproject.util.FileManager;
import com.example.instagramproject.util.SessionManager;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StoryService {

    public static final String ALL_STORIES_FOLDER = "allStories";

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private SessionManager sessionManager;

    @SneakyThrows
    public String createStory(Long userId, MultipartFile multipartFile, HttpServletRequest request) {
        if (userId == null || multipartFile.isEmpty()) throw new InvalidDataException("Data missing from request");

        sessionManager.authorizeSession(userId, request.getSession(), request);

        String fileName = System.nanoTime() + "."
                + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        String currentPostFolder = "story-" + userId;
        new File(ALL_STORIES_FOLDER + File.separator + currentPostFolder).mkdir();

        Path fullPath = Path.of(ALL_STORIES_FOLDER
                + File.separator
                + currentPostFolder
                + File.separator
                + fileName);

        Files.copy(multipartFile.getInputStream(), fullPath);

        LocalDateTime dateTime = LocalDateTime.now();
        StoryEntity storyEntity = new StoryEntity();
        storyEntity.setUserId(userId);
        storyEntity.setMedia(fullPath.toString());
        storyEntity.setExpDate(dateTime.plusMinutes(2));

        storyRepository.save(storyEntity);

        return fileName;
    }

    @SneakyThrows
    public void getStoryMedia(Long storyId, String requestedFile, HttpServletRequest request, HttpServletResponse response) {
        if (storyId == null || requestedFile == null) throw new InvalidDataException("Missing request parameters");
        sessionManager.authorizeSession(null, request.getSession(), request);

        String currentPostFolder = "story-" + (long) request.getSession().getAttribute(SessionManager.USER_ID);

        File mediaToGet = new File(
                ALL_STORIES_FOLDER
                        + File.separator
                        + currentPostFolder
                        + File.separator
                        + requestedFile);
        if (!mediaToGet.exists()) throw new InvalidDataException("Picture not found on the server");

        Files.copy(mediaToGet.toPath(), response.getOutputStream());
    }

    @Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 60)
    public void cronJob() {
        List<StoryEntity> storiesForDelete = storyRepository.findAllByExpDateBefore(LocalDateTime.now());
        for (StoryEntity storyEntity : storiesForDelete) {
            String currentPostFolder = "story-" + storyEntity.getUserId();
            File mediaOfDeletedPost = new File(
                    ALL_STORIES_FOLDER
                            + File.separator
                            + currentPostFolder);
            FileManager.deleteDirectory(mediaOfDeletedPost);
        }
        storyRepository.deleteAll(storiesForDelete);
    }

    public List<Long> getAllStoriesFromUser(Long userId, HttpServletRequest request) {
        //Validator.userExistsCheck(userId);

        sessionManager.authorizeSession(null, request.getSession(), request);

        List<Long> storyEntities = storyRepository.findAllByUserId(userId);
        if (storyEntities.isEmpty()) {
            throw new InvalidDataException("User doesn't have any stories yet");
        }
        return storyEntities;
    }
}
