package com.future.OfficeInventorySystem.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;

@Entity
@TableGenerator(name = "Administrator", initialValue = 13516000)
public class Administrator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

}
