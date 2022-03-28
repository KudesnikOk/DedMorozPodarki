package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;

public interface IChildService {
    ChildEntity get(String firstName, String middleName, String secondName);
}
