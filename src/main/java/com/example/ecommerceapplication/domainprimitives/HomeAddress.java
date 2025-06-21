package com.example.ecommerceapplication.domainprimitives;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.HomeAddressType;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.PostalCodeType;
import jakarta.persistence.Embeddable;
import lombok.*;


@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PROTECTED)
public class HomeAddress implements HomeAddressType {
    private String street;
    private String city;
    private PostalCode postalCode; // Use PostalCode implementation provided

    public static HomeAddressType of(String street, String city, PostalCodeType postalCode) {
        if (street == null || street.isEmpty()) {
            throw new ShopException("Street cannot be null or empty.");
        }
        if (city == null || city.isEmpty()) {
            throw new ShopException("City cannot be null or empty.");
        }
        if (postalCode == null) {
            throw new ShopException("PostalCode cannot be null.");
        }
        // Ensure casting is safe since PostalCodeType is the interface PostalCode implements
        if (!(postalCode instanceof PostalCode)) {
            throw new ShopException("PostalCode must be an instance of the specific PostalCode class.");
        }
        return new HomeAddress(street, city, (PostalCode) postalCode);

    }

    @Override
    public String getStreet() {
        return this.street;
    }

    @Override
    public String getCity() {
        if(this.city == null){
            return null;
        }else {
            return this.city;
        }

    }

    @Override
    public PostalCode getPostalCode() {
        if (postalCode == null) {
            return null;
        } else {
            return this.postalCode;
        }
    }
}