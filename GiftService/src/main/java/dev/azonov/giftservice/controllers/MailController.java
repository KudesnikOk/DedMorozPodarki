package dev.azonov.giftservice.controllers;

import dev.azonov.giftservice.exceptions.GiftNotFoundException;
import dev.azonov.giftservice.exceptions.GiftOutOfStockException;
import dev.azonov.giftservice.model.MailRequest;
import dev.azonov.giftservice.service.IGiftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Handle gift requests.
 */
@RestController
@RequestMapping("/mails")
public class MailController {

    private final Logger logger = LoggerFactory.getLogger(MailController.class);

    private final IGiftService giftService;

    public MailController(IGiftService giftService) {
        this.giftService = giftService;
    }

    @PostMapping("/process")
    public ResponseEntity<?> processMail(@Valid @RequestBody MailRequest request) {
        logger.info("Gift for kind {} requested", request.getGiftKind());

        try {
            giftService.sendGift(request);
        } catch (GiftNotFoundException| GiftOutOfStockException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }
}
