package com.example.ecommerceapplication.solution.thing.application;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.domainprimitives.Money;

import com.example.ecommerceapplication.solution.purchase.domain.Purchase;
import com.example.ecommerceapplication.solution.purchase.domain.PurchaseRepository;
import com.example.ecommerceapplication.solution.shoppingbasket.domain.ShoppingBasket;
import com.example.ecommerceapplication.solution.shoppingbasket.domain.ShoppingBasketRepository;
import com.example.ecommerceapplication.solution.thing.domain.Thing;
import com.example.ecommerceapplication.solution.thing.domain.ThingRepository;
import com.example.ecommerceapplication.usecases.ThingCatalogUseCases;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
@Service

public class ThingCatalogService implements ThingCatalogUseCases {
    @Autowired
    private ThingRepository thingRepository;
    @Autowired
    private ShoppingBasketRepository shoppingBasketRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Override
    public void addThingToCatalog(UUID thingId, String name, String description, Float size, MoneyType buyingPrice, MoneyType sellPrice) {
        if(thingId == null || thingRepository.existsByThingId(thingId))
        {
            throw new ShopException("ThingId null, or thingRepository.existsByThingId(thingId)");
        }
        if(size!= null && size <= 0){
            throw new ShopException("Size <= 0");
        }
        if(name == null || name.isEmpty()|| description == null || description.isEmpty()){
            throw new ShopException("Name or description are null or empty");
        }
        if(buyingPrice==null || sellPrice==null){
            throw new ShopException("buyingPrice or sellPrice are null");
        }
        if(buyingPrice.largerThan(sellPrice)){
            throw new ShopException("No profit for buying price");
        }
        Thing thing = new Thing(thingId,name,description,size,(Money)buyingPrice, (Money) sellPrice);
        thingRepository.save(thing);


    }

    @Override
    public void removeThingFromCatalog(UUID thingId) {
        if(thingId == null){
            throw new ShopException("ThingId null");
        }
        Thing thing = thingRepository.findByThingId(thingId);
        if(thing == null){
            throw new ShopException("Does not exist");
        }
        if(thing.getThingStock()!=null && thing.getThingStock()>0){
            throw new ShopException("Does have stock");
        }

        for(ShoppingBasket basket: shoppingBasketRepository.findAll()){
            for(Map.Entry<UUID,Integer> entry:basket.getBasket().entrySet()){
                UUID thingInBasket = entry.getKey();
                Integer quantity = entry.getValue();
                if(thingId.equals(thingInBasket)){
                    throw new ShopException("Thing still in basket");
                }
            }
        }
        for(Purchase purchase: purchaseRepository.findAll()){
            if(purchase.getPurchaseHistory().containsKey(thingId)){
                throw new ShopException("Thing part of a completed purchase");
            }
        }
        thingRepository.delete(thing);
    }

    @Override
    public MoneyType getSellPrice(UUID thingId) {
        if(thingId == null){
            throw new ShopException("ThingId null");
        }
        if(!thingRepository.existsByThingId(thingId)){
            throw new ShopException("Does not exist");
        }
        return thingRepository.findByThingId(thingId).getSellPrice();
    }

    @Override
    public void deleteThingCatalog() {
        thingRepository.deleteAll();
    }
}
