package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.GiftEntity;
import dev.azonov.giftservice.exceptions.GiftNotDeservedException;
import dev.azonov.giftservice.exceptions.GiftNotFoundException;
import dev.azonov.giftservice.exceptions.GiftOutOfStockException;
import dev.azonov.giftservice.model.GiftModel;
import dev.azonov.giftservice.model.MailRequest;
import dev.azonov.giftservice.repository.GiftRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftServiceTest {

    @InjectMocks
    private GiftService giftService;

    @Mock
    private GiftRepository giftRepositoryMock;

    @Mock
    private EvaluationService evaluationService;

    @Mock
    private DeliveryService deliveryService;

    @Mock
    private ChildService childService;

    @Captor
    private ArgumentCaptor<GiftEntity> giftCaptor;

    private List<GiftModel> getExpectedGifts(Map<String, Integer> giftsInformation) {
        List<GiftModel> gifts = new ArrayList<>();

        for (String kind : giftsInformation.keySet()) {
            var gift = new GiftModel();
            gift.setKind(kind);
            gift.setQuantity(giftsInformation.get(kind));
            gifts.add(gift);
        }

        return gifts;
    }

    private List<GiftEntity> getLoadedKinds(Map<String, Integer> giftsInformation) {
        List<GiftEntity> giftEntities = new ArrayList<>();

        for (String kind : giftsInformation.keySet()) {
            var gift = new GiftEntity();
            gift.setKind(kind);
            gift.setQuantity(giftsInformation.get(kind));
            giftEntities.add(gift);
        }

        return giftEntities;
    }

    @Test
    void findAllShouldCallToRepository() {
        giftService.findAll();

        verify(giftRepositoryMock, times(1)).findAll();
    }

    @Test
    void findAllShouldConvertDataToProperModel() {
        Map<String, Integer> giftsInformation = new HashMap<>();
        giftsInformation.put("car", 10);
        giftsInformation.put("doll", 14);

        when(giftRepositoryMock.findAll()).thenReturn(getLoadedKinds(giftsInformation));

        List<GiftModel> actualKinds = giftService.findAll();
        assertThat(actualKinds).isEqualTo(getExpectedGifts(giftsInformation));
    }

    @Test
    void getShouldUseRepository() {
        String kind = "kind";
        giftService.get(kind);

        verify(giftRepositoryMock, times(1)).findFirstByKind(kind);
    }

    @Test
    void sendGiftShouldThrowInCaseGiftIsNotFound() {
        MailRequest request = new MailRequest();
        request.setGiftKind("car");

        when(evaluationService.isGiftDeserved(any())).thenReturn(true);

        Exception exception = assertThrows(GiftNotFoundException.class, () -> giftService.sendGift(request));

        String expectedMessage = "Gift with kind car is not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void sendGiftShouldThrowInCaseGiftIsOutOfStock() {
        MailRequest request = new MailRequest();
        request.setGiftKind("car");

        GiftEntity giftEntity = new GiftEntity();
        giftEntity.setQuantity(0);
        when(giftRepositoryMock.findFirstByKind("car")).thenReturn(giftEntity);
        when(evaluationService.isGiftDeserved(any())).thenReturn(true);

        Exception exception = assertThrows(GiftOutOfStockException.class, () -> giftService.sendGift(request));

        String expectedMessage = "Gift with kind car is out of stock";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void sendGiftShouldThrowInCaseGiftIsNotDeserved() {
        MailRequest request = new MailRequest();
        request.setFirstName("Tom");
        request.setSecondName("Soyer");

        when(evaluationService.isGiftDeserved(any())).thenReturn(false);

        Exception exception = assertThrows(GiftNotDeservedException.class, () -> giftService.sendGift(request));

        String expectedMessage = "Gift for child Tom Soyer is not deserved";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void sendGiftShouldUpdateQuantity() {
        MailRequest request = new MailRequest();
        request.setGiftKind("car");

        int quantity = 4;
        GiftEntity giftEntity = new GiftEntity();
        giftEntity.setQuantity(quantity);
        when(giftRepositoryMock.findFirstByKind("car")).thenReturn(giftEntity);
        when(evaluationService.isGiftDeserved(any())).thenReturn(true);

        giftService.sendGift(request);

        verify(giftRepositoryMock, times(1)).save(giftCaptor.capture());
        assertEquals(quantity - 1, giftCaptor.getValue().getQuantity());
    }

    @Test
    void sendGiftShouldDeliverGift() {
        MailRequest request = new MailRequest();
        request.setGiftKind("car");
        request.setFirstName("Tom");

        int quantity = 4;
        GiftEntity giftEntity = new GiftEntity();
        giftEntity.setQuantity(quantity);
        when(giftRepositoryMock.findFirstByKind("car")).thenReturn(giftEntity);
        when(evaluationService.isGiftDeserved(any())).thenReturn(true);

        giftService.sendGift(request);

        verify(deliveryService, times(1)).deliverGift(any(), any());
    }

    @Test
    void increaseQuantityShouldThrowForUnknownGiftKind() {
        when(giftRepositoryMock.findFirstByKind("car")).thenReturn(null);

        Exception exception = assertThrows(
                GiftNotFoundException.class,
                () -> giftService.increaseQuantity("car", 2));

        String expectedMessage = "Gift with kind car is not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void increaseQuantityShouldSaveGiftInformationWithIncreasedQuantity() {
        String kind = "car";
        int increment = 3;
        int quantity = 4;

        GiftEntity giftEntity = new GiftEntity();
        giftEntity.setQuantity(quantity);

        when(giftRepositoryMock.findFirstByKind(kind)).thenReturn(giftEntity);

        giftService.increaseQuantity(kind, increment);

        verify(giftRepositoryMock, times(1)).save(giftCaptor.capture());

        assertEquals(quantity + increment, giftCaptor.getValue().getQuantity());
    }
}