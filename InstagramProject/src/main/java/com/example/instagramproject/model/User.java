package com.example.instagramproject.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "full_name")
    private String fullName;

    //@Email(regexp=".*@.*\\..*", message = "Email should be valid")
    @Column(name = "email")
    private String email;

    @Column(name = "pass")
    private String password;

    @Column(name = "profile_pic")
    private String profilePicture;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private LocalDate date;

    @Column(name = "website")
    protected String website;

    @Column(name = "bio")
    protected String bio;

//    @OneToMany(mappedBy = "images")
//    protected List<Image> images;

    @Column(name = "is_verified")
    protected boolean isVerified;

}
