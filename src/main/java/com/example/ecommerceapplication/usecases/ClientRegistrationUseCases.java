package com.example.ecommerceapplication.usecases;


import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.HomeAddressType;

/**
 * This interface defines the methods needed to register a client
 */
public interface ClientRegistrationUseCases {
    /**
     * Registers a new client
     * @param name
     * @param homeAddress
     * @throws ShopException if ...
     *      - name is null or empty
     *      -emailAddress is null
     *      -client with the given email already exists
     *      -homeAddress is null
     */
    public void register(String name, EmailAddressType emailAddress, HomeAddressType homeAddress);

    /**
     *
     * @param clientEmailAddress
     * @param clientHomeAddress
     * @throws ShopException if ...
     *      - emailAddress is null
     *      -client with the given email address does not exist
     *      -homeAddress is null
     */
    public void changeAddress(EmailAddressType clientEmailAddress, HomeAddressType clientHomeAddress);

    /**
     *
     * @param clientEmailAddress
     * @return the client data
     * @throws ShopException if ...
     *         - emailAddress is null
     *         - the client with the given email address does not exist
     */
    public ClientType getClientData(EmailAddressType clientEmailAddress);

    /**
     * Clear all clients and all purchases and shopping baskets
     */
    public void deleteAllClients();

}
