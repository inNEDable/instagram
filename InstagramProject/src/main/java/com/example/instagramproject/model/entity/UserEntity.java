package com.example.instagramproject.model.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class UserEntity {

    //`id`, `username`, `full_name`, `email`, `pass`, `profile_pic`, `phone_number`, `gender`, `birth_date`, `website`, `bio`, `is_verified`

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column(name = "full_name")
    private String fullName;

    //@Email(regexp=".*@.*\\..*", message = "Email should be valid")
    @Column
    private String email;

    @Column(name = "pass")
    private String password;

    @Column(name = "profile_pic")
    private String profilePicture;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private String gender;

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private LocalDate birthDate;

    @Column
    protected String website;

    @Column
    protected String bio;

//    @OneToMany(mappedBy = "images")
//    protected List<Image> images;

    @Column(name = "is_verified")
    protected boolean isVerified;



}
