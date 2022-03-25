package dev.azonov.giftservice.service;

import dev.azonov.giftservice.model.GiftKind;
import dev.azonov.giftservice.repository.GiftKindRepository;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GiftKindServiceTest {

    @InjectMocks
    private GiftKindService giftKindService;

    @Mock
    private GiftKindRepository giftKindRepositoryMock;

    private List<GiftKind> getExpectedKinds(String ... names) {
        List<GiftKind> kinds = new ArrayList<>();

        for (String name : names) {
            var kind = new GiftKind();
            kind.setName(name);
            kinds.add(kind);
        }

        return kinds;
    }

    private List<dev.azonov.giftservice.entity.GiftKind> getLoadedKinds(String ... names) {
        List<dev.azonov.giftservice.entity.GiftKind> kinds = new ArrayList<>();

        for (String name : names) {
            var kind = new dev.azonov.giftservice.entity.GiftKind();
            kind.setName(name);
            kinds.add(kind);
        }

        return kinds;
    }

    @Test
    void findAllShouldCallToRepository() {
        giftKindService.findAll();

        verify(giftKindRepositoryMock, times(1)).findAll();
    }

    @Test
    void findAllShouldConvertDataToProperModel() {
        when(giftKindRepositoryMock.findAll()).thenReturn(getLoadedKinds("car", "book"));

        List<GiftKind> actualKinds = giftKindService.findAll();
        assertThat(actualKinds).isEqualTo(getExpectedKinds("car", "book"));
    }
}