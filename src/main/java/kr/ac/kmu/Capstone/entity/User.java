package kr.ac.kmu.Capstone.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Table(name = "user")
@Entity
@ToString
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private Role roles;



    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }



//    @Builder
//    public User(Long id, String email, String password, String nickname, Role roles) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.nickname = nickname;
//        this.roles = roles;
//    }

    public void update(String password, String nickname){
        this.password = password;
        this.nickname = nickname;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    public String getRoleKey() {
        return this.roles.name();
    }

    public Role getRoles() {
        return roles;
    }
}
