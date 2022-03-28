package dev.azonov.giftservice.controllers;

import dev.azonov.giftservice.model.GiftModel;
import dev.azonov.giftservice.model.PopulationRequest;
import dev.azonov.giftservice.service.GiftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public List<GiftModel> findAll() {
        logger.info("Request to findAll");

        return giftService.findAll();
    }

    /**
     * Get gift of given kind
     * @param kind of a gift
     * @return information about a gift
     */
    @GetMapping("/{kind}")
    public ResponseEntity<?> get(@PathVariable("kind") @NotBlank @Size(max = 50) String kind) {
        logger.info("Request to get for gift with kind {}", kind);

        return ResponseEntity.of(Optional.ofNullable(giftService.get(kind)));
    }

    /**
     * Increase quantity of a gifts of a given kind
     * @param kind of a gift
     * @param request with increment
     * @return status of operation
     */
    @PostMapping("/{kind}/populate")
    public ResponseEntity<?> increaseQuantity(
            @PathVariable("kind") @NotBlank @Size(max = 50) String kind,
            @Valid @RequestBody PopulationRequest request) {

        giftService.increaseQuantity(kind, request.getIncrement());

        return ResponseEntity.ok().build();
    }
}
