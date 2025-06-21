package com.example.ecommerceapplication.solution.thing.domain;

import com.example.ecommerceapplication.domainprimitives.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Entity
public class Thing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private Float size;
    @Getter
    @Setter
    @Embedded
    private Money purchasePrice;
    @Getter
    @Setter
    @Embedded
    private Money sellPrice;
    @Getter
    @Setter
    private UUID thingId;

}
