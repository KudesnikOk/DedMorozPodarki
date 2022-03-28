package dev.azonov.giftservice.service;

import dev.azonov.giftservice.entity.ChildEntity;
import dev.azonov.giftservice.repository.ChildRepository;
import org.springframework.stereotype.Service;

/**
 * Process child related operations
 */
@Service
public class ChildService implements IChildService {
    private final ChildRepository childRepository;

    public ChildService(ChildRepository childRepository) {
        this.childRepository = childRepository;
    }

    @Override
    public ChildEntity get(String firstName, String middleName, String secondName) {
        return childRepository.findFirstByFirstNameAndSecondNameAndMiddleName(firstName, secondName, middleName);
    }
}
