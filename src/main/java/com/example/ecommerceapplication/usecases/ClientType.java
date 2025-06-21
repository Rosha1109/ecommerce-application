package com.example.ecommerceapplication.usecases;

import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.HomeAddressType;

public interface ClientType {
    public String getName();
    public EmailAddressType getEmailAddress();
    public HomeAddressType getHomeAddress();
}
