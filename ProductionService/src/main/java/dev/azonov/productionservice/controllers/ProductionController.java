package dev.azonov.productionservice.controllers;

import dev.azonov.productionservice.model.ProduceRequest;
import dev.azonov.productionservice.services.IProductionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/produce")
public class ProductionController {

    private final Logger logger = LoggerFactory.getLogger(ProductionController.class);
    private final IProductionService productionService;

    public ProductionController(IProductionService productionService) {
        this.productionService = productionService;
    }

    @PostMapping
    public ResponseEntity<?> produceGift(@Valid @RequestBody ProduceRequest request) {

        String kind = request.getKind();

        logger.info("Requested production for {}", kind);

        productionService.startProduction(kind);

        return ResponseEntity.ok().build();
    }
}
