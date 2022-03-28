package dev.azonov.giftservice.exceptions;

import org.springframework.util.StringUtils;

/**
 * Case then gift is not deserved by child
 */
public class GiftNotDeservedException extends RuntimeException {
    private static final String message = "Gift for child %s is not deserved";

    public GiftNotDeservedException(String childFullName) {
        super(String.format(message, childFullName));
    }
}
