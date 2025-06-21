package com.example.ecommerceapplication.solution.client.domain;

import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ClientRepository extends CrudRepository<Client, UUID> {
    Client findByEmailAddress(EmailAddressType emailAddress);
}
