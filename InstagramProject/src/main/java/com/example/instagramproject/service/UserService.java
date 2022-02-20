package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidData;
import com.example.instagramproject.model.DTO.RequestUserDTO;
import com.example.instagramproject.model.DTO.ReturnUserDTO;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.UserRepository;
import com.example.instagramproject.util.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    public ReturnUserDTO logOut(RequestUserDTO userToLogOut, HttpServletRequest request) {
        if (userToLogOut.getId() == null) throw new InvalidData("Please provide user ID ");
        if (!userRepository.existsById(userToLogOut.getId())) throw new InvalidData("Such user doesn't exist");

        sessionManager.authorizeSession(userToLogOut.getId(), request.getSession(), request);
        sessionManager.logOut(request.getSession());

        return modelMapper.map(userToLogOut, ReturnUserDTO.class);
    }

    public ReturnUserDTO login(RequestUserDTO userToLogin, HttpServletRequest request) {
        if (sessionManager.isLogged(request.getSession())) throw new InvalidData("Already logged!");
        String username = userToLogin.getUsername();
        String email = userToLogin.getEmail();
        String password = userToLogin.getPassword();
        UserEntity userEntity;
        if (password == null) throw new InvalidData("Password required");
        if (username == null && email == null) throw new InvalidData("Username OR Email needed for login!");
        if (username == null) userEntity = loginWithEmail(email, password);
        else userEntity = loginWithUsername(username, password);

        sessionManager.login(userEntity.getId(), request.getSession(), request);

        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    public ReturnUserDTO changePassword(RequestUserDTO requestUserDTO, HttpServletRequest request) {
        sessionManager.authorizeSession(requestUserDTO.getId(), request.getSession(), request);
        UserEntity userEntity = userRepository.findById((long) request.getSession().getAttribute(SessionManager.USER_ID))
                .orElseThrow(() -> new InvalidData("Please provide user ID"));

        if (!passwordEncoder.matches(requestUserDTO.getPassword(), userEntity.getPassword())) {
            throw new InvalidData("Please provide valid password");
        }
        if (!requestUserDTO.getNewPassword().equals(requestUserDTO.getConfirmPassword())) {
            throw new InvalidData("Please provide valid conf password");
        }

        String hashedPassword = passwordEncoder.encode(requestUserDTO.getNewPassword());
        userEntity.setPassword(hashedPassword);
        userRepository.save(userEntity);

        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    public ReturnUserDTO edit(RequestUserDTO requestUserDTO, HttpServletRequest request) {
        sessionManager.authorizeSession(requestUserDTO.getId(), request.getSession(), request);

        UserEntity user = userRepository.findById(requestUserDTO.getId()).orElseThrow(() -> new InvalidData("Please provide user ID"));
        if (!user.getUsername().equals(requestUserDTO.getUsername())) {
            Validator.validateUsernameExists(userRepository, requestUserDTO.getUsername(), "This username isn't available. Please try another");
        }
        if (!user.getEmail().equals(requestUserDTO.getEmail())) {
            Validator.validateEmailExists(userRepository, requestUserDTO.getEmail());
        }
        if (!Validator.validateEmailPatternMatches(requestUserDTO.getEmail()))
            throw new InvalidData("Enter a valid email address.");
        if (requestUserDTO.getWebsite() != null) {
            if (!Validator.validateWebSitePatternMatches(requestUserDTO.getWebsite()))
                throw new InvalidData("Enter a valid website.");
        }

        UserEntity updatedUserEntity = modelMapper.map(requestUserDTO, UserEntity.class);

        updatedUserEntity.setPassword(user.getPassword());
        userRepository.save(updatedUserEntity);

        return modelMapper.map(updatedUserEntity, ReturnUserDTO.class);
    }

    public ReturnUserDTO registerUser(RequestUserDTO requestUserDTO, HttpServletRequest request) {
        if (sessionManager.isLogged(request.getSession())) throw new InvalidData("Cant Register while logged");
        String username = requestUserDTO.getUsername();
        String email = requestUserDTO.getEmail();
        String password = requestUserDTO.getPassword();
        String confirmPassword = requestUserDTO.getConfirmPassword();

        if (username.isBlank()) throw new InvalidData("Username can't be blank!");
        Validator.validateUsernameExists(userRepository, username, "This username isn't available. Please try another.");
        if (!password.equals(confirmPassword)) throw new InvalidData("Passwords don't match");
        if (!Validator.validateEmailPatternMatches(email)) throw new InvalidData("Invalid email");
        Validator.validateEmailExists(userRepository, requestUserDTO.getEmail());

        UserEntity userEntity = modelMapper.map(requestUserDTO, UserEntity.class);
        String hashedPassword = passwordEncoder.encode(requestUserDTO.getPassword());
        userEntity.setPassword(hashedPassword);
        userRepository.save(userEntity);

        //TODO: sendEmailVerification(userEntity.getEmail()); SomeClass.sendEmail("http://123.453.456.45:8080/confirm/" + userEntity.getId())

        return ReturnUserDTO.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .build();
    }

    public ReturnUserDTO getById(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new InvalidData("User with id - " + id + " does not exists."));
        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    public ReturnUserDTO getByUsername(String username) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() -> new InvalidData("Username '" + username + "' does not exists."));
        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    public ReturnUserDTO getByFullName(String fullName) {
        UserEntity userEntity = userRepository.findUserEntityByFullName(fullName).orElseThrow(() -> new InvalidData("User with full name - [" + fullName + "] does not exists."));
        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    private UserEntity loginWithEmail(String email, String password) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(() -> new InvalidData("Invalid email"));
        boolean passwordMatches = passwordEncoder.matches(password, userEntity.getPassword());
        if (!passwordMatches) throw new InvalidData("Invalid password");

        return userEntity;
    }

    private UserEntity loginWithUsername(String username, String password) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() -> new InvalidData("Invalid username"));
        boolean passwordMatches = passwordEncoder.matches(password, userEntity.getPassword());
        if (!passwordMatches) throw new InvalidData("Invalid password");

        return userEntity;
    }

    public ReturnUserDTO deleteUser(RequestUserDTO userToDelete, HttpServletRequest request) {

        if (userToDelete.getId() == null) throw new InvalidData("Please provide user ID ");
        UserEntity userEntity = userRepository.findById(userToDelete.getId()).orElseThrow(() -> new InvalidData("No such user found"));

        sessionManager.authorizeSession(userEntity.getId(), request.getSession(), request);
        sessionManager.logOut(request.getSession());
        request.getSession().invalidate();

        userRepository.deleteById(userToDelete.getId());
        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

//    public UserToReturnDTO forgottenPassword(RequestUserDTO requestUserDTO, HttpSession session, HttpServletRequest request) {
//        sessionManager.authorizeSession(requestUserDTO.getId(), session, request);
//
//        Validator.validateEmailPatternMatches(requestUserDTO.getEmail());
//        UserEntity userEntity = userRepository.findUserEntityByEmail(requestUserDTO.getEmail()).orElseThrow(() -> new InvalidUserData("Enter a valid email address."));
//
//
//        return null;
//    }
}
