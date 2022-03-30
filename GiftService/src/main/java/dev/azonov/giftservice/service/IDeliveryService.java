package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import dev.azonov.giftservice.entity.GiftEntity;
import dev.azonov.giftservice.model.DeliveryModel;

import java.util.List;

/**
 * Delivery related operations
 */
public interface IDeliveryService {
    /**
     * Deliver a gift to a child
     * @param child a child to deliver gift to
     * @param gift a gift to deliver
     */
    void deliverGift(ChildEntity child, GiftEntity gift);

    /**
     * Find all performed deliveries
     * @return performed deliveries
     */
    List<DeliveryModel> findAll();
}
