package com.example.ecommerceapplication.solution.thing.application;

import com.example.ecommerceapplication.usecases.DepotUseCases;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DepotManagementService implements DepotUseCases {
    @Override
    public void addToStock(UUID thingId, int addedQuantity) {

    }

    @Override
    public void removeFromStock(UUID thingId, int removedQuantity) {

    }

    @Override
    public void changeStockTo(UUID thingId, int newTotalQuantity) {

    }

    @Override
    public int getAvailableStock(UUID thingId) {
        return 0;
    }
}
