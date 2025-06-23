package com.example.ecommerceapplication.solution.purchase.application;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.solution.client.domain.Client;
import com.example.ecommerceapplication.solution.client.domain.ClientRepository;
import com.example.ecommerceapplication.solution.purchase.domain.PurchaseRepository;
import com.example.ecommerceapplication.usecases.PurchaseUseCases;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
@Service
public class PurchaseService implements PurchaseUseCases {
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public Map<UUID, Integer> getPurchaseHistory(EmailAddressType clientEmailAddress) {
        if(clientEmailAddress == null) {
            throw new ShopException("clientEmailAddress is null");
        }
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        if(client == null) {
            throw new ShopException("client not found");
        }
        if( purchaseRepository.findByClient(client)==null){
            return Map.of();
        }else{
            return purchaseRepository.findByClient(client).getPurchaseHistory();
        }
    }

    @Override
    public void deleteAllPurchases() {
        purchaseRepository.deleteAll();
    }
}
