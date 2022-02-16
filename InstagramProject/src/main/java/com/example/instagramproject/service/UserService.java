package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidUserData;
import com.example.instagramproject.model.DTO.RequestUserDTO;
import com.example.instagramproject.model.DTO.UserToReturnDTO;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.UserRepository;
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

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

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

        return modelMapper.map(requestUserDTO, UserToReturnDTO.class);
    }

    public UserToReturnDTO edit(RequestUserDTO requestUserDTO, HttpSession session, HttpServletRequest request) {
        System.out.println(requestUserDTO);
        sessionManager.authorizeSession(requestUserDTO.getId(), session, request);

        UserEntity user = userRepository.findById(requestUserDTO.getId()).orElseThrow(() -> new InvalidUserData("Please provide user ID"));
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
        if (!password.equals(confirmPassword)) throw new InvalidUserData("Passwords don't match");
        if (!patternMatches(email)) throw new InvalidUserData("Invalid email");
        if (userRepository.findUserEntityByUsername(username).isPresent())
            throw new InvalidUserData("Username already exist");

        UserEntity userEntity;
        if (!userRepository.findUserEntityByEmail(email).isPresent()) {
            userEntity = modelMapper.map(requestUserDTO.toEntity(), UserEntity.class);
            String hashedPassword = passwordEncoder.encode(requestUserDTO.getPassword());
            userEntity.setPassword(hashedPassword);
            userRepository.save(userEntity);
        }
        else throw new InvalidUserData("Email already exist");

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
        Optional<UserEntity> userEntity = userRepository.findUserEntityByEmail(email);
        if (!userEntity.isPresent()) throw new InvalidUserData("Invalid email");
        boolean passwordMatches = passwordEncoder.matches(password, userEntity.get().getPassword());
        if (!passwordMatches) {
            throw new InvalidUserData("Invalid password");
        }
        return userEntity.get();
    }

    private UserEntity loginWithUsername(String username, String password) {
        Optional<UserEntity> userEntity = userRepository.findUserEntityByUsername(username);
        if (!userEntity.isPresent()) throw new InvalidUserData("Invalid username");
        boolean passwordMatches = passwordEncoder.matches(password, userEntity.get().getPassword());
        if (!passwordMatches) {
            throw new InvalidUserData("Invalid password");
        }
        return userEntity.get();
    }

    public UserToReturnDTO deleteUser(RequestUserDTO userToDelete, HttpSession session, HttpServletRequest request) {

        if (userToDelete.getId() == null) throw new InvalidUserData("Please provide user ID ");
        Optional<UserEntity> userEntity = userRepository.findById(userToDelete.getId());
        if (userEntity.isEmpty()) throw new InvalidUserData("No such user found");
        sessionManager.authorizeSession(userEntity.get().getId(), session, request);
        userRepository.deleteById(userToDelete.getId());
        return modelMapper.map(userEntity, UserToReturnDTO.class);
    }

    public static boolean patternMatches(String emailAddress) {
        return Pattern.compile(EMAIL_REGEX)
                .matcher(emailAddress)
                .matches();
    }
}
