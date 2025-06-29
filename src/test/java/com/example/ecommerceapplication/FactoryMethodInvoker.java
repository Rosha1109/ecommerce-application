package com.example.ecommerceapplication;

import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.HomeAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.MoneyType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.PostalCodeType;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactoryMethodInvoker {

    public static EmailAddressType instantiateEmailAddress(String emailAddressAsString ) {
        Method factoryMethod = null;
        try {
            Class<?> interfaceClass = Class.forName(
                    "com.example.ecommerceapplication.domainprimitives.EmailAddress" );
            Class<?> implementingClass = findImplementation( interfaceClass );

            factoryMethod = implementingClass.getDeclaredMethod( "of", String.class );
            assertNotNull( factoryMethod );
            int modifiers = factoryMethod.getModifiers();
            assertTrue( Modifier.isStatic( modifiers ), "The method 'of' should be static" );
        } catch (Exception e) {
            fail( "Failed to find implementation for EmailAddressType", e );
        }

        Object instance = null;
        try {
            instance = factoryMethod.invoke( null, emailAddressAsString );
        } catch (Exception e) {
            if ( e instanceof InvocationTargetException && ( (InvocationTargetException) e ).getTargetException()
                    instanceof ShopException ) throw new ShopException( e.getMessage() );
            fail( "Failed to invoke factory method 'of' for '" + emailAddressAsString + "'", e );
        }
        assertNotNull( instance );
        return (EmailAddressType) instance;
    }


    public static HomeAddressType instantiateHomeAddress(
            String street, String city, PostalCodeType postalCode ) {
        Method factoryMethod = null;
        try {
            Class<?> interfaceClass = Class.forName(
                    "com.example.ecommerceapplication.domainprimitives.HomeAddress" );
            Class<?> implementingClass = findImplementation( interfaceClass );

            factoryMethod = implementingClass.getDeclaredMethod( "of",
                    String.class, String.class, PostalCodeType.class );
            assertNotNull( factoryMethod );
            int modifiers = factoryMethod.getModifiers();
            assertTrue( Modifier.isStatic( modifiers ), "The method 'of' should be static" );
        } catch (Exception e) {
            fail( "Failed to find implementation for HomeAddressType", e );
        }

        Object instance = null;
        try {
            instance = factoryMethod.invoke( null, street, city, postalCode );
        } catch (Exception e) {
            if ( e instanceof InvocationTargetException && ( (InvocationTargetException) e ).getTargetException()
                    instanceof ShopException ) throw new ShopException( e.getMessage() );
            fail( "Failed to invoke factory method 'of' for '" +
                    street + "', '" + city + "', '" + postalCode + "'", e );
        }
        assertNotNull( instance );
        return (HomeAddressType) instance;
    }


    public static PostalCodeType instantiatePostalCode( String postalCodeAsString ) {
        Method factoryMethod = null;
        try {
            Class<?> interfaceClass = Class.forName(
                    "com.example.ecommerceapplication.domainprimitives.PostalCode" );
            Class<?> implementingClass = findImplementation( interfaceClass );

            factoryMethod = implementingClass.getDeclaredMethod( "of", String.class );
            assertNotNull( factoryMethod );
            int modifiers = factoryMethod.getModifiers();
            assertTrue( Modifier.isStatic( modifiers ), "The method 'of' should be static" );
        } catch (Exception e) {
            fail( "Failed to find implementation for PostalCodeType", e );
        }

        Object instance = null;
        try {
            instance = factoryMethod.invoke( null, postalCodeAsString );
        } catch (Exception e) {
            if ( e instanceof InvocationTargetException && ( (InvocationTargetException) e ).getTargetException()
                    instanceof ShopException ) throw new ShopException( e.getMessage() );
            fail( "Failed to invoke factory method 'of' for '" + postalCodeAsString + "'", e );
        }
        assertNotNull( instance );
        return (PostalCodeType) instance;
    }


    public static MoneyType instantiateMoney(Float amount, String currency ) {
        Method factoryMethod = null;
        try {
            Class<?> interfaceClass = Class.forName(
                    "com.example.ecommerceapplication.domainprimitives.Money" );
            Class<?> implementingClass = findImplementation( interfaceClass );

            factoryMethod = implementingClass.getDeclaredMethod( "of", Float.class, String.class );
            assertNotNull( factoryMethod );
            int modifiers = factoryMethod.getModifiers();
            assertTrue( Modifier.isStatic( modifiers ), "The method 'of' should be static" );
        } catch (Exception e) {
            fail( "Failed to find implementation for MoneyType", e );
        }

        Object instance = null;
        try {
            instance = factoryMethod.invoke( null, amount, currency );
        } catch (Exception e) {
            if ( e instanceof InvocationTargetException && ( (InvocationTargetException) e ).getTargetException()
                    instanceof ShopException ) throw new ShopException( e.getMessage() );
            fail( "Failed to invoke factory method 'of' for '" + amount + "', '" + currency + "'", e );
        }
        assertNotNull( instance );
        return (MoneyType) instance;
    }


    private static Class<?> findImplementation( Class<?> interfaceClass ) {
        Method factoryMethod = null;
        try {
            String packageName = "com.example.ecommerceapplication.domainprimitives";
            Class<?> implementingClass = findImplementingClass( packageName, interfaceClass );
            assertNotNull( implementingClass );
            return implementingClass;
        } catch (Exception e) {
            fail( "Cannot find implementation for " + interfaceClass.getSimpleName(), e );
            return null; // This line will never be reached, but it's necessary to satisfy the compiler
        }
    }


    private static Class<?> findImplementingClass( String packageName, Class<?> interfaceClass )
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace( '.', '/' );
        Enumeration<URL> resources = classLoader.getResources( path );
        int count = 0;
        Class<?> result = null;
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File( URLDecoder.decode( resource.getFile(), StandardCharsets.UTF_8 ) );
            File[] files = directory.listFiles();
            for ( File file : files ) {
                if ( file.getName().endsWith( ".class" ) ) {
                    String className = packageName + '.' + file.getName().substring( 0, file.getName().length() - 6 );
                    Class<?> clazz = Class.forName( className );
                    if ( interfaceClass.isAssignableFrom( clazz ) && !clazz.isInterface() && !Modifier.isAbstract( clazz.getModifiers() ) ) {
                        count++;
                        result = clazz;
                    }
                }
            }
        }
        assertEquals( 1, count, "There should be exactly one implementing class" );
        return result;
    }
}

