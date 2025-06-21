package com.example.ecommerceapplication.usecases;

import java.util.UUID;

/**
 * This interface takes care of all methods which handles the shop stock,
 *  for instance adding and removing things in the depot.
 */
public interface DepotUseCases {

    /**
     * Adds a certain quantity of a given thing to the stock
     * @param thingId
     * @param addedQuantity
     * @throws ShopException if...
     *      - thingId is null
     *      - the thing with that id does not exist in the ThingCatalog
     *      - addedQuantity <= 0
     */
    public void addToStock(UUID thingId, int addedQuantity);

    /**
     * Removes a certain amount of things from the stock
     * If new total quantity is lower than currently reserved things, than some of the things in the clients
     * shopping baskets are removed, due to some mistakes in the stock management
     * @param thingId
     * @param removedQuantity
     * @throws ShopException if...
     *      - thingId is null
     *      - thing with given id that does not exist
     *      - removedQuantity <= 0
     *      - removed quantity is greater than the current stock and the currently reserved things together
     */
    public void removeFromStock(UUID thingId, int removedQuantity);

    /**
     * Changes the total quantity of a given thing in the stock.
     * If the new total quantity is lower than the currently reserved things, some of currently reserved things
     * (in the clients' shopping baskets) are removed. This means that some of the reserved things are lost for
     * the client. (This is necessary because there probably was a mistake in the stock management, a mis-counting,
     * or some of the things were stolen from the depot, are broken, etc.)
     * @param thingId
     * @param newTotalQuantity
     * @throws ShopException if ...
     *      - thingId is null
     *      - the thing with that id does not exist
     *      - newTotalQuantity < 0
     */
    public void changeStockTo(UUID thingId, int newTotalQuantity);

    /**
     * Get current total stock of a given thing + currently reserved things in shopping baskets
     * @param thingId
     * @return the current total stock of the thing
     * @throws ShopException if ...
     *      - thingId is null
     *      - thing with that id does not exist
     */
    public int getAvailableStock(UUID thingId);
}
