package com.example.instagramproject.controler;

import com.example.instagramproject.exceptions.InvalidUserData;
import com.example.instagramproject.model.DTO.RequestUserDTO;
import com.example.instagramproject.model.DTO.UserToReturnDTO;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PutMapping("/logout")
    public ResponseEntity<UserToReturnDTO> logOut (@RequestBody RequestUserDTO userToLogout, HttpSession session, HttpServletRequest request ){
        System.out.println("LOGOUT ATTEMPT");
        userService.logOut(userToLogout, session, request);
        UserToReturnDTO userToReturnDTO = modelMapper.map(userToLogout, UserToReturnDTO.class);
        System.out.println(userToReturnDTO);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UserToReturnDTO> deleteUser (@RequestBody RequestUserDTO userToDelete, HttpSession session, HttpServletRequest request){
        UserEntity userEntity = userService.deleteUser(userToDelete, session, request);

        UserToReturnDTO userToReturnDTO = modelMapper.map(userEntity, UserToReturnDTO.class);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserToReturnDTO> login (@RequestBody RequestUserDTO userToLogin, HttpSession session, HttpServletRequest request){
        String username = userToLogin.getUsername();
        String email = userToLogin.getEmail();
        String password = userToLogin.getPassword();

        UserEntity userEntity = userService.login(username, email, password, session, request);
        UserToReturnDTO userToReturnDTO = modelMapper.map(userEntity, UserToReturnDTO.class);

        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserToReturnDTO> registerUser (@RequestBody RequestUserDTO requestUserDTO){
        UserToReturnDTO userToReturnDTO =  userService.registerUser(requestUserDTO);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<UserToReturnDTO> getById(@PathVariable(name = "id") long id) {
        UserEntity userEntity = userService.getById(id);
        UserToReturnDTO userToReturnDTO = modelMapper.map(userEntity, UserToReturnDTO.class);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @GetMapping("/find-by-username/{username}")
    public ResponseEntity<UserToReturnDTO> getByUsername(@PathVariable(name = "username") String username) {
        UserEntity userEntity = userService.getByUsername(username);
        UserToReturnDTO userToReturnDTO = modelMapper.map(userEntity, UserToReturnDTO.class);
        return ResponseEntity.ok(userToReturnDTO);
    }


    @GetMapping("/find-by-fullName/{fullName}")
    public ResponseEntity<UserToReturnDTO> getByFullName(@PathVariable(name = "fullName") String fullName) {
        UserEntity userEntity = userService.getByFullName(fullName);
        UserToReturnDTO userToReturnDTO = modelMapper.map(userEntity, UserToReturnDTO.class);
        return ResponseEntity.ok(userToReturnDTO);
    }


}
