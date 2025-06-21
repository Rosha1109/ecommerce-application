package com.example.ecommerceapplication.usecases;



import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;

import java.util.Map;
import java.util.UUID;

/**
 * This interface contains methods needed in the context of use cases handling the shopping basket of
 * a cient
 */
public interface ShoppingBasketUseCases {

    /**
     * Adds a things to the shopping basket of a client
     * @param clientEmailAddress
     * @param thingId
     * @param quantity
     * @throws ShopException if ...
     *          - the client the with the given emailAddress does not exist,
     *          - the thing does not exist,
     *          - the quantity <= 0,
     *          - the thing is not available in the requested quantity
     */
    public void addThingToShoppingBasket(EmailAddressType clientEmailAddress, UUID thingId, int quantity);

    /**
     * Removes a thing from the shopping basket of a client
     * @param clientEmailAddress
     * @param thingId
     * @param quantity
     * @throws  ShopException if ...
     *        - clientEmailAddress is null,
     *        - the client with the given emailAddrress does not exist,
     *        - the quantity <= 0
     *        - the thing is not in the shopping basket in the requested quantity
     */
    public void removeThingFromShoppingBasket(EmailAddressType clientEmailAddress, UUID thingId, int quantity);
    /**
     * Returns a map showing which things are in the shopping basket of a client and how many of each thing
     *
     * @param clientEmailAddress
     * @return the shopping basket of the client (map is empty if the shopping basket is empty)
     * @throws ShopException if ...
     *      - clientEmailAddress is null,
     *      - the client with the given emailAddress does not exist
     */
    public Map<UUID, Integer> getShoppingBasketAsMap(EmailAddressType clientEmailAddress);


    /**
     * Returns the current value of all things in the shopping basket of a client
     *
     * @param clientEmailAddress
     * @return the value of shopping basket of the client
     * @throws "ShopException" if ...
     *      - clientEmailAddress is null,
     *      - the client with the given emailAddress does not exist
     */
    public MoneyType getShoppingBasketAsMoneyValue(EmailAddressType clientEmailAddress);


    /**
     * Get the number units of a specific thing that are currently reserved in the shopping baskets of all clients
     * @param thingId
     * @return the number of reserved things of that type in all shopping baskets
     * @throws "ShopException"
     *      - thingId is null
     *      - if the thing id does not exist
     */
    public int getReservedStockInShoppingBaskets(UUID thingId);


    /**
     * Checks if the shopping basket of a client is empty
     *
     * @param clientEmailAddress
     * @return true if the shopping basket is empty, false otherwise
     * @throws ShopException if ...
     *    - clientEmailAddress is null
     *    - the client with the given email address does not exist
     */
    public boolean isEmpty(EmailAddressType clientEmailAddress);


    /**
     * Checks out the shopping basket of a client. This means that the things in the shopping basket
     * are removed from the stock. The shopping basket is emptied.
     *
     * @param clientEmailAddress
     * @return the id of the purchase that was created
     * @throws ShopException if ...
     *      - clientEmailAddress is null
     *      - the client with the given email address does not exist
     *      - the shopping basket is empty
     *      - the things in the shopping basket are not available in the requested quantity
     */
    public UUID checkout(EmailAddressType clientEmailAddress);


    /**
     * Empties all shopping baskets for all clients
     */
    public void emptyAllShoppingBaskets();


}
