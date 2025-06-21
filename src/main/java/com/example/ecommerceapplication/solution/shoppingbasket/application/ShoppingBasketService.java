package com.example.ecommerceapplication.solution.shoppingbasket.application;

import com.example.ecommerceapplication.usecases.ShoppingBasketUseCases;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ShoppingBasketService implements ShoppingBasketUseCases {
    @Override
    public void addThingToShoppingBasket(EmailAddressType clientEmailAddress, UUID thingId, int quantity) {

    }

    @Override
    public void removeThingFromShoppingBasket(EmailAddressType clientEmailAddress, UUID thingId, int quantity) {

    }

    @Override
    public Map<UUID, Integer> getShoppingBasketAsMap(EmailAddressType clientEmailAddress) {
        return Map.of();
    }

    @Override
    public MoneyType getShoppingBasketAsMoneyValue(EmailAddressType clientEmailAddress) {
        return null;
    }

    @Override
    public int getReservedStockInShoppingBaskets(UUID thingId) {
        return 0;
    }

    @Override
    public boolean isEmpty(EmailAddressType clientEmailAddress) {
        return false;
    }

    @Override
    public UUID checkout(EmailAddressType clientEmailAddress) {
        return null;
    }

    @Override
    public void emptyAllShoppingBaskets() {

    }
}
