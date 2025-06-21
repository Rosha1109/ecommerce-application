package com.example.ecommerceapplication.usecases.domainprimitivetypes;

public interface PostalCodeType {
    /**
     * @return the postal code as a string
     */
    public String toString();

    /**
     * You will need some idea of "imprecise distance" between two postal codes for properly
     * implementing depots in your shopping platform. This method calculates such a
     * distance. You can decide for yourself what values you return, unless you comply
     * with the following rules.
     * - The return value is 0 if both postal codes are the same
     * - If not:
     *      - The distance is > 0 if both postal codes differ in the last digit, like 5673x
     *        and 5673y (with x != y). However, the exact numbers for x and y don't matter.
     *        So, 56733 and 56734 have the same distance as 56733 and 56739.
     *      - The distance grows if more digits (counted from the right side) differ.
     *        So, 5abcd and 5rstu have a larger distance than 53bcd and 53stu (if abcd
     *        and rstu are not the same). Again, the precise numbers don't matter. Therefore,
     *        53876 and 54876 have the same distance as 53876 and 57261.
     *      - However, the distance between 5abcd and 6rstu must be smaller than the one
     *        between 5abcd and 7rstu, and this distance in turn must be smaller than the
     *        one between 5abcd and 9rstu.
     *      - This last condition reflects the fact the first digits of a postal code marks a region
     *        that is (usually) next to the region for an adjacent number. I.e. the "4" region
     *        is next to the "5" number. Same applies to "0" and "9", they are also next to each
     *        other.
     * @param otherPostalCode
     * @return the calculated distance
     * @throws ShopException if otherPostalCode is null
     */
    public int distance( PostalCodeType otherPostalCode );


    /**
     * Unfortunately, Java interfaces cannot contain static methods. However, we expect the
     * implementing class to provide a static factory method (simply named "of(...)"),
     * which creates an postal code, given as a string.
     * We specify this factory method here as a comment, using the Javadoc documentation style.
     *
     * @param postalCodeAsString - the postal code as a string.
     *      We will use a much simplified validation method to check if the postal code is valid:
     *      - It must contain exactly 5 digits.
     *      - The last 4 digits must not be 0000 (i.e. 20000 is not a valid postal code, but 20001 is valid)
     * @return a new postal code object matching the given string
     * @throws ShopException if ...
     *      - postalCodeAsString is null
     *      - postalCodeAsString is not a valid postal code (see above)
     */
    // public static PostalCodeType of( String postalCodeAsString );
}
