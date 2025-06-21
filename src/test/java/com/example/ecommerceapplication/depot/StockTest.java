package com.example.ecommerceapplication.depot;

import com.example.ecommerceapplication.ClientTestHelper;
import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.ThingAndStockTestHelper;
import static com.example.ecommerceapplication.ThingAndStockTestHelper.*;
import com.example.ecommerceapplication.usecases.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Checks the stock management across depots
 */
@SpringBootTest
@Transactional
public class StockTest {
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
    UUID nonExistingId1;
    UUID nonExistingId2;

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

        nonExistingId1 = UUID.randomUUID();
        nonExistingId2 = UUID.randomUUID();
    }


    @Test
    public void testAddToStock() {
        // given
        thingAndStockTestHelper.addAllStock();
        UUID thingId5 = (UUID) THING_DATA[5][0];

        // when
        int stock5before = depotUseCases.getAvailableStock( thingId5 );
        assertEquals( THING_STOCK.get( thingId5 )[0], stock5before );
        depotUseCases.addToStock( thingId5, 23 );
        int stock5after = depotUseCases.getAvailableStock( thingId5 );
        depotUseCases.addToStock( thingId5, 0 );
        int stock5after2 = depotUseCases.getAvailableStock( thingId5 );
        int stock5after3 = depotUseCases.getAvailableStock( thingId5 );

        // then
        assertEquals( stock5before + 23, stock5after );
        assertEquals( stock5after, stock5after2 );
        assertEquals( stock5after, stock5after3 );
    }


    @Test
    public void testInvalidAddToStock() {
        // given
        thingAndStockTestHelper.addAllStock();
        UUID thingId2 = (UUID) THING_DATA[2][0];

        // when
        // then
        assertThrows( ShopException.class,
                () -> depotUseCases.addToStock( nonExistingId1, 12 ) );
        assertThrows( ShopException.class,
                () -> depotUseCases.addToStock( thingId2, -1 ) );
    }


    @Test
    public void testRemoveFromStock() {
        // given
        thingAndStockTestHelper.addAllStock();
        UUID thingId5 = (UUID) THING_DATA[5][0];
        int stock5before = THING_STOCK.get( thingId5 )[0];
        UUID thingId6 = (UUID) THING_DATA[6][0];
        int stock6before = THING_STOCK.get( thingId6 )[0];
        UUID thingId0 = (UUID) THING_DATA[0][0];

        // when
        depotUseCases.removeFromStock( thingId5, 1 );
        int stock5after = depotUseCases.getAvailableStock( thingId5 );
        depotUseCases.removeFromStock( thingId0, 0 );
        int stock0after = depotUseCases.getAvailableStock( thingId0 );
        depotUseCases.removeFromStock( thingId6, stock6before );
        int stock6after = depotUseCases.getAvailableStock( thingId6 );
        int stock6after2 = depotUseCases.getAvailableStock( thingId6 );
        // then
        assertEquals( stock5before - 1, stock5after );
        assertEquals( 0, stock0after );
        assertEquals( 0, stock6after );
        assertEquals( stock6after, stock6after2 );
    }


    @Test
    public void testInvalidRemoveFromStock() {
        // given
        thingAndStockTestHelper.addAllStock();
        UUID thingId5 = (UUID) THING_DATA[5][0];
        int stock5before = THING_STOCK.get( thingId5 )[0];
        UUID thingId0 = (UUID) THING_DATA[0][0];

        // when
        // then
        assertThrows( ShopException.class,
                () -> depotUseCases.removeFromStock( nonExistingId1, 12 ) );
        assertThrows( ShopException.class,
                () -> depotUseCases.removeFromStock( thingId5, -1 ) );
        assertThrows( ShopException.class,
                () -> depotUseCases.removeFromStock(
                        thingId5, stock5before + 1 ) );
        assertThrows( ShopException.class,
                () -> depotUseCases.removeFromStock( thingId0, 1 ) );
    }


    @Test
    public void testChangeStock() {
        // given
        thingAndStockTestHelper.addAllStock();
        UUID thingId5 = (UUID) THING_DATA[5][0];

        // when
        depotUseCases.changeStockTo( thingId5, 111 );
        int stock10after = depotUseCases.getAvailableStock( thingId5 );

        // then
        assertEquals( 111, stock10after );
    }


    @Test
    public void testInvalidChangeStock() {
        // given
        thingAndStockTestHelper.addAllStock();
        UUID thingId6 = (UUID) THING_DATA[6][0];

        // when
        // then
        assertThrows( ShopException.class, () -> depotUseCases.changeStockTo(
                nonExistingId1, 12 ) );
        assertThrows( ShopException.class, () -> depotUseCases.changeStockTo(
                thingId6, -1 ) );
    }

}
