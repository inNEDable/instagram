package com.example.instagramproject.controler;

import com.example.instagramproject.exceptions.InvalidUserData;
import com.example.instagramproject.model.DTO.UserToRegisterDTO;
import com.example.instagramproject.model.DTO.UserToReturnDTO;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.service.UserService;
import org.modelmapper.ModelMapper;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    public static final String SESSION = "Session";
    public static final String LOGGED_FROM = "LoggedFrom";
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<UserToReturnDTO> login (@RequestBody UserToRegisterDTO userToLogin, HttpSession session, HttpServletRequest request){
        String username = userToLogin.getUsername();
        String email = userToLogin.getEmail();
        String password = userToLogin.getPassword();
        UserEntity userEntity;
        if (password == null) throw new InvalidUserData("Password required");
        if (username == null && email == null) throw new InvalidUserData("Username OR Email needed for login!");
        if (username == null) userEntity = userService.loginWithEmail(email, password);
                else userEntity = userService.loginWithUsername(username, password);

        session.setAttribute(SESSION, 1);
        session.setAttribute(LOGGED_FROM, request.getRemoteAddr());

        UserToReturnDTO userToReturnDTO = modelMapper.map(userEntity, UserToReturnDTO.class);
        return new ResponseEntity<>(userToReturnDTO, HttpStatus.OK);

    }


    @PostMapping("/register")
    public ResponseEntity<UserToReturnDTO> registerUser (@RequestBody UserToRegisterDTO userToRegisterDTO){
        UserToReturnDTO userToReturnDTO =  userService.registerUser(userToRegisterDTO);
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



    @GetMapping("/all")
    @ResponseBody
    public List<UserEntity> getAllUsers() {
        //Todo method body..
        return null;
    }


}
