package dev.azonov.productionservice.services;

/**
 * Notify gift service that production of gift of given kind is completed
 * and that increment new gifts are available
 */
public interface INotificationService {
    void notifyProductionComplete(String kind, int increment);
}
