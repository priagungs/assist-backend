package com.future.OfficeInventorySystem.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "employee_nip", initialValue = 16516000)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_nip")
    @Getter @Setter
    private Long NIP;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String picture; //path to an image in filesystem

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String division;

    @Getter @Setter
    private String role;

    @Getter @Setter
    private Long superiorNIP;

}
