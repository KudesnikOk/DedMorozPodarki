package dev.azonov.giftservice.service;

import dev.azonov.giftservice.model.GiftModel;
import dev.azonov.giftservice.model.MailRequest;

import java.util.List;

/**
 * Interface for handling gift related operations
 */
public interface IGiftService {
    /**
     * Find all gifts
     * @return all gifts
     */
    List<GiftModel> findAll();

    /**
     * Get gift by its kind
     * @param kind kind of a gift to look for
     * @return gift with given kind
     */
    GiftModel get(String kind);

    /**
     * Send gift according to the request
     * @param request what gift requested and for whom
     */
    void sendGift(MailRequest request);

    /**
     * Increase amount of gifts of given kind by quantity
     * @param kind gift kind to populate
     * @param quantity number of gifts produced
     */
    void increaseQuantity(String kind, int quantity);
}
