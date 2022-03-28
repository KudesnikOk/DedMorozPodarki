package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import dev.azonov.giftservice.entity.DeliveryEntity;
import dev.azonov.giftservice.entity.GiftEntity;
import dev.azonov.giftservice.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService implements IDeliveryService {
    private final DeliveryRepository repository;

    public DeliveryService(DeliveryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void deliverGift(ChildEntity child, GiftEntity gift) {
        DeliveryEntity delivery = new DeliveryEntity();
        delivery.setChild(child);
        delivery.setGift(gift);

        repository.save(delivery);
    }
}
