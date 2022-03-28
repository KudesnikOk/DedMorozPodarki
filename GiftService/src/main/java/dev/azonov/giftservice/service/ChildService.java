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
    public void createOrRead(ChildEntity child) {
        ChildEntity existentChild =
                childRepository.findFirstByFirstNameAndSecondNameAndMiddleName(
                        child.getFirstName(), child.getSecondName(), child.getMiddleName());
        if (existentChild == null) {
            childRepository.save(child);
        } else {
            child.setId(existentChild.getId());
        }
    }
}
