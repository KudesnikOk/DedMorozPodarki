package dev.azonov.giftservice.exceptions;

/**
 * Thrown then kind of gift is not known
 */
public class GiftNotFoundException extends RuntimeException {
    private static final String message = "Gift with kind %s is not found";

    public GiftNotFoundException(String kind) {
        super(String.format(message, kind));
    }
}
