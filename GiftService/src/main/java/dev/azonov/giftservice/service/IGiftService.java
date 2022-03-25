package dev.azonov.giftservice.service;

import dev.azonov.giftservice.model.Gift;

import java.util.List;

public interface IGiftService {
    List<Gift> findAll();
}
