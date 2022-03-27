package dev.azonov.giftservice.service;

import dev.azonov.giftservice.model.Gift;

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
}
