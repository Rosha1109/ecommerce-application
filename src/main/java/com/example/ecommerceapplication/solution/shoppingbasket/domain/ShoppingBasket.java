package com.example.ecommerceapplication.solution.shoppingbasket.domain;

import com.example.ecommerceapplication.solution.client.domain.Client;
import jakarta.persistence.*;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShoppingBasket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ElementCollection
    @Nullable
    private Map<UUID,Integer> basket  = new HashMap<>();

    @OneToOne
    private Client client;
}
