package dev.azonov.giftservice.exceptions;

/**
 * Case then gift is not deserved by child
 */
public class GiftNotDeservedException extends RuntimeException {
    private static final String MESSAGE = "Gift for child %s is not deserved";

    public GiftNotDeservedException(String childFullName) {
        super(String.format(MESSAGE, childFullName));
    }
}
