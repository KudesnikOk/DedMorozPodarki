package dev.azonov.giftservice.model;

import lombok.Data;

import java.util.List;

@Data
public class DeliveryModel {
    private final ChildModel child;
    private final List<GiftModel> gifts;
}
