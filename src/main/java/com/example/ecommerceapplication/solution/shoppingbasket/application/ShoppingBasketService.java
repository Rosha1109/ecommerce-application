package com.example.ecommerceapplication.solution.shoppingbasket.application;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.domainprimitives.Money;
import com.example.ecommerceapplication.solution.client.domain.Client;
import com.example.ecommerceapplication.solution.client.domain.ClientRepository;
import com.example.ecommerceapplication.solution.purchase.domain.Purchase;
import com.example.ecommerceapplication.solution.purchase.domain.PurchaseRepository;
import com.example.ecommerceapplication.solution.shoppingbasket.domain.ShoppingBasket;
import com.example.ecommerceapplication.solution.shoppingbasket.domain.ShoppingBasketRepository;
import com.example.ecommerceapplication.solution.thing.domain.Thing;
import com.example.ecommerceapplication.solution.thing.domain.ThingRepository;
import com.example.ecommerceapplication.usecases.ShoppingBasketUseCases;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class ShoppingBasketService implements ShoppingBasketUseCases {
    @Autowired
    private ShoppingBasketRepository shoppingBasketRepository;
    @Autowired
    private ThingRepository thingRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Override
    public void addThingToShoppingBasket(EmailAddressType clientEmailAddress, UUID thingId, int quantity) {
        if(!clientRepository.existsByEmailAddress(clientEmailAddress))throw new ShopException("Client not found");
        if(!thingRepository.existsByThingId(thingId))throw new ShopException("Thing not found");
        if( quantity <= 0 )throw new ShopException("Quantity cannot be less than zero");
        Thing thing = thingRepository.findByThingId(thingId);
        if(quantity > thing.getThingStock())throw new ShopException("Quantity cannot be greater than thingStock");
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        int stockPlusReserved = thing.getThingStock();
        if(client !=null && shoppingBasketRepository.findByClient(client).getBasket() !=null ){
            stockPlusReserved += shoppingBasketRepository.findByClient(client).getBasket().get(thingId);
        }
        if(quantity > stockPlusReserved)throw new ShopException("Quantity caanot be greater than thingStock + reserved things");
        ShoppingBasket shoppingBasket = shoppingBasketRepository.findByClient(client);
        if(shoppingBasket == null){
            shoppingBasket = new ShoppingBasket();
            shoppingBasket.setClient(client);
        }
        int newQuantity;
        if(shoppingBasket.getBasket().get(thingId)==null){
            newQuantity=quantity;
        }else {
             newQuantity = quantity + shoppingBasket.getBasket().get(thingId);
        }
        shoppingBasket.getBasket().put(thingId, newQuantity);
        shoppingBasketRepository.save(shoppingBasket);
    }

    @Override
    public void removeThingFromShoppingBasket(EmailAddressType clientEmailAddress, UUID thingId, int quantity) {
        if(clientEmailAddress== null||!clientRepository.existsByEmailAddress(clientEmailAddress))throw new ShopException("Client not found");
        if(quantity <= 0 )throw new ShopException("Quantity cannot be less than zero");
        Thing thing = thingRepository.findByThingId(thingId);
        if(thing.getThingStock() < quantity)throw new ShopException("Quantity cannot be larger than thingStock");
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        ShoppingBasket shoppingBasket = shoppingBasketRepository.findByClient(client);
        int currentQuantity = shoppingBasket.getBasket().get(thingId);
        if(currentQuantity == quantity){shoppingBasket.getBasket().remove(thingId);}
        else{
            shoppingBasket.getBasket().put(thingId, currentQuantity - quantity);
        }
        shoppingBasketRepository.save(shoppingBasket);

    }

    @Override
    public Map<UUID, Integer> getShoppingBasketAsMap(EmailAddressType clientEmailAddress) {
        if(clientEmailAddress==null ||!clientRepository.existsByEmailAddress(clientEmailAddress))throw new ShopException("Client not found");
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        return shoppingBasketRepository.findByClient(client).getBasket();
    }

    @Override
    public MoneyType getShoppingBasketAsMoneyValue(EmailAddressType clientEmailAddress) {
        Map<UUID,Integer> basket = getShoppingBasketAsMap(clientEmailAddress);
        Money sum = new Money();
        for(Map.Entry<UUID,Integer> entry :basket.entrySet())
        {
            UUID thingId = entry.getKey();
            Integer quantity = entry.getValue();
            Thing thing = thingRepository.findByThingId(thingId);
            sum.add(thing.getSellPrice());
        }
        return sum;
    }

    @Override
    public int getReservedStockInShoppingBaskets(UUID thingId) {
        if(thingId == null || !thingRepository.existsByThingId(thingId))throw new ShopException("Thing not found");
        int reservedThingQuantity = 0;
        for(ShoppingBasket basket : shoppingBasketRepository.findAll()){
            if(basket.getBasket().get(thingId) != null){
                reservedThingQuantity += basket.getBasket().get(thingId);
            }
        }
        return reservedThingQuantity;
    }

    @Override
    public boolean isEmpty(EmailAddressType clientEmailAddress) {
        if(clientEmailAddress==null ||!clientRepository.existsByEmailAddress(clientEmailAddress))throw new ShopException("Client not found");
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        return shoppingBasketRepository.findByClient(client).getBasket().isEmpty();
    }

    @Override
    public UUID checkout(EmailAddressType clientEmailAddress) {
        if( clientEmailAddress==null )throw new ShopException("Email address cannot be null");
        if( !clientRepository.existsByEmailAddress(clientEmailAddress) )throw new ShopException("Client not found");
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        if(isEmpty(clientEmailAddress))throw new ShopException("Client not found");
        ShoppingBasket clientsBasket = shoppingBasketRepository.findByClient(client);
        Purchase purchase = purchaseRepository.findByClient(client);
        if(purchase==null){
            purchase = new Purchase();
            purchase.setClient(client);
        }
        for(Map.Entry<UUID,Integer> entry : clientsBasket.getBasket().entrySet()){
            UUID thingId = entry.getKey();
            Integer quantity = entry.getValue();
            //purchase the baskets content
            purchase.getPurchaseHistory().put(thingId, quantity);
            Thing thing = thingRepository.findByThingId(thingId);
            thing.setThingStock(thing.getThingStock() - quantity);
            thingRepository.save(thing);
        }
        clientsBasket.getBasket().clear();
        shoppingBasketRepository.save(clientsBasket);
        purchaseRepository.save(purchase);
        return purchase.getId();
    }

    @Override
    public void emptyAllShoppingBaskets() {
        shoppingBasketRepository.deleteAll();
    }
}
