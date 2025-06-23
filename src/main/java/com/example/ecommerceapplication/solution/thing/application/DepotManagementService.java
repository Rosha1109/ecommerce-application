package com.example.ecommerceapplication.solution.thing.application;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.solution.shoppingbasket.domain.ShoppingBasket;
import com.example.ecommerceapplication.solution.shoppingbasket.domain.ShoppingBasketRepository;
import com.example.ecommerceapplication.solution.thing.domain.Thing;
import com.example.ecommerceapplication.solution.thing.domain.ThingRepository;
import com.example.ecommerceapplication.usecases.DepotUseCases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class DepotManagementService implements DepotUseCases {
    @Autowired
    private ThingRepository thingRepository;
    @Autowired
    private ShoppingBasketRepository shoppingBasketRepository;
    @Override
    public void addToStock(UUID thingId, int addedQuantity) {
        if(thingId == null ) {
            throw new ShopException("Thing not found");
        }
        Thing thing = thingRepository.findByThingId(thingId);
        if(thing==null) {
            throw new ShopException("Thing not found");
        }
//        if(thing.getThingStock()!=null && thing.getThingStock()>0){
//            throw new ShopException("Still in Stock");
//        }
        if(addedQuantity < 0) {
            throw new ShopException("Invalid quantity");
        }

        thing.addToStock( addedQuantity );
        thingRepository.save(thing);

    }

    @Override
    public void removeFromStock(UUID thingId, int removedQuantity) {
        if(thingId==null)throw new ShopException("Thing not found");
        Thing thing = thingRepository.findByThingId(thingId);
        if (thingRepository.findByThingId(thingId)==null) throw new ShopException("Thing not found");
        if(thing.getThingStock() == null && removedQuantity> 0)throw new ShopException("Thing has no stock anyways");
        if(removedQuantity < 0) {throw new ShopException("Invalid quantity");}
        int reservedQuantity = 0;
        for(ShoppingBasket basket: shoppingBasketRepository.findAll()) {
            for(Map.Entry<UUID,Integer> entry:basket.getBasket().entrySet()) {
                UUID thingIdInBasket = entry.getKey();
                Integer quantity = entry.getValue();
                if(thingIdInBasket.equals(thingId)) {
                    reservedQuantity += quantity;
                }
            }
        }
        if(thing.getThingStock() != null && thing.getThingStock() + removedQuantity < removedQuantity){
            throw new ShopException("Invalid quantity");
        }
        if(thing.getThingStock() != null && reservedQuantity > thing.getThingStock()){

        }
        int newTotalQuantity;
        if(thing.getThingStock() !=null) {
             newTotalQuantity = thing.getThingStock() - removedQuantity;
        }else{
            newTotalQuantity = 0;
        }

        //TODO: check if removed quant ist greater than stock + reserved
        //TODO: take something of the clients baskets if stock < reserved
        if(newTotalQuantity <0){
            throw new ShopException("cant remove so much");
        }
        thing.setThingStock(newTotalQuantity);
        thingRepository.save(thing);
    }

    @Override
    public void changeStockTo(UUID thingId, int newTotalQuantity) {
        if(thingId==null)
        {
            throw new ShopException("Thing not found");
        }
        Thing thing = thingRepository.findByThingId(thingId);
        if(thing==null) {throw new ShopException("Thing not found");}
        if(newTotalQuantity< 0) throw new ShopException("Invalid quantity");

//        if(thing.getThingReserved()> thing.getThingStock()){
//            //take some things off a basket
//        }
        thing.setThingStock(newTotalQuantity);
        thingRepository.save(thing);
    }

    @Override
    public int getAvailableStock(UUID thingId) {
        if(thingId==null)throw new ShopException("Thing not found");
        Thing thing = thingRepository.findByThingId(thingId);
        if(thing==null) throw new ShopException("Thing not found");
        if(thing.getThingStock()==null){
            return 0;
        }else{
            return thing.getThingStock();
        }

    }
}
