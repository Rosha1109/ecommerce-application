package com.example.ecommerceapplication.domainprimitives;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.PostalCodeType;
import jakarta.persistence.Embeddable;
import lombok.*;



@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
@Setter(AccessLevel.PROTECTED)
public class PostalCode implements PostalCodeType {

   String postalCode;


    @Override
    public int distance(PostalCodeType otherPostalCode) {
        String myCode = this.postalCode;
        if (of(postalCode) == otherPostalCode) {
            return 0;
        }

        int distance = 0;
        for (int i = 0; i < this.postalCode.length(); i++) {
            if (this.postalCode.charAt(i) != otherPostalCode.toString().charAt(i)) {
                // Simple distance calculation considering positions and values
                int diff = Math.abs(this.postalCode.charAt(i) - otherPostalCode.toString().charAt(i));
                distance += (int) Math.pow(10, (4 - i)) * diff;
            }
        }
        return distance;
    }
    @Override
    public String toString(){
        return postalCode;
    }


    public static PostalCodeType of ( String postalCodeAsString){
      if(postalCodeAsString == null || postalCodeAsString.isEmpty()){
          throw new ShopException("");
      }

        if (!postalCodeAsString.matches("^\\d{5}$") || postalCodeAsString.endsWith("0000"))
            throw new ShopException("Invalid postal code format.");

        return new PostalCode(postalCodeAsString);
    }
}
