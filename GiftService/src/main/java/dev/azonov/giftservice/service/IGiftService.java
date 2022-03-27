package dev.azonov.giftservice.service;

import dev.azonov.giftservice.model.Gift;
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
    List<Gift> findAll();

    /**
     * Get gift by its kind
     * @param kind kind of a gift to look for
     * @return gift with given kind
     */
    Gift get(String kind);

    /**
     * Send gift according to the request
     * @param request what gift requested and for whom
     */
    void sendGift(MailRequest request);
}
