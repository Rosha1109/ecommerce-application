package com.example.ecommerceapplication.solution.client.application;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.domainprimitives.EmailAddress;
import com.example.ecommerceapplication.domainprimitives.HomeAddress;
import com.example.ecommerceapplication.solution.client.domain.Client;
import com.example.ecommerceapplication.solution.client.domain.ClientRepository;
import com.example.ecommerceapplication.usecases.ClientRegistrationUseCases;
import com.example.ecommerceapplication.usecases.ClientType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.HomeAddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientRegistrationService implements ClientRegistrationUseCases {
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public void register(String name, EmailAddressType emailAddress, HomeAddressType homeAddress) {
        if(name ==null || name.isEmpty()){
            throw new ShopException("Name cannot be empty");
        }
        if(emailAddress == null){
            throw new ShopException("EmailAddress cannot be null");
        }
        if(clientRepository.findByEmailAddress(emailAddress) != null){
            throw new ShopException("EmailAddress is already registered");
        }
        if(homeAddress == null){
            throw new ShopException("HomeAddress cannot be null");
        }
        Client client = new Client(name,(EmailAddress) emailAddress,(HomeAddress) homeAddress);
        clientRepository.save(client);
    }

    @Override
    public void changeAddress(EmailAddressType clientEmailAddress, HomeAddressType clientHomeAddress) {
        if(clientEmailAddress == null){
            throw new ShopException("EmailAddress cannot be null");
        }
        if(clientRepository.findByEmailAddress(clientEmailAddress) == null){
            throw new ShopException("Client not found");
        }
        if(clientHomeAddress == null){
            throw new ShopException("HomeAddress cannot be null");
        }
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        client.setHomeAddress((HomeAddress) clientHomeAddress);
    }

    @Override
    public ClientType getClientData(EmailAddressType clientEmailAddress) {
        if(clientEmailAddress == null){
            throw new ShopException("EmailAddress cannot be null");
        }
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        if(client == null){
            throw new ShopException("Client not found");
        }
      return client;
    }

    @Override
    public void deleteAllClients() {
        clientRepository.deleteAll();
    }
}
