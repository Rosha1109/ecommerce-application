package com.example.ecommerceapplication.solution.thing.application;

import com.example.ecommerceapplication.usecases.ThingCatalogUseCases;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class ThingCatalogService implements ThingCatalogUseCases {
    @Override
    public void addThingToCatalog(UUID thingId, String name, String description, Float size, MoneyType buyingPrice, MoneyType sellPrice) {

    }

    @Override
    public void removeThingFromCatalog(UUID thingId) {

    }

    @Override
    public MoneyType getSellPrice(UUID thingId) {
        return null;
    }

    @Override
    public void deleteThingCatalog() {

    }
}
