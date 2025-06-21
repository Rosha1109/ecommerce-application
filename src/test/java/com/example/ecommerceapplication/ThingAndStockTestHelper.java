package com.example.ecommerceapplication;

import com.example.ecommerceapplication.usecases.DepotUseCases;
import com.example.ecommerceapplication.usecases.ThingCatalogUseCases;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@SuppressWarnings("PMD")
public class ThingAndStockTestHelper {
    private ThingCatalogUseCases thingCatalogUseCases;
    private DepotUseCases depotUseCases;
    private static Random random = new Random();

    public static final String EUR = "EUR";

    @Autowired
    public ThingAndStockTestHelper( ThingCatalogUseCases thingCatalogUseCases,
                                    DepotUseCases depotUseCases ) {
        this.thingCatalogUseCases = thingCatalogUseCases;
        this.depotUseCases = depotUseCases;
    }

    // Contains initialization data for thing instances, and their stock in the depot
    // Depot feature is only "prepared" here, not actually implemented. All stock is in
    // depot 0.

    public static final int THING_NUMOF = 15;
    public static final Object[][] THING_DATA = new Object[][]{
            {UUID.randomUUID(), "0-TCD-34 v2.1", "Universelles Verbindungsstück für den einfachen Hausgebrauch bei der Schnellmontage",
                    1.5f, FactoryMethodInvoker.instantiateMoney( 5.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 10.0f, EUR ), "0"},
            {UUID.randomUUID(), "1-EFG-56", "Hochleistungsfähiger Kondensator für elektronische Schaltungen",
                    0.3f, FactoryMethodInvoker.instantiateMoney( 2.5f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 4.0f, EUR ), "0"},
            {UUID.randomUUID(), "2-MNP-89ff", "Langlebiger und robuster Motor für industrielle Anwendungen",
                    7.2f, FactoryMethodInvoker.instantiateMoney( 50.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 80.0f, EUR ), "0"},
            {UUID.randomUUID(), "3-Gh-25", "Kompakter und leichter Akku für mobile Geräte",
                    null, FactoryMethodInvoker.instantiateMoney( 6.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 8.0f, EUR ), "0"},
            {UUID.randomUUID(), "4-MultiBeast2", "Vielseitiger Adapter für verschiedene Steckertypen",
                    null, FactoryMethodInvoker.instantiateMoney( 0.6f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 1.0f, EUR ), "0"},
            {UUID.randomUUID(), "5-ABC-99 v4.2", "Leistungsstarker Prozessor für Computer und Server",
                    1.0f, FactoryMethodInvoker.instantiateMoney( 150.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 250.0f, EUR ), "0"},
            {UUID.randomUUID(), "6-Stuko22", "Ersatzteil Spitze für Präzisionswerkzeug zum Löten und Schrauben",
                    null, FactoryMethodInvoker.instantiateMoney( 0.3f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 0.5f, EUR ), "0"},
            {UUID.randomUUID(), "7-Btt2-Ah67", "Kraftstoffeffiziente und umweltfreundliche Hochleistungsbatterie",
                    6.0f, FactoryMethodInvoker.instantiateMoney( 80.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 120.0f, EUR ), "0"},
            {UUID.randomUUID(), "8-JKL-67", "Wasserdichtes Gehäuse",
                    3.0f, FactoryMethodInvoker.instantiateMoney( 1.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 1.2f, EUR ), "0"},
            {UUID.randomUUID(), "9-MNO-55-33", "Modulares Netzteil für flexible Stromversorgung",
                    5.5f, FactoryMethodInvoker.instantiateMoney( 25.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 45.0f, EUR ), "0"},
            {UUID.randomUUID(), "10-PQR-80", "Effizienter Kühler für verbesserte Wärmeableitung",
                    4.0f, FactoryMethodInvoker.instantiateMoney( 20.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 35.0f, EUR ), "0"},
            {UUID.randomUUID(), "11-STU-11 Ld", "Hochwertiger Grafikchip für leistungsstarke PCs",
                    null, FactoryMethodInvoker.instantiateMoney( 200.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 350.0f, EUR ), "0"},
            {UUID.randomUUID(), "12-VWX-90 FastWupps", "Schnellladegerät für eine Vielzahl von Geräten",
                    null, FactoryMethodInvoker.instantiateMoney( 15.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 25.0f, EUR ), "0"},
            {UUID.randomUUID(), "13-YZZ-22 v1.8", "Leichter und stabiler Rahmen aus Aluminium",
                    3.5f, FactoryMethodInvoker.instantiateMoney( 60.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 100.0f, EUR ), "0"},
            {UUID.randomUUID(), "14-Nosedive", "Klammer zum Verschließen der Nase beim Tauchen",
                    5.0f, FactoryMethodInvoker.instantiateMoney( 2.0f, EUR ),
                    FactoryMethodInvoker.instantiateMoney( 5.0f, EUR ), "0"}
    };


    /**
     * Used for creating invalid input data for the things in the tests.
     */
    public enum InvalidReason {
        NULL, EMPTY;

        public Object getInvalidValue( Object originalValue ) {
            switch (this) {
                case NULL:
                    return null;
                case EMPTY:
                    return "";
                default:
                    return null;
            }
        }
    }

    public void addAllThings() {
        for ( Object[] thingData : THING_DATA ) {
            addThingDataToCatalog( thingData );
        }
    }

