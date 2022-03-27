package dev.azonov.giftservice.controllers;

import dev.azonov.giftservice.model.Gift;
import dev.azonov.giftservice.service.GiftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

/**
 * Handle requests related to gifts
 */
@RestController
@RequestMapping("/gifts")
@Validated
public class GiftController {
    Logger logger = LoggerFactory.getLogger(GiftController.class);

    private final GiftService giftService;

    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    /**
     * Get all available gifts
     * @return list of available gifts
     */
    @GetMapping("")
    public List<Gift> findAll() {
        logger.info("Request to findAll");

        return giftService.findAll();
    }

    @GetMapping("/{kind}")
    public ResponseEntity<?> get(@PathVariable("kind") @NotBlank @Size(max = 50) String kind) {
        logger.info("Request to get for gift with kind {}", kind);
        
        return ResponseEntity.of(Optional.ofNullable(giftService.get(kind)));
    }
}
