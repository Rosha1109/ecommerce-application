package com.example.ecommerceapplication.solution.purchase.domain;

import com.example.ecommerceapplication.solution.client.domain.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PurchaseRepository extends CrudRepository<Purchase, UUID> {
    Purchase findByClient(Client client);
}