    public void addThingDataToCatalog( Object[] thingData ) {
        thingCatalogUseCases.addThingToCatalog( (UUID) thingData[0], (String) thingData[1], (String) thingData[2],
                (Float) thingData[3], (MoneyType) thingData[4], (MoneyType) thingData[5] );
    }

    public Object[] getThingDataInvalidAtIndex( int index, InvalidReason reason ) {
        Object[] thingData = THING_DATA[1];
        Object[] thingDataInvalid = new Object[thingData.length];
        System.arraycopy( thingData, 0, thingDataInvalid, 0, thingData.length );
        thingDataInvalid[index] = thingData[index].getClass().cast(
                reason.getInvalidValue( thingData[index] ) );
        return thingDataInvalid;
    }


    // These home addresss are used for the depots. The depot name will equal
    // the postal code of the home address. Their index number will be visible in the house number.
    // The depots are used as such:
    // - depot 0 is holds all things 0 - 6, and is used for all tests where multiple
    //   shipment units are irrelevant.
    // - depots 1 - 3 are used for the proximity tests, where you can deliver things 7 to
    //   to a client from the closest depot.
    // - depots 4 - 8 are used for the tests where you need to deliver things 8 - 14 in
    //   the most cost-efficient way, as multiple shipment units.
    // - depot 9 is empty.
    public final static int DEPOT_NUMOF = 1;
    public final static UUID[] DEPOT_ID = new UUID[DEPOT_NUMOF];

    public void addAllDepots() {
        for ( int i = 0; i < DEPOT_NUMOF; i++ ) {
            DEPOT_ID[i] = UUID.randomUUID();  // TODO in next milestone: actually create depots
        }
    }


    // These data structures contain the stock of the things in the depots.
    // THING_STOCK is a map thing id -> Integer[DEPOT_NUMOF].
    // The Integer[DEPOT_NUMOF] contains the stock of the thing in each of
    // the depots.
    //
    // The following rules apply:
    // - thing 0 is out of stock
    // - thing 1 / 2 / 3 have fixed quantities of 10 / 20 / 30 respectively, all ONLY in depot 0
    // - All other things have a random stock between 30 and 130
    // - All stock is in depot 0

    public static final Map<UUID, Integer[]> THING_STOCK = new HashMap<>();

    static {
        // things 0, 1, 2, and 3 have fixed quantities of 0, 10, 20, and 30.
        THING_STOCK.put( (UUID) THING_DATA[0][0],
                getStockDistribution( 0, (String) THING_DATA[0][6] ) );
        THING_STOCK.put( (UUID) THING_DATA[1][0],
                getStockDistribution( 10, (String) THING_DATA[1][6] ) );
        THING_STOCK.put( (UUID) THING_DATA[2][0],
                getStockDistribution( 20, (String) THING_DATA[2][6] ) );
        THING_STOCK.put( (UUID) THING_DATA[3][0],
                getStockDistribution( 30, (String) THING_DATA[3][6] ) );

        // The other things have a random stock between 30 and 130,
        for ( int i = 4; i < THING_NUMOF; i++ ) {
            Integer totalNumber = random.nextInt( 100 ) + 30;
            Integer[] stockInDepots =
                    getStockDistribution( totalNumber, (String) THING_DATA[i][6] );
            THING_STOCK.put( (UUID) THING_DATA[i][0], stockInDepots );
        }
    }


    /**
     * This method creates a random stock distribution for the given thing.
     *
     * @param totalNumber - the total number of things in the depots
     * @param zeroToNine  - a string with numbers between 0 and 9, representing the depots
     * @return an Integer array with the stock distribution for the thing, according to
     * the rules described above.
     */
    private static Integer[] getStockDistribution( Integer totalNumber, String zeroToNine ) {
        Integer[] stockInDepots = new Integer[DEPOT_NUMOF];
        for ( int i = 0; i < DEPOT_NUMOF; i++ ) stockInDepots[i] = 0;
        if ( zeroToNine.length() == 1 ) {
            stockInDepots[0] = totalNumber;
            return stockInDepots;
        }
        Set<Integer> depotIndices = getDepotIndices( zeroToNine );
        for ( Integer i = 0; i < DEPOT_NUMOF; i++ ) {
            if ( depotIndices.contains( i ) ) {
                stockInDepots[i] = 3;
            } else {
                stockInDepots[i] = 0;
            }
        }
        stockInDepots[DEPOT_NUMOF - 1] =
                totalNumber - 3 * ( DEPOT_NUMOF - 1 );
        return stockInDepots;
    }


    private static Set<Integer> getDepotIndices( String zeroToNine ) {
        Set<Integer> depotIndices = new HashSet<>();
        for ( int i = 0; i < zeroToNine.length(); i++ ) {
            depotIndices.add( Integer.parseInt( zeroToNine.substring( i, i + 1 ) ) );
        }
        return depotIndices;
    }


    public void addAllStock() {
        for ( Object[] thingData : THING_DATA ) {
            Integer[] stockInDepots =
                    THING_STOCK.get( thingData[0] );
            for ( int iDepot = 0; iDepot < DEPOT_NUMOF; iDepot++ ) {
                if ( stockInDepots[iDepot] > 0 )
                    depotUseCases.addToStock(
                            (UUID) thingData[0],
                            stockInDepots[iDepot] );
            }
        }
    }
}
