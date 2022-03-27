package dev.azonov.giftservice.exceptions;

public class GiftOutOfStockException extends RuntimeException {
    private static final String message = "Gift with kind %s is out of stock";

    public GiftOutOfStockException(String giftKind) {
        super(String.format(message, giftKind));
    }
}
