package com.example.ecommerceapplication.usecases.domainprimitivetypes;

public interface EmailAddressType {
    /**
     *
     * @return the email as a string
     */
    public String toString();

    /**
     *
     * @param domainString
     * @return the new email address with a new domain (part right to the "@" sign) but same identifyer
     * @throws ShopException if ...
     *          - domainString is null
     *          -the new email address would not be valid (according to off)
     */
    public EmailAddressType sameIdentifyerDifferentDomain( String domainString );

    /**
     *
     * @param identifyerString
     * @return new email address with a new identifyer (part left to the "@" sign)
     * @throws ShopException if ...
     *      - identifyerString is null
     *      - the new email address would not be valid (according to off)
     */
    public EmailAddressType sameDomainDifferentIdentifyer(String identifyerString );

    /**
     * @param emailAddressAsString
     *         Is a valid email if ...
     *         - it contains exactly one '@' character
     *         - left and right part before and after the '@' is not empty and contains
     *         at least one of these characters (A..Z, a..z, or 0..9) and must not contain any whitespace characters
     *            - the parts before and after the '@' may contain one or several '.' as separators
     *            - two '.' characters must not be directly next to each other (so "test@this..example.com" is invalid)
     *            - the part after the '@' must end with ".de", ".at", ".ch", ".com", ".org"
     *              (for simplicity we do not allow any other domains)
     *       @return a new EmailAddressType object matching the given email address
     *       @throws ShopException if ...
     *        - emailAddressAsString is null
     *           - emailAddressAsString is not a valid email address (see above)
     */
    //public static EmailAddressType of( String emailAddressAsString )
}
