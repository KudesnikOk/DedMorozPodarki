package dev.azonov.productionservice.services;

import dev.azonov.productionservice.model.PopulateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService implements INotificationService {

    final static Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final RestTemplate restTemplate;
    private final String requestTemplate = "http://localhost:%s/gifts/%s/populate";

    @Value("${production.reportready.server.port}")
    private String port;

    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void notifyProductionComplete(String kind, int increment) {
        logger.info("Start notification for {}: produced {} gifts", kind, increment);

        PopulateRequest request = new PopulateRequest();
        request.setIncrement(increment);

        String url = String.format(requestTemplate, port, kind);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, request, String.class);

        logger.info("Notification result code {}", response == null ? "" : response.getStatusCode());
    }
}
