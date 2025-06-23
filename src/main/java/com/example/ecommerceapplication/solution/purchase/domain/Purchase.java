package com.example.ecommerceapplication.solution.purchase.domain;

import com.example.ecommerceapplication.solution.client.domain.Client;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
public class Purchase {
    @Id
    @GeneratedValue(strategy = AUTO)
    protected UUID id;
    @ManyToOne
    @Setter
    protected Client client;

    @ElementCollection
    @Setter
    private Map<UUID,Integer> purchaseHistory = new HashMap<>();
}
