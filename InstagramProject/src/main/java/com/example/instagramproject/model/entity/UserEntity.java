package com.example.instagramproject.model.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor

public class UserEntity {

    // id, username, full_name, email, pass, profile_pic, phone_number, gender, birth_date, website, bio, is_verified

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column(name = "full_name")
    private String fullName;

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

    @Column(name = "is_verified")
    protected boolean isVerified;

    public UserEntity(String username, String email, String password, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
