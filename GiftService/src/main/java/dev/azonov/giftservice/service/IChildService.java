package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;

public interface IChildService {
    /**
     * Create new child if not exists or read existing child information
     * @param child information about child
     */
    void createOrRead(ChildEntity child);
}
