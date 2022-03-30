package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import dev.azonov.giftservice.entity.DeliveryEntity;
import dev.azonov.giftservice.entity.GiftEntity;
import dev.azonov.giftservice.model.ChildModel;
import dev.azonov.giftservice.model.DeliveryModel;
import dev.azonov.giftservice.model.GiftModel;
import dev.azonov.giftservice.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Override
    public List<DeliveryModel> findAll() {
        List<DeliveryModel> result = new ArrayList<>();
        List<DeliveryEntity> entities = repository.findAll();
        for (DeliveryEntity entity: entities) {
            ChildEntity childEntity = entity.getChild();
            ChildModel child =
                    new ChildModel(childEntity.getFirstName(), childEntity.getSecondName(), childEntity.getMiddleName());

            DeliveryModel deliveryModel = getDeliveryForChild(result, child);
            GiftModel giftModel = getGiftByKind(entity.getGift().getKind(), deliveryModel);

            giftModel.increaseQuantity(1);
        }

        return result;
    }

    private GiftModel getGiftByKind(String kind, DeliveryModel delivery) {
        GiftModel gift;
        Optional<GiftModel> giftModel =
                delivery.getGifts().stream().filter(g -> Objects.equals(g.getKind(), kind)).findFirst();
        if (giftModel.isEmpty()) {
            gift = new GiftModel();
            gift.setKind(kind);
            gift.setQuantity(0);
            delivery.getGifts().add(gift);
        } else {
            gift = giftModel.get();
        }

        return gift;
    }

    private DeliveryModel getDeliveryForChild(List<DeliveryModel> deliveries, ChildModel child) {
        DeliveryModel result;
        Optional<DeliveryModel> deliveryModel =
                deliveries.stream().filter(
                        delivery -> delivery.getChild().equals(child)).findFirst();
        if (deliveryModel.isPresent()) {
            result = deliveryModel.get();
        } else {
            result = new DeliveryModel(child, new ArrayList<>());
            deliveries.add(result);
        }

        return result;
    }
}
