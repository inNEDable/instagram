package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.PicturePurifyException;
import com.example.instagramproject.util.pic_purify.PicPurifyModerationTaskName;
import com.example.instagramproject.util.pic_purify.status.Status;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class PicturePurifyService {

    private static final String API_KEY = "uKuzco6OVkplI8jrzO7eK6UitT7bDXnx";
    private static final String url = "https://www.picpurify.com/analyse/1.1";

    public void verifyImagePurity (Path localPath){
        File file = new File(localPath.toString());
        String tasks = configureTasks(PicPurifyModerationTaskName.PORN_MODERATION, PicPurifyModerationTaskName.SUGGESTIVE_NUDITY_MODERATION);
        StringBuilder responseString = new StringBuilder();
        Status status = null;
        try {
            requestImageVerification(file, null, null, tasks, responseString);
            ObjectMapper objectMapper = new ObjectMapper(new JsonFactory());
            status = objectMapper.readValue(responseString.toString(), Status.class);
        } catch (Exception e) {
            throw new PicturePurifyException("IO exception during API call");
        }
        System.out.println(responseString);
        System.out.println("/////////////////////////////////////////////////////");
        System.out.println(status);

        handleStatus(status, localPath);
    }


    private int requestImageVerification(
            File file,
            String originId,
            String referenceId,
            String tasks,
            StringBuilder sb
    ) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        FileBody fileBody = new FileBody(file);
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
        multipartEntity.addPart("file_image", fileBody);
        multipartEntity.addPart("API_KEY", new StringBody(API_KEY));

        if (originId != null && !originId.isBlank())
            multipartEntity.addPart("origin_id", new StringBody(originId));

        if (referenceId != null && !referenceId.isBlank())
            multipartEntity.addPart("reference_id", new StringBody(referenceId));

        if (tasks != null && !tasks.isBlank())
            multipartEntity.addPart("task", new StringBody(tasks));

        connection.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
        try (OutputStream out = connection.getOutputStream()) {
            multipartEntity.writeTo(out);
        }
        sb.append(new Scanner(connection.getInputStream()).nextLine());
        return connection.getResponseCode();
    }


    private String configureTasks(PicPurifyModerationTaskName... tasks) {
        return Arrays.stream(tasks)
                .map(task -> task.name().toLowerCase(Locale.ROOT))
                .collect(Collectors.joining(","));
    }


    private void handleStatus(Status status, Path localPath) {
        if (status.getFinalDecision().equalsIgnoreCase("OK")){
            System.out.println("IMAGE OK");
        } else {
            try {
                Files.delete(localPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("IMAGE NOT OK");
            String exceptionMessage = "Porn content: " + status.getPornModeration().getPornContent() +
                    " || Confidence: " + status.getPornModeration().getConfidenceScore() +
                    " Nudity content: " + status.getNudityModeration().getNudityContent() +
                    " || Confidence: " + status.getNudityModeration().getConfidenceScore();
            throw new PicturePurifyException(exceptionMessage);
        }
    }
}
