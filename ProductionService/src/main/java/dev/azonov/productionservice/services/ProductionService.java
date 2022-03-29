package dev.azonov.productionservice.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ProductionService implements IProductionService {

    private final static Logger logger = LoggerFactory.getLogger(ProductionService.class);

    private final INotificationService notificationService;

    @Value("${production.delayInSeconds}")
    private String delayInSecondsSetting;

    @Value("${production.increment}")
    private String incrementSetting;

    public ProductionService(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    @Async
    public void startProduction(String kind) {
        long delay;
        try {
            delay = Long.parseLong(delayInSecondsSetting) * 1000;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            delay = 1000L;
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int increment;
        try {
            increment = Integer.parseInt(incrementSetting);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            increment = 1;
        }

        notificationService.notifyProductionComplete(kind, increment);
        logger.info("Production of {} finished", kind);
    }
}
