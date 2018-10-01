package com.future.OfficeInventorySystem.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@TableGenerator(name = "employee_nip", initialValue = 16516000)
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "employee_nip")
    private Long idUser;

    private String name;

    private String username;

    private String picture; //path to an image in filesystem

    private String password;

    private String division;

    private String role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUser", nullable = false)
    private User Superior;

}
