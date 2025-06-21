package com.example.ecommerceapplication.solution.shoppingbasket.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ShoppingBasketRepository extends CrudRepository<ShoppingBasket, UUID> {
}
