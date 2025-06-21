package com.example.ecommerceapplication.usecases;

import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;

import java.util.Map;
import java.util.UUID;

/**
 * This interface contains methods needed in the context of the purchase history of a client
 */
public interface PurchaseUseCases {

    /**
     *
     * Returns a map of things and each quantity bought by a client
     * @param clientEmailAddress
     * @return the purchase History of the client as a map (empty map if the client has not bought anything)
     * @throws ShopException if ...
     *        - the email address is null
     *        - the client with the given email address does not exist
     */
    public Map<UUID, Integer> getPurchaseHistory(EmailAddressType clientEmailAddress);

    /**
     * Deletes all purchases in the system
     */
    public void deleteAllPurchases();
}
