package com.example.ecommerceapplication.domainprimitives;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.HomeAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.PostalCodeType;
import org.junit.jupiter.api.Test;

import static com.example.ecommerceapplication.FactoryMethodInvoker.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

public class HomeAddressTest {

    @Test
    public void testGetterHomeAddress() {
        // given
        String street = "Irgendeinestraße 42";
        String city = "Irgendeinestadt";
        PostalCodeType plz = instantiatePostalCode( "12345" );

        // when
        HomeAddressType instance = instantiateHomeAddress( street, city, plz );

        // then
        assertEquals( street, instance.getStreet() );
        assertEquals( city, instance.getCity() );
        assertEquals( plz, instance.getPostalCode() );
    }

    @Test
    public void testFactoryValidHomeAddress() {
        // given
        String street = "Irgendeinestraße 42";
        String city = "Irgendeinestadt";
        PostalCodeType plz = instantiatePostalCode( "12345" );

        // when
        // then
        assertDoesNotThrow( () -> instantiateHomeAddress( street, city, plz ) );
    }

    @Test
    public void testFactoryInvalidHomeAddress() {
        // given
        String street = "Irgendeinestraße 42";
        String city = "Irgendeinestadt";
        PostalCodeType plz = instantiatePostalCode( "12345" );

        // when
        // then
        assertThrows( ShopException.class, () -> instantiateHomeAddress( null, city, plz ) );
        assertThrows( ShopException.class, () -> instantiateHomeAddress( "", city, plz ) );
        assertThrows( ShopException.class, () -> instantiateHomeAddress( street, null, plz ) );
        assertThrows( ShopException.class, () -> instantiateHomeAddress( street, "", plz ) );
        assertThrows( ShopException.class, () -> instantiateHomeAddress( street, city, null ) );
    }

    @Test
    public void testEqualityHomeAddress() {
        // given
        String street = "Irgendeinestraße 42";
        String city = "Irgendeinestadt";
        PostalCodeType plz = instantiatePostalCode( "12345" );

        // when
        HomeAddressType instance1 = instantiateHomeAddress( street, city, plz );
        HomeAddressType instance2 = instantiateHomeAddress( street, city, plz );
        HomeAddressType instance3 = instantiateHomeAddress( "Anderestr. 12", city, plz );
        HomeAddressType instance4 = instantiateHomeAddress( street, "AndereStadt", plz );
        HomeAddressType instance5 = instantiateHomeAddress( street, city, instantiatePostalCode( "54321" ) );

        // then
        assertEquals( instance1, instance2 );
        assertNotEquals( instance1, instance3 );
        assertNotEquals( instance1, instance4 );
        assertNotEquals( instance1, instance5 );
    }

    @Test
    public void testImmutabilityHomeAddress() {
        // given
        String street = "Irgendeinestraße 42";
        String city = "Irgendeinestadt";
        PostalCodeType plz = instantiatePostalCode( "12345" );

        // when
        HomeAddressType instance = instantiateHomeAddress( street, city, plz );

        // then
        try {
            instance.getClass().getMethod( "setStreet", String.class );
            fail( "setStreet method should not exist" );
        } catch (NoSuchMethodException e) {
            // Success: the object is immutable
        }
        try {
            instance.getClass().getMethod( "setCity", String.class );
            fail( "setCity method should not exist" );

        } catch (NoSuchMethodException e) {
            // Success: the object is immutable
        }
        try {
            instance.getClass().getMethod( "setPostalCode", PostalCodeType.class );
            fail( "setPostalCode method should not exist" );
        } catch (NoSuchMethodException e) {
            // Success: the object is immutable
        }
    }
}
