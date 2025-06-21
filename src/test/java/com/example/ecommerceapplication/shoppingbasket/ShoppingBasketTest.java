package com.example.ecommerceapplication.shoppingbasket;

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

import java.util.Map;
import java.util.UUID;

import static com.example.ecommerceapplication.ClientTestHelper.CLIENT_EMAIL;
import static com.example.ecommerceapplication.FactoryMethodInvoker.instantiateEmailAddress;
import static com.example.ecommerceapplication.ThingAndStockTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ShoppingBasketTest {
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

    private EmailAddressType nonExistingEmailAddress = instantiateEmailAddress( "this@nononono.de" );

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
    public void testInvalidAddToShoppingBasket() {
        // given
        UUID nonExistentThingId = UUID.randomUUID();
        UUID thingId5 = (UUID) THING_DATA[5][0];
        UUID thingId0 = (UUID) THING_DATA[0][0];
        UUID thingId1 = (UUID) THING_DATA[1][0];
        UUID thingId2 = (UUID) THING_DATA[2][0];
        EmailAddressType clientEmailAddress0 = CLIENT_EMAIL[0];

        // when
        shoppingBasketUseCases.addThingToShoppingBasket(
                clientEmailAddress0, thingId2, 6 );
        shoppingBasketUseCases.addThingToShoppingBasket(
                clientEmailAddress0, thingId2, 13 );

        // then
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.addThingToShoppingBasket(
                        clientEmailAddress0, nonExistentThingId, 12 ) );
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.addThingToShoppingBasket(
                        nonExistingEmailAddress, thingId5, 12 ) );
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.addThingToShoppingBasket(
                        clientEmailAddress0, thingId5, -1 ) );
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.addThingToShoppingBasket(
                        clientEmailAddress0, thingId0, 1 ) );
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.addThingToShoppingBasket(
                        clientEmailAddress0, thingId1, 11 ) );
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.addThingToShoppingBasket(
                        clientEmailAddress0, thingId2, 2 ) );
    }


    @Test
    public void testInvalidRemoveFromShoppingBasket() {
        // given
        UUID nonExistentThingId = UUID.randomUUID();
        UUID thingId5 = (UUID) THING_DATA[5][0];
        UUID thingId0 = (UUID) THING_DATA[0][0];
        UUID thingId1 = (UUID) THING_DATA[1][0];
        UUID thingId2 = (UUID) THING_DATA[2][0];
        EmailAddressType clientEmailAddress0 = CLIENT_EMAIL[0];
        shoppingBasketUseCases.addThingToShoppingBasket(
                clientEmailAddress0, thingId1, 5 );
        shoppingBasketUseCases.addThingToShoppingBasket(
                clientEmailAddress0, thingId2, 15 );

        // when
        shoppingBasketUseCases.removeThingFromShoppingBasket(
                clientEmailAddress0, thingId1, 2 );
        shoppingBasketUseCases.removeThingFromShoppingBasket(
                clientEmailAddress0, thingId2, 4 );
        shoppingBasketUseCases.removeThingFromShoppingBasket(
                clientEmailAddress0, thingId2, 7 );

        // then
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.removeThingFromShoppingBasket(
                        clientEmailAddress0, nonExistentThingId, 12 ) );
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.removeThingFromShoppingBasket(
                        nonExistingEmailAddress, thingId5, 12 ) );
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.removeThingFromShoppingBasket(
                        clientEmailAddress0, thingId5, -1 ) );
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.removeThingFromShoppingBasket(
                        clientEmailAddress0, thingId0, 1 ) );
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.removeThingFromShoppingBasket(
                        clientEmailAddress0, thingId1, 4 ) );
        assertThrows( ShopException.class,
                () -> shoppingBasketUseCases.removeThingFromShoppingBasket(
                        clientEmailAddress0, thingId2, 5 ) );
    }



    @Test
    public void testAddRemoveThingsFromAndToShoppingBasket() {
        // given
        UUID thingId1 = (UUID) THING_DATA[1][0];
        UUID thingId2 = (UUID) THING_DATA[2][0];
        EmailAddressType clientEmailAddress3 = CLIENT_EMAIL[3];
        EmailAddressType clientEmailAddress5 = CLIENT_EMAIL[5];

        // when
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId1, 2 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId2, 3 );
        shoppingBasketUseCases.removeThingFromShoppingBasket( clientEmailAddress3, thingId1, 2 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId1, 1 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId2, 6 );
        // client3 has 1x thingId1 and 9x thingId2 in cart
        Map<UUID, Integer> cart3 = shoppingBasketUseCases.getShoppingBasketAsMap( clientEmailAddress3 );

        shoppingBasketUseCases.addThingToShoppingBasket(
                clientEmailAddress5, thingId1, 2 );
        shoppingBasketUseCases.addThingToShoppingBasket(
                clientEmailAddress5, thingId2, 8 );
        shoppingBasketUseCases.removeThingFromShoppingBasket(
                clientEmailAddress5, thingId1, 1 );
        shoppingBasketUseCases.addThingToShoppingBasket(
                clientEmailAddress5, thingId2, 2 );

        // client5 has 1x thingId1 and 10x thingId2 in cart
        Map<UUID, Integer> cart5 = shoppingBasketUseCases.getShoppingBasketAsMap( clientEmailAddress5 );
        int reservedStock1 = shoppingBasketUseCases.getReservedStockInShoppingBaskets( thingId1 );
        int reservedStock2 = shoppingBasketUseCases.getReservedStockInShoppingBaskets( thingId2 );

        // then
        assertEquals( 2, cart3.size() );
        assertEquals( 1, cart3.get( thingId1 ) );
        assertEquals( 9, cart3.get( thingId2 ) );

        assertEquals( 2, cart5.size() );
        assertEquals( 1, cart5.get( thingId1 ) );
        assertEquals( 10, cart5.get( thingId2 ) );

        assertEquals( 2, reservedStock1 );
        assertEquals( 19, reservedStock2 );
    }


    @Test
    public void testImpactOfStockCorrectionToOneShoppingBasket() {
        // given
        UUID thingId1 = (UUID) THING_DATA[1][0];
        UUID thingId2 = (UUID) THING_DATA[2][0];
        UUID thingId3 = (UUID) THING_DATA[3][0];
        UUID depotId0 = DEPOT_ID[0];
        EmailAddressType clientEmailAddress3 = CLIENT_EMAIL[3];

        // when
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId1, 6 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId2, 15 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId3, 1 );
        depotUseCases.changeStockTo( thingId1, 4 );
        depotUseCases.changeStockTo( thingId2, 16 );
        depotUseCases.changeStockTo( thingId3, 0 );
        Map<UUID, Integer> cart = shoppingBasketUseCases.getShoppingBasketAsMap( clientEmailAddress3 );
        int reservedStock1 = shoppingBasketUseCases.getReservedStockInShoppingBaskets( thingId1 );
        int reservedStock2 = shoppingBasketUseCases.getReservedStockInShoppingBaskets( thingId2 );
        int reservedStock3 = shoppingBasketUseCases.getReservedStockInShoppingBaskets( thingId3 );

        // then
        assertEquals( 4, cart.get( thingId1 ) );
        assertEquals( 15, cart.get( thingId2 ) );
        assertTrue( cart.get( thingId3 ) == null || cart.get( thingId3 ) == 0 );
        assertEquals( 4, reservedStock1 );
        assertEquals( 15, reservedStock2 );
        assertEquals( 0, reservedStock3 );
    }


    @Test
    public void testImpactOfStockCorrectionToSeveralShoppingBaskets() {
        // given
        UUID thingId2 = (UUID) THING_DATA[2][0];
        EmailAddressType clientEmailAddress3 = CLIENT_EMAIL[3];
        EmailAddressType clientEmailAddress6 = CLIENT_EMAIL[6];
        EmailAddressType clientEmailAddress9 = CLIENT_EMAIL[9];
        UUID depotId0 = DEPOT_ID[0];

        // when
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId2, 3 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress6, thingId2, 6 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress9, thingId2, 9 );
        depotUseCases.removeFromStock( thingId2, 2 );
        Map<UUID, Integer> cart31 = shoppingBasketUseCases.getShoppingBasketAsMap( clientEmailAddress3 );
        Map<UUID, Integer> cart61 = shoppingBasketUseCases.getShoppingBasketAsMap( clientEmailAddress6 );
        Map<UUID, Integer> cart91 = shoppingBasketUseCases.getShoppingBasketAsMap( clientEmailAddress9 );
        int reservedStock21 = shoppingBasketUseCases.getReservedStockInShoppingBaskets( thingId2 );

        depotUseCases.removeFromStock( thingId2, 8 );
        Map<UUID, Integer> cart32 = shoppingBasketUseCases.getShoppingBasketAsMap( clientEmailAddress3 );
        Map<UUID, Integer> cart62 = shoppingBasketUseCases.getShoppingBasketAsMap( clientEmailAddress6 );
        Map<UUID, Integer> cart92 = shoppingBasketUseCases.getShoppingBasketAsMap( clientEmailAddress9 );
        int quantity32 = cart32.get( thingId2 ) == null ? 0 : cart32.get( thingId2 );
        int quantity62 = cart62.get( thingId2 ) == null ? 0 : cart62.get( thingId2 );
        int quantity92 = cart92.get( thingId2 ) == null ? 0 : cart92.get( thingId2 );
        int reservedStock22 = shoppingBasketUseCases.getReservedStockInShoppingBaskets( thingId2 );

        // then
        assertEquals( 3, cart31.get( thingId2 ) );
        assertEquals( 6, cart61.get( thingId2 ) );
        assertEquals( 9, cart91.get( thingId2 ) );
        assertEquals( 18, reservedStock21 );
        assertEquals( 10, reservedStock22 );
        assertEquals( reservedStock22, quantity32 + quantity62 + quantity92 );
    }


    @Test
    public void testShoppingBasketValue() {
        // given
        UUID thingId3 = (UUID) THING_DATA[3][0];
        UUID thingId6 = (UUID) THING_DATA[6][0];
        UUID thingId8 = (UUID) THING_DATA[8][0];
        MoneyType price3 = (MoneyType) THING_DATA[3][5];
        MoneyType price6 = (MoneyType) THING_DATA[6][5];
        MoneyType price8 = (MoneyType) THING_DATA[8][5];
        EmailAddressType clientEmailAddress3 = CLIENT_EMAIL[3];

        // when
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId3, 3 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId6, 2 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId8, 5 );
        // client3 has 3x thingId3, 2x thingId6 and 5x thingId8 in cart
        MoneyType cartValue = shoppingBasketUseCases.getShoppingBasketAsMoneyValue( clientEmailAddress3 );

        // then
        //assertEquals( 3 * price3 + 2 * price6 + 5 * price8, cartValue, 0.1f );
        assertEquals( price3.multiplyBy( 3 ).add( price6.multiplyBy( 2 ) ).add( price8.multiplyBy( 5 ) ),
                cartValue );
    }


    @Test
    public void testShoppingBasketValueInvalid() {
        // given
        // when
        // then
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.getShoppingBasketAsMoneyValue( nonExistingEmailAddress ) );
    }


    @Test
    public void testCheckout() {
        // given
        UUID thingId1 = (UUID) THING_DATA[1][0];
        UUID thingId2 = (UUID) THING_DATA[2][0];
        UUID thingId3 = (UUID) THING_DATA[3][0];
        int stock1before = THING_STOCK.get( thingId1 )[0];
        int stock2before = THING_STOCK.get( thingId2 )[0];
        int stock3before = THING_STOCK.get( thingId3 )[0];
        EmailAddressType clientEmailAddress3 = CLIENT_EMAIL[3];
        Map<UUID, Integer> purchaseHistoryBefore = purchaseUseCases.getPurchaseHistory( clientEmailAddress3 );

        // when
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId1,
                stock1before-2 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId2, 4 );
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress3, thingId3, 5 );
        shoppingBasketUseCases.checkout( clientEmailAddress3 );
        int stock1after = depotUseCases.getAvailableStock( thingId1 );
        int stock2after = depotUseCases.getAvailableStock( thingId2 );
        int stock3after = depotUseCases.getAvailableStock( thingId3 );
        Map<UUID, Integer> purchaseHistoryAfter = purchaseUseCases.getPurchaseHistory( clientEmailAddress3 );

        // then
        assertEquals( 0, purchaseHistoryBefore.size() );
        assertEquals( 2, stock1after );
        assertEquals( stock2before-4, stock2after );
        assertEquals( stock3before-5, stock3after );
        assertEquals( 3, purchaseHistoryAfter.size() );
        assertEquals( stock1before-2, purchaseHistoryAfter.get( thingId1 ) );
        assertEquals( 4, purchaseHistoryAfter.get( thingId2 ) );
        assertEquals( 5, purchaseHistoryAfter.get( thingId3 ) );
    }


    @Test
    public void testCheckoutInvalid() {
        // given
        EmailAddressType clientEmailAddress3 = CLIENT_EMAIL[3];
        EmailAddressType clientEmailAddress5 = CLIENT_EMAIL[5];
        UUID thingId2 = (UUID) THING_DATA[2][0];

        // when
        shoppingBasketUseCases.addThingToShoppingBasket( clientEmailAddress5, thingId2, 4 );
        shoppingBasketUseCases.removeThingFromShoppingBasket( clientEmailAddress5, thingId2, 4 );

        // then
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.checkout( nonExistingEmailAddress ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.checkout( clientEmailAddress3 ) );
        assertThrows( ShopException.class, () -> shoppingBasketUseCases.checkout( clientEmailAddress5 ) );
    }

}
