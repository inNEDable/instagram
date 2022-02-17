package com.example.instagramproject.controler;

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

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/logout")
    public ResponseEntity<UserToReturnDTO> logOut(@RequestBody RequestUserDTO userToLogout, HttpSession session, HttpServletRequest request) {
        UserToReturnDTO userToReturnDTO = userService.logOut(userToLogout, session, request);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<UserToReturnDTO> changePassword(@RequestBody RequestUserDTO requestUserDTO, HttpSession session, HttpServletRequest request) {
        UserToReturnDTO userToReturnDTO = userService.changePassword(requestUserDTO, session, request);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @PutMapping("/edit-profile-info")
    public ResponseEntity<UserToReturnDTO> edit(@RequestBody RequestUserDTO requestUserDTO, HttpSession session, HttpServletRequest request) {
        UserToReturnDTO userToReturnDTO = userService.edit(requestUserDTO, session, request);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<UserToReturnDTO> deleteUser(@RequestBody RequestUserDTO userToDelete, HttpSession session, HttpServletRequest request) {
        UserToReturnDTO userToReturnDTO = userService.deleteUser(userToDelete, session, request);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserToReturnDTO> login(@RequestBody RequestUserDTO userToLogin, HttpSession session, HttpServletRequest request) {
        UserToReturnDTO userToReturnDTO = userService.login(userToLogin, session, request);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserToReturnDTO> registerUser(@RequestBody RequestUserDTO requestUserDTO) {
        UserToReturnDTO userToReturnDTO = userService.registerUser(requestUserDTO);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<UserToReturnDTO> getById(@PathVariable(name = "id") long id) {
        UserToReturnDTO userToReturnDTO = userService.getById(id);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @GetMapping("/find-by-username/{username}")
    public ResponseEntity<UserToReturnDTO> getByUsername(@PathVariable(name = "username") String username) {
        UserToReturnDTO userToReturnDTO = userService.getByUsername(username);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }

    @GetMapping("/find-by-full-name/{fullName}")
    public ResponseEntity<UserToReturnDTO> getByFullName(@PathVariable(name = "fullName") String fullName) {
        UserToReturnDTO userToReturnDTO = userService.getByFullName(fullName);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);
    }
}
