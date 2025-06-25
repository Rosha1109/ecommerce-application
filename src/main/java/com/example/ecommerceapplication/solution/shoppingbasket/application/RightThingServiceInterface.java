package com.example.ecommerceapplication.solution.shoppingbasket.application;

import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;

import java.util.UUID;

public interface RightThingServiceInterface {
    MoneyType getSellPrice (UUID thingId);
    int getThingStock (UUID thingId);
    void setThingStock (UUID thingId, int quantity);

}
