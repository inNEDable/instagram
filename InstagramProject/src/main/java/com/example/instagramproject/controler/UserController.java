package com.example.instagramproject.controler;

import com.example.instagramproject.model.DTO.RequestUserDTO;
import com.example.instagramproject.model.DTO.ReturnUserDTO;
import com.example.instagramproject.model.DTO.UserToReturnDTO;
import com.example.instagramproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/logout")
    public ResponseEntity<ReturnUserDTO> logOut(@RequestBody RequestUserDTO userToLogout, HttpSession session, HttpServletRequest request) {
        ReturnUserDTO returnUserDTO = userService.logOut(userToLogout, session, request);
        return new ResponseEntity<>(returnUserDTO, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ReturnUserDTO> changePassword(@RequestBody RequestUserDTO requestUserDTO, HttpSession session, HttpServletRequest request) {
        ReturnUserDTO returnUserDTO = userService.changePassword(requestUserDTO, session, request);
        return new ResponseEntity<>(returnUserDTO, HttpStatus.OK);
    }

    @PutMapping("/edit-profile-info")
    public ResponseEntity<ReturnUserDTO> edit(@RequestBody RequestUserDTO requestUserDTO, HttpSession session, HttpServletRequest request) {
        ReturnUserDTO returnUserDTO = userService.edit(requestUserDTO, session, request);
        return new ResponseEntity<>(returnUserDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ReturnUserDTO> deleteUser(@RequestBody RequestUserDTO userToDelete, HttpSession session, HttpServletRequest request) {
        ReturnUserDTO returnUserDTO = userService.deleteUser(userToDelete, session, request);
        return new ResponseEntity<>(returnUserDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ReturnUserDTO> login(@RequestBody RequestUserDTO userToLogin, HttpSession session, HttpServletRequest request) {
        ReturnUserDTO returnUserDTO = userService.login(userToLogin, session, request);
        return new ResponseEntity<>(returnUserDTO, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ReturnUserDTO> registerUser(@RequestBody RequestUserDTO requestUserDTO) {
        ReturnUserDTO returnUserDTO = userService.registerUser(requestUserDTO);
        return new ResponseEntity<>(returnUserDTO, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ReturnUserDTO> getById(@PathVariable(name = "id") long id) {
        ReturnUserDTO returnUserDTO = userService.getById(id);
        return new ResponseEntity<>(returnUserDTO, HttpStatus.OK);
    }

    @GetMapping("/find-by-username/{username}")
    public ResponseEntity<ReturnUserDTO> getByUsername(@PathVariable(name = "username") String username) {
        ReturnUserDTO returnUserDTO = userService.getByUsername(username);
        return new ResponseEntity<>(returnUserDTO, HttpStatus.OK);
    }

    @GetMapping("/find-by-full-name/{fullName}")
    public ResponseEntity<ReturnUserDTO> getByFullName(@PathVariable(name = "fullName") String fullName) {
        ReturnUserDTO returnUserDTO = userService.getByFullName(fullName);
        return new ResponseEntity<>(returnUserDTO, HttpStatus.OK);
    }
}
