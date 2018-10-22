package com.future.office_inventory_system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@TableGenerator(name = "employee_generator", initialValue = 16516000)
@Data
@Table(name = TableName.USER)
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
    @JsonIgnoreProperties({"superior", "subordinates", "requests", "hasItems", "transactions"})
    private User superior;

    @OneToMany(mappedBy = "superior")
    @JsonIgnore
    private List<User> subordinates;

    private Boolean isAdmin;

    @OneToMany(mappedBy = "requestBy")
    @JsonIgnore
    private List<Request> requests;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserHasItem> hasItems;

    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    private List<Transaction> transactions;

    private Boolean isActive = true;
    
}
