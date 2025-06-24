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

import java.util.HashMap;
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
      Client client = clientRepository.findByEmailAddress(clientEmailAddress);
      if(client ==null)throw new ShopException("Client not found");
      if(quantity < 0) throw new ShopException("quantity cannot be negative");
      Map<UUID,Integer> newBasket = new HashMap<>();
      Thing thing = thingRepository.findByThingId(thingId);
      if(thing == null)throw new ShopException("Thing not found");
      if(thing.getThingStock() < quantity)throw new ShopException("Cannot add so much in the basket");
      ShoppingBasket basket = shoppingBasketRepository.findByClient(client);
      if(basket == null){
          basket = new ShoppingBasket();
          basket.setClient(client);
          basket.setBasket(newBasket);
      }else{
          newBasket = basket.getBasket();
      }

      if(basket.getBasket().containsKey(thingId)){
          Integer basketQuantity = basket.getBasket().get(thingId);
          basket.getBasket().put(thingId,quantity + basketQuantity);
      }else{
          newBasket.put(thingId,quantity);
          basket.setBasket(newBasket);
      }
      shoppingBasketRepository.save(basket);
      clientRepository.save(client);
      thing.setThingStock(thing.getThingStock() - quantity);
      thingRepository.save(thing);

    }

    @Override
    public void removeThingFromShoppingBasket(EmailAddressType clientEmailAddress, UUID thingId, int quantity) {
        if(clientEmailAddress == null) throw new ShopException("");
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        if(client==null)throw new ShopException("Client not found");
        if(quantity < 0)throw new ShopException("Cannot be negative");
        ShoppingBasket basket = shoppingBasketRepository.findByClient(client);
        if(basket==null || basket.getBasket()==null)throw new ShopException("Basket not found");
        if(!basket.getBasket().containsKey(thingId) || basket.getBasket().get(thingId) < quantity){
            throw new ShopException("Not removable");
        }
        basket.getBasket().compute(thingId, (k,preQuantity) ->preQuantity - quantity);
        shoppingBasketRepository.save(basket);
        Thing thing = thingRepository.findByThingId(thingId);
        thing.setThingStock(thing.getThingStock() - quantity);
        thingRepository.save(thing);
        clientRepository.save(client);

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
        float amount= 0;
        String currency="";

        for(Map.Entry<UUID,Integer> entry :basket.entrySet())
        {
            UUID thingId = entry.getKey();
            Integer quantity = entry.getValue();
            Thing thing = thingRepository.findByThingId(thingId);
            amount += quantity * thing.getSellPrice().getAmount();
            currency = thing.getSellPrice().getCurrency();

        }
        MoneyType sum = Money.of(amount,currency);
        return  sum;
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
        if(clientEmailAddress==null )throw new ShopException("Client email empty");
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        if(client == null)throw new ShopException("Client not found");
        ShoppingBasket basket = shoppingBasketRepository.findByClient(client);
        if(basket == null) {
            return false;
        }
        else{
            if(basket.getBasket()==null){
                return false;
            }else{
                return basket.getBasket().isEmpty();
            }
        }

    }

    @Override
    public UUID checkout(EmailAddressType clientEmailAddress) {
        if( clientEmailAddress==null )throw new ShopException("Email address cannot be null");
        if( !clientRepository.existsByEmailAddress(clientEmailAddress) )throw new ShopException("Client not found");
        Client client = clientRepository.findByEmailAddress(clientEmailAddress);
        if(isEmpty(clientEmailAddress))throw new ShopException("Client not found");
        ShoppingBasket clientsBasket = shoppingBasketRepository.findByClient(client);
        if(clientsBasket==null)throw new ShopException("No basket found");
        Purchase purchase = purchaseRepository.findByClient(client);
        if(purchase==null){
            purchase = new Purchase();
            purchase.setClient(client);
        }
        if(nothingToCheckout(clientsBasket.getBasket()))throw new ShopException("Nothing to checkout");
        for(Map.Entry<UUID,Integer> entry : clientsBasket.getBasket().entrySet()){
            UUID thingId = entry.getKey();
            Integer quantity = entry.getValue();
            if(!purchase.getPurchaseHistory().containsKey(thingId)) {
                purchase.getPurchaseHistory().put(thingId, quantity);
            }else{
                purchase.getPurchaseHistory().put(thingId, purchase.getPurchaseHistory().get(thingId) + quantity);
            }

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
    private boolean nothingToCheckout(Map<UUID,Integer> basket) {
        boolean nothingToCheckout = false;
        for(Map.Entry<UUID,Integer> entry : basket.entrySet()){
            if(entry.getValue() == 0){
                nothingToCheckout = true;
            }else{
                nothingToCheckout = false;
            }
        }
        return nothingToCheckout;
    }
}
