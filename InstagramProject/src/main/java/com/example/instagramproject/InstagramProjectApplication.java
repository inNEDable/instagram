package com.example.instagramproject;

import com.example.instagramproject.service.PostService;
import com.example.instagramproject.service.StoryService;
import com.example.instagramproject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.util.Properties;


@SpringBootApplication
@EnableScheduling
public class InstagramProjectApplication {

    public static void main(String[] args) {
        File allPostsFolder = new File(PostService.ALL_POSTS_FOLDER);
        if (!allPostsFolder.exists()) {
            allPostsFolder.mkdir();
        }
        File allUsersProfilePicturesFolder = new File(UserService.ALL_PROFILE_PICTURES_FOLDER);
        if (!allUsersProfilePicturesFolder.exists()) {
            allUsersProfilePicturesFolder.mkdir();
        }
        File allUsersStory = new File(StoryService.ALL_STORIES_FOLDER);
        if (!allUsersStory.exists()) {
            allUsersStory.mkdir();
        }
        SpringApplication.run(InstagramProjectApplication.class, args);

    }
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("instagramdemo149@gmail.com");
        mailSender.setPassword("insta_R#1");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
