package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import dev.azonov.giftservice.entity.GiftEntity;
import dev.azonov.giftservice.exceptions.GiftNotDeservedException;
import dev.azonov.giftservice.exceptions.GiftNotFoundException;
import dev.azonov.giftservice.exceptions.GiftOutOfStockException;
import dev.azonov.giftservice.model.GiftModel;
import dev.azonov.giftservice.model.MailRequest;
import dev.azonov.giftservice.repository.GiftRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftService implements IGiftService {
    private final GiftRepository repository;
    private final IEvaluationService evaluationService;
    private final IChildService childService;

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

    public GiftService(GiftRepository repository, IEvaluationService evaluationService, IChildService childService) {
        this.repository = repository;
        this.evaluationService = evaluationService;
        this.childService = childService;
    }

    @Override
    public List<GiftModel> findAll() {
        return repository.findAll()
                .stream()
                .map(GiftService::mapGift)
                .collect(Collectors.toList());
    }

    @Override
    public GiftModel get(String kind) {
        return mapGift(repository.findFirstByKind(kind));
    }

    @Override
    public void sendGift(MailRequest request) {
        ChildEntity child = new ChildEntity(request.getFirstName(), request.getMiddleName(), request.getSecondName());

        if (!evaluationService.isGiftDeserved(child)) {
            throw new GiftNotDeservedException(child.getFullName());
        }

        String giftKind = request.getGiftKind();
        GiftModel gift = get(giftKind);

        if (gift == null) {
            throw new GiftNotFoundException(giftKind);
        }
        if (gift.getQuantity() <= 0) {
            throw new GiftOutOfStockException(giftKind);
        }
        gift.reduceQuantity();
        repository.saveAndFlush(mapGift(gift));
    }
}
