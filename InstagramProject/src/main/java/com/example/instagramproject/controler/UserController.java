package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.UserToRegisterDTO;
import com.example.instagramproject.model.DTO.UserToReturnDTO;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/register")
    public ResponseEntity<UserToReturnDTO> registerUser (@RequestBody UserToRegisterDTO userToRegisterDTO){
        UserToReturnDTO userToReturnDTO =  userService.registerUser(userToRegisterDTO);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserToReturnDTO> getById(@PathVariable long id) {
        UserEntity userEntity = userService.getById(id);
        UserToReturnDTO userToReturnDTO = modelMapper.map(userEntity, UserToReturnDTO.class);
        return ResponseEntity.ok(userToReturnDTO);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<UserEntity> getAllUsers() {
        //Todo method body..
        return null;
    }


}
