package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;

/**
 * Evaluate child if he deserves a gift
 */
public interface IEvaluationService {
    boolean isGiftDeserved(ChildEntity child);
}
