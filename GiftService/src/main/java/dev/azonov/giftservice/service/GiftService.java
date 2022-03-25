package dev.azonov.giftservice.service;

import dev.azonov.giftservice.model.Gift;
import dev.azonov.giftservice.repository.GiftRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftService implements IGiftService {
    private final GiftRepository repository;

    /**
     * Convert db model for gift into api model
     * @param gift database model
     * @return api model for gift
     */
    private static Gift mapGift(dev.azonov.giftservice.entity.Gift gift) {
        Gift gf = new Gift();
        gf.setKind(gift.getKind());
        gf.setQuantity(gift.getQuantity());
        return gf;
    }

    public GiftService(GiftRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Gift> findAll() {
        return repository.findAll()
                .stream()
                .map(GiftService::mapGift)
                .collect(Collectors.toList());
    }
}
