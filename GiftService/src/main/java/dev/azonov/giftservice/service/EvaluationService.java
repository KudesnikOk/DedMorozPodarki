package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EvaluationService implements IEvaluationService {
    private static final Random random = new Random();

    @Override
    public boolean isGiftDeserved(ChildEntity child) {
        return random.nextDouble() > 0.5;
    }
}
