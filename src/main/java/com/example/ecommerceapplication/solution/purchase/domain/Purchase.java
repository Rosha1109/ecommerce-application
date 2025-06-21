package com.example.ecommerceapplication.solution.purchase.domain;

import com.example.ecommerceapplication.solution.client.domain.Client;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.GenerationType.*;

public class Purchase {
    @Id
    @GeneratedValue(strategy = AUTO)
    protected UUID id;
    @ManyToOne
    protected Client client;

    @ElementCollection
    private List<PurchasePart> purchaseParts = new ArrayList<>();
}
