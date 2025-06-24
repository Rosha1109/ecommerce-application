package com.example.ecommerceapplication.solution.thing.application;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.solution.shoppingbasket.domain.ShoppingBasket;
import com.example.ecommerceapplication.solution.shoppingbasket.domain.ShoppingBasketRepository;
import com.example.ecommerceapplication.solution.thing.domain.Thing;
import com.example.ecommerceapplication.solution.thing.domain.ThingRepository;
import com.example.ecommerceapplication.usecases.DepotUseCases;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Thing thing = thingRepository.findByThingId(thingId);
        if(thing==null) {throw new ShopException("Thing not found");}
        if(removedQuantity < 0) {throw new ShopException("Invalid quantity");}
        if(removedQuantity > thing.getThingStock() + getReservedQuantity(thingId)) {
            throw new ShopException("Invalid quantity");
        }
        if(removedQuantity <= thing.getThingStock()) {
            thing.setThingStock(thing.getThingStock() - removedQuantity);
        }else{
            int rest = removedQuantity;
            List<ShoppingBasket> rightBaskets = new ArrayList<>();
            for(ShoppingBasket shoppingBasket : shoppingBasketRepository.findAll()) {
                if(shoppingBasket.getBasket() != null && shoppingBasket.getBasket().containsKey(thingId)) {
                    rightBaskets.add(shoppingBasket);
                }
            }
        for(ShoppingBasket shoppingBasket : rightBaskets) {
            if(rest == 0){
                break;
            }
            Map<UUID,Integer> map = new HashMap<>(shoppingBasket.getBasket());
            if(map == null)map = new HashMap<>();
            int stockQuantity = map.get(thingId);

            if(stockQuantity <=rest){
                rest -= stockQuantity;
                map.remove(thingId);
                shoppingBasket.setBasket(map);
                shoppingBasketRepository.save(shoppingBasket);
            }else{
                map.put(thingId,stockQuantity-rest);
                shoppingBasket.setBasket(map);
                shoppingBasketRepository.save(shoppingBasket);
                rest=0;
            }


        }
        }
        thingRepository.save(thing);
    }

    @Override
    public void changeStockTo(UUID thingId, int newTotalQuantity) {
        if (thingId == null) {
            throw new ShopException("Thing not found");
        }
        Thing thing = thingRepository.findByThingId(thingId);
        if (thing == null) {
            throw new ShopException("Thing not found");
        }
        List<ShoppingBasket> baskets= new ArrayList<>();
        for(ShoppingBasket shoppingBasket : shoppingBasketRepository.findAll()) {
            if(shoppingBasket.getBasket() != null && shoppingBasket.getBasket().containsKey(thingId)) {
                baskets.add(shoppingBasket);
            }
        }
        if (newTotalQuantity < 0) throw new ShopException("Invalid quantity");
        if( newTotalQuantity < getReservedQuantity(thingId) ) {
            int removeInOtherBaskets = getReservedQuantity(thingId) - newTotalQuantity;
                for(ShoppingBasket shoppingBasket : baskets) {
                    int thingQuantity = shoppingBasket.getBasket().get(thingId);
                    if(removeInOtherBaskets <= thingQuantity) {
                        shoppingBasket.getBasket().put(thingId,thingQuantity - removeInOtherBaskets);
                        shoppingBasketRepository.save(shoppingBasket);
                        break;
                    }else{
                        removeInOtherBaskets -= thingQuantity;
                        shoppingBasket.getBasket().put(thingId,0);
                    }
                }
                thing.setThingStock(0);
        }
        thing.setThingStock(newTotalQuantity - getReservedQuantity(thingId));
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
            return thing.getThingStock() - getReservedQuantity(thingId);
        }

    }
    public int getReservedQuantity(UUID thingId){
        if(thingId==null)throw new ShopException("Thing not found");
        Thing thing = thingRepository.findByThingId(thingId);
        if(thing==null) throw new ShopException("Thing not found");
        int sum = 0;
        for(ShoppingBasket basket : shoppingBasketRepository.findAll()) {
            if(basket.getBasket() == null){
                continue;
            }else{
                if(basket.getBasket().get(thingId)!=null){
                sum += basket.getBasket().get(thingId);
                }
            }
        }
        return sum;
    }

}
