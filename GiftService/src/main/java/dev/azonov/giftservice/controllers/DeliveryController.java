package dev.azonov.giftservice.controllers;

import dev.azonov.giftservice.model.DeliveryModel;
import dev.azonov.giftservice.service.IDeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller to handle deliveries related requests
 */
@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final Logger logger = LoggerFactory.getLogger(DeliveryController.class);

    private final IDeliveryService deliveryService;

    public DeliveryController(IDeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryModel>> findAll() {
        logger.info("Requested all deliveries");

        return ResponseEntity.ok(deliveryService.findAll());
    }
}
