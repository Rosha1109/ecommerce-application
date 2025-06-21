package com.example.ecommerceapplication.domainprimitives;

import com.example.ecommerceapplication.ShopException;
import com.example.ecommerceapplication.usecases.domainprimitivetypes.EmailAddressType;
import jakarta.persistence.Embeddable;
import lombok.*;


@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter(AccessLevel.PROTECTED)
public class EmailAddress implements EmailAddressType {
   private String identifier;
   private String domain;



    @Override
    public EmailAddressType sameIdentifyerDifferentDomain(String domainString) {
        if (domainString == null) {
            throw new ShopException("Domain string is null");
        }

        return EmailAddress.of(this.identifier +"@" + domainString);
    }

    @Override
    public EmailAddressType sameDomainDifferentIdentifyer(String identifyerString) {
       if(identifyerString == null) {
           throw new ShopException("Identifiyer string is null");
       }
        String email = new EmailAddress(identifyerString,this.domain).toString();
        return EmailAddress.of(identifyerString + "@" + this.domain);
        }

    public static EmailAddressType of(String emailAddressAsString) {
        if (emailAddressAsString == null) {
            throw new ShopException("Email address string cannot be null");
        }
        if (!isValidEmailAddress(emailAddressAsString)) {
            throw new ShopException("Email address is not valid");
        }
        int atIndex = emailAddressAsString.indexOf("@");
        String identifier = emailAddressAsString.substring(0, atIndex);
        String domain = emailAddressAsString.substring(atIndex + 1);
        return new EmailAddress(identifier, domain);
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        if (!emailAddress.matches("^[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*\\.(de|at|ch|com|org)$")) {
            return false;
        }
        // Check for the single '@' requirement and string splitting validity is made redundant by the above regex.
        return true;
    }
    @Override
    public String toString(){
       return this.identifier + "@" + this.domain;
    }

    private static int countChars(String input){
       int count = 0;
       for ( int i = 0; i < input.length(); i++ ){
           if(input.charAt(i) == '@'){
               count++;
           }
       }
       return count;
   }
}
