package com.example.ecommerceapplication.solution.thing.application;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.solution.shoppingbasket.application.RightThingServiceInterface;
import com.example.ecommerceapplication.solution.thing.domain.Thing;
import com.example.ecommerceapplication.solution.thing.domain.ThingRepository;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.UUID;
@Service
public class RightThingService implements RightThingServiceInterface {
    @Autowired
    private ThingRepository thingRepository;
    @Override
    public MoneyType getSellPrice(UUID thingId) {
        Thing thing = thingRepository.findByThingId(thingId);
        if(thing == null) {throw new ShopException("does not exist");}
        return thing.getSellPrice();
    }

    @Override
    public int getThingStock(UUID thingId) {
        Thing thing = thingRepository.findByThingId(thingId);
        if(thing == null) {throw new ShopException("does not exist");}
        return thing.getThingStock();
    }

    @Override
    public void setThingStock(UUID thingId, int quantity) {
        Thing thing = thingRepository.findByThingId(thingId);
        if (thing == null) {throw new ShopException("Thing not found");}
        thing.setThingStock(quantity);
        thingRepository.save(thing);
    }
}
