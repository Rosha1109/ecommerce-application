package com.example.ecommerceapplication;

import com.example.ecommerceapplication.usecases.ClientType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.HomeAddressType;
import lombok.Setter;

import java.util.Objects;

@Setter
public class MockClient implements ClientType {
    private String name;
    private EmailAddressType emailAddress;
    private HomeAddressType homeAddress;


    public MockClient( String name, EmailAddressType emailAddress, HomeAddressType homeAddress) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.homeAddress = homeAddress;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EmailAddressType getEmailAddress() {
        return emailAddress;
    }

    @Override
    public HomeAddressType getHomeAddress() {
        return homeAddress;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !( o instanceof MockClient ) ) return false;
        MockClient that = (MockClient) o;
        return Objects.equals( getName(), that.getName() ) && Objects.equals( emailAddress, that.emailAddress ) && Objects.equals( homeAddress, that.homeAddress );
    }

    @Override
    public int hashCode() {
        return Objects.hash( getName(), emailAddress, homeAddress );
    }
}
