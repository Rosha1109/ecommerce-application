package com.example.ecommerceapplication.solution.shoppingbasket.domain;

import com.example.ecommerceapplication.solution.client.domain.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ShoppingBasketRepository extends CrudRepository<ShoppingBasket, UUID> {
    ShoppingBasket findByClient(Client client);
}
