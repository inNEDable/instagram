package com.example.instagramproject;

import com.example.instagramproject.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;


@SpringBootApplication
public class InstagramProjectApplication {

    public static void main(String[] args) {
        File allPostsFolder = new File(PostService.ALL_POSTS_FOLDER);
        if (!allPostsFolder.exists()) allPostsFolder.mkdir();

        SpringApplication.run(InstagramProjectApplication.class, args);

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
