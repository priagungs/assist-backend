package com.future.OfficeInventorySystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.JoinColumn;

import lombok.Data;
import org.hibernate.annotations.Where;

import java.util.List;


@Entity
@TableGenerator(name = "employee_generator", initialValue = 16516000)
@Data
@Table(name = "user_employee")
@Where(clause = "is_deleted=0")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "employee_generator")
    private Long idUser;

    private String username;

    @JsonIgnore
    private String password;

    private String name;

    private String pictureURL;

    private String role;

    private String division;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSuperior", updatable = false, insertable = false)
    private User superior;

    @OneToMany(mappedBy = "superior")
    @JsonIgnore
    private List<User> subordinates;

    private Boolean isAdmin;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Request> requests;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<UserHasItem> hasItems;

    @OneToMany(mappedBy = "admin")
    @JsonIgnoreProperties("admin")
    private List<Transaction> transactions;

    @Column(name = "is_active")
    private Boolean active = true;


}
