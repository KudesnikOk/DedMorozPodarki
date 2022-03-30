package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import dev.azonov.giftservice.entity.DeliveryEntity;
import dev.azonov.giftservice.entity.GiftEntity;
import dev.azonov.giftservice.model.ChildModel;
import dev.azonov.giftservice.model.DeliveryModel;
import dev.azonov.giftservice.model.GiftModel;
import dev.azonov.giftservice.repository.DeliveryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private DeliveryRepository deliveryRepositoryMock;

    @Captor
    private ArgumentCaptor<DeliveryEntity> deliveryEntityCaptor;

    @Test
    public void deliverGiftCallRepository() {
        ChildEntity child = new ChildEntity();
        GiftEntity gift = new GiftEntity();

        deliveryService.deliverGift(child, gift);

        Mockito.verify(
                deliveryRepositoryMock, Mockito.times(1)).save(deliveryEntityCaptor.capture());

        assertEquals(child, deliveryEntityCaptor.getValue().getChild());
        assertEquals(gift, deliveryEntityCaptor.getValue().getGift());
    }

    @Test
    public void findAllCallRepository() {
        deliveryService.findAll();

        Mockito.verify(deliveryRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    public void findAllForSeveralChildsSeveralGifts() {

        Mockito.when(deliveryRepositoryMock.findAll()).thenReturn(generateDeliveries());

        List<DeliveryModel> actualDeliveries = deliveryService.findAll();
        List<DeliveryModel> expectedDeliveries = generateExpectedDeliveries();

        assertEquals(expectedDeliveries, actualDeliveries);
    }

    private List<DeliveryEntity> generateDeliveries() {
        List<DeliveryEntity> deliveries = new ArrayList<>();
        ChildEntity child1 = new ChildEntity();
        child1.setFirstName("Tim");
        child1.setSecondName("Rodgers");

        ChildEntity child2 = new ChildEntity();
        child2.setFirstName("Bob");
        child2.setMiddleName("Mac");
        child2.setSecondName("Svensen");

        GiftEntity gift1 = new GiftEntity();
        gift1.setKind("car");

        GiftEntity gift2 = new GiftEntity();
        gift2.setKind("phone");

        DeliveryEntity delivery1 = new DeliveryEntity();
        delivery1.setChild(child1);
        delivery1.setGift(gift1);

        DeliveryEntity delivery2 = new DeliveryEntity();
        delivery2.setChild(child1);
        delivery2.setGift(gift1);

        DeliveryEntity delivery3 = new DeliveryEntity();
        delivery3.setChild(child2);
        delivery3.setGift(gift1);

        DeliveryEntity delivery4 = new DeliveryEntity();
        delivery4.setChild(child2);
        delivery4.setGift(gift2);

        deliveries.add(delivery1);
        deliveries.add(delivery2);
        deliveries.add(delivery3);
        deliveries.add(delivery4);

        return deliveries;
    }

    private List<DeliveryModel> generateExpectedDeliveries() {
        List<DeliveryModel> deliveries = new ArrayList<>();
        ChildModel child1 = new ChildModel("Tim", "Rodgers", null);
        ChildModel child2 = new ChildModel("Bob", "Svensen", "Mac");

        GiftModel gift1ForChild1 = new GiftModel();
        gift1ForChild1.setKind("car");
        gift1ForChild1.setQuantity(2);

        GiftModel gift1ForChild2 = new GiftModel();
        gift1ForChild2.setKind("car");
        gift1ForChild2.setQuantity(1);

        GiftModel gift2ForChild2 = new GiftModel();
        gift2ForChild2.setKind("phone");
        gift2ForChild2.setQuantity(1);

        DeliveryModel delivery1 = new DeliveryModel(child1, new ArrayList<>());
        delivery1.getGifts().add(gift1ForChild1);

        DeliveryModel delivery2 = new DeliveryModel(child2, new ArrayList<>());
        delivery2.getGifts().add(gift1ForChild2);
        delivery2.getGifts().add(gift2ForChild2);

        deliveries.add(delivery1);
        deliveries.add(delivery2);

        return deliveries;
    }
}