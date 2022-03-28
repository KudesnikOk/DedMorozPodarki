package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import dev.azonov.giftservice.entity.GiftEntity;

/**
 * Deliver gift to a child
 */
public interface IDeliveryService {
    void deliverGift(ChildEntity child, GiftEntity gift);
}
