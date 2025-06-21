package com.example.ecommerceapplication.domainprimitives;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.example.ecommerceapplication.FactoryMethodInvoker.*;
public class EmailAddressTest {
    @Test
    public void testToStringEmailAddress() {
        //given
        String input="test@example.com";
        //when
        EmailAddressType instance = instantiateEmailAddress( input );
        //then
        assertEquals( input, instance.toString() );
    }
    @Test
    public void testEqualityEmailAddress() {
        // given
        // when
        EmailAddressType instance1 = instantiateEmailAddress( "test@example.com" );
        EmailAddressType instance2 = instantiateEmailAddress( "test@example.com" );
        EmailAddressType instance3 = instantiateEmailAddress( "different@example.com" );

        // then
        assertEquals( instance1, instance2 );
        assertNotEquals( instance1, instance3 );
    }
    @Test
    public void testImmutabilityEmailAddress() {
        // given
        // when
        EmailAddressType instance = instantiateEmailAddress( "test@example.com" );

        // then
        try {
            instance.getClass().getMethod( "setEmailAddress", String.class );
            fail( "setEmailAddress method should not exist" );
        } catch (NoSuchMethodException e) {
            // Success: the object is immutable
        }
    }



    @Test
    public void smokeTestForCopyConstructors() {
        // given
        EmailAddressType emailAddress =
                instantiateEmailAddress( "peter.schmidt@web.de" );

        // when
        EmailAddressType emailAddressOtherIdentifyer =
                emailAddress.sameDomainDifferentIdentifyer( "p.e.t.e.r" );
        assertThrows( ShopException.class, () ->
                emailAddress.sameDomainDifferentIdentifyer( "p...eter" ) );
        EmailAddressType emailAddressOtherDomain =
                emailAddress.sameIdentifyerDifferentDomain( "web.ch" );
        assertThrows( ShopException.class, () ->
                emailAddress.sameIdentifyerDifferentDomain( "web.edu" ) );

        // then
        assertEquals( "p.e.t.e.r@web.de", emailAddressOtherIdentifyer.toString() );
        assertEquals( "peter.schmidt@web.ch", emailAddressOtherDomain.toString() );
    }
}
