package com.example.ecommerceapplication.solution.purchase.application;

import com.example.ecommerceapplication.usecases.PurchaseUseCases;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
@Service
public class PurchaseService implements PurchaseUseCases {
    @Override
    public Map<UUID, Integer> getPurchaseHistory(EmailAddressType clientEmailAddress) {
        return Map.of();
    }

    @Override
    public void deleteAllPurchases() {

    }
}
