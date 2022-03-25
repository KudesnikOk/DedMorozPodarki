package dev.azonov.giftservice.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handle gift requests.
 */
@RestController
@RequestMapping("/gifts")
public class GiftsController {

    Logger logger = LoggerFactory.getLogger(GiftsController.class);

    @PostMapping("/request")
    public void requestGift() {
        logger.info("Gift requested");
    }
}
