package com.example.instagramproject.model.entity;

import lombok.*;
import org.apache.catalina.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    private Character gender;

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "dd/mm/yyyy")
    private LocalDate birthDate;

    @Column
    protected String website;

    @Column
    protected String bio;

    @Column(name = "is_verified")
    protected boolean isVerified;

    @Column(name = "verification_token")
    private String verificationToken;

    @OneToMany(mappedBy= "user", cascade = CascadeType.ALL)
    private Set<PostEntity> posts;

    @OneToMany(mappedBy= "user", cascade = CascadeType.ALL)
    private Set<CommentEntity> comments;

    @ManyToMany(mappedBy = "likers")
    private Set<CommentEntity> likedComments;

    @ManyToMany(mappedBy = "likers")
    private Set<SubCommentEntity> likedSubComments;

    @ManyToMany
    @JoinTable(
            name = "users_follow_users",
            joinColumns = @JoinColumn(name = "followed_user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_user_id"))
    private Set<UserEntity> followers;

    @ManyToMany(mappedBy= "followers", cascade = CascadeType.ALL)
    private Set<UserEntity> followed;

    public UserEntity(String username, String email, String password, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
