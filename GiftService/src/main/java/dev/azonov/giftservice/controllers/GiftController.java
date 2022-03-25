package dev.azonov.giftservice.controllers;

import dev.azonov.giftservice.model.GiftKind;
import dev.azonov.giftservice.service.GiftKindService;
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

    private final GiftKindService giftKindService;

    public GiftController(GiftKindService giftKindService) {
        this.giftKindService = giftKindService;
    }

    /**
     * Get all known gift kinds
     * @return list of known gift kinds
     */
    @GetMapping("/kinds")
    public List<GiftKind> findAll() {
        return giftKindService.findAll();
    }
}
