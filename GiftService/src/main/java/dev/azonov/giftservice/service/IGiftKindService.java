package dev.azonov.giftservice.service;

import dev.azonov.giftservice.model.GiftKind;

import java.util.List;

public interface IGiftKindService {
    List<GiftKind> findAll();
}
