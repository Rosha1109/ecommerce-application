package com.example.ecommerceapplication.purchase;

import com.example.ecommerceapplication.ClientTestHelper;
import com.example.ecommerceapplication.ThingAndStockTestHelper;
import com.example.ecommerceapplication.usecases.*;

import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

import static com.example.ecommerceapplication.ClientTestHelper.CLIENT_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class PurchaseTest {
    @Autowired
    private ClientRegistrationUseCases clientRegistrationUseCases;
    @Autowired
    private ShoppingBasketUseCases shoppingBasketUseCases;
    @Autowired
    private ThingCatalogUseCases thingCatalogUseCases;
    @Autowired
    private DepotUseCases depotUseCases;
    @Autowired
    private PurchaseUseCases purchaseUseCases;

    private ClientTestHelper clientTestHelper;
    private ThingAndStockTestHelper thingAndStockTestHelper;


    @BeforeEach
    public void setUp() {
        purchaseUseCases.deleteAllPurchases();
        shoppingBasketUseCases.emptyAllShoppingBaskets();
        clientRegistrationUseCases.deleteAllClients();
        thingCatalogUseCases.deleteThingCatalog();

        clientTestHelper = new ClientTestHelper( clientRegistrationUseCases );
        clientTestHelper.registerAllClients();

        thingAndStockTestHelper = new ThingAndStockTestHelper(
                thingCatalogUseCases, depotUseCases );
        thingAndStockTestHelper.addAllThings();
        thingAndStockTestHelper.addAllDepots();
        thingAndStockTestHelper.addAllStock();
    }


    @Test
    public void testpurchaseHistory() {
        // given
        UUID thingId1 = (UUID) thingAndStockTestHelper.THING_DATA[1][0];
        UUID thingId2 = (UUID) thingAndStockTestHelper.THING_DATA[2][0];
        EmailAddressType clientEmailAddress = CLIENT_EMAIL[7];
        Map<UUID, Integer> purchaseHistoryBefore = purchaseUseCases.getPurchaseHistory( clientEmailAddress );

        // when
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress, thingId1, 3 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress, thingId2, 2 );
        shoppingBasketUseCases.checkout( clientEmailAddress );
        Map<UUID, Integer> purchaseHistory1 = purchaseUseCases.getPurchaseHistory( clientEmailAddress );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress, thingId1, 6 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress, thingId2, 2 );
        shoppingBasketUseCases.checkout( clientEmailAddress );
        Map<UUID, Integer> purchaseHistory2 = purchaseUseCases.getPurchaseHistory( clientEmailAddress );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress, thingId1, 1 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress, thingId2, 6 );
        shoppingBasketUseCases.checkout( clientEmailAddress );
        Map<UUID, Integer> purchaseHistory3 = purchaseUseCases.getPurchaseHistory( clientEmailAddress );

        // then
        assertEquals( 0, purchaseHistoryBefore.size() );
        assertEquals( 2, purchaseHistory1.size() );
        assertEquals( 2, purchaseHistory2.size() );
        assertEquals( 2, purchaseHistory3.size() );
        assertEquals( 3, purchaseHistory1.get( thingId1 ) );
        assertEquals( 2, purchaseHistory1.get( thingId2 ) );
        assertEquals( 9, purchaseHistory2.get( thingId1 ) );
        assertEquals( 4, purchaseHistory2.get( thingId2 ) );
        assertEquals( 10, purchaseHistory3.get( thingId1 ) );
        assertEquals( 10, purchaseHistory3.get( thingId2 ) );
    }


    @Test
    public void testForEmptyPurchaseHistory() {
        // given
        EmailAddressType clientEmailAddress4 = CLIENT_EMAIL[4];

        // when
        // then
        Map<UUID, Integer> purchaseHistory = purchaseUseCases.getPurchaseHistory(
                clientEmailAddress4 );
        assertEquals( 0, purchaseHistory.size() );
    }
}
