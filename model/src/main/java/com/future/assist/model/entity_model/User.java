package com.future.assist.model.entity_model;

import com.future.assist.model.TableName;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = TableName.USER)
public class User {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue
    private Long idUser;

    private String username;

    private String password;

    private String name;

    private String pictureURL;

    private String role;

    private String division;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idSuperior")
    private User superior;

    @OneToMany(mappedBy = "superior")
    private List<User> subordinates;

    private Boolean isAdmin;

    @OneToMany(mappedBy = "requestBy")
    private List<Request> requests;

    @OneToMany(mappedBy = "user")
    private List<UserHasItem> hasItems;

    @OneToMany(mappedBy = "admin")
    private List<Transaction> transactions;

    private Boolean isActive = true;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public void setPasswordWithoutEncode(String password) {
        this.password = password;
    }
}
