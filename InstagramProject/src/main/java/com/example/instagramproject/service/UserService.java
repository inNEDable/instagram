package com.example.instagramproject.service;

import com.example.instagramproject.exceptions.InvalidDataException;
import com.example.instagramproject.model.DTO.RequestUserDTO;
import com.example.instagramproject.model.DTO.ReturnUserDTO;
import com.example.instagramproject.model.entity.PostMediaEntity;
import com.example.instagramproject.model.entity.UserEntity;
import com.example.instagramproject.model.repository.UserRepository;
import com.example.instagramproject.util.PasswordGenerator;
import com.example.instagramproject.util.SessionManager;
import com.example.instagramproject.util.Validator;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class UserService {


    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MIN_FULL_NAME_LENGTH = 3;
    public static final int MAX_FULL_NAME_LENGTH = 30;
    public static final String ALL_PROFILE_PICTURES_FOLDER = "allUsersProfilePicture";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionManager sessionManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ModelMapper modelMapper;

    public ReturnUserDTO logOut(RequestUserDTO userToLogOut, HttpServletRequest request) {
        if (userToLogOut.getId() == null) throw new InvalidDataException("Please provide user ID ");
        if (!userRepository.existsById(userToLogOut.getId())) throw new InvalidDataException("Such user doesn't exist");
        sessionManager.authorizeSession(userToLogOut.getId(), request.getSession(), request);

        sessionManager.logOut(request.getSession());

        return modelMapper.map(userToLogOut, ReturnUserDTO.class);
    }

    public ReturnUserDTO login(RequestUserDTO userToLogin, HttpServletRequest request) {
        if (sessionManager.isLogged(request.getSession())) throw new InvalidDataException("Already logged!");
        String username = userToLogin.getUsername();
        String email = userToLogin.getEmail();
        String password = userToLogin.getPassword();
        UserEntity userEntity;
        if (password == null) throw new InvalidDataException("Password required");
        if (username == null && email == null) throw new InvalidDataException("Username OR Email needed for login!");
        if (username == null) userEntity = loginWithEmail(email, password);
        else userEntity = loginWithUsername(username, password);

        sessionManager.login(userEntity.getId(), request.getSession(), request);

        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    public ReturnUserDTO changePassword(RequestUserDTO requestUserDTO, HttpServletRequest request) {
        UserEntity userEntity = getUserAndWithFullVerification(requestUserDTO.getId(), request);

        if (!passwordEncoder.matches(requestUserDTO.getPassword(), userEntity.getPassword())) {
            throw new InvalidDataException("Please provide valid password");
        }
        if (!requestUserDTO.getNewPassword().equals(requestUserDTO.getConfirmPassword())) {
            throw new InvalidDataException("Please provide valid conf password");
        }
        Validator.validateStrongPassword(requestUserDTO.getNewPassword());

        String hashedPassword = passwordEncoder.encode(requestUserDTO.getNewPassword());
        userEntity.setPassword(hashedPassword);
        userRepository.save(userEntity);

        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    public ReturnUserDTO edit(RequestUserDTO requestUserDTO, HttpServletRequest request) {
        UserEntity user = getUserAndWithFullVerification(requestUserDTO.getId(), request);

        if (requestUserDTO.getUsername() != null && !user.getUsername().equals(requestUserDTO.getUsername())) {
            Validator.validateUsernameExists(userRepository, requestUserDTO.getUsername());
            Validator.validateStringLength(MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH, requestUserDTO.getUsername() );
        }

        if (requestUserDTO.getFullName() != null && !requestUserDTO.getFullName().equals(user.getFullName())){
            Validator.validateStringLength(MIN_FULL_NAME_LENGTH, MAX_FULL_NAME_LENGTH, requestUserDTO.getFullName());
        }

        if (requestUserDTO.getEmail() != null && !user.getEmail().equals(requestUserDTO.getEmail())) {
            Validator.validateEmailExists(userRepository, requestUserDTO.getEmail());
            Validator.validateRealEmail(requestUserDTO.getEmail());
        }

        if (requestUserDTO.getGender() != null && user.getGender()!= requestUserDTO.getGender()){
            Validator.validateGender(requestUserDTO.getGender());
        }

        if (requestUserDTO.getWebsite() != null && !requestUserDTO.getWebsite().equals(user.getWebsite()) ){
            Validator.validateRealWebSite(requestUserDTO.getWebsite());
        }


        UserEntity updatedUserEntity = modelMapper.map(requestUserDTO, UserEntity.class);

        updatedUserEntity.setPassword(user.getPassword());
        userRepository.save(updatedUserEntity);

        return modelMapper.map(updatedUserEntity, ReturnUserDTO.class);
    }

    public ReturnUserDTO registerUser(RequestUserDTO requestUserDTO, HttpServletRequest request) {
        if (sessionManager.isLogged(request.getSession())) throw new InvalidDataException("Cant Register while logged");
        String username = requestUserDTO.getUsername();
        String email = requestUserDTO.getEmail();
        String password = requestUserDTO.getPassword();
        String confirmPassword = requestUserDTO.getConfirmPassword();

        if (username.isBlank()) throw new InvalidDataException("Username can't be blank!");
        Validator.validateUsernameExists(userRepository, username);
        if (!password.equals(confirmPassword)) throw new InvalidDataException("Passwords don't match");
        Validator.validateRealEmail(email);
        Validator.validateStrongPassword(requestUserDTO.getPassword());
        Validator.validateEmailExists(userRepository, requestUserDTO.getEmail());
        Validator.validateStringLength(MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH, requestUserDTO.getUsername());
        Validator.validateStringLength(MIN_FULL_NAME_LENGTH, MAX_FULL_NAME_LENGTH, requestUserDTO.getFullName());

        UserEntity userEntity = modelMapper.map(requestUserDTO, UserEntity.class);
        String hashedPassword = passwordEncoder.encode(requestUserDTO.getPassword());
        userEntity.setPassword(hashedPassword);
        String token = UUID.randomUUID().toString();
        userEntity.setVerificationToken(token);
        userRepository.save(userEntity);

        sendVerificationVerificationEmail(userEntity, token);

        return ReturnUserDTO.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .fullName(userEntity.getFullName())
                .email(userEntity.getEmail())
                .build();
    }

    public ReturnUserDTO getById(long id) {
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new InvalidDataException("User with id - " + id + " does not exists."));
        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    public ReturnUserDTO getByUsername(String username, HttpServletRequest request) {
        Validator.validateStringLength(0, MAX_USERNAME_LENGTH, username);
        sessionManager.authorizeSession(null, request.getSession(), request);
        UserEntity userEntity = userRepository.findUserEntityByUsernameContaining(username).orElseThrow(() -> new InvalidDataException("Username '" + username + "' does not exists."));
        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    public ReturnUserDTO getByFullName(String fullName) {
        Validator.validateStringLength(0, MAX_FULL_NAME_LENGTH, fullName);
        UserEntity userEntity = userRepository.findUserEntityByFullNameContaining(fullName).orElseThrow(() -> new InvalidDataException("User with full name - [" + fullName + "] does not exists."));
        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    private UserEntity loginWithEmail(String email, String password) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(() -> new InvalidDataException("Invalid email"));
        boolean passwordMatches = passwordEncoder.matches(password, userEntity.getPassword());
        if (!passwordMatches) throw new InvalidDataException("Invalid password");

        return userEntity;
    }

    private UserEntity loginWithUsername(String username, String password) {
        UserEntity userEntity = userRepository.findUserEntityByUsernameContaining(username).orElseThrow(() -> new InvalidDataException("Invalid username"));
        boolean passwordMatches = passwordEncoder.matches(password, userEntity.getPassword());
        if (!passwordMatches) throw new InvalidDataException("Invalid password");

        return userEntity;
    }

    public ReturnUserDTO deleteUser(RequestUserDTO userToDelete, HttpServletRequest request) {
        UserEntity userEntity = getUserAndWithFullVerification(userToDelete.getId(), request);

        sessionManager.logOut(request.getSession());
        request.getSession().invalidate();

        userRepository.deleteById(userToDelete.getId());
        return modelMapper.map(userEntity, ReturnUserDTO.class);
    }

    public void confirmRegistration(Long userId, String token, HttpServletRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidDataException("Please provide user ID"));
        if (user.isVerified()) {
            throw new InvalidDataException("User is already verified");
        }
        if (user.getVerificationToken().equals(token)) {
            user.setVerified(true);
            userRepository.save(user);
        }
    }

    private void sendVerificationVerificationEmail(UserEntity userEntity, String token){
        String confirmationUrl = "/api/users/registrationConfirm/"+ userEntity.getId()+ "/" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEntity.getEmail());
        message.setSubject("Instagram email verification");
        message.setText("Please click here to verify your email: " +
                "http://localhost:8080" + confirmationUrl);
        emailSender.send(message);
    }

    public void sendForgottenPasswordEmail(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidDataException("Please provide valid email"));
        String newPass = PasswordGenerator.makePassword(20);
        user.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(user);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Instagram Password change");
        message.setText("Your new password is: " + newPass);
        emailSender.send(message);
    }

    public ReturnUserDTO userFollowsUser(Long followerId, Long followedId, HttpServletRequest request) {
        if (followerId == null || followedId == null) throw new InvalidDataException("Please provide both users IDs");

        UserEntity followerUser = getUserAndWithFullVerification(followerId, request);
        UserEntity followedUser = userRepository.findById(followedId).orElseThrow(() -> new InvalidDataException("Followed doesn't exist"));

        if (followedUser.getFollowers().contains(followerUser))
            throw new InvalidDataException(followerUser.getUsername() + " already follows " + followedUser.getUsername());

        followedUser.getFollowers().add(followerUser);
        userRepository.save(followedUser);

        return modelMapper.map(followedUser, ReturnUserDTO.class);
    }

    public ReturnUserDTO unFollowsUser(Long followerId, Long followedId, HttpServletRequest request) {
        if (followerId == null || followedId == null) throw new InvalidDataException("Please provide both users IDs");

        UserEntity followerUser = getUserAndWithFullVerification(followerId, request);
        UserEntity followedUser = userRepository.findById(followedId).orElseThrow(() -> new InvalidDataException("Followed doesn't exist"));

        if (!followedUser.getFollowers().contains(followerUser))
            throw new InvalidDataException(followerUser.getUsername() + " doesn't follows " + followedUser.getUsername());

        followedUser.getFollowers().remove(followerUser);
        userRepository.save(followedUser);

        return modelMapper.map(followedUser, ReturnUserDTO.class);
    }

    public Integer getFollowers(Long userId, HttpServletRequest request) {
        UserEntity userEntity = getUserAndWithLoginVerification(userId, request);
        return userEntity.getFollowers().size();
    }

    public Integer getFollowed(Long userId, HttpServletRequest request) {
        UserEntity userEntity = getUserAndWithLoginVerification(userId, request);
        return userEntity.getFollowed().size();
    }

    private UserEntity getUserAndWithLoginVerification(Long userId, HttpServletRequest request){
        sessionManager.authorizeSession(null, request.getSession(), request);
        if (userId == null) throw new InvalidDataException("Please provide user ID");
        return userRepository.findById(userId).orElseThrow(() -> new InvalidDataException("User doesn't exist"));
    }

    private UserEntity getUserAndWithFullVerification(Long userId, HttpServletRequest request){
        sessionManager.authorizeSession(userId, request.getSession(), request);
        if (userId == null) throw new InvalidDataException("Please provide user ID");
        return userRepository.findById(userId).orElseThrow(() -> new InvalidDataException("User doesn't exist"));
    }

    public String changeProfilePicture(MultipartFile multipartFile, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute(SessionManager.USER_ID);
        UserEntity userEntity = getUserAndWithFullVerification(userId, request);

        String fileName = System.nanoTime() + "."
                + FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        String currentPostFolder = "profilePicture-" + userId;
        new File(ALL_PROFILE_PICTURES_FOLDER + File.separator + currentPostFolder).mkdir();

        Path fullPath = Path.of(ALL_PROFILE_PICTURES_FOLDER
                + File.separator
                + currentPostFolder
                + File.separator
                + fileName);

        try {
            Files.copy(multipartFile.getInputStream(), fullPath);
        } catch (IOException e) {
            throw new InvalidDataException("Invalid data");
        }

        userEntity.setProfilePicture(fullPath.toString());
        userRepository.save(userEntity);

        return fileName;

    }
}
