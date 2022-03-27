package dev.azonov.giftservice.service;

import dev.azonov.giftservice.exceptions.GiftNotFoundException;
import dev.azonov.giftservice.model.Gift;
import dev.azonov.giftservice.model.MailRequest;
import dev.azonov.giftservice.repository.GiftRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private List<Gift> getExpectedGifts(Map<String, Integer> giftsInformation) {
        List<Gift> gifts = new ArrayList<>();

        for (String kind : giftsInformation.keySet()) {
            var gift = new Gift();
            gift.setKind(kind);
            gift.setQuantity(giftsInformation.get(kind));
            gifts.add(gift);
        }

        return gifts;
    }

    private List<dev.azonov.giftservice.entity.Gift> getLoadedKinds(Map<String, Integer> giftsInformation) {
        List<dev.azonov.giftservice.entity.Gift> gifts = new ArrayList<>();

        for (String kind : giftsInformation.keySet()) {
            var gift = new dev.azonov.giftservice.entity.Gift();
            gift.setKind(kind);
            gift.setQuantity(giftsInformation.get(kind));
            gifts.add(gift);
        }

        return gifts;
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

        List<Gift> actualKinds = giftService.findAll();
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
        Exception exception = assertThrows(GiftNotFoundException.class, () -> giftService.sendGift(request));

        String expectedMessage = "Gift with kind car is not found";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }
}