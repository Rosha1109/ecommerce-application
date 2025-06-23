package com.example.ecommerceapplication.solution.shoppingbasket.domain;

import com.example.ecommerceapplication.solution.client.domain.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
public class ShoppingBasket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ElementCollection
    @Setter
    private Map<UUID,Integer> basket  = new HashMap<>();

    @OneToOne
    @Setter
    private Client client;
}
