package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidUserData;
import com.example.instagramproject.model.DTO.RequestUserDTO;
import com.example.instagramproject.model.DTO.UserToReturnDTO;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.UserRepository;
import com.example.instagramproject.util.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public UserToReturnDTO logOut(RequestUserDTO userToLogOut, HttpSession session, HttpServletRequest request) {
        if (userToLogOut.getId() == null) throw new InvalidUserData("Please provide user ID ");

        sessionManager.authorizeSession(userToLogOut.getId(), session, request);
        sessionManager.logOut(session);

        return modelMapper.map(userToLogOut, UserToReturnDTO.class);
    }

    public UserToReturnDTO login(RequestUserDTO userToLogin, HttpSession session, HttpServletRequest request) {
        String username = userToLogin.getUsername();
        String email = userToLogin.getEmail();
        String password = userToLogin.getPassword();
        UserEntity userEntity;
        if (password == null) throw new InvalidUserData("Password required");
        if (username == null && email == null) throw new InvalidUserData("Username OR Email needed for login!");
        if (username == null) userEntity = loginWithEmail(email, password);
        else userEntity = loginWithUsername(username, password);

        sessionManager.login(userEntity.getId(), session, request);

        return modelMapper.map(userEntity, UserToReturnDTO.class);
    }

    public UserToReturnDTO changePassword(RequestUserDTO requestUserDTO, HttpSession session, HttpServletRequest request) {
        sessionManager.authorizeSession(requestUserDTO.getId(), session, request);
        UserEntity userEntity = userRepository.findById((long) session.getAttribute(SessionManager.USER_ID)).orElseThrow(() -> new InvalidUserData("Please provide user ID"));

        if (!passwordEncoder.matches(requestUserDTO.getPassword(), userEntity.getPassword())) {
            throw new InvalidUserData("Please provide valid password");
        }
        if (!requestUserDTO.getNewPassword().equals(requestUserDTO.getConfirmPassword())) {
            throw new InvalidUserData("Please provide valid conf password");
        }

        String hashedPassword = passwordEncoder.encode(requestUserDTO.getNewPassword());
        userEntity.setPassword(hashedPassword);
        userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserToReturnDTO.class);
    }

    public UserToReturnDTO edit(RequestUserDTO requestUserDTO, HttpSession session, HttpServletRequest request) {
        sessionManager.authorizeSession(requestUserDTO.getId(), session, request);

        UserEntity user = userRepository.findById(requestUserDTO.getId()).orElseThrow(() -> new InvalidUserData("Please provide user ID"));
        if (!user.getUsername().equals(requestUserDTO.getUsername())) {
            Validator.validateUsernameExists(userRepository, requestUserDTO.getUsername(), "This username isn't available. Please try another");
        }
        if (!user.getEmail().equals(requestUserDTO.getEmail())) {
            Validator.validateEmailExists(userRepository, requestUserDTO.getEmail());
        }
        if (!Validator.validateEmailPatternMatches(requestUserDTO.getEmail()))
            throw new InvalidUserData("Enter a valid email address.");
        if (requestUserDTO.getWebsite() != null) {
            if (!Validator.validateWebSitePatternMatches(requestUserDTO.getWebsite()))
                throw new InvalidUserData("Enter a valid website.");
        }

        UserEntity updatedUserEntity = modelMapper.map(requestUserDTO, UserEntity.class);

        updatedUserEntity.setPassword(user.getPassword());
        userRepository.save(updatedUserEntity);

        return modelMapper.map(updatedUserEntity, UserToReturnDTO.class);
    }

    public UserToReturnDTO registerUser(RequestUserDTO requestUserDTO) {
        String username = requestUserDTO.getUsername();
        String email = requestUserDTO.getEmail();
        String password = requestUserDTO.getPassword();
        String confirmPassword = requestUserDTO.getConfirmPassword();

        if (username.isBlank()) throw new InvalidUserData("Username can't be blank!");
        Validator.validateUsernameExists(userRepository, username, "This username isn't available. Please try another.");
        if (!password.equals(confirmPassword)) throw new InvalidUserData("Passwords don't match");
        if (!Validator.validateEmailPatternMatches(email)) throw new InvalidUserData("Invalid email");
        Validator.validateEmailExists(userRepository, requestUserDTO.getEmail());

        UserEntity userEntity = modelMapper.map(requestUserDTO, UserEntity.class);
        String hashedPassword = passwordEncoder.encode(requestUserDTO.getPassword());
        userEntity.setPassword(hashedPassword);
        userRepository.save(userEntity);

        //TODO: sendEmailVerification(userEntity.getEmail()); SomeClass.sendEmail("http://123.453.456.45:8080/confirm/" + userEntity.getId())

        return UserToReturnDTO.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .build();
    }

    public UserToReturnDTO getById(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new InvalidUserData("User with " + id + " does not exists."));
        return modelMapper.map(userEntity, UserToReturnDTO.class);
    }

    public UserToReturnDTO getByUsername(String username) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() -> new InvalidUserData("Username '" + username + "' does not exists."));
        return modelMapper.map(userEntity, UserToReturnDTO.class);
    }

    public UserToReturnDTO getByFullName(String fullName) {
        UserEntity userEntity = userRepository.findUserEntityByFullName(fullName).orElseThrow(() -> new InvalidUserData("User with full name - [" + fullName + "] does not exists."));
        return modelMapper.map(userEntity, UserToReturnDTO.class);
    }

    private UserEntity loginWithEmail(String email, String password) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(() -> new InvalidUserData("Invalid email"));
        boolean passwordMatches = passwordEncoder.matches(password, userEntity.getPassword());
        if (!passwordMatches) throw new InvalidUserData("Invalid password");

        return userEntity;
    }

    private UserEntity loginWithUsername(String username, String password) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() -> new InvalidUserData("Invalid username"));
        boolean passwordMatches = passwordEncoder.matches(password, userEntity.getPassword());
        if (!passwordMatches) throw new InvalidUserData("Invalid password");

        return userEntity;
    }

    public UserToReturnDTO deleteUser(RequestUserDTO userToDelete, HttpSession session, HttpServletRequest request) {

        if (userToDelete.getId() == null) throw new InvalidUserData("Please provide user ID ");
        UserEntity userEntity = userRepository.findById(userToDelete.getId()).orElseThrow(() -> new InvalidUserData("No such user found"));
        sessionManager.authorizeSession(userEntity.getId(), session, request);
        userRepository.deleteById(userToDelete.getId());

        return modelMapper.map(userEntity, UserToReturnDTO.class);
    }


}
