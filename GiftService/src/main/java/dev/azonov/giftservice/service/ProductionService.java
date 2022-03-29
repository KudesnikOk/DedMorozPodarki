package dev.azonov.giftservice.service;

import dev.azonov.giftservice.model.ProduceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductionService implements IProductionService {

    final static Logger logger = LoggerFactory.getLogger(ProductionService.class);
    private final RestTemplate restTemplate;
    private final String requestTemplate = "http://localhost:%s/produce";

    @Value("${production.server.port}")
    private String port;

    public ProductionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void RequestProduction(String kind) {
        logger.info("Start production request for {}", kind);

        ProduceRequest request = new ProduceRequest();
        request.setKind(kind);

        String url = String.format(requestTemplate, port);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, request, String.class);

        logger.info("Production request result code {}", response == null ? "" : response.getStatusCode());
    }
}
