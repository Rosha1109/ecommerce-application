package com.example.ecommerceapplication.domainprimitives;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.PostalCodeType;
import org.junit.jupiter.api.Test;

import static com.example.ecommerceapplication.FactoryMethodInvoker.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostalCodeTest {
    @Test
    public void testToStringPostalCode() {
        // given
        String input = "12345";

        // when
        PostalCodeType instance = instantiatePostalCode( input );

        // then
        assertEquals( input, instance.toString() );
    }

    @Test
    public void testFactoryValidPostalCode() {
        // given
        // when
        // then
        assertDoesNotThrow( () -> instantiatePostalCode( "12345" ) );
    }

    @Test
    public void testFactoryInvalidPostalCode() {
        // given
        // when
        // then
        assertThrows( ShopException.class, () -> instantiatePostalCode( "123456" ) );
        assertThrows( ShopException.class, () -> instantiatePostalCode( "1234" ) );
        assertThrows( ShopException.class, () -> instantiatePostalCode( "20000" ) );
        assertThrows( ShopException.class, () -> instantiatePostalCode( null ) );
    }

    @Test
    public void testEqualityPostalCode() {
        // given
        // when
        PostalCodeType instance1 = instantiatePostalCode( "12345" );
        PostalCodeType instance2 = instantiatePostalCode( "12345" );
        PostalCodeType instance3 = instantiatePostalCode( "54321" );

        // then
        assertEquals( instance1, instance2 );
        assertNotEquals( instance1, instance3 );
    }

    @Test
    public void testImmutabilityPostalCode() {
        // given
        // when
        PostalCodeType instance = instantiatePostalCode( "12345" );

        // then
        try {
            instance.getClass().getMethod( "setpostalCode", String.class );
            fail( "setpostalCode method should not exist" );
        } catch (NoSuchMethodException e) {
            // Success: the object is immutable
        }
    }


    @Test
    public void smokeTestForDistance() {
        // given
        PostalCodeType instance1a = instantiatePostalCode( "12345" );
        PostalCodeType instance1b = instantiatePostalCode( "12345" );
        PostalCodeType instance2 = instantiatePostalCode( "12479" );
        PostalCodeType instance3 = instantiatePostalCode( "21456" );

        // when
        int distanceAB = instance1a.distance( instance1b );
        int distance12 = instance1a.distance( instance2 );
        int distance23 = instance2.distance( instance3 );

        // then
        assertEquals( 0, distanceAB );
        assertTrue( distance12 > 0 );
        assertTrue( distance23 > distance12 );
    }
}
