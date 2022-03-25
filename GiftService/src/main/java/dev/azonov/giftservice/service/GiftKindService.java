package dev.azonov.giftservice.service;

import dev.azonov.giftservice.model.GiftKind;
import dev.azonov.giftservice.repository.GiftKindRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GiftKindService implements IGiftKindService {
    private final GiftKindRepository repository;

    public GiftKindService(GiftKindRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<GiftKind> findAll() {
        return repository.findAll()
                .stream()
                .map(giftKind -> { GiftKind gf = new GiftKind(); gf.setName(giftKind.getName()); return gf; })
                .collect(Collectors.toList());
    }
}
