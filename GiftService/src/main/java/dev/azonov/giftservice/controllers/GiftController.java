package dev.azonov.giftservice.controllers;

import dev.azonov.giftservice.model.Gift;
import dev.azonov.giftservice.service.GiftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Handle requests related to gifts
 */
@RestController
@RequestMapping("/gifts")
public class GiftController {
    Logger logger = LoggerFactory.getLogger(GiftController.class);

    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    /**
     * Get all known gift kinds
     * @return list of known gift kinds
     */
    @GetMapping("")
    public List<Gift> findAll() {
        return giftService.findAll();
    }
}
