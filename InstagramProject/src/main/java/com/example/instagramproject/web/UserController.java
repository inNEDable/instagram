package com.example.instagramproject.web;

import com.example.instagramproject.model.User;
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
    public List<User> getAllUsers() {
        //Todo method body..
        return null;
    }
}
