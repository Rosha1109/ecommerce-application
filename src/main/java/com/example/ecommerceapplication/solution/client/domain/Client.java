package com.example.ecommerceapplication.solution.client.domain;

import com.example.ecommerceapplication.domainprimitives.EmailAddress;
import com.example.ecommerceapplication.domainprimitives.HomeAddress;
import com.example.ecommerceapplication.usecases.ClientType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
public class Client implements ClientType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private UUID id;

    @Embedded
    private EmailAddress emailAddress;

    private String name;

    @Embedded
    @Setter
    private HomeAddress homeAddress;

    public Client(String name, EmailAddress emailAddress, HomeAddress homeAddress) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.homeAddress = homeAddress;
    }


    public Client() {
        //jpa
    }
}
