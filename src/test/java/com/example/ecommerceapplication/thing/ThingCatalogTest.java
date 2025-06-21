package com.example.ecommerceapplication.thing;

import com.example.ecommerceapplication.ClientTestHelper;
import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.ThingAndStockTestHelper;
import com.example.ecommerceapplication.usecases.*;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.example.ecommerceapplication.ClientTestHelper.CLIENT_EMAIL;
import static com.example.ecommerceapplication.ThingAndStockTestHelper.DEPOT_ID;
import static com.example.ecommerceapplication.ThingAndStockTestHelper.InvalidReason.NULL;
import static com.example.ecommerceapplication.ThingAndStockTestHelper.InvalidReason.EMPTY;
import static com.example.ecommerceapplication.ThingAndStockTestHelper.THING_DATA;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ThingCatalogTest {
    @Autowired
    private ClientRegistrationUseCases clientRegistrationUseCases;
    @Autowired
    private ShoppingBasketUseCases shoppingBasketUseCases;
    @Autowired
    private PurchaseUseCases purchaseUseCases;
    @Autowired
    private ThingCatalogUseCases thingCatalogUseCases;
    @Autowired
    private DepotUseCases depotUseCases;

    private ClientTestHelper clientTestHelper;
    private ThingAndStockTestHelper thingAndStockTestHelper;


    @BeforeEach
    public void setUp() {
        purchaseUseCases.deleteAllPurchases();
        shoppingBasketUseCases.emptyAllShoppingBaskets();
        thingCatalogUseCases.deleteThingCatalog();

        clientTestHelper = new ClientTestHelper( clientRegistrationUseCases );
        clientTestHelper.registerAllClients();

        thingAndStockTestHelper = new ThingAndStockTestHelper(
                thingCatalogUseCases, depotUseCases );
        thingAndStockTestHelper.addAllThings();
        thingAndStockTestHelper.addAllDepots();
    }


    @Test
    public void testAddThingToCatalog() {
        // given
        // when
        MoneyType sellPrice = thingCatalogUseCases.getSellPrice( (UUID) THING_DATA[4][0] );

        // then
        assertEquals( THING_DATA[4][5], sellPrice );
    }


    @Test
    public void testAddThingWithInvalidData() {
        // given
        Object[][] invalidThingData = {
                thingAndStockTestHelper.getThingDataInvalidAtIndex( 0, NULL ),     // id
                thingAndStockTestHelper.getThingDataInvalidAtIndex( 1, NULL ),     // name
                thingAndStockTestHelper.getThingDataInvalidAtIndex( 1, EMPTY ),    // name
                thingAndStockTestHelper.getThingDataInvalidAtIndex( 2, NULL ),     // description
                thingAndStockTestHelper.getThingDataInvalidAtIndex( 2, EMPTY ),    // description
                thingAndStockTestHelper.getThingDataInvalidAtIndex( 4, NULL ),     // buyingPrice
                thingAndStockTestHelper.getThingDataInvalidAtIndex( 5, NULL ),     // sellPrice
        };

        // when
        // then
        for ( Object[] invalidThingDataInstance : invalidThingData ) {
            StringBuffer invalidDataString = new StringBuffer().append( invalidThingDataInstance[0] )
                    .append( ", " ).append( invalidThingDataInstance[1] ).append( ", " ).append( invalidThingDataInstance[2] )
                    .append( ", " ).append( invalidThingDataInstance[3] ).append( ", " ).append( invalidThingDataInstance[4] )
                    .append( ", " ).append( invalidThingDataInstance[5] );
            assertThrows( ShopException.class, () -> thingAndStockTestHelper.addThingDataToCatalog( invalidThingDataInstance ),
                    "Invalid data: " + invalidDataString.toString() );
        }
    }


    @Test
    public void testRemoveThingFromCatalog() {
        // given
        UUID thingId = (UUID) THING_DATA[4][0];

        // when
        assertDoesNotThrow( () -> thingCatalogUseCases.getSellPrice( thingId ) );
        thingCatalogUseCases.removeThingFromCatalog( thingId );

        // then
        assertThrows( ShopException.class, () ->
                thingCatalogUseCases.getSellPrice( thingId ) );
    }



    @Test
    public void testRemoveNonExistentThing() {
        // given
        UUID nonExistentThingId = UUID.randomUUID();

        // when
        // then
        assertThrows( ShopException.class,
                () -> thingCatalogUseCases.removeThingFromCatalog( nonExistentThingId ) );
    }



    @Test
    public void testRemoveThingThatIsInStock() {
        // given
        UUID thingId = (UUID) THING_DATA[4][0];
        UUID depotId0 = DEPOT_ID[0];
        depotUseCases.addToStock( thingId, 3 );

        // when
        // then
        assertThrows( ShopException.class,
                () -> thingCatalogUseCases.removeThingFromCatalog( thingId ) );
    }


    @Test
    public void testRemoveThingThatIsInShoppingBasketOrPurchase() {
        // given
        UUID thingId3 = (UUID) THING_DATA[3][0];
        UUID thingId4 = (UUID) THING_DATA[4][0];
        EmailAddressType clientEmailAddress3 = CLIENT_EMAIL[3];
        EmailAddressType clientEmailAddress4 = CLIENT_EMAIL[4];
        depotUseCases.addToStock( thingId3, 3 );
        depotUseCases.addToStock( thingId4, 4 );

        // when
        shoppingBasketUseCases.addThingToShoppingBasket(
                clientEmailAddress3, thingId3, 3 );
        shoppingBasketUseCases.addThingToShoppingBasket(
                clientEmailAddress4, thingId4, 4 );
        shoppingBasketUseCases.checkout( clientEmailAddress4 );

        // then
        assertThrows( ShopException.class,
                () -> thingCatalogUseCases.removeThingFromCatalog( thingId3 ) );
        assertThrows( ShopException.class,
                () -> thingCatalogUseCases.removeThingFromCatalog( thingId4 ) );
    }


    @Test
    public void testClearThingCatalog() {
        // given
        // when
        thingCatalogUseCases.deleteThingCatalog();

        // then
        assertThrows( ShopException.class,
                () -> thingCatalogUseCases.getSellPrice( (UUID) THING_DATA[4][0] ) );
    }

}
