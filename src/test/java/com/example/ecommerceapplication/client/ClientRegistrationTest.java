package com.example.ecommerceapplication.client;

import com.example.ecommerceapplication.ClientTestHelper;
import com.example.ecommerceapplication.FactoryMethodInvoker;
import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.usecases.ClientRegistrationUseCases;
import com.example.ecommerceapplication.usecases.ClientType;
import com.example.ecommerceapplication.usecases.PurchaseUseCases;
import com.example.ecommerceapplication.usecases.ShoppingBasketUseCases;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.ecommerceapplication.ClientTestHelper.*;
import org.junit.jupiter.api.Assertions;

import static com.example.ecommerceapplication.ClientTestHelper.*;
import static com.example.ecommerceapplication.FactoryMethodInvoker.instantiateEmailAddress;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ClientRegistrationTest {
    @Autowired
    private ClientRegistrationUseCases clientRegistrationUseCases;
    @Autowired
    private ShoppingBasketUseCases shoppingBasketUseCases;
    @Autowired
    private PurchaseUseCases purchaseUseCases;

    private EmailAddressType nonExistingEmailAddress;
    private ClientTestHelper clientTestHelper;

    @BeforeEach
    public void setUp() {
        purchaseUseCases.deleteAllPurchases();
        shoppingBasketUseCases.emptyAllShoppingBaskets();
        clientRegistrationUseCases.deleteAllClients();
        clientTestHelper = new ClientTestHelper(clientRegistrationUseCases );
        nonExistingEmailAddress = instantiateEmailAddress("this@nononono.de" );



    }
    @Test
    public void testAllClientsRegistered(){
        //given
        clientTestHelper.registerAllClients();

        //when
        ClientType client3 = clientRegistrationUseCases.getClientData( CLIENT_EMAIL[3] );

        //then
        Assertions.assertEquals( CLIENT_NAME[3], client3.getName() );
        Assertions.assertEquals( CLIENT_EMAIL[3], client3.getEmailAddress() );
        Assertions.assertEquals( CLIENT_ADDRESS[3], client3.getHomeAddress() );
    }
    @Test
    public void testRegisterClientWithDuplicateEmailAddress(){
        //given
        clientTestHelper.registerAllClients();
        //when
        //then
        Assertions.assertThrows(ShopException.class, () ->
                clientRegistrationUseCases.register("Some Customer",CLIENT_EMAIL[5],CLIENT_ADDRESS[5] ));
    }

    @Test
    public void testRegisterClientWithInvalidData(){
        //given
        //when
        //then
        assertThrows( ShopException.class, () ->
                clientRegistrationUseCases.register( null, CLIENT_EMAIL[5],
                        CLIENT_ADDRESS[5] ) );
        assertThrows( ShopException.class, () ->
                clientRegistrationUseCases.register( "", CLIENT_EMAIL[5],
                        CLIENT_ADDRESS[5] ) );
        assertThrows( ShopException.class, () ->
                clientRegistrationUseCases.register( CLIENT_NAME[5], null,
                        CLIENT_ADDRESS[5] ) );
        assertThrows( ShopException.class, () ->
                clientRegistrationUseCases.register( CLIENT_NAME[5], CLIENT_EMAIL[5],
                        null ) );
    }
    @Test
    public void testChangeAddressWithInvalidData() {
        // given
        clientTestHelper.registerAllClients();

        // when
        // then
        assertThrows( ShopException.class, () ->
                clientRegistrationUseCases.changeAddress( nonExistingEmailAddress, CLIENT_ADDRESS[7] ) );
        assertThrows( ShopException.class, () ->
                clientRegistrationUseCases.changeAddress( null, CLIENT_ADDRESS[7] ) );
        assertThrows( ShopException.class, () ->
                clientRegistrationUseCases.changeAddress( CLIENT_EMAIL[5], null ) );
    }
    @Test
    public void testGetDataForNonexistingEmailAddress() {
        // given
        clientTestHelper.registerAllClients();

        // when
        // then
        assertThrows( ShopException.class, () ->
                clientRegistrationUseCases.getClientData( nonExistingEmailAddress ) );
    }


    @Test
    public void testDeleteClientsNoMoreClients() {
        // given
        clientTestHelper.registerAllClients();

        // when
        clientRegistrationUseCases.deleteAllClients();

        // then
        assertThrows( ShopException.class, () -> clientRegistrationUseCases.getClientData( CLIENT_EMAIL[0] ) );
    }

}
