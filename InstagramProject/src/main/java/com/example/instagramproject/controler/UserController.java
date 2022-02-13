package com.example.instagramproject.controlers;

import com.example.instagramproject.model.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/all")
    @ResponseBody
    public List<UserEntity> getAllUsers() {
        //Todo method body..
        return null;
    }
}
