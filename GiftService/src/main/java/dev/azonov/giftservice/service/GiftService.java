package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import dev.azonov.giftservice.entity.GiftEntity;
import dev.azonov.giftservice.exceptions.GiftNotDeservedException;
import dev.azonov.giftservice.exceptions.GiftNotFoundException;
import dev.azonov.giftservice.exceptions.GiftOutOfStockException;
import dev.azonov.giftservice.model.GiftModel;
import dev.azonov.giftservice.model.MailRequest;
import dev.azonov.giftservice.repository.GiftRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Service
public class GiftService implements IGiftService {
    private final GiftRepository repository;
    private final IEvaluationService evaluationService;
    private final IChildService childService;
    private final IDeliveryService deliveryService;
    private final IProductionService productionService;

    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Value("${production.minimum-gifts-threshold}")
    private String giftThresholdSetting;

    private int giftThreshold;

    /**
     * Convert db model for gift into api model
     * @param giftEntity database model
     * @return api model for gift
     */
    private static GiftModel mapGift(GiftEntity giftEntity) {
        if (giftEntity == null) {
            return null;
        }

        GiftModel result = new GiftModel();
        result.setId(giftEntity.getId());
        result.setKind(giftEntity.getKind());
        result.setQuantity(giftEntity.getQuantity());
        return result;
    }

    private static GiftEntity mapGift(GiftModel gift) {
        if (gift == null) {
            return null;
        }

        GiftEntity result = new GiftEntity();
        result.setId(gift.getId());
        result.setQuantity(gift.getQuantity());
        result.setKind(gift.getKind());
        return result;
    }

    public GiftService(
            GiftRepository repository,
            IEvaluationService evaluationService,
            IChildService childService,
            IDeliveryService deliveryService,
            IProductionService productionService) {
        this.repository = repository;
        this.evaluationService = evaluationService;
        this.childService = childService;
        this.deliveryService = deliveryService;
        this.productionService = productionService;
    }

    @PostConstruct
    public void Init() {
        try {
            giftThreshold = Integer.parseInt(giftThresholdSetting);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            giftThreshold = 5;
        }
    }

    @Override
    public List<GiftModel> findAll() {
        Lock readLock = lock.readLock();
        try {
            readLock.lock();
            return repository.findAll()
                    .stream()
                    .map(GiftService::mapGift)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public GiftModel get(String kind) {
        Lock readLock = lock.readLock();
        try {
            readLock.lock();
            return mapGift(repository.findFirstByKind(kind));
        } finally {
            readLock.unlock();
        }
    }

    @Override
    @Transactional
    public void sendGift(MailRequest request) {
        ChildEntity child = new ChildEntity(request.getFirstName(), request.getMiddleName(), request.getSecondName());

        if (!evaluationService.isGiftDeserved(child)) {
            throw new GiftNotDeservedException(child.getFullName());
        }

        String giftKind = request.getGiftKind();
        GiftModel gift;

        Lock writeLock = lock.writeLock();
        try {
            writeLock.lock();

            gift = get(giftKind);

            if (gift == null) {
                throw new GiftNotFoundException(giftKind);
            }
            if (gift.getQuantity() <= 0) {
                throw new GiftOutOfStockException(giftKind);
            }
            gift.reduceQuantity();

            GiftEntity giftEntity = mapGift(gift);
            repository.save(giftEntity);
            childService.createOrRead(child);
            deliveryService.deliverGift(child, giftEntity);
        } finally {
            writeLock.unlock();
        }

        if (gift.getQuantity() < giftThreshold) {
            productionService.RequestProduction(gift.getKind());
        }
    }

    @Override
    public void increaseQuantity(String kind, int increment) {
        Lock writeLock = lock.writeLock();

        try {
            writeLock.lock();

            GiftModel gift = get(kind);

            if (gift == null) {
                throw new GiftNotFoundException(kind);
            }
            gift.increaseQuantity(increment);
            repository.save(mapGift(gift));
        } finally {
            writeLock.unlock();
        }
    }
}
