package com.future.OfficeInventorySystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.Set;

@Entity
@TableGenerator(name = "employee_generator", initialValue = 16516000)
@Data
@Table(name = "user_employee")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "employee_generator")
    private Long idUser;

    private String username;

    @JsonIgnore
    private String password;

    private String name;

    private String picture; //path to an image in filesystem

    private String role;

    private String division;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSuperior", updatable = false, insertable = false)
    private User superior;

    @OneToMany(mappedBy="superior")
    @JsonIgnore
    private List<User> subordinates;

    private Boolean isAdmin;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Request> request;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserHasItem> hasItem;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "admin")
    private List<Transaction> transaction;


}
