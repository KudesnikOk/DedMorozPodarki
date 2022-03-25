package dev.azonov.giftservice.controllers;

import dev.azonov.giftservice.model.MailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(MailController.class);

    @PostMapping("/process")
    public void processMail(@Valid @RequestBody MailRequest request) {
        logger.info("Gift requested");
    }
}
