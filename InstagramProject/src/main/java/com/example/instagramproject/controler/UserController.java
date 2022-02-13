package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.UserToRegisterDTO;
import com.example.instagramproject.model.DTO.UserToReturnDTO;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public UserToReturnDTO registerUser (@RequestBody UserToRegisterDTO userToRegisterDTO){
        return userService.registerUser(userToRegisterDTO);
    }


    @GetMapping("/all")
    @ResponseBody
    public List<UserEntity> getAllUsers() {
        //Todo method body..
        return null;
    }
}
