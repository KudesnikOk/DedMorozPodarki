package dev.azonov.productionservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ProductionService implements IProductionService {
    final static Logger logger = LoggerFactory.getLogger(ProductionService.class);

    @Value("${production.delayInSeconds}")
    private String delayInSeconds;

    @Override
    @Async
    public void startProduction(String kind) {
        long delay;
        try {
            delay = Long.parseLong(delayInSeconds) * 1000;
        } catch (NumberFormatException e) {
            delay = 5000L;
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Production of {} finished", kind);
    }
}
