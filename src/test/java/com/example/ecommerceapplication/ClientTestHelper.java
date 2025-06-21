package com.example.ecommerceapplication;

import com.example.ecommerceapplication.usecases.ClientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.ecommerceapplication.usecases.ClientRegistrationUseCases;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.HomeAddressType;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Component
@SuppressWarnings("PMD")
public class ClientTestHelper {

    private ClientRegistrationUseCases clientRegistrationUseCases;
    private Random random = new Random();

    public static final String EUR = "EUR";

    @Autowired
    public ClientTestHelper( ClientRegistrationUseCases clientRegistrationUseCases ) {
        this.clientRegistrationUseCases = clientRegistrationUseCases;
    }

    public final static String[] CLIENT_NAME = new String[]{
            "Max Müller",
            "Sophie Schmitz",
            "Irene Mihalic",
            "Emilia Fischer",
            "Filiz Polat",
            "Lina Wagner",
            "Leon Becker",
            "Agnieszka Kalterer",
            "Felix Bauer",
            "Lara Schulz"
    };

    public final static EmailAddressType[] CLIENT_EMAIL = new EmailAddressType[]{
            FactoryMethodInvoker.instantiateEmailAddress( "99Z@example.com" ),
            FactoryMethodInvoker.instantiateEmailAddress( "a@4.com" ),
            FactoryMethodInvoker.instantiateEmailAddress( "irene@wearefreedomnow.com" ),
            FactoryMethodInvoker.instantiateEmailAddress( "emilia.fischer@example.com" ),
            FactoryMethodInvoker.instantiateEmailAddress( "j877d3@example.this.com" ),
            FactoryMethodInvoker.instantiateEmailAddress( "lina.marie.wagner@example.com" ),
            FactoryMethodInvoker.instantiateEmailAddress( "0.1.2.3.4.5.6.7.8.becker@example.com" ),
            FactoryMethodInvoker.instantiateEmailAddress( "agna@here.ch" ),
            FactoryMethodInvoker.instantiateEmailAddress( "felix.bauer@example.org" ),
            FactoryMethodInvoker.instantiateEmailAddress( "lara.schulz@example.at" )
    };

    // The following array is used to create a list of homeAddresss for the clients.
    // The indices are coded into the house number.
    // - Persons 0 - 5 are used for proximity tests with one shipment unit.
    public final static HomeAddressType[] CLIENT_ADDRESS = new HomeAddressType[]{
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Marktstraße 0", "Viertelstadt",
                    FactoryMethodInvoker.instantiatePostalCode( "02314" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Hauptstraße 1", "Viertelstadt",
                    FactoryMethodInvoker.instantiatePostalCode( "02368" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Kirchplatz 2", "Niemandstown",
                    FactoryMethodInvoker.instantiatePostalCode( "12345" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Schulstraße 3", "Geisterhausen",
                    FactoryMethodInvoker.instantiatePostalCode( "31463" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Rosenweg 4", "Kuhhausen",
                    FactoryMethodInvoker.instantiatePostalCode( "72162" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Wiesenstraße 5", "Waldschenkensdorf",
                    FactoryMethodInvoker.instantiatePostalCode( "82195" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Mühlenweg 6", "Köln",
                    FactoryMethodInvoker.instantiatePostalCode( "50667" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Goethestraße 7", "Frankfurt am Main",
                    FactoryMethodInvoker.instantiatePostalCode( "60311" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Dorfstraße 8", "Stuttgart",
                    FactoryMethodInvoker.instantiatePostalCode( "70173" ) ),
            FactoryMethodInvoker.instantiateHomeAddress(
                    "Bahnhofstraße 9", "München",
                    FactoryMethodInvoker.instantiatePostalCode( "80331" ) )
    };

    public final static ClientType[] mockClients;

    static {
        mockClients = new ClientType[CLIENT_NAME.length];
        for ( int i = 0; i < CLIENT_NAME.length; i++ ) {
            mockClients[i] = new MockClient(
                    CLIENT_NAME[i], CLIENT_EMAIL[i], CLIENT_ADDRESS[i] );
        }
        // TODO - remove and replace by current _norepo, in M3
        assertThrows( ShopException.class, () -> {
            new MockClient( "No one",
                    FactoryMethodInvoker.instantiateEmailAddress( null ),
                    CLIENT_ADDRESS[0] ); }, "Check your email address validation!"  );
        assertThrows( ShopException.class, () -> {
            new MockClient( "No one",
                    FactoryMethodInvoker.instantiateEmailAddress( "Max..Gideon.Hammer@example.com" ),
                    CLIENT_ADDRESS[0] ); }, "Check your email address validation!" );
        assertThrows( ShopException.class, () -> {
            new MockClient( "No one",
                    FactoryMethodInvoker.instantiateEmailAddress( "test@example.42" ),
                    CLIENT_ADDRESS[0] ); }, "Check your email address validation!"  );
        assertThrows( ShopException.class, () -> {
            new MockClient( "No one",
                    FactoryMethodInvoker.instantiateEmailAddress( "test@" ),
                    CLIENT_ADDRESS[0] ); }, "Check your email address validation!"  );
    }


    public void registerAllClients() {
        for ( int i = 0; i < CLIENT_NAME.length; i++ ) {
            registerClient( CLIENT_NAME[i], CLIENT_EMAIL[i], CLIENT_ADDRESS[i] );
        }
    }

    public void registerClient( String name, EmailAddressType emailAddress, HomeAddressType homeAddress ) {
        clientRegistrationUseCases.register( name, emailAddress, homeAddress );
    }


}
