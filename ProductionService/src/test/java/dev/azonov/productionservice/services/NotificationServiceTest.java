package dev.azonov.productionservice.services;

import dev.azonov.productionservice.model.PopulateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private RestTemplate restTemplateMock;

    @Captor
    private ArgumentCaptor<PopulateRequest> populateRequestCaptor;

    @Test
    public void notifyProductionCompleteShouldCallRestTemplate() {
        String kind = "car";
        int increment = 2;

        notificationService.notifyProductionComplete(kind, increment);

        verify(restTemplateMock, times(1))
                .postForEntity(contains(kind), populateRequestCaptor.capture(), eq(String.class));

        assertEquals(increment, populateRequestCaptor.getValue().getIncrement());
    }

}