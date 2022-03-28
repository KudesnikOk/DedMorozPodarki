package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import dev.azonov.giftservice.exceptions.GiftNotDeservedException;
import dev.azonov.giftservice.exceptions.GiftNotFoundException;
import dev.azonov.giftservice.exceptions.GiftOutOfStockException;
import dev.azonov.giftservice.model.Gift;
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
     * @param gift database model
     * @return api model for gift
     */
    private static Gift mapGift(dev.azonov.giftservice.entity.Gift gift) {
        if (gift == null) {
            return null;
        }

        Gift result = new Gift();
        result.setId(gift.getId());
        result.setKind(gift.getKind());
        result.setQuantity(gift.getQuantity());
        return result;
    }

    private static dev.azonov.giftservice.entity.Gift mapGift(Gift gift) {
        if (gift == null) {
            return null;
        }

        dev.azonov.giftservice.entity.Gift result = new dev.azonov.giftservice.entity.Gift();
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
    public List<Gift> findAll() {
        return repository.findAll()
                .stream()
                .map(GiftService::mapGift)
                .collect(Collectors.toList());
    }

    @Override
    public Gift get(String kind) {
        return mapGift(repository.findFirstByKind(kind));
    }

    @Override
    public void sendGift(MailRequest request) {
        ChildEntity child = new ChildEntity(request.getFirstName(), request.getMiddleName(), request.getSecondName());

        if (!evaluationService.isGiftDeserved(child)) {
            throw new GiftNotDeservedException(child.getFullName());
        }

        String giftKind = request.getGiftKind();
        Gift gift = get(giftKind);

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
