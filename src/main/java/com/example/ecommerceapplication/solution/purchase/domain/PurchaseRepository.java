package com.example.ecommerceapplication.solution.purchase.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PurchaseRepository extends CrudRepository<Purchase, UUID> {
}
